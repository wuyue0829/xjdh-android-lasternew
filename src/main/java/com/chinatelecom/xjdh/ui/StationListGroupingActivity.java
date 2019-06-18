package com.chinatelecom.xjdh.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppManager;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.ApiResponseStationList;
import com.chinatelecom.xjdh.bean.StationList;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

@EActivity(R.layout.activity_download_station)
public class StationListGroupingActivity extends Activity {
	// private static final String IMAGEURL
	// ="http://192.168.1.102/public/portal/Station_image/";// 本地IP
	private static final String IMAGEURL = "http://120.70.237.235/public/portal/Station_image/";// 服务器IP
	@ViewById(R.id.img_btn_reback)
	ImageButton img_btn_reback;
	@ViewById(R.id.lv_stationName_grouping)
	ListView lv_stationName_grouping;
	@RestService
	ApiRestClientInterface mApiClient;
	private stationGroupingAdapter adapter;
	private ProgressDialog pDialog;
//	private String stationName = "all";
	private List<StationList> stList = new ArrayList<StationList>();
	private Double Longitude;
	private Double latitude;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("局站列表");
		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
		pDialog = new ProgressDialog(this);
		pDialog.setMessage(getResources().getString(R.string.progress_loading_msg));
		adapter = new stationGroupingAdapter(this);
	}

	@AfterViews
	void initData() {
		pDialog.show();
		getData();
		adapter.setData(stList);
		lv_stationName_grouping.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	@Click(R.id.all_left_img)
	void goBack(){
		finish();
	}
	
//	@Click(R.id.navigation)
//	void goNavigation(){
//		NavigationActivity_.intent(this).start();
//	}
	@SuppressWarnings("deprecation")
	@UiThread
	void onPreferenceLogoutClicked() {
		final AlertDialog mExitDialog = new AlertDialog.Builder(StationListGroupingActivity.this).create();
		mExitDialog.setTitle("下线提示");
		mExitDialog.setIcon(R.drawable.index_btn_exit);
		mExitDialog.setMessage("您的账户已在另一个设备登录,请尝试重新登陆");
		mExitDialog.setButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mExitDialog.dismiss();
				String account = PreferenceUtils.getPrefString(StationListGroupingActivity.this, PreferenceConstants.ACCOUNT, "");
				PreferenceUtils.clearPreference(StationListGroupingActivity.this, PreferenceManager.getDefaultSharedPreferences(StationListGroupingActivity.this));
				PreferenceUtils.setPrefString(StationListGroupingActivity.this, PreferenceConstants.ACCOUNT, account);
				AppManager.getAppManager().finishAllActivity();
				LoginActivity_.intent(StationListGroupingActivity.this).start();
			}
		});
		mExitDialog.show();
	}
	@Background
	public void getData() {
		try {
			ApiResponse apiResp = mApiClient.stationList();
			L.d("55555555555", apiResp.toString());
			if (apiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				ApiResponseStationList apiResponseStationList = mapper.readValue(apiResp.getData(),
						ApiResponseStationList.class);
				List<StationList> list = Arrays.asList(apiResponseStationList.getStationLists());
				stList.addAll(list);

				L.e( "data :" + apiResp.getData());
				adapter.setData(stList);
				L.e("getRet  :" + apiResp.getRet());
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
			lv_stationName_grouping.setAdapter(adapter);
			T.showLong(this, "加载成功");
			pDialog.dismiss();
		} else {
			T.showLong(this, apiResp.getData());
		}
	}

	@Click(R.id.img_btn_reback)
	void rebackButtonClicked() {
		this.finish();
	}

	@ItemClick(R.id.lv_stationName_grouping)
	void stationGroupingClicked(int position) {
		// Toast.makeText(this, "第" + position + "个被点击", 0).show();
		Longitude = stList.get(position).getLng();
		latitude = stList.get(position).getLat();
		/*
		 * StationListActivity_.intent(this).substation_id(stList.get(position).
		 * getSubstation_id())
		 * .groupoingName(stList.get(position).getNewGrouping()).Longitude(
		 * Longitude).latitude(latitude)
		 * .station_name(stList.get(position).getName()).start();
		 */
		StationGroupingActivity_.intent(this).substation_id(stList.get(position).getId().toString())
				.station_name(stList.get(position).getName()).Longitude(Longitude).latitude(latitude).start();
	}


	public class stationGroupingAdapter extends BaseAdapter {
		private Context mContext;
		private LayoutInflater inflater;
		private List<StationList> data;

		public stationGroupingAdapter(Context Context) {
			super();
			this.mContext = Context;
			inflater = LayoutInflater.from(mContext);
		}

		public void setData(List<StationList> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.station_list_grouping_pattern, null);
				holder = new ViewHolder();
				holder.tv_station_name = (TextView) convertView.findViewById(R.id.tv_show_stationName);
				holder.tv_grouping = (TextView) convertView.findViewById(R.id.tv_show_grouping);
				holder.tv_id = (TextView) convertView.findViewById(R.id.tv_id);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_id.setText(String.valueOf(position +1));
			holder.tv_station_name.setText(data.get(position).getName().toString());
			holder.tv_grouping.setText(data.get(position).getNewGrouping());
			return convertView;
		}

	}

	public class ViewHolder {
		TextView tv_station_name;// 局站名称
		TextView tv_grouping;// 分组
		TextView tv_id;// ID
	}
}
