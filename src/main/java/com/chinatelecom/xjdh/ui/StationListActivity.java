package com.chinatelecom.xjdh.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppManager;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.ApiResponseStationList;
import com.chinatelecom.xjdh.bean.StationImg;
import com.chinatelecom.xjdh.bean.StationList;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author admin
 *
 */
@EActivity(R.layout.activity_station_list)
public class StationListActivity extends BaseActivity {

	@ViewById(R.id.list_station_cache)
	ListView list_station_cache;
	List<StationList> stList = new ArrayList<StationList>();
	private StationListAdapter adapter;

//	private static final String IMAGEURL = "http://192.168.1.101/public/portal/Station_image/";// 本地IP
	 private static final String IMAGEURL = "http://120.70.237.235/public/portal/Station_image/";// 服务器IP
	ImageSize mImageSize = new ImageSize(300, 300);// 设置图片的大小
	DisplayImageOptions options;
	List<StationImg> img;
	@RestService
	ApiRestClientInterface mApiClient;
	ProgressDialog pDialog;
	@ViewById(R.id.ll_search)
	LinearLayout ll_search;
	@ViewById(R.id.ll_confirm)
	LinearLayout ll_confirm;
	@ViewById(R.id.et_station_name)
	EditText et_station_name;
	private String stationName = "all";
	@Extra
	String substation_id;
	@Extra
	double Longitude;
	@Extra
	double latitude;
	@Extra
	String groupingName;
	@Extra
	String station_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
		pDialog = new ProgressDialog(this);
		pDialog.setMessage(getResources().getString(R.string.progress_loading_msg));
		adapter = new StationListAdapter(this);
		showImage();
	}

	private void showImage() {
		// 显示图片的配置
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.sc_publish_spinner) // resource//
				.showImageForEmptyUri(R.drawable.defaultcovers) // resource or
				.showImageOnFail(R.drawable.defaultcovers) // resource or //
				.resetViewBeforeLoading(true) // default
				.delayBeforeLoading(1000).cacheInMemory(true) // default
				.cacheOnDisk(true) // default
				.preProcessor(null).postProcessor(null).considerExifParams(true) // default
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
				.bitmapConfig(Bitmap.Config.ARGB_8888) // default
				.displayer(new SimpleBitmapDisplayer()) // default
				.handler(new Handler()) // default
				.build();
	}

	@AfterViews
	void initData() {
		pDialog.show();
		getData(stationName);
		adapter.setData(stList);
		list_station_cache.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	// private String imgs[];
	@SuppressWarnings("deprecation")
	@UiThread
	void onPreferenceLogoutClicked() {
		final AlertDialog mExitDialog = new AlertDialog.Builder(StationListActivity.this).create();
		mExitDialog.setTitle("下线提示");
		mExitDialog.setIcon(R.drawable.index_btn_exit);
		mExitDialog.setMessage("您的账户已在另一个设备登录,请尝试重新登陆");
		mExitDialog.setButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mExitDialog.dismiss();
				String account = PreferenceUtils.getPrefString(StationListActivity.this, PreferenceConstants.ACCOUNT, "");
				PreferenceUtils.clearPreference(StationListActivity.this, PreferenceManager.getDefaultSharedPreferences(StationListActivity.this));
				PreferenceUtils.setPrefString(StationListActivity.this, PreferenceConstants.ACCOUNT, account);
				AppManager.getAppManager().finishAllActivity();
				LoginActivity_.intent(StationListActivity.this).start();
			}
		});
		mExitDialog.show();
	}
	@Background
	public void getData(String stationName) {

		try {
			ApiResponse apiResp = mApiClient.newGrouping(groupingName, substation_id);
			if (apiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				ApiResponseStationList apiResponseStationList = mapper.readValue(apiResp.getData(),
						ApiResponseStationList.class);
				List<StationList> list = Arrays.asList(apiResponseStationList.getStationLists());
				stList.addAll(list);
				adapter.setData(stList);
				L.e("getRet  :" + apiResp.getRet());
				L.e("JSON格式:" + apiResp.getData() + "   " + apiResp.getRet());
				if (apiResp.getRet() == 0) {
					ShowResponse(apiResp);
				}
			}else if(apiResp.getData().equals("Access token is not valid")){
				onPreferenceLogoutClicked();
			}
		} catch (Exception e) {
			L.e("Exception" + e.toString());
		}
	}

	@UiThread
	public void ShowResponse(ApiResponse apiResp) {
		if (apiResp.getRet() == 0) {
			list_station_cache.setAdapter(adapter);
			T.showLong(this, "加载成功");
			pDialog.dismiss();
		} else {
			T.showLong(this, apiResp.getData());
		}

	}

	public class StationListAdapter extends BaseAdapter {
		private Context mContext;
		private LayoutInflater inflater;
		private List<StationList> data;

		public StationListAdapter(Context context) {
			super();
			mContext = context;
			inflater = LayoutInflater.from(mContext);
		}

		public void setData(List<StationList> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			// return 3;
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder horder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.activity_station_list_pattern, null);
				horder = new ViewHolder();
				horder.tv_station_name = (TextView) convertView.findViewById(R.id.tv_station_name_cache);
				horder.tv_longitude_cache = (TextView) convertView.findViewById(R.id.tv_longitude_cache);
				horder.tv_latitude_cache = (TextView) convertView.findViewById(R.id.tv_latitude_cache);
				// horder.btn_cache = (Button)
				// convertView.findViewById(R.id.btn_cache);
				horder.imageView = (ImageView) convertView.findViewById(R.id.img_cache);
				convertView.setTag(horder);
			} else {
				horder = (ViewHolder) convertView.getTag();
			}
			ImageLoader.getInstance().displayImage(IMAGEURL + data.get(position).getStationImage().toString(),
					horder.imageView, options);
			/*
			 * ObjectMapper mapper = new ObjectMapper(); String[] imageList; try
			 * { imageList =
			 * mapper.readValue(stList.get(position).getStationImage(),
			 * String[].class); for (int i = 0; i < imageList.length; i++) {
			 * ImageLoader.getInstance().displayImage(IMAGEURL + imageList[i],
			 * horder.imageView, options);
			 * L.e("-----------------------"+imageList[i]); }
			 * ImageLoader.getInstance().displayImage(IMAGEURL
			 * +data.get(position).getStationImage().toString(),
			 * horder.imageView, options); } catch (Exception e) {
			 * e.printStackTrace(); }
			 */
			horder.tv_station_name.setText(station_name + " :" + data.get(position).getNewGrouping());
			horder.tv_longitude_cache.setText(Longitude + "");
			horder.tv_latitude_cache.setText(latitude + "");
			return convertView;
		}

		class ViewHolder {
			TextView tv_station_name;// 局站名称
			TextView tv_longitude_cache;// 经度
			TextView tv_latitude_cache;// 纬度
			Button btn_cache;// 缓存
			ImageView imageView;
		}
	}

//	@ItemClick(R.id.list_station_cache)
//	void cacheImagClicked(int position) {
//		Intent intent = new Intent(StationListActivity.this, RoutePlanDemo_.class);
//		intent.putExtra("longitude", Longitude);
//		intent.putExtra("latitude", latitude);
//		intent.putExtra("adress", station_name);
//		startActivity(intent);
//	}

	/*@Click(R.id.img_btn_search)
	void searchClicked() {
		list_station_cache.setVisibility(View.GONE);
		ll_search.setVisibility(View.VISIBLE);
	
	}*/

	/**
	 * 返回
	 */
//	@Click(R.id.img_btn_left)
//	void rebackClicked() {
//		list_station_cache.setVisibility(View.VISIBLE);
//		ll_search.setVisibility(View.GONE);
//		this.finish();
//	}

	/**
	 * 确定搜索局站
	 */
	@Click(R.id.btn_search_station)
	void searchStationClicked() {
		if (et_station_name.getText().toString() == null || et_station_name.getText().toString().equals("")) {
			et_station_name.setError("局站名称不能为空");
			return;
		}
		pDialog.show();
		list_station_cache.setVisibility(View.VISIBLE);
		ll_search.setVisibility(View.GONE);
		stList.clear();
		adapter.notifyDataSetChanged();
		stationName = et_station_name.getText().toString();
		// getData(stationName);
		L.e("stationName  :" + stationName);
	}
}