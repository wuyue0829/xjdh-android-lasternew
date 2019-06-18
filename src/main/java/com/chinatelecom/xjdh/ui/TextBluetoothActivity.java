package com.chinatelecom.xjdh.ui;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.adapter.MyListAdapter;
import com.chinatelecom.xjdh.tool.BluetoothTool;
import com.chinatelecom.xjdh.utils.L;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

@EActivity(R.layout.activity_bluetooth)
public class TextBluetoothActivity extends BaseActivity {
	@ViewById
	Button btn_search_devices, btn_close_devices;
	@ViewById
	ListView list_bonded_devices, list_search_devices;
	@ViewById(R.id.rd302A)
	RadioButton rd302A;

	@ViewById(R.id.rd301E)
	RadioButton rd301E;
	ProgressDialog pDialog;
	private MyListAdapter mBondedAdapter;
	private MyListAdapter mSearchAdapter;
	private BluetoothAdapter adapter;
	private List<BluetoothDevice> bondedDevicesList;
	private List<BluetoothDevice> searchDevicesList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 检查设备是否支持蓝牙,若支持则打开
		checkBluetooth();

		// 注册用以接收到已搜索到的蓝牙设备的receiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(receiver, filter);

		filter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		registerReceiver(receiver, filter);

		// Register for broadcasts when discovery has finished
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(receiver, filter);

		filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		registerReceiver(receiver, filter);

		filter = new IntentFilter(BluetoothDevice.ACTION_UUID);
		registerReceiver(receiver, filter);

		// 注册广播接收器，接收并处理搜索结果

	}

	@AfterViews
	protected void Show() {
		rd302A.setChecked(true);
		rd301E.setVisibility(View.GONE);
		pDialog = new ProgressDialog(this);

		bondedDevicesList = new ArrayList<BluetoothDevice>();
		// 设置适配器
		mBondedAdapter = new MyListAdapter(this, bondedDevicesList);
		list_bonded_devices.setAdapter(mBondedAdapter);

		list_bonded_devices.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				adapter.cancelDiscovery();
				BluetoothDevice device = bondedDevicesList.get(position);
				// L.d("===========", device.toString());

				//
				setDevice(device);
			}
		});

		list_bonded_devices.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				try {

					BluetoothDevice btDevice = bondedDevicesList.get(position);
					removeBond(btDevice.getClass(), btDevice);
					// mBondedAdapter.refresh(bondedDevicesList);
					mBondedAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
		});

		searchDevicesList = new ArrayList<BluetoothDevice>();
		mSearchAdapter = new MyListAdapter(this, searchDevicesList);
		list_search_devices.setAdapter(mSearchAdapter);
		list_search_devices.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				BluetoothDevice device = searchDevicesList.get(position);
				try {
					// 配对
					// BluetoothTool.initSocket(device);
					// BluetoothTool.setup(device);
					Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
					createBondMethod.invoke(device);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		// 获取所有已经绑定的蓝牙设备
		getBondedDevices();
	}

	@Click({ R.id.btn_search_devices, R.id.btn_close_devices })
	void onBtnClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search_devices:
			setProgressBarIndeterminateVisibility(true);
			setTitle("正在扫描....");
			searchDevicesList.clear();
			mSearchAdapter.notifyDataSetChanged();
			// 如果正在搜索，就先取消搜索
			if (!adapter.isDiscovering()) {
				adapter.cancelDiscovery();
			}
			// 开始搜索蓝牙设备,搜索到的蓝牙设备通过广播返回
			adapter.startDiscovery();
			break;
		case R.id.btn_close_devices:
			// 如果正在搜索，就先取消搜索
			if (adapter.isDiscovering()) {
				adapter.cancelDiscovery();
			}
			break;
		}
	}

	/**
	 * 检查设备是否支持蓝牙,若支持则打开
	 */
	private void checkBluetooth() {
		adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter == null) {
			// 设备不支持蓝牙
			Toast.makeText(this, "设备不支持蓝牙", Toast.LENGTH_SHORT).show();
		} else {
			// 判断蓝牙是否打开，如果没有则打开蓝牙
			// adapter.enable() 直接打开蓝牙，但是不会弹出提示，以下方式会提示用户是否打开
			if (!adapter.isEnabled()) {
				Intent intent = new Intent();
				// 打开蓝牙设备
				intent.setAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				// 是设备能够被搜索
				intent.setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				// 设置蓝牙可见性，最多300秒
				intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
				startActivity(intent);
			}
		}
	}

	private void getBondedDevices() {
		bondedDevicesList.clear();
		Set<BluetoothDevice> devices = adapter.getBondedDevices();
		bondedDevicesList.addAll(devices);
		// 为listview动态设置高度（有多少条目就显示多少条目）
		setListViewHeight(bondedDevicesList.size());
		mBondedAdapter.notifyDataSetChanged();
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 获得已经搜索到的蓝牙设备
			if (action.equals(BluetoothDevice.ACTION_FOUND)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// 搜索到的不是已经绑定的蓝牙设备
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					// 防止重复添加
					if (searchDevicesList.indexOf(device) == -1)
						searchDevicesList.add(device);
					L.d("未配对 |============= " + device.getName() + "（" + device.getAddress() + "）");
					mSearchAdapter.notifyDataSetChanged();
				}
				// 搜索完成
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				setProgressBarIndeterminateVisibility(false);  
				setTitle("搜索完成");  
				Toast.makeText(getBaseContext(), "discovery finished", Toast.LENGTH_LONG);
			} else if (BluetoothDevice.ACTION_UUID.equals(action)) {
				UUID uuid = intent.getParcelableExtra(BluetoothDevice.EXTRA_UUID);
				if (uuid != null) {
					Toast.makeText(getBaseContext(), uuid.toString(), Toast.LENGTH_LONG);
				} else {
					Toast.makeText(getBaseContext(), "uuid not found", Toast.LENGTH_LONG);
				}
			} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
				getBondedDevices();
			}
		}
	};

	// 为listview动态设置高度（有多少条目就显示多少条目）
	private void setListViewHeight(int count) {
		if (mBondedAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < count; i++) {
			View listItem = mBondedAdapter.getView(i, null, list_bonded_devices);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = list_bonded_devices.getLayoutParams();
		params.height = totalHeight;
		list_bonded_devices.setLayoutParams(params);
	}

	@Background
	public void setDevice(BluetoothDevice device) {
//		L.d("77777777777777777", device.getName());
		if (BluetoothTool.setup(device)) {
			// // 成功，跳转
			if (rd302A.isChecked()) {
				DevMonitorListActivity_.intent(this).model("302A").start();
			} else if (rd301E.isChecked()) {
				DevMonitorListActivity_.intent(this).model("301E").start();
			}
		} else {
			ShowMessage("请再次连接");
		}

		HideProgress();
		return;

	}

	// 与设备解除配对
	public boolean removeBond(Class btClass, BluetoothDevice btDevice) throws Exception {
		Method removeBondMethod = btClass.getMethod("removeBond");
		Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
		return returnValue.booleanValue();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { //
			BluetoothTool.Close();
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@UiThread
	public void HideProgress() {
		pDialog.hide();
	}

	@UiThread
	public void ShowMessage(String msg) {
		Toast.makeText(TextBluetoothActivity.this, msg, Toast.LENGTH_SHORT).show();
	}

	 @Override
	 protected void onDestroy() {
	 super.onDestroy();
	 // 解除注册
	 unregisterReceiver(receiver);
	 }

	@Override
	protected void onResume() {
		super.onResume();
		getBondedDevices();
	}
}
