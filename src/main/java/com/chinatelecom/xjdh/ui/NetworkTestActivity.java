package com.chinatelecom.xjdh.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.CmdIP;
import com.chinatelecom.xjdh.bean.CmdResult;
import com.chinatelecom.xjdh.tool.BluetoothTool;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.T;
import com.chinatelecom.xjdh.view.DropEditText;

import android.app.ProgressDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * @author peter
 * 
 */
@EActivity(R.layout.activity_network_test)
public class NetworkTestActivity extends BaseActivity {
	@ViewById
	DropEditText etIP;

	@ViewById
	TextView tvResult,tvMsg;
	@ViewById
	ScrollView sll;
	ProgressDialog pDialog;
	@AfterViews
	void Show()
	{
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("正在运行");
		
		etIP.setAdapter(new BaseAdapter() {
			private List<String> mList = new ArrayList<String>() {
				{
					add("194.0.0.1");
					add("192.168.200.200");
				}
			};

			@Override
			public int getCount() {
				return mList.size();
			}

			@Override
			public Object getItem(int position) {
				return mList.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView tv = new TextView(NetworkTestActivity.this);
				tv.setTextSize(20);
				tv.setText(mList.get(position));
				return tv;
			}
		});
	}
	
	ObjectMapper om = new ObjectMapper();
	
	@Click(R.id.btnPing)
	void onBtnTestClick()
	{
		String ip = etIP.getText().toString();
		if(ip.isEmpty())
		{
			T.showShort(this, "请输入IP地址");
			return;
		}
		String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";  
        Pattern pat = Pattern.compile(rexp);    
        Matcher mat = pat.matcher(ip);    
        if(!mat.find())
        {
        	T.showShort(this, "请输入正确的IP地址");
        	return;
        }
        pDialog.show();
        tvResult.setText("");
        CmdIP cmd = new CmdIP();
        cmd.setCmd("ping");
        cmd.setIP(ip);
		try {
	        String content;
			content = om.writeValueAsString(cmd);
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
	CmdResult cmdRet;
	@Background
	void ReadResult()
	{
		String content = BluetoothTool.readMsg();
		try {
			CmdResult cmdRet = (CmdResult)om.readValue(content, CmdResult.class);
			L.d("8888888888888", cmdRet.toString());
			ShowResult(cmdRet.getResult());
			ShowMsg("运行成功");
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
		ShowMsg("运行失败");
	}
	
	@UiThread
	void ShowMsg(String result)
	{
		pDialog.dismiss();
		tvMsg.setText(result);
	}
	@UiThread
	void ShowResult(String results)
	{
		pDialog.dismiss();
		tvResult.setText(results);
	}
	
}
