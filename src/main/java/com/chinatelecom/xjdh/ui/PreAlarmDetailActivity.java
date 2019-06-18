package com.chinatelecom.xjdh.ui;

import java.util.LinkedHashMap;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.AlarmItem;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author peter
 * 
 */
@EActivity(R.layout.activity_pre_alarm_detail)
public class PreAlarmDetailActivity extends BaseActivity {
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
	ApiRestClientInterface mApiClient;
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
		setTitle("告警详情");
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
//		 if (modelKey.equalsIgnoreCase("smd_device"))
//		 btnMonitor.setVisibility(View.GONE);
		 tvAlarmDetailRoom.setText(alarmItem.getRoom_name());
//		 if (alarmItem.getStatus().equalsIgnoreCase("unresolved")) {
//		 tvAlarmDetailStatus.setText("未处理");
//		 tvAlarmDetailStatus.setTextColor(Color.RED);
//		 } else if (alarmItem.getStatus().equalsIgnoreCase("sloving")) {
//		 tvAlarmDetailStatus.setText("处理中");
//		 tvAlarmDetailStatus.setTextColor(Color.YELLOW);
//		 } else if (alarmItem.getStatus().equalsIgnoreCase("sloved")) {
//		 tvAlarmDetailStatus.setText("处理完成");
//		 tvAlarmDetailStatus.setTextColor(Color.GREEN);
		//// }
		if (alarmItem.getStatus().equalsIgnoreCase("unresolved")) {
			tvAlarmDetailStatus.setText("未处理");
			tvAlarmDetailStatus.setTextColor(Color.RED);
		} // else if (alarmItem.getStatus().equalsIgnoreCase("sloved")) {
		// tvAlarmDetailStatus.setText("处理完成");
		// tvAlarmDetailStatus.setTextColor(Color.GREEN);
		// }
		tvAlarmDetailSubstation.setText(alarmItem.getSubstation_name());
	}

	/**
	 * 确认
	 */
	@Click(R.id.btn_confirm_emergency)
	void onBtnMonitorClicked() {

		getData();
		PreAlarmDetailActivity.this.finish();
		// L.e("---------------- ："+alarmItem.getId());
	}

	@Background
	void getData() {
		LinkedHashMap<String, String> lastId = new LinkedHashMap<String, String>(0);
		lastId.put("lastId", alarmItem.getId());
		ApiResponse apiResp = mApiClient.postPreAlarmList(lastId);
		if (apiResp.getRet().equals("loadsucceed")) {
			Toast.makeText(PreAlarmDetailActivity.this, "确认告警成功", 0).show();
		}
	}

}
