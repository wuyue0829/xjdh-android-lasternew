package com.chinatelecom.xjdh.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.adapter.CityAdapter;
import com.chinatelecom.xjdh.adapter.CityInfoAdapter;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.ApiResponseStationList;
import com.chinatelecom.xjdh.bean.City;
import com.chinatelecom.xjdh.bean.Country;
import com.chinatelecom.xjdh.bean.UserInfo;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.ListView;

@EActivity(R.layout.activity_download_station)
public class SecurityActivity extends BaseActivity {
	@ViewById(R.id.lv_stationName_grouping)
	ListView lv_stationName_grouping;
	@RestService
	ApiRestClientInterface mApiClient;
	private CityInfoAdapter adapter;
	UserInfo mUserInfo;
	@ViewById(R.id.srl_alarm)
	SwipeRefreshLayout mSrlAlarm;
	private List<City> stList = new ArrayList<City>();
	private final static int LOCATYPE=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String token = PreferenceUtils.getPrefString(this,PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
		adapter = new CityInfoAdapter(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		stList.clear();
		getData(LOCATYPE);
	}
	
	@AfterViews
	void showData() {
		mSrlAlarm.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				mSrlAlarm.setRefreshing(false);
				stList.clear();
				getData(LOCATYPE);
			}
		});
	}
	

	@Background
	void getData(int type) {
		try {
			ApiResponse apiResp = mApiClient.getLocations(type);
			L.d("22222222222222222222", apiResp.getData());
			if (apiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				ApiResponseStationList apiResponseStationList = mapper.readValue(apiResp.getData(),
						ApiResponseStationList.class);
				List<City> list = Arrays.asList(apiResponseStationList.getCityList());
				stList.addAll(list);
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
			adapter.setData(stList);
			lv_stationName_grouping.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			T.showLong(this, "加载成功");
		} else {
			T.showLong(this, apiResp.getData());
		}
	}

	@ItemClick(R.id.lv_stationName_grouping)
	void stationGroupingClicked(int position) {
		StationPhotoActivity_.intent(this).city_code(stList.get(position).getCity_code()).start();
	}

}
