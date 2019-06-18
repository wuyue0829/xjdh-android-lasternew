package com.chinatelecom.xjdh.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.DevTypeItem;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

@EActivity(R.layout.activity_download_station)
public class DoingToolActivity extends BaseActivity{
	@Extra
	int room_ID;
	@Extra
	int Station_ID;

	@ViewById(R.id.lv_stationName_grouping)
	ListView lv_stationName_grouping;
	@RestService
	ApiRestClientInterface mApiClient;
	List<DevTypeItem> mDevTypeList = new ArrayList<>();
	List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
	SimpleAdapter mDevTypeAdapter;
	@ViewById(R.id.srl_alarm)
	SwipeRefreshLayout mSrlAlarm;
	private final static int TYPE = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
		mDevTypeAdapter = new SimpleAdapter(this, listItem, R.layout.question_item,
				new String[] { "name" }, new int[] {R.id.question });
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getQuestionData(TYPE, room_ID);
		mDevTypeAdapter.notifyDataSetChanged();
	}

	@AfterViews
	void show() {
		
		mSrlAlarm.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				mSrlAlarm.setRefreshing(false);
				mDevTypeList.clear();
				getQuestionData(TYPE, room_ID);
			}
		});
	}

	@Background
	void getQuestionData(int type, int room_ID) {
		try {
			ApiResponse apiResp = mApiClient.GetQuestion(type, room_ID);
			L.d("---------------", apiResp.getData());
			if (apiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				List<DevTypeItem> l = mapper.readValue(apiResp.getData(), new TypeReference<List<DevTypeItem>>() {

				});
				mDevTypeList.clear();
				mDevTypeList.addAll(l);
//				int index = 1;
				listItem.clear();
				for (DevTypeItem devTypeItem : l) {
					L.d("============", mapper.writeValueAsString(devTypeItem));
					Map<String, Object> item = new HashMap<>();
//					item.put("num", String.valueOf(index++));
					item.put("name", devTypeItem.getName());
					listItem.add(item);
				}
			}
		} catch (Exception e) {
			L.e("Exception" + e.toString());
		}
		updateRoomDevListView();
	}

	@UiThread
	void updateRoomDevListView() {
		lv_stationName_grouping.setAdapter(mDevTypeAdapter);
		mDevTypeAdapter.notifyDataSetChanged();
	}

	@ItemClick(R.id.lv_stationName_grouping)
	void questionClick(int pos) {
		ToolUploadActivity_.intent(this).pos(pos).devTypeItem(mDevTypeList.get(pos)).station_code(Station_ID)
				.room_ID(room_ID).start();
	}

	// @UiThread
	// public void ShowResponse(ApiResponse apiResp) {
	// if (apiResp.getRet() == 0) {
	// adapter.setData(stList);
	// lv_stationName_grouping.setAdapter(adapter);
	// T.showLong(getActivity(), "加载成功");
	// } else {
	// T.showLong(getActivity(), apiResp.getData());
	// }
	// }

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		finish();
	}


}
