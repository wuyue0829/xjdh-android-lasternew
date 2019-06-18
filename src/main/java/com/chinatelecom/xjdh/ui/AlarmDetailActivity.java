package com.chinatelecom.xjdh.ui;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppManager;
import com.chinatelecom.xjdh.bean.AlarmItem;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.DevTypeItem;
import com.chinatelecom.xjdh.bean.LoginResponse;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterfaceV1;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;
import com.chinatelecom.xjdh.utils.URLs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author peter
 * 
 */
@EActivity(R.layout.activity_alarm_detail)
public class AlarmDetailActivity extends BaseActivity {
	@ViewById(R.id.btn_monitor)
	Button btnMonitor;
	@ViewById(R.id.tv_alarm_detail_add_datetime)
	TextView tvAlarmDetailAddDatetime;
	@ViewById(R.id.tv_alarm_detail_city)
	TextView tvAlarmDetailCity;
	@ViewById(R.id.tv_alarm_detail_content)
	TextView tvAlarmDetailContent;
	@ViewById(R.id.tv_alarm_detail_county)
	TextView tvAlarmDetailCounty;
	@ViewById(R.id.tv_alarm_detail_dev_name)
	TextView tvAlarmDetailDevName;
	@ViewById(R.id.tv_alarm_detail_level)
	TextView tvAlarmDetailLevel;
	@ViewById(R.id.tv_alarm_detail_model)
	TextView tvAlarmDetailModel;
	@ViewById(R.id.tv_alarm_detail_room)
	TextView tvAlarmDetailRoom;
	@ViewById(R.id.tv_alarm_detail_status)
	TextView tvAlarmDetailStatus;
	@ViewById(R.id.tv_alarm_detail_substation)
	TextView tvAlarmDetailSubstation;
	@ViewById(R.id.tv_alarm_signal_name)
	TextView tvAlarmDetailSignalName;
	@ViewById(R.id.tv_alarm_signal_id)
	TextView tvAlarmDetailSignalId;

	@Extra("alarmItem")
	AlarmItem alarmItem;
	@RestService
	ApiRestClientInterfaceV1 mApiClient;
	ProgressDialog pDialog;
	LoginResponse response;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
		if (pDialog == null) {
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("加载数据中...");
			pDialog.setCancelable(true);
		}
	}

	@AfterViews
	void bindData() {
		setTitle("告警详情");
		
		String infoStr = PreferenceUtils.getPrefString(this, PreferenceConstants.USER_INFO, "");
		ObjectMapper mapper = new ObjectMapper();
		try {
			response = mapper.readValue(infoStr, LoginResponse.class);
		} catch (Exception ex) {

		}
		
		tvAlarmDetailAddDatetime.setText(alarmItem.getAdded_datetime());
		tvAlarmDetailCity.setText(alarmItem.getCity());
		tvAlarmDetailContent.setText(alarmItem.getSubject());
		tvAlarmDetailCounty.setText(alarmItem.getCounty());
		tvAlarmDetailDevName.setText(alarmItem.getDev_name());
		tvAlarmDetailSignalName.setText(alarmItem.getSignalName());
		tvAlarmDetailSignalId.setText(alarmItem.getSignalId());
		String level = "一级";
		switch (alarmItem.getLevel()) {
		case 1:
			level = "一级";
			break;
		case 2:
			level = "二级";
			break;
		case 3:
			level = "三级";
			break;
		case 4:
			level = "四级";
			break;
		default:
			break;
		}

		tvAlarmDetailLevel.setText(level);

		String modelKey = alarmItem.getDev_model();
		if (SharedConst.DEV_MODEL_MAP.containsKey(modelKey)) {
			tvAlarmDetailModel.setText(SharedConst.DEV_MODEL_MAP.get(modelKey));
		} else {
			tvAlarmDetailModel.setText("其他设备类型");
		}
		if (modelKey.equalsIgnoreCase("smd_device"))
			btnMonitor.setVisibility(View.GONE);
		tvAlarmDetailRoom.setText(alarmItem.getRoom_name());
		if (alarmItem.getStatus().equalsIgnoreCase("unresolved")) {
			tvAlarmDetailStatus.setText("正在告警");
			tvAlarmDetailStatus.setTextColor(Color.RED);
		} else if (alarmItem.getStatus().equalsIgnoreCase("solving")) {
			tvAlarmDetailStatus.setText("告警结束未确认恢复");
			tvAlarmDetailStatus.setTextColor(Color.YELLOW);
		} else if (alarmItem.getStatus().equalsIgnoreCase("solved")) {
			tvAlarmDetailStatus.setText("已确认恢复");
			tvAlarmDetailStatus.setTextColor(Color.GREEN);
		}
		tvAlarmDetailSubstation.setText(alarmItem.getSubstation_name());
	}

	@Click(R.id.btn_monitor)
	void onBtnMonitorClicked() {
		String type = "", typeName = "";
			
//		if (alarmItem.getDev_model().equalsIgnoreCase("water") || alarmItem.getDev_model().equalsIgnoreCase("smoke")) {
//			type = "di";
//			typeName = "开关量";
//		} else if (alarmItem.getDev_model().equalsIgnoreCase("temperature") || alarmItem.getDev_model().equalsIgnoreCase("humid")) {
//			type = "ad";
//			typeName = "模拟量";
//		} else if (alarmItem.getDev_model().equalsIgnoreCase("imem_12")) {
//			type = "imem12";
//			typeName = "智能电表";
//		}
//		if (type.equalsIgnoreCase("ad") || type.equalsIgnoreCase("di") || type.equalsIgnoreCase("imem12")) {
//		if (alarmItem.getRoom_code().equals("")) {
//			getData();
//		}else{
		if (alarmItem.getDev_model().equalsIgnoreCase("enviroment")) {
			String originalUrl=URLs.WAP_BASE_URL + "/loadrealtime?room_code=" + alarmItem.getRoom_id() + "&model=" + alarmItem.getDev_model() + "&access_token="
					+ mApiClient.getHeader(SharedConst.HTTP_AUTHORIZATION);
			L.d("----------",originalUrl);
			WebViewActivity_.intent(this).originalUrl(originalUrl).title(alarmItem.getDev_name()).start();
				L.v("状态："+alarmItem.getRoom_code());
		}else{
			String originalUrl=URLs.WAP_BASE_URL + "/loadrealtime?data_id=" + alarmItem.getData_id() + "&model=" + alarmItem.getDev_model() + "&access_token="
					+ mApiClient.getHeader(SharedConst.HTTP_AUTHORIZATION);
			L.d("----------",originalUrl);
			WebViewActivity_.intent(this).originalUrl(originalUrl).title(alarmItem.getDev_name()).start();
				L.v("状态："+alarmItem.getRoom_code());
		}
			
//		}
		
//		} else {
//			pDialog.show();
			
//		}
	}
	
	@SuppressWarnings("deprecation")
	@UiThread
	void onPreferenceLogoutClicked() {
		final AlertDialog mExitDialog = new AlertDialog.Builder(AlarmDetailActivity.this).create();
		mExitDialog.setTitle("下线提示");
		mExitDialog.setIcon(R.drawable.index_btn_exit);
		mExitDialog.setMessage("您的账户已在另一个设备登录,请尝试重新登陆");
		mExitDialog.setButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mExitDialog.dismiss();
				String account = PreferenceUtils.getPrefString(AlarmDetailActivity.this, PreferenceConstants.ACCOUNT, "");
				PreferenceUtils.clearPreference(AlarmDetailActivity.this, PreferenceManager.getDefaultSharedPreferences(AlarmDetailActivity.this));
				PreferenceUtils.setPrefString(AlarmDetailActivity.this, PreferenceConstants.ACCOUNT, account);
				AppManager.getAppManager().finishAllActivity();
				LoginActivity_.intent(AlarmDetailActivity.this).start();
			}
		});
		mExitDialog.show();
	}
	List<DevTypeItem> l;
	@Background
	void getData() {
		try {
			ApiResponse apiResp = mApiClient.get_room_dev_list(alarmItem.getRoom_code(),"");
			L.d(">>>>>>>>>>>>>>>>>", apiResp.toString());
			if (apiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				l = mapper.readValue(apiResp.getData(), new TypeReference<List<DevTypeItem>>() {
				});
				L.d(">>>>>>>>>>>>>>>>>", l.get(0).toString());
//				mTypeList.clear();
//				mTypeList.addAll(l);
				if (l.size() > 0) {
					onResult(l.get(0));
					return;
				}
			}else if(apiResp.getData().equals("Access token is not valid")){
				onPreferenceLogoutClicked();
			}
		} catch (Exception e) {
			L.e(e.toString());
		}
//		onResult("");
	}

	@UiThread
	void onResult(DevTypeItem devTypeItem) {
		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}
//		if (devTypeItem !="") {
			L.d(">>>>>>>>>>>>>>>>>", l.get(0).toString());
			RealtimeActivity_.intent(this).devTypeItem(devTypeItem).start();
//		} else {
//			T.showShort(this, "加载数据失败");
//		}
	}
}
