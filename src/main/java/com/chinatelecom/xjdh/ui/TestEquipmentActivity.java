package com.chinatelecom.xjdh.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.tool.BluetoothTool;
import com.chinatelecom.xjdh.utils.L;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

@EActivity(R.layout.activity_select_device)
public class TestEquipmentActivity extends BaseActivity {

	@ViewById(R.id.btn_scanning)
	Button btn_scanning;

	@ViewById(R.id.rd302A)
	RadioButton rd302A;
	
	@ViewById(R.id.rd301E)
	RadioButton rd301E;
	
	BluetoothAdapter mAdapter;
	private List<BluetoothDevice> devices;

	private List<String> deviceList;
	ArrayAdapter<String> deviceAdapter;
	@ViewById(R.id.lvDevice)
	ListView lvDevice;

	ProgressDialog pDialog;
	
	@AfterViews
	protected void Show() {
		rd302A.setChecked(true);
		rd301E.setVisibility(View.GONE);
		pDialog = new ProgressDialog(this);
		
		deviceList = new ArrayList<String>();
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		/**
		 * 检查手机的蓝牙设备是否打开，未打开则 强制 打开蓝牙设备
		 */
		if (mAdapter != null) {
			if (!mAdapter.isEnabled()) {
				Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivity(intent);
			}
		}
		devices = new ArrayList<BluetoothDevice>();
		Set<BluetoothDevice> pairedDevices = mAdapter.getBondedDevices();
//
		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				devices.add(device);
				deviceList.add(device.getAddress() + " " + device.getName());
			}
		}

		deviceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, deviceList);
		lvDevice.setAdapter(deviceAdapter);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter);

		filter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		registerReceiver(mReceiver, filter);

		// Register for broadcasts when discovery has finished
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(mReceiver, filter);

		filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		registerReceiver(mReceiver, filter);

		filter = new IntentFilter(BluetoothDevice.ACTION_UUID);
		registerReceiver(mReceiver, filter);

		
	}

	/**
	 * 扫描设备
	 */

	@Click(R.id.btn_scanning)
	public void OnFabClick() {
		if (mAdapter.isDiscovering()) {
			mAdapter.cancelDiscovery();
		}
		devices.clear();
		deviceList.clear();
		deviceAdapter.notifyDataSetChanged();
		mAdapter.startDiscovery();
		pDialog.setMessage("正在扫描");
		pDialog.show();
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 发现设备
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// 从Intent中获取设备对象
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// Only get not bounded devices
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {// 判断给定地址下的device是否已经配对
					// 保存设备地址与名字、
					if (devices.indexOf(device) == -1)
					devices.add(device);
					deviceList.clear();
					deviceList.add(device.getAddress() + " " + device.getName());
					L.d("未配对 |============= " + device.getName() + "（" + device.getAddress() + "）");
					deviceAdapter.notifyDataSetChanged();
				}
				
//				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
//					// 防止重复添加
//					if (devices.indexOf(device) == -1)
//						devices.add(device);
//					L.d("未配对 |============= " + device.getName() + "（" + device.getAddress() + "）");
//					deviceAdapter.notifyDataSetChanged();
//				}
				// When discovery is finished, change the Activity title搜索结束
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				Toast.makeText(getBaseContext(), "discovery finished", Toast.LENGTH_LONG);
				pDialog.hide();
			} else if (BluetoothDevice.ACTION_UUID.equals(action)) {
				UUID uuid = intent.getParcelableExtra(BluetoothDevice.EXTRA_UUID);
				if (uuid != null) {
					Toast.makeText(getBaseContext(), uuid.toString(), Toast.LENGTH_LONG);
				} else {
					Toast.makeText(getBaseContext(), "uuid not found", Toast.LENGTH_LONG);
				}
			} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {

			}
		}
	};

	// private final BluetoothSocket mSocket;
	@ItemClick(R.id.lvDevice)
	public void OnLvDeviceClick(int position) {
		pDialog.setMessage("正在连接");
		pDialog.show();
		mAdapter.cancelDiscovery();
		BluetoothDevice device = devices.get(position);
		setDevice(device);
		
	}

	@Background
	public void setDevice(final BluetoothDevice device) {

		if (BluetoothTool.setup(device)) {
            		// 成功，跳转
 			if(rd302A.isChecked())
 			{
 				DevMonitorListActivity_.intent(this).model("302A").start();
 			}else if(rd301E.isChecked())
 			{
 				DevMonitorListActivity_.intent(this).model("301E").start();
 			}
 			HideProgress();
 			return;
         }else{
        	 ShowMessage("连接失败");
         }
	}
	@UiThread
	public void HideProgress()
	{
		pDialog.hide();
	}
	@UiThread
	public void ShowMessage(String msg)
	{
		Toast.makeText(TestEquipmentActivity.this, msg, Toast.LENGTH_LONG).show();
	}
}
