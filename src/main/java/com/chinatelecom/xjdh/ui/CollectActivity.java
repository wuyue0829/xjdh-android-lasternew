package com.chinatelecom.xjdh.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.rest.MediaType;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.ApiResponseUpLoad;
import com.chinatelecom.xjdh.db.DatabaseHelper;
import com.chinatelecom.xjdh.model.FileNameGPSTable;
import com.chinatelecom.xjdh.model.StationTable;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.FileUtils;
import com.chinatelecom.xjdh.utils.ImageItem;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;
import com.chinatelecom.xjdh.view.GridImageView_;
import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 采集Activity
 * 
 * @author admin
 *
 */
@EActivity(R.layout.activity_collet)
public class CollectActivity extends Activity {
	@RestService
	ApiRestClientInterface mApiClient;
	Dialog mImgChangeDialog;
	Uri pendingImageUri;
	public static final int CAPTURE_PICTURE = 1;// 调用相机拍照
	public static final int RESULT_PICTURE = 3;// 剪切返回结果
	private FileNameGPSTable fileNameGPSTable;// 文件名和GPS表,局站名称,分组名称

	private ArrayList<ImageItem> mBitmapList = new ArrayList<ImageItem>();
	private ArrayList<String> imagePathList = new ArrayList<String>();
	private GridAdapter adapter;// 展示拍照后的照片
	private ProgressDialog pDialog;
	private String fileName;
	private List<BDLocation> list = new ArrayList<BDLocation>();
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	BitmapDescriptor mCurrentMarker;
	@ViewById(R.id.bmapView)
	MapView mMapView;
	BaiduMap mBaiduMap;
	boolean isFirstLoc = true;// 是否首次定位
	public static final int VIEW_PHOTOS = 2;

	// 经度
	@ViewById(R.id.tv_Longitude)
	TextView tv_Longitude;
	// 纬度
	@ViewById(R.id.tv_Latitude)
	TextView tv_Latitude;

	@ViewById(R.id.noScrollgridview)
	GridView noScrollgridview;
	// 局站名称
	@Extra
	String stationName;
	@Extra("newGrouping")
	String newGrouping;//分组名称 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new GridAdapter(this);
		adapter.SetBitmapList(mBitmapList);
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("提交数据中...");
	}

	@OrmLiteDao(helper = DatabaseHelper.class, model = StationTable.class)
	Dao<StationTable, Integer> stDao;// 局站名称

	@AfterViews
	void initData() {
		mImgChangeDialog = new Dialog(this);
		mImgChangeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		fileNameGPSTable = new FileNameGPSTable();
		noScrollgridview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		// 地图初始化
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		mMapView.showZoomControls(false);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	File cameraFile;

	@ItemClick(R.id.noScrollgridview)
	public void OnGridItemClick(int position) {
		if (position == mBitmapList.size()) {
			// 图片名
			fileName = String.valueOf(System.currentTimeMillis());
			cameraFile = new File(FileUtils.getImagePath(fileName));
			// cameraFile = new File(FileUtils.getImagePath_(fileName));
			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
			CollectActivity.this.startActivityForResult(openCameraIntent, CAPTURE_PICTURE);
		} else {
			// Show Big Images
			ViewPhotosActivity_.intent(this).extra("position", position).imagePathList(imagePathList)
					.startForResult(VIEW_PHOTOS);
		}
	}

	ImageItem takePhoto;

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CAPTURE_PICTURE:
			if (resultCode == Activity.RESULT_OK) {

				if (cameraFile.exists()) {
					try {
						FileInputStream fis = new FileInputStream(cameraFile);

						Display display = this.getWindowManager().getDefaultDisplay();
						int dw = display.getWidth();
						int dh = display.getHeight();

						// 加载图像
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inJustDecodeBounds = true;// 设置之后可以设置长宽
						Bitmap bitmap = BitmapFactory.decodeFile(cameraFile.getAbsolutePath(), options);

						int heightRatio = (int) Math.ceil(options.outHeight / (float) dh);
						int widthRatio = (int) Math.ceil(options.outWidth / (float) dw);

						// 判断长宽哪个大 Calculate inSampleSize
						if (heightRatio > 1 && widthRatio > 1) {
							if (heightRatio > widthRatio) {
								options.inSampleSize = heightRatio;
							} else {
								options.inSampleSize = widthRatio;
							}
						}
						// 对它进行真正的解码
						options.inJustDecodeBounds = false;
						bitmap = BitmapFactory.decodeFile(cameraFile.getAbsolutePath(), options);
						L.e("图片名.................................." + cameraFile.getName());
						imagePathList.add(cameraFile.getAbsolutePath());

						takePhoto = new ImageItem();
						takePhoto.setImagePath(cameraFile.getName());
						takePhoto.setBitmap(bitmap);
						mBitmapList.add(takePhoto);
						// adapter.SetBitmapList(mBitmapList);
						adapter.notifyDataSetChanged();
					} catch (Exception ex) {

					}
				}
			}
			break;
		case VIEW_PHOTOS:
			if (resultCode == Activity.RESULT_OK) {
				imagePathList = (ArrayList<String>) data.getExtras().get("imageList");
				mBitmapList = new ArrayList<ImageItem>();
				for (String imagePath : imagePathList) {
					ImageItem takePhoto = new ImageItem();
					takePhoto.setImagePath(imagePath);
					mBitmapList.add(takePhoto);
				}
				adapter.SetBitmapList(mBitmapList);
				adapter.notifyDataSetChanged();
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@OrmLiteDao(helper = DatabaseHelper.class, model = FileNameGPSTable.class)
	Dao<FileNameGPSTable, Integer> sDao;

	@ItemClick(R.id.lv_station_list)
	void listStationClicked(int position) {

	}

	/**
	 * 保存
	 */
	/*@Click(R.id.btn_save_collect)
	void saveClicked() {
		if (tv_Longitude.getText().toString() == null || tv_Longitude.getText().toString().equals("") || list == null) {
			tv_Longitude.setError("");
			return;
		} else if (tv_Latitude.getText().toString() == null || tv_Latitude.getText().toString().equals("")
				|| list == null) {
			tv_Latitude.setError("");
			return;
		} else if (mBitmapList.size() == 0) {
			Toast.makeText(this, "请拍照......", 0).show();
			return;
		}
		fileNameGPSTable.setFileName(cameraFile.getName());
		fileNameGPSTable.setLongitude(list.get(0).getLongitude() + "");
		fileNameGPSTable.setLatitude(list.get(0).getLatitude() + "");
		fileNameGPSTable.setStationName(stationName);
		fileNameGPSTable.setGroupingName(newGrouping);
		try {
			sDao.createOrUpdate(fileNameGPSTable);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.finish();
	}*/

	/**
	 * 上传
	 */
	@Click(R.id.btn_upload_)
	void uploadClicked() {
		if (tv_Longitude.getText().toString() == null || tv_Longitude.getText().toString().equals("") || list == null) {
			tv_Longitude.setError("");
			return;
		} else if (tv_Latitude.getText().toString() == null || tv_Latitude.getText().toString().equals("")
				|| list == null) {
			tv_Latitude.setError("");
			return;
		} else if (mBitmapList.size() == 0) {
			Toast.makeText(this, "请拍照", 0).show();
			return;
		}
		pDialog.show();
		DoWorkerUpload(list.get(0).getLongitude(), list.get(0).getLatitude(), stationName,newGrouping);
		fileNameGPSTable.setFileName(cameraFile.getName());
		fileNameGPSTable.setLongitude(list.get(0).getLongitude() + "");
		fileNameGPSTable.setLatitude(list.get(0).getLatitude() + "");
		fileNameGPSTable.setStationName(stationName);
		fileNameGPSTable.setGroupingName(newGrouping);
		try {
			sDao.createOrUpdate(fileNameGPSTable);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Background
	public void DoWorkerUpload(double longitude, double latitude, String stationName,String newGrouping) {
		try {
			MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
			for (ImageItem item : mBitmapList) {
				// byte[] imageBytes = FileUtils.getFileData(path);
				for (int i = 0; i < 2; i++) {
					L.e("----" + item.getImagePath());
				}
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				item.getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, stream);// 压缩10%把压缩后的数据存放到stream中
				// byte[] imageBytes = stream.toByteArray();
				ByteArrayResource image = new ByteArrayResource(stream.toByteArray(), item.getImagePath()) {
					@Override
					public String getFilename() throws IllegalStateException {
						return super.getDescription();
					}

					@Override
					public String getDescription() {
						return super.getDescription();
					}
				};
				formData.add("uploadImg[]", image);
				formData.add("longitude", String.valueOf(longitude));
				formData.add("latitude", String.valueOf(latitude));
				formData.add("stationName", stationName);
				formData.add("newGroupingName", newGrouping);
			}
			L.e("@@@@formData" + formData);
			mApiClient.setHeader("Content-Type", MediaType.MULTIPART_FORM_DATA);
			ApiResponseUpLoad resp = mApiClient.StationImage(formData);

			L.e("上传照片........" + resp.getRet() + "" + resp.getData() + "formData"
					+ formData.get(SharedConst.UPLOAD_IMG));
			L.e("NewGrouping :"+resp.getNewGrouping());
			if (resp.getRet() == 0) {
				ShowResponse(resp);
				pDialog.dismiss();
				return;
			}
		} catch (Exception ex) {
			ApiResponseUpLoad resp = new ApiResponseUpLoad();
			// resp.setRet(1);
			resp.setData("请求失败");
			// ShowResponse(resp);
			L.e("ex" + ex.toString());
			L.e("ex" + resp.getRet());
		}

	}

	@UiThread
	public void ShowResponse(ApiResponseUpLoad resp) {

		if (resp.getRet() == 0) {
			T.showLong(this, "上传成功");
			pDialog.dismiss();
			this.finish();
		} else {
			T.showLong(this, resp.getData());
		}
	}

	@Click(R.id.btn_gps_collect)
	void collectClicked() {
		if (list != null) {

			tv_Longitude.setText("经度:" + list.get(0).getLongitude());
			tv_Latitude.setText("维度:" + list.get(0).getLatitude());
		} else {
			Toast.makeText(this, "需要去定位当前位置", 0).show();
			this.finish();
			// LocationDemo_.intent(this).start();
		}
	}

	public class GridAdapter extends BaseAdapter {
		private int selectedPosition = -1;
		private boolean shape;

		private ArrayList<ImageItem> mBitmapList;
		private Context context;// 运行上下文

		public void SetBitmapList(ArrayList<ImageItem> imageList) {
			mBitmapList = imageList;
		}

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			super();
			this.context = context;
		}

		public int getCount() {
			return mBitmapList.size() + 1;
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = GridImageView_.build(context);
			}

			if (position == mBitmapList.size()) {
				((GridImageView_) convertView)
						.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_focused));
			} else {
				((GridImageView_) convertView).setImage(mBitmapList.get(position).getBitmap());
			}
			return convertView;
		}
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;

			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);

			if (isFirstLoc) {
				isFirstLoc = false;
				MapStatus.Builder mapStatusBuilder = new MapStatus.Builder();
				mapStatusBuilder.zoom(17f);
				LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
				mapStatusBuilder.target(ll);
				mapStatusBuilder.overlook(0);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatusBuilder.build()));
				list.add(location);
			}

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode);
		// inte
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		switch (newConfig.orientation) {
		case Configuration.ORIENTATION_PORTRAIT:
			Log.i("1", "竖屏");
			break;
		case Configuration.ORIENTATION_LANDSCAPE:
			Log.i("2", "横屏");
			break;
		}
	}
}
