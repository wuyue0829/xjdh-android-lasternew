package com.chinatelecom.xjdh.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.adapter.CityStationAdapter;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.ApiResponseStationList;
import com.chinatelecom.xjdh.bean.CheckCityList;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.ui.DoingToolActivity_;
import com.chinatelecom.xjdh.ui.EquipmentSubActivity_;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.ListView;
@EFragment(R.layout.activity_download_station)
public class DoingFragment extends Fragment{

	
	
	@ViewById(R.id.lv_stationName_grouping)
	ListView lv_stationName_grouping;
	@RestService
	ApiRestClientInterface mApiClient;
	private CityStationAdapter adapter;
	@ViewById(R.id.srl_alarm)
	SwipeRefreshLayout mSrlAlarm;
	private List<CheckCityList> stList = new ArrayList<CheckCityList>();
	private static int LOCATIONTYPE=2;
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		String token = PreferenceUtils.getPrefString(getActivity(),PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
		adapter = new CityStationAdapter(getActivity());
	}
	int Station_ID;
	public void SetData(int Station_ID) {
		// TODO Auto-generated method stub
		this.Station_ID = Station_ID;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		stList.clear();
		getData(LOCATIONTYPE,Station_ID,LOCATIONTYPE);
		adapter.notifyDataSetChanged();
	}

	@AfterViews
	void initData() {
		
		mSrlAlarm.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				
				mSrlAlarm.setRefreshing(false);
				stList.clear();
				getData(LOCATIONTYPE,Station_ID,LOCATIONTYPE);
			}
		});
	}

	@Background
	public void getData(int type,int stationid,int locationType) {
		try {
			ApiResponse apiResp = mApiClient.GetLocation(type, stationid,locationType);
					L.d("22222222222222222222",apiResp.getData());
			if (apiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				ApiResponseStationList apiResponseStationList =

				mapper.readValue(apiResp.getData(), ApiResponseStationList.class);
				List<CheckCityList> list = Arrays.asList(apiResponseStationList.getCheckCityList());
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
			T.showLong(getActivity(), "加载成功");
//			pDialog.dismiss();
		} else {
			T.showLong(getActivity(), apiResp.getData());
		}
	}

	@ItemClick(R.id.lv_stationName_grouping)
	void groupingClicked(int position) {
		DoingToolActivity_.intent(this).room_ID(stList.get(position).getCity_code()).Station_ID(Station_ID).start();
	}
	
	

}

