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
import com.chinatelecom.xjdh.adapter.EnergyDataAdapter;
import com.chinatelecom.xjdh.bean.EnergyData;
import com.chinatelecom.xjdh.tool.BluetoothTool;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.T;

import android.util.Log;
import android.widget.ListView;

@EActivity(R.layout.activity_smddevice_monitors)
public class EnergysActivity extends BaseActivity{
	@Extra 
	String model;
	@ViewById
	ListView devListss;
	EnergyDataAdapter adapter ;
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@AfterViews
	public void Show()
	{
		adapter= new EnergyDataAdapter(this);
		adapter.setModel(model);
		BluetoothTool.SendCmd("{\"cmd\": \"get_powerinfo_1\"}\r\n");
		ReadData();
	}
	
	@Click(R.id.btnRefresh)
	public void RefreshData()
	{
		BluetoothTool.SendCmd("{\"cmd\": \"get_powerinfo_1\"}\r\n");
		ReadData();
	}
	
	
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
		devListss.setAdapter(adapter);
//		adapter.notifyDataSetChanged();
	}
	@UiThread
	public void ShowMessage(String msg)
	{
		T.showShort(this,  msg);
	}

}
