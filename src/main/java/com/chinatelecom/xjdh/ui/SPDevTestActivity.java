package com.chinatelecom.xjdh.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.CmdResult;
import com.chinatelecom.xjdh.bean.CmdSPDev;
import com.chinatelecom.xjdh.bean.SPDev;
import com.chinatelecom.xjdh.bean.SPDevResponse;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.tool.BluetoothTool;
import com.chinatelecom.xjdh.utils.FileUtils;
import com.chinatelecom.xjdh.utils.T;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author peter
 * 
 */
@EActivity(R.layout.activity_spdev_test)
public class SPDevTestActivity extends BaseActivity {
	@ViewById
	Button btnTest;
	@ViewById
	Spinner spModel,spIndex,spRate;
	
	@ViewById
	TextView tvBanner,tvSendData,tvRecvData,tvResult;
	
	@StringArrayRes(R.array.spdev_rate_array)
	String[] baud_rate;
	@RestService
	ApiRestClientInterface apiRestClient;
	
	SPDevResponse res;
	
	ProgressDialog pDialog;
	
	ObjectMapper om = new ObjectMapper();
	
	@AfterViews
	void Show()
	{
		spModel.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(res != null)
				{
					if(position < res.getSpdevList().size())
					{
						SPDev sp = res.getSpdevList().get(position);
						int rateIndex = Arrays.asList(baud_rate).indexOf(Integer.toString(sp.getBaud_rate()));
						spRate.setSelection(rateIndex);
						tvSendData.setText(sp.getCmd());
						tvRecvData.setText(sp.getReply());
						tvResult.setText("");
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				tvSendData.setText("");
				tvRecvData.setText("");
				tvResult.setText("");
			}
		});
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("更新数据中");
		
		
		String cityData = new String(FileUtils.getFileData(this, "SPDEV_DATA"));
		if(cityData.isEmpty())
		{
			getData();
		}else{
		
			try {
				res = om.readValue(cityData, SPDevResponse.class);
				showData();
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
				
		}
		
			
		
	}
	
	@Background
	void getData()
	{
		try
		{
			res = apiRestClient.get_spdev_list();
			String content = om.writeValueAsString(res);
			FileUtils.setToData(this, "SPDEV_DATA", content.getBytes());
			showData();
		}catch(Exception e) {
			//L.e(e.toString());
		}
		//T.showShort(this, "");
	}
	
	@UiThread
	void showData()
	{
		List<SPDev> spDevList = res.getSpdevList();
		List<String> nameList = new ArrayList<String>();
		for(SPDev sp:spDevList)
		{
			nameList.add(sp.getName());
		}
		tvBanner.setText("当前支持" + Integer.toString(nameList.size()) + "种智能设备");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, nameList);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spModel.setAdapter(dataAdapter);	
		dataAdapter.notifyDataSetChanged();
	}
	
	@Click(R.id.btnUpdate)
	void onBtnUpdateClick()
	{
		getData();
	}
	
	
	@Click(R.id.btnTest)
	void onBtnTestClick() 
	{
		int selModel = spModel.getSelectedItemPosition();
		
		CmdSPDev cmdSp = new CmdSPDev();
		cmdSp.setCmd("send_sp");
		cmdSp.setPort(spIndex.getSelectedItemPosition());
		cmdSp.setRate(Integer.parseInt(spRate.getSelectedItem().toString()));
		String data = res.getSpdevList().get(selModel).getCmd();
		cmdSp.setData(data);
		
		
		try {
			String content;
			content = om.writeValueAsString(cmdSp);
			content += "\r\n";
			BluetoothTool.SendCmd(content);
			ReadResult();
			T.showShort(this, "发送成功");
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Background
	void ReadResult()
	{
		String content = BluetoothTool.readMsg();
		try {
			CmdResult cmdRet = (CmdResult)om.readValue(content, CmdResult.class);
			if(cmdRet.getResult().isEmpty())
			{
				ShowResult("没有数据返回，选定端口无本智能设备");
			}else{
				ShowResult(cmdRet.getResult());
			}
			return;
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		ShowResult("通讯失败");
	}
	
	@UiThread
	void ShowResult(String result)
	{
		pDialog.hide();
		tvResult.setText(result);
	}
}
