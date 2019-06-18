package com.chinatelecom.xjdh.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.adapter.DevInfoAdapter;
import com.chinatelecom.xjdh.adapter.DevQuestionAdapter;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.ApiResponseStationList;
import com.chinatelecom.xjdh.bean.DevQuestion;
import com.chinatelecom.xjdh.bean.DeviceInfo;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.widget.ImageButton;
import android.widget.ListView;

@EActivity(R.layout.activity_download_station)
public class ComputerRoomActivity extends BaseActivity{
	@Extra
	int station_code;
	@Extra
	int roomID;
	@ViewById(R.id.img_btn_reback)
	ImageButton img_btn_reback;
	@ViewById(R.id.lv_stationName_grouping)
	ListView lv_stationName_grouping;
	@RestService
	ApiRestClientInterface mApiClient;
	private List<DevQuestion> stList = new ArrayList<DevQuestion>();
	private DevQuestionAdapter adapter;
	@ViewById(R.id.srl_alarm)
	SwipeRefreshLayout mSrlAlarm;
	private static int TYPE=2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("局站验收");
		String token = PreferenceUtils.getPrefString(this,PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
		adapter = new DevQuestionAdapter(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
			stList.clear();
			getData(TYPE,roomID);
			adapter.notifyDataSetChanged();
	}
	@AfterViews
	void initData() {
		mSrlAlarm.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				mSrlAlarm.setRefreshing(false);
				stList.clear();
				getData(TYPE,roomID);
			}
		});
	}

	@Background
	public void getData(int type,int code) {
		try {
			ApiResponse apiResp = mApiClient.getEditQuestions(type,code);
			L.d("22222222222222222222",apiResp.getData());
			if (apiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				ApiResponseStationList apiResponseStationList =

				mapper.readValue(apiResp.getData(), ApiResponseStationList.class);
				List<DevQuestion> list = Arrays.asList(apiResponseStationList.getDevContentList());
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
			T.showLong(this, "加载成功");
		} else {
			T.showLong(this, apiResp.getData());
		}
	}

	@ItemClick(R.id.lv_stationName_grouping)
	void groupingClicked(int position) {
		EditDevActivity_.intent(this).room_ID(roomID)
		.station_code(station_code)
		.devQuestion(stList.get(position))
		.imageList(stList.get(position).getContent()).start();
	}

}