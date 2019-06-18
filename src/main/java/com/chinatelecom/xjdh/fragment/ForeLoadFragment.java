package com.chinatelecom.xjdh.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.codehaus.jackson.map.ObjectMapper;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.adapter.EnergyDataAdapter;
import com.chinatelecom.xjdh.bean.EnergyData;
import com.chinatelecom.xjdh.tool.BluetoothTool;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.T;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
@EFragment(R.layout.activity_smddevice_monitor)
public class ForeLoadFragment  extends Fragment{
	
	String model;
	public void setData(String model)
	{
		this.model = model;
	}
	@ViewById
	Button btnRefresh;
	@ViewById
	ListView devList;
	EnergyDataAdapter adapter ;
	private ObjectMapper objectMapper = new ObjectMapper();
	@ViewById(R.id.srl_alarm)
	SwipeRefreshLayout mSrlAlarm;
	@AfterViews
	public void Show()
	{
		btnRefresh.setVisibility(View.GONE);
		adapter= new EnergyDataAdapter(getActivity());
		adapter.setModel(model);
		BluetoothTool.SendCmd("{\"cmd\": \"get_powerinfo_4\"}\r\n");
		ReadData();
		mSrlAlarm.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				
				mSrlAlarm.setRefreshing(false);
				BluetoothTool.SendCmd("{\"cmd\": \"get_powerinfo_3\"}\r\n");
				ReadData();
			}
		});
	}
	
//	@Click(R.id.btnRefresh)
//	public void RefreshData()
//	{
//		BluetoothTool.SendCmd("{\"cmd\": \"get_powerinfo_4\"}\r\n");
//		ReadData();
//	}
	
	
//	@Background
//	void refresh() {
//		BluetoothTool.SendCmd("{\"cmd\": \"get_powerinfo_4\"}\r\n");
//		ReadData();
//	}
//
//	public Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			refresh();
//		}
//	};
//
//	public void run() {
//		// TODO Auto-generated method stub
//		while (true) {
//			try {
//				Thread.sleep(1000);
//				handler.sendMessage(handler.obtainMessage());
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
	
	EnergyData energyData;
	@Background
	public void ReadData()
	{
		String jsonData = BluetoothTool.readMsg();
		L.d("------------", jsonData);
		if(jsonData.isEmpty())
		{
			ShowMessage("请求数据失败");
		}
		else{
			try {
				energyData = (EnergyData)objectMapper.readValue(jsonData, EnergyData.class);
				ShowData();
			}
			catch(Exception e)
			{
				Log.d("bluetooth", e.getMessage());
			}
		}
	}
	@UiThread
	public void ShowData()
	{
		adapter.setEnergyData(energyData);
		devList.setAdapter(adapter);
	}
	@UiThread
	public void ShowMessage(String msg)
	{
		T.showShort(getActivity(),  msg);
	}
}
