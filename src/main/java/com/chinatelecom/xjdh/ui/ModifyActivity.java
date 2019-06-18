package com.chinatelecom.xjdh.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.rest.MediaType;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.ApiResponseUpLoad;
import com.chinatelecom.xjdh.bean.UserInfo;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.ImageItem;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;
import com.chinatelecom.xjdh.utils.URLs;
import com.chinatelecom.xjdh.view.AutoGridView;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

@EActivity(R.layout.modify_view)
public class ModifyActivity extends BaseActivity{
	@Extra
	String question;
	@Extra
	String Titles;
	@Extra
	String[] editimgpath;
	@Extra
	String Device_id;
	@Extra
	int station_code;
	@Extra
	int question_ID;
	@ViewById
	AutoGridView answer_gridview;
	@ViewById
	GridView gridview;
	private ArrayList<String> imagePathList = new ArrayList<String>();
	private ArrayList<ImageItem> mBitmapList = new ArrayList<ImageItem>();
	private File tempFile;
	public static final int CAPTURE_PICTURE = 1;
	@RestService
	ApiRestClientInterface mApiClient;
	private GridAdapter upadapter;
	private ImgGridAdapter adapter;
	public static Bitmap bimap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(question);
		bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
	}

	@AfterViews
	void ShowView() {
		gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		upadapter = new GridAdapter(this);
		adapter = new ImgGridAdapter(this,editimgpath);
		upadapter.SetBitmapList(mBitmapList);
		gridview.setAdapter(upadapter);
		answer_gridview.setAdapter(adapter);
		upadapter.notifyDataSetChanged();
		getUserInfo();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged(); // 当在ImageZoomActivity中删除图片时，返回这里需要刷新
	}
	UserInfo mUserInfo;

	@Background
	void getUserInfo() {
		try {
			ApiResponse mApiResps = mApiClient.getUserInfo();
			if (mApiResps.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				L.d("&&&&&&&&&&&&&&&&&&&&&&&&&&", mapper.writeValueAsString(mApiClient.getUserInfo().toString()));

				mUserInfo = mapper.readValue(mApiResps.getData(), UserInfo.class);
				L.d("++++++++++++", mapper.writeValueAsString(mUserInfo.toString()));
				return;
			}
		} catch (Exception e) {
			L.e(e.toString());
		}
	}
	File vFile;
	private static final int REQUEST_IMAGE = 2;
	public static final int VIEW_PHOTOS = 1;
	@ItemClick(R.id.gridview)
	void OnGridItemClick(int position) {
		if (position == mBitmapList.size()) {
			Intent intent = new Intent(Intent.ACTION_PICK,       
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, REQUEST_IMAGE);
		} else {
			ViewPhotosActivity_.intent(this).extra("position", position).imagePathList(imagePathList)
					.startForResult(VIEW_PHOTOS);
		}
	}
	@ItemClick(R.id.answer_gridview)
	void seeImageClick(int pos){
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		ImageView imgView = getView(pos);
		dialog.setView(imgView);
		dialog.show();
		imgView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}
	private ImageView getView(int pos) {
		ImageView imgView = new ImageView(this);
		imgView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				Picasso.with(getApplicationContext())
				.load(URLs.HTTP + URLs.HOST + "/public/portal/Check_image/" + editimgpath[pos])
				.into(imgView);

		return imgView;
	}
	
	@Click({R.id.con_sign,R.id.submit})
	void signOnClick(View v){
		switch (v.getId()) {
		case R.id.con_sign:
//			AutographActivity_.intent(this).start();
			break;
		case R.id.submit:
			DoWorkerUpload(station_code,mUserInfo.getId(), question_ID);
			break;
		default:
			break;
		}
	}
	
	@Background
	public void DoWorkerUpload(int station_code, int user_ID, int question_ID) {
		try {
			MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
			for (ImageItem path : mBitmapList) {
				// byte[] imageBytes = FileUtils.getFileData(path);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				path.getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, stream);
				ByteArrayResource image = new ByteArrayResource(stream.toByteArray(), path.getImagePath()) {
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

			}
			formData.add("typeID", String.valueOf(CAPTURE_PICTURE));
			formData.add("substationID", String.valueOf(station_code));
			// formData.add("roomID",String.valueOf(room_ID) );
			formData.add("userID", String.valueOf(user_ID));
			formData.add("questionID", String.valueOf(question_ID));
			L.e("@@@@formData" + formData);
			mApiClient.setHeader("Content-Type", MediaType.MULTIPART_FORM_DATA);
			ApiResponseUpLoad resp = mApiClient.CheckUpload(formData);

			L.e("上传照片........" + resp.getRet() + "" + resp.getData() + "formData"
					+ formData.get(SharedConst.UPLOAD_IMG));
			L.e("NewGrouping :" + resp.getNewGrouping());
			if (resp.getRet() == 0) {
				ShowResponse(resp);
				return;
			}
		} catch (Exception ex) {
			ApiResponseUpLoad resp = new ApiResponseUpLoad();
			resp.setRet(1);
			resp.setData("请求失败");
			ShowResponse(resp);
			L.e("Exception" + ex.toString());
			L.e("Exception" + resp.getRet());
		}

	}

	@UiThread
	public void ShowResponse(ApiResponseUpLoad resp) {
		if (resp.getRet() == 0) {
			T.showLong(this, "上传成功");
			finish();

		} else {
			T.showLong(this, resp.getData());
		}
	}
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_IMAGE:
				Uri uri = data.getData();  
	            if (!TextUtils.isEmpty(uri.getAuthority())) {  
	            	
	                //查询选择图片  
	                Cursor cursor = getContentResolver().query(  
	                        uri,  
	                        new String[] { MediaStore.Images.Media.DATA },  
	                        null,   
	                        null,   
	                        null);  
	                //返回 没找到选择图片  
	                if (null == cursor) {  
	                    return;  
	                }  
	                //光标移动至开头 获取图片路径  
	                cursor.moveToFirst();  
	               String pathImage = cursor.getString(cursor  
	                        .getColumnIndex(MediaStore.Images.Media.DATA));  
//	               FileInputStream fis = new FileInputStream(vFile);

					Display display = getWindowManager().getDefaultDisplay();
					int dw = display.getWidth();
					int dh = display.getHeight();

					// 加载图像
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;// 设置之后可以设置长宽
					Bitmap bitmap = BitmapFactory.decodeFile(pathImage, options);

					int heightRatio = (int) Math.ceil(options.outHeight / (float) dh);
					int widthRatio = (int) Math.ceil(options.outWidth / (float) dw);

					// 判断长宽哪个大
					if (heightRatio > 1 && widthRatio > 1) {
						if (heightRatio > widthRatio) {
							options.inSampleSize = heightRatio;
						} else {
							options.inSampleSize = widthRatio;
						}
					}
					// 对它进行真正的解码
					options.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeFile(pathImage, options);

					imagePathList.add(pathImage);
					ImageItem takePhoto = new ImageItem();
					takePhoto.setImagePath(pathImage);
					takePhoto.setBitmap(bitmap);
					mBitmapList.add(takePhoto);
					upadapter.SetBitmapList(mBitmapList);
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
				upadapter.SetBitmapList(mBitmapList);
				upadapter.notifyDataSetChanged();
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;
		private ArrayList<ImageItem> mBitmapList;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void SetBitmapList(ArrayList<ImageItem> imageList) {
			mBitmapList = imageList;
		}

		public int getCount() {
			return (mBitmapList.size() + 1);
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
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_image, parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.imageView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == mBitmapList.size()) {
				holder.image
						.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
			} else {
				holder.image.setImageBitmap(mBitmapList.get(position).getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

	}
	
	class ImgGridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private Context con;
		private String[] editimgpath;

		public ImgGridAdapter(Context con, String[] editimgpath) {
			this.editimgpath = editimgpath;
			inflater = LayoutInflater.from(con);
		}

		@Override
		public int getCount() {
			return editimgpath.length;
			
		}

		@Override
		public Object getItem(int position) {
			return editimgpath[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.lv_item_img, null);
				holder = new ViewHolder();
				holder.img = (ImageView) convertView.findViewById(R.id.lv_item_iv_img);
				LayoutParams p = holder.img.getLayoutParams();
				p.width = p.height = mGetScreenWidth() / 3 - 20;
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (editimgpath.length == 0) {
				holder.img.setImageResource(R.drawable.ic_launcher);
			}else{
			Picasso.with(getApplicationContext())
					.load(URLs.HTTP + URLs.HOST + "/public/portal/Check_image/" + editimgpath[position])
					.into(holder.img);
			}
			return convertView;
		}

		private class ViewHolder {
			ImageView img;
		}

		private int mGetScreenWidth() {
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			return dm.widthPixels;
		}
	}
}
