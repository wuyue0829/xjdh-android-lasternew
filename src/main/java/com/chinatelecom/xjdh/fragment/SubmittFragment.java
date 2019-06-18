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
import com.chinatelecom.xjdh.adapter.EditQuestionAdapter;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.ApiResponseStationList;
import com.chinatelecom.xjdh.bean.EditQuestion;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.ui.EditActivity_;
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
public class SubmittFragment extends Fragment {
	
	@ViewById(R.id.lv_stationName_grouping)
	ListView lv_stationName_grouping;
	@RestService
	ApiRestClientInterface mApiClient;

	@ViewById(R.id.srl_alarm)
	SwipeRefreshLayout mSrlAlarm;
	private List<EditQuestion> stList = new ArrayList<EditQuestion>();
	private EditQuestionAdapter adapter;
	private final static int TYPE=1;
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		String token = PreferenceUtils.getPrefString(getActivity(), PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
		adapter = new EditQuestionAdapter(getActivity());

	}
	
	 int station_code;
		public void SetData(int station_code) {
			// TODO Auto-generated method stub
			this.station_code=station_code;
		}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
			stList.clear();
			getQuestionData(TYPE,station_code);
			adapter.notifyDataSetChanged();
	}

	@AfterViews
	void show() {
		mSrlAlarm.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				mSrlAlarm.setRefreshing(false);
				stList.clear();
				getQuestionData(TYPE,station_code);
			}
		});
	}
	

	@Background
	void getQuestionData(int type,int station_ID) {
		try {
			ApiResponse apiResp = mApiClient.getEditQuestions(type,station_ID);
			L.d("---------------", apiResp.getData());
			if (apiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				ApiResponseStationList apiResponseStationList =

				mapper.readValue(apiResp.getData(), ApiResponseStationList.class);
				List<EditQuestion> list = Arrays.asList(apiResponseStationList.getSubContentList());
				stList.addAll(list);
				if (apiResp.getRet() == 0) {
					ShowResponse(apiResp);
				}
			}
		} catch (Exception e) {
			L.e("Exception" + e.toString());
		}
	}
	

	@ItemClick(R.id.lv_stationName_grouping)
	void questionClick(int pos) {
		EditActivity_.intent(this).station_code(station_code).question(stList.get(pos).getQuestion())
			.question_ID(stList.get(pos).getQuestion_id()).editimgpath(stList.get(pos).getContent()).start();
		
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
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().finish();
	}
	  
}

