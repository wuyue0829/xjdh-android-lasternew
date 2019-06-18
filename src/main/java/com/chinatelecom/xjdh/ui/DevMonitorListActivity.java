package com.chinatelecom.xjdh.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppManager;
import com.chinatelecom.xjdh.tool.BluetoothTool;
import com.chinatelecom.xjdh.utils.T;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author peter
 * 
 */
@EActivity(R.layout.test_device_list)
public class DevMonitorListActivity extends BaseActivity {


	@Extra
	public String model;
	@ViewById(R.id.lvDevMode)
	ListView lvDevMode;

	@ViewById(R.id.tvModel)
	TextView tvModel;
	private final static int TAG = 0;
	String[] devModeArray = new String[] { "DI/AI数据", "智能设备测试", "网络测试", "系统设置", "重启程序", "重启板子", "退出设置", "能耗测试" };
	ArrayAdapter<String> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	void Show() {
		tvModel.setText(model);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, devModeArray);
		lvDevMode.setAdapter(mAdapter);

	}

	@Background
	void refresh() {
		BluetoothTool.SendCmd("{\"cmd\": \"request_status\"}\r\n");
		refreshData();
	}

	@Background
	void refreshData() {
		String jsonData = BluetoothTool.readMsg();
		if (jsonData.isEmpty()) {
			ShowMessage("蓝牙连接失败");
			finish();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	@Background
	public void ReadData() {

		String jsonData = BluetoothTool.readMsg();
		if (jsonData.isEmpty()) {
			ShowMessage("蓝牙连接失败");
			finish();
		} else {
			ShowMessage("蓝牙连接成功");
		}

	}

	@UiThread
	public void ShowMessage(String msg) {
		T.showShort(this, msg);
	}

	@ItemClick(R.id.lvDevMode)
	void onDevModelicked(int pos) {
		switch (pos) {
		case 0:
			SMDDeviceMonitor_.intent(this).model(model).start();
			break;
		case 1:
			SPDevTestActivity_.intent(this).start();
			// smart device test
			break;
		case 2:
			// network test
			NetworkTestActivity_.intent(this).start();
			break;
		case 3:
			BoardSettingActivity_.intent(this).start();
			break;
		case 4: {
			// quit
			new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("重启采集程序")
					.setMessage("请确认重启采集程序以使用新设置?").setPositiveButton("是", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							BluetoothTool.SendCmd("{\"cmd\": \"quit\"}\r\n");
							finish();
						}

					}).setNegativeButton("否", null).show();
			break;
		}
		case 5: {
			// reboot
			new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("重启采集板")
					.setMessage("请确认重启采集板以使用新设置?").setPositiveButton("是", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							BluetoothTool.SendCmd("{\"cmd\": \"reboot\"}\r\n");
							finish();
						}

					}).setNegativeButton("否", null).show();
			break;
		}
		case 6:
			BluetoothTool.Close();
			finish();
			break;
		case 7:
			EnergyActivity_.intent(this).model(model).start();
			break;
		}

	}

	protected void onDestroy() {
		super.onDestroy();
		// 结束Activity&从栈中移除该Activity
		BluetoothTool.Close();
		AppManager.getAppManager().removeActivity(this);

	}

}
