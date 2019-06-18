package com.chinatelecom.xjdh.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.codehaus.jackson.map.ObjectMapper;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.adapter.DeviceDataAdapter;
import com.chinatelecom.xjdh.bean.BoardJsonData;
import com.chinatelecom.xjdh.tool.BluetoothTool;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.T;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

@EActivity(R.layout.activity_energy_view)
public class SMDDeviceMonitor extends BaseActivity {

	@Extra
	String model;
	@ViewById
	ListView devList;
	DeviceDataAdapter adapter = new DeviceDataAdapter(this);
	private ObjectMapper objectMapper = new ObjectMapper();
	@ViewById
	Button btnRefresh;
	@ViewById(R.id.srl_alarm)
	SwipeRefreshLayout mSrlAlarm;
	@AfterViews
	public void Show() {
		btnRefresh.setVisibility(View.GONE);
		adapter.setModel(model);
		BluetoothTool.SendCmd("{\"cmd\": \"call_data\"}\r\n");
		ReadData();
		mSrlAlarm.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				
				mSrlAlarm.setRefreshing(false);
				BluetoothTool.SendCmd("{\"cmd\": \"call_data\"}\r\n");
				ReadData();
			}
		});
	}

	@Click(R.id.btnRefresh)
	public void RefreshData() {
		BluetoothTool.SendCmd("{\"cmd\": \"call_data\"}\r\n");
		ReadData();
	}



	BoardJsonData boardData;

	@Background
	public void ReadData() {
		String jsonData = BluetoothTool.readMsg();
		L.d("------------", jsonData);
		if (jsonData.isEmpty()) {
			ShowMessage("请求数据失败");
			finish();
		} else {
			try {
				boardData = (BoardJsonData) objectMapper.readValue(jsonData, BoardJsonData.class);
				L.d("8888888888888", boardData.toString());
				ShowData();
			} catch (Exception e) {
				Log.d("bluetooth", e.getMessage());
			}
		}
	}

	@UiThread
	public void ShowData() {
		adapter.setBoardData(boardData);
		devList.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	@UiThread
	public void ShowMessage(String msg) {
		T.showShort(this, msg);
	}
}
