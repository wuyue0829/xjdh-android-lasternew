package com.chinatelecom.xjdh.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
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
import android.widget.ListView;
import android.widget.TextView;

@EActivity(R.layout.activity_station_grouping)
public class StationGroupingActivity extends Activity {
	private ProgressDialog pDialog;

	@ViewById(R.id.tv_grouping_id)
	TextView tv_grouping_id;
	@ViewById(R.id.tv_grouping_name)
	TextView tv_grouping_name;
	@ViewById(R.id.lv_grouping_name)
	ListView lv_grouping_name;
	@RestService
	ApiRestClientInterface mApiClient;
	private GroupingNameAdapter adapter;

	@Extra
	String substation_id;
	@Extra
	String station_name;
	@Extra
	double Longitude;
	@Extra
	double latitude;
	private List<StationList> stList = new ArrayList<StationList>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
		pDialog = new ProgressDialog(this);
		pDialog.setMessage(getResources().getString(R.string.progress_loading_msg));
		adapter = new GroupingNameAdapter(this);
	}

	@AfterViews
	void initData() {
		pDialog.show();
		getData(substation_id);
		adapter.setData(stList);
		lv_grouping_name.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	@SuppressWarnings("deprecation")
	@UiThread
	void onPreferenceLogoutClicked() {
		final AlertDialog mExitDialog = new AlertDialog.Builder(StationGroupingActivity.this).create();
		mExitDialog.setTitle("下线提示");
		mExitDialog.setIcon(R.drawable.index_btn_exit);
		mExitDialog.setMessage("您的账户已在另一个设备登录,请尝试重新登陆");
		mExitDialog.setButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mExitDialog.dismiss();
				String account = PreferenceUtils.getPrefString(StationGroupingActivity.this, PreferenceConstants.ACCOUNT, "");
				PreferenceUtils.clearPreference(StationGroupingActivity.this, PreferenceManager.getDefaultSharedPreferences(StationGroupingActivity.this));
				PreferenceUtils.setPrefString(StationGroupingActivity.this, PreferenceConstants.ACCOUNT, account);
				AppManager.getAppManager().finishAllActivity();
				LoginActivity_.intent(StationGroupingActivity.this).start();
			}
		});
		mExitDialog.show();
	}
	@Background
	public void getData(String substation_id) {
		ApiResponse apiResp = mApiClient.Group(substation_id);
		L.d("55555555555", apiResp.getData());
		if (apiResp.getRet() == 0) {
			ObjectMapper mapper = new ObjectMapper();
			ApiResponseStationList apiResponseStationList;
			try {
				apiResponseStationList = mapper.readValue(apiResp.getData(), ApiResponseStationList.class);
				List<StationList> list = Arrays.asList(apiResponseStationList.getStationLists());
				stList.addAll(list);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			L.e("------" + apiResp.getData() + "     " + apiResp.getRet());
			if (apiResp.getRet() == 0) {
				ShowResponse(apiResp);
			}
		}else if(apiResp.getData().equals("Access token is not valid")){
			onPreferenceLogoutClicked();
		}
	}

	@UiThread
	public void ShowResponse(ApiResponse apiResp) {
		if (apiResp.getRet() == 0) {
			lv_grouping_name.setAdapter(adapter);
			T.showLong(this, "加载成功");
			pDialog.dismiss();
		} else {
			T.showLong(this, apiResp.getData());
		}
	}

	@ItemClick(R.id.lv_grouping_name)
	void groupingClicked(int position) {
		StationListActivity_.intent(this).substation_id(substation_id)
				.groupingName(stList.get(position).getNewGrouping().toString()).Longitude(Longitude).latitude(latitude).station_name(station_name).start();
	}

	@Click(R.id.img_btn_reback)
	void rebackClicked() {
		this.finish();
	}

	public class GroupingNameAdapter extends BaseAdapter {
		private Context mContext;
		private LayoutInflater inflater;
		private List<StationList> data;

		public GroupingNameAdapter(Context Context) {
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
				convertView = inflater.inflate(R.layout.activity_station_grouping_pattern, null);
				holder = new ViewHolder();
				holder.tv_grouping_id = (TextView) convertView.findViewById(R.id.tv_grouping_id);
				holder.tv_grouping_name = (TextView) convertView.findViewById(R.id.tv_grouping_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_grouping_id.setText(position + 1 + "");
			holder.tv_grouping_name.setText(data.get(position).getNewGrouping().toString());
			return convertView;
		}

	}

	public class ViewHolder {
		TextView tv_grouping_name;// 分组名称
		TextView tv_grouping_id;// ID

	}
}
