package com.chinatelecom.xjdh.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.ListView;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.adapter.RoomAdapter;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.ApiResponseStationList;
import com.chinatelecom.xjdh.bean.RoomSubs;
import com.chinatelecom.xjdh.bean.UserInfo;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.ui.ComputerRoomActivity_;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@EFragment(R.layout.activity_download_station)
public class DoneFragment extends Fragment{
	
	@ViewById(R.id.lv_stationName_grouping)
	ListView lv_stationName_grouping;
	@RestService
	ApiRestClientInterface mApiClient;
	private RoomAdapter adapter;
	
	@ViewById(R.id.srl_alarm)
	SwipeRefreshLayout mSrlAlarm;
	private List<RoomSubs> stList = new ArrayList<RoomSubs>();
//	private static int APPLY=1;
	private static int LOCATIONTYPE=2;
	UserInfo mUserInfo;
	int station_code;
	public void SetData(int station_code) {
		// TODO Auto-generated method stub
		this.station_code = station_code;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		String token = PreferenceUtils.getPrefString(getActivity(),PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
		adapter = new RoomAdapter(getActivity());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		stList.clear();
		getData(station_code,LOCATIONTYPE,LOCATIONTYPE);
		adapter.notifyDataSetChanged();
	}
	
	@AfterViews
	void initData() {
		mSrlAlarm.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				
				mSrlAlarm.setRefreshing(false);
				stList.clear();
				getData(station_code,LOCATIONTYPE,LOCATIONTYPE);
			}
		});
	}

	@Background
	public void getData(int code,int type,int locationType) {
		try {
			ApiResponse apiResp = mApiClient.getEditSubs(code, type, locationType);
			L.d("22222222222222222222",apiResp.getData());
			if (apiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				ApiResponseStationList apiResponseStationList =

				mapper.readValue(apiResp.getData(), ApiResponseStationList.class);
				List<RoomSubs> list = Arrays.asList(apiResponseStationList.getRoomsList());
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
		} else {
			T.showLong(getActivity(), apiResp.getData());
		}
	}

	@ItemClick(R.id.lv_stationName_grouping)
	void groupingClicked(int position) {
		ComputerRoomActivity_.intent(this).roomID(stList.get(position).getRoom_id()).station_code(station_code).start();
	}
}

