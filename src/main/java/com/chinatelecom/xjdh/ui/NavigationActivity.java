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
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.ApiResponseStationList;
import com.chinatelecom.xjdh.bean.StationList;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

@EActivity(R.layout.activity_navigation)
public class NavigationActivity extends BaseActivity{
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
		setTitle("局站导航");
		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
		pDialog = new ProgressDialog(this);
		pDialog.setMessage(getResources().getString(R.string.progress_loading_msg));
		adapter = new stationGroupingAdapter(this);
	}

	@AfterViews
	void initData() {
		pDialog.show();
//		getData(stationName);
		adapter.setData(stList);
		lv_stationName_grouping.setAdapter(adapter);
		adapter.notifyDataSetChanged();
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

				L.e("data :" + apiResp.getData());
				adapter.setData(stList);
				L.e("getRet  :" + apiResp.getRet());
				if (apiResp.getRet() == 0) {
					ShowResponse(apiResp);
				}
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
//		Intent intent = new Intent(NavigationActivity.this, RoutePlanDemo_.class);
//		intent.putExtra("longitude", Longitude);
//		intent.putExtra("latitude", latitude);
//		intent.putExtra("adress", stList.get(position).getName());
//		startActivity(intent);
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
			holder.tv_id.setText(data.get(position).getId().toString());
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
