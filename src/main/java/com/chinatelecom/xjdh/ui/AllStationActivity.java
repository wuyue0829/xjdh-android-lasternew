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
import com.chinatelecom.xjdh.adapter.FaultAdapter;
import com.chinatelecom.xjdh.bean.AllID;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.ApiResponseStationList;
import com.chinatelecom.xjdh.bean.DeviceFault;
import com.chinatelecom.xjdh.bean.UserInfo;
import com.chinatelecom.xjdh.fragment.AllStationFragment_;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.widget.ListView;

@EActivity(R.layout.data_list_view)
public class AllStationActivity extends BaseActivity{
	
	@ViewById
	ListView lv_bts_alarm;
	AllID allID=new AllID();
	@RestService
	ApiRestClientInterface mApiClient;
	private FaultAdapter adapter;
	UserInfo mUserInfo;
	@ViewById
	SwipeRefreshLayout mSrlAlarm;
	private List<DeviceFault> stList = new ArrayList<DeviceFault>();
	private final static int LOCATYPE=4;
	ProgressDialog pDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String token = PreferenceUtils.getPrefString(this,PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
		adapter = new FaultAdapter(this);
	}
	String cityid,counid,subid,roomid;
	public void onSearchClicked(String cityid, String counid, String subid, String roomid) {
		menu.toggle();
		this.cityid = cityid;
		this.counid = counid;
		this.subid = subid;
		this.roomid = roomid;
		pDialog.show();
		getData(LOCATYPE, roomid);
	}
	SlidingMenu menu;
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		stList.clear();
//		getData(LOCATYPE,roomid);
//		
//	}

	@AfterViews
	void showData() {
		menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setShadowWidth(40);
//		menu.setBehindWidth(430);//设置SlidingMenu菜单的宽度
		menu.setFadeDegree(0.35f);//SlidingMenu滑动时的渐变程度
		menu.setFadeDegree(0f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMode(SlidingMenu.LEFT); 
		menu.setMenu(R.layout.bts_alarm_menu);
		AllStationFragment_ fragment = new AllStationFragment_();
		getSupportFragmentManager().beginTransaction().replace(R.id.alarm_search, fragment).commit();
		menu.toggle();
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("正在查询，请稍后...");
		mSrlAlarm.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				mSrlAlarm.setRefreshing(false);
				if(TextUtils.isEmpty(roomid))
				{
					return;
				}
				pDialog.show();
				stList.clear();
				getData(LOCATYPE,roomid);
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			menu.showContent();
		} else {
			super.onBackPressed();
		}
	}
	@Background
	void getData(int type,String roomID) {
		try {
			int rooid=Integer.valueOf(roomID);
			ApiResponse apiResp = mApiClient.getLocations(type,rooid);
			L.d("22222222222222222222", apiResp.getData());
			if (apiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				ApiResponseStationList apiResponseStationList = mapper.readValue(apiResp.getData(),
						ApiResponseStationList.class);
				List<DeviceFault> list = Arrays.asList(apiResponseStationList.getDeviceList());
				stList.clear();
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
		pDialog.dismiss();
		if (apiResp.getRet() == 0) {
			pDialog.dismiss();
			adapter.setData(stList);
			lv_bts_alarm.setAdapter(adapter);
			T.showLong(this, "加载成功");
		} else {
			T.showLong(this, apiResp.getData());
		}
		adapter.notifyDataSetChanged();
	}

	@ItemClick(R.id.lv_bts_alarm)
	void stationGroupingClicked(int position) {
		SubmitFaultActivity_.intent(this)
		.station_code(Integer.valueOf(subid))
		.roomID(Integer.valueOf(roomid))
		.deviceID(stList.get(position).getData_id())
		.deviceName(stList.get(position).getName())
		.start();
	}

}
