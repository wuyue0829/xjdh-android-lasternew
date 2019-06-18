package com.chinatelecom.xjdh.ui;

import java.io.IOException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.BoardSettingData;
import com.chinatelecom.xjdh.tool.BluetoothTool;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.T;

import android.util.Log;
import android.widget.EditText;

/**
 * @author peter
 * 
 */
@EActivity(R.layout.activity_board_setting)
public class BoardSettingActivity extends BaseActivity {
	
	@ViewById
	EditText etServerAddr,etDeviceId,etIP0,etNetmask0,etGateway0,etDns0,etIP1,etNetmask1,etGateway1,etDns1;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@AfterViews
	void showData(){
		BluetoothTool.SendCmd("{\"cmd\": \"call_setting\"}\r\n");
		ReadData();
	}
	
	@Click(R.id.btnReadSetting)
	void ReadSetting()
	{
		BluetoothTool.SendCmd("{\"cmd\": \"call_setting\"}\r\n");
		ReadData();
	}
	
	BoardSettingData boardData;
	@Background
	public void ReadData()
	{
		String jsonData = BluetoothTool.readMsg();
		L.d("?????????????", jsonData);
		if(jsonData.isEmpty())
		{
			ShowMessage("请求数据失败");
			finish();
		}else{
			try {
				boardData = (BoardSettingData)objectMapper.readValue(jsonData, BoardSettingData.class);
				L.d("<><><><><><><><>", boardData.toString());
				ShowData();
				ShowMessage("请求数据成功");
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
		etServerAddr.setText(boardData.getServer_addr());
		etDeviceId.setText(boardData.getDevice_id());
		etIP0.setText(boardData.getIp0());
		etNetmask0.setText(boardData.getNetmask0());
		etGateway0.setText(boardData.getGateway0());
		etDns0.setText(boardData.getDns0());
		etIP1.setText(boardData.getIp1());
		etNetmask1.setText(boardData.getNetmask1());
		etGateway1.setText(boardData.getGateway1());
		etDns1.setText(boardData.getDns1());
	}
	
	@UiThread
	public void ShowMessage(String msg)
	{
		T.showShort(this,  msg);
	}
	
	@Click(R.id.btnWriteSetting)
	void SaveSetting()
	{
//		boardData = new BoardSettingData();
		boardData.setServer_addr(etServerAddr.getText().toString());
		boardData.setDevice_id(etDeviceId.getText().toString());
		boardData.setIp0(etIP0.getText().toString());
		boardData.setNetmask0(etNetmask0.getText().toString());
		boardData.setGateway0(etGateway0.getText().toString());
		boardData.setDns0(etDns0.getText().toString());
		boardData.setIp1(etIP1.getText().toString());
		boardData.setNetmask1(etNetmask1.getText().toString());
		boardData.setGateway1(etGateway1.getText().toString());
		boardData.setDns1(etDns1.getText().toString());
		boardData.setCmd("send_setting");
		L.d(">>>>>>>>>>>>>>>>>>", boardData.toString());
		try {
			String content = objectMapper.writeValueAsString(boardData);
			content += "\r\n";
			BluetoothTool.SendCmd(content);
			BluetoothTool.readMsg();
			T.showShort(this,  "下发成功");
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			T.showShort(this,  e.getMessage());
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			T.showShort(this,  e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			T.showShort(this,  e.getMessage());
		}
		
	}
	
}
