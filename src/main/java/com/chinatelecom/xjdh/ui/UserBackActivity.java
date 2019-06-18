package com.chinatelecom.xjdh.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppContext;
import com.chinatelecom.xjdh.app.AppManager;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.LoginResponse;
import com.chinatelecom.xjdh.bean.OauthParam;
import com.chinatelecom.xjdh.bean.OauthRespose;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.rest.client.OauthRestClientInterface;
import com.chinatelecom.xjdh.service.ScheduleService_;
import com.chinatelecom.xjdh.utils.CryptoUtils;
import com.chinatelecom.xjdh.utils.FileUtils;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;
import com.chinatelecom.xjdh.utils.Update;
import com.chinatelecom.xjdh.utils.UpdateManager;

import android.app.ProgressDialog;
import android.preference.PreferenceManager;
import android.widget.TextView;

@EActivity(R.layout.user_back)
public class UserBackActivity extends BaseActivity{
	@Extra("isDoLogins")
	boolean isDoLogins = false;
	@ViewById
	TextView user_info;
	@AfterViews
	void initData() {
		if (!AppContext.getInstance().isNetworkConnected()) {
			T.showLong(this, "网络未连接，无法登陆");
		}
		if (isDoLogins) {
			doLogin();
		}
		
	}
	@RestService
	ApiRestClientInterface mApiClient;
	private int curVersionCode;
	private Update mUpdate;
	private String curVersionName = "";
	private String city = "all";
	private String station = "all";
	private String stationNames = "all";

	@Background
	void getFilterData() {
		int retryTimes = 0;
		while (retryTimes++ < 5) {
			try {
				ApiResponse apiResp = mApiClient.getAreaData(city, station, stationNames);
				if (apiResp.getRet() == 0) {
					L.d("00000000000000", apiResp.toString());
					FileUtils.setToData(this, SharedConst.FILE_AREA_JSON, apiResp.getData().getBytes());
					break;
				}
			} catch (Exception e) {
				L.e(e.toString());
			}
		}
		retryTimes = 0;
		while (retryTimes++ < 5) {
			try {
				ApiResponse apiResp = mApiClient.getDevModelData();
				if (apiResp.getRet() == 0) {
					FileUtils.setToData(this, SharedConst.FILE_MODEL_JSON, apiResp.getData().getBytes());
					break;
				}
			} catch (Exception e) {
				L.e(e.toString());
			}
		}
	}

	
	
	@RestService
	OauthRestClientInterface mOauthClient;
	ProgressDialog pDialog;
	boolean isLogined = false;

	@Background(delay = 1000)
	void doLogin() {
		String password = PreferenceUtils.getPrefString(this, PreferenceConstants.PASSWORD, "");
		String account = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCOUNT, "");
		try {
			String pwdStr = CryptoUtils.decrypt(SharedConst.PASSWORD_CRYPRO_SEED, SharedConst.CLIENT_PASSWORD);
			OauthParam param = new OauthParam(SharedConst.CLIENT_ID, pwdStr, SharedConst.CLIENT_REDIRECT_URL, account,
					password);
			LoginResponse resp = mOauthClient.login(param);
			onLoginResult(resp);
			return;
		} catch (Exception e) {
			L.e(e.toString());
		}
		onLoginResult(new LoginResponse(-1, "", ""));
	}
	

	@UiThread
	void onLoginResult(LoginResponse resp) {
		pDialog.dismiss();
		if (resp.getRet() == 0) {
			isLogined = true;
			ObjectMapper mapper = new ObjectMapper();
			try {
				OauthRespose mOauthResp = mapper.readValue(resp.getResponse(), OauthRespose.class);
				PreferenceUtils.setPrefString(this, PreferenceConstants.ACCESSTOKEN, mOauthResp.getAccess_token());
				PreferenceUtils.setPrefInt(this, PreferenceConstants.EXPIRES, mOauthResp.getExpires());
				PreferenceUtils.setPrefInt(this, PreferenceConstants.EXPIRE_IN, mOauthResp.getExpires_in());
				PreferenceUtils.setPrefString(this, PreferenceConstants.REFRESHTOKEN, mOauthResp.getRefresh_token());
				mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, mOauthResp.getAccess_token());
				if (!AppContext.isServiceRunning(this, ScheduleService_.class.getName()))
					ScheduleService_.intent(this).start();

				if (AppContext.getInstance().isNetworkConnected())
					UpdateManager.getUpdateManager().checkAppUpdate(this, false);
				getFilterData();
				return;
			} catch (Exception e) {
				L.e(e.toString());
			}
		} else if (resp.getRet() == 2) {
			T.showLong(this, "账户名或密码有误");
			String account = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCOUNT, "");
			PreferenceUtils.clearPreference(this, PreferenceManager.getDefaultSharedPreferences(this));
			PreferenceUtils.setPrefString(this, PreferenceConstants.ACCOUNT, account);
			AppManager.getAppManager().finishAllActivity();
			LoginActivity_.intent(this).start();
			return;
		}
		T.showShort(this, "登陆失败");
	}


}
