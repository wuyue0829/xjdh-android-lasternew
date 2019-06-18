package com.chinatelecom.xjdh.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppManager;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.DevTypeItem;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterfaceV1;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.URLs;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author peter
 * 
 */
@EActivity(R.layout.normal_list_view)
public class RoomDevListActivity extends BaseActivity {

	@ViewById(R.id.lv_items)
	ListView mLvDevType;
//	@ViewById(R.id.tv_refresh)
//	TextView mTvRefresh;
	@RestService
	ApiRestClientInterface mApiClient;
	
	@RestService
	ApiRestClientInterfaceV1 mApiClientV1;
	
	@Extra("roomcode")
	String mRoomCode;
	@Extra("roomname")
	String mRoomName;
	List<DevTypeItem> mDevTypeList = new ArrayList<>();
	List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
	SimpleAdapter mDevTypeAdapter;
	ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);

		pDialog = new ProgressDialog(this);
		pDialog.setMessage(getResources().getString(R.string.progress_loading_msg));
	}

	@AfterViews
	void bindData() {
		setTitle(mRoomName + "-设备类型列表");
		mDevTypeAdapter = new SimpleAdapter(this, listItem, R.layout.area_list_item, new String[] { "num", "name" },
				new int[] { R.id.tv_num, R.id.tv_info });
		mLvDevType.setAdapter(mDevTypeAdapter);
		pDialog.show();
		getRoomDeviceList();
	}

	@SuppressWarnings("deprecation")
	@UiThread
	void onPreferenceLogoutClicked() {
		final AlertDialog mExitDialog = new AlertDialog.Builder(RoomDevListActivity.this).create();
		mExitDialog.setTitle("下线提示");
		mExitDialog.setIcon(R.drawable.index_btn_exit);
		mExitDialog.setMessage("您的账户已在另一个设备登录,请尝试重新登陆");
		mExitDialog.setButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mExitDialog.dismiss();
				String account = PreferenceUtils.getPrefString(RoomDevListActivity.this, PreferenceConstants.ACCOUNT, "");
				PreferenceUtils.clearPreference(RoomDevListActivity.this, PreferenceManager.getDefaultSharedPreferences(RoomDevListActivity.this));
				PreferenceUtils.setPrefString(RoomDevListActivity.this, PreferenceConstants.ACCOUNT, account);
				AppManager.getAppManager().finishAllActivity();
				LoginActivity_.intent(RoomDevListActivity.this).start();
			}
		});
		mExitDialog.show();
	}
	@Background
	void getRoomDeviceList() {
		try {
			L.d("111111111111", mRoomCode);
			ApiResponse apiResp = mApiClientV1.get_room_dev_list(mRoomCode, "");
			L.d("aaaaaaaaaaaaaaaaaaa" ,apiResp.toString());
			if (apiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				List<DevTypeItem> l = mapper.readValue(apiResp.getData(), new TypeReference<List<DevTypeItem>>() {
					
				});
				mDevTypeList.clear();
				mDevTypeList.addAll(l);
				int index = 1;
				for (DevTypeItem devTypeItem : l) {
					L.d("============", mapper.writeValueAsString(devTypeItem));
					Map<String, Object> item = new HashMap<>();
					item.put("num", String.valueOf(index++));
					item.put("name", devTypeItem.getName());
					listItem.add(item);
				}
			}else if(apiResp.getData().equals("Access token is not valid")){
				onPreferenceLogoutClicked();
			}
		} catch (Exception e) {
			L.e(e.toString());
		}
		updateRoomDevListView();
	}

	@UiThread
	void updateRoomDevListView() {
		pDialog.dismiss();
		mDevTypeAdapter.notifyDataSetChanged();
	}

	private static String[] WEBVIEW_MODEL = { "ad", "di", "imem12", "motor_battery" };

	@ItemClick(R.id.lv_items)
	void onRoomItemClicked(int pos) {
//		List<DevItem> devList = Arrays.asList(mDevTypeList.get(pos).getDevlist());
//		String[] dataId = new String[devList.size()];
//		for (int i = 0; i < devList.size(); i++) {
//			DevItem devItem = devList.get(i);
//			dataId[i] = devItem.getData_id();
//		}
		if (mDevTypeList.get(pos).getType().equals("door"))
		{
			DoorListActivity_.intent(this).devTypeItem(mDevTypeList.get(pos)).start();
			//DoorActivity_.intent(this).Name(mDevTypeList.get(pos).getDevlist()[0].getName()).DataId(mDevTypeList.get(pos).getDevlist()[0].getData_id()).CanOpen(mDevTypeList.get(pos).getDevlist()[0].getCan_open()).start();
		}
		else if(mDevTypeList.get(pos).getType().equals("camera")){
//			TestActivity_.intent(this).start();
//			RtspPlayerActivity_.intent(this).devTypeItem(mDevTypeList.get(pos)).start();
			RstpVideoActivity_.intent(this).devTypeItem(mDevTypeList.get(pos)).start();
		}
		else if (Arrays.asList(WEBVIEW_MODEL).contains(mDevTypeList.get(pos).getType())) {
			String loadUrl = URLs.WAP_BASE_URL + "/loadrealtime?room_code=" + mRoomCode + "&model="
					+ mDevTypeList.get(pos).getType() + "&access_token="
					+ mApiClient.getHeader(SharedConst.HTTP_AUTHORIZATION);
			L.d("=================", loadUrl);
			WebViewActivity_.intent(this)
					.originalUrl(loadUrl)
					.title(mDevTypeList.get(pos).getName()).start();
		} 
		else{
			RealtimeActivity_.intent(this).devTypeItem(mDevTypeList.get(pos)).start();
		}
	}
}
