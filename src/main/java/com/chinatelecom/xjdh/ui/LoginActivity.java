package com.chinatelecom.xjdh.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppManager;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.ClaimOpenId;
import com.chinatelecom.xjdh.bean.ClaimResoure;
import com.chinatelecom.xjdh.bean.ClaimToken;
import com.chinatelecom.xjdh.bean.IdentificationPlatform;
import com.chinatelecom.xjdh.bean.LoginResponse;
import com.chinatelecom.xjdh.bean.MobileAuth;
import com.chinatelecom.xjdh.bean.OauthParam;
import com.chinatelecom.xjdh.bean.OauthRespose;
import com.chinatelecom.xjdh.bean.UserInfo;
import com.chinatelecom.xjdh.linphone.LinphoneUtils;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.rest.client.ClaimTokenRestClientInterface;
import com.chinatelecom.xjdh.rest.client.OauthRestClientInterface;
import com.chinatelecom.xjdh.utils.AESUtil;
import com.chinatelecom.xjdh.utils.CryptoUtils;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;
import com.chinatelecom.xjdh.utils.URLs;
import com.ultrapower.auth.AuthWbLoginActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.rest.MediaType;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author peter
 * 
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity{
	@ViewById(R.id.et_login_password)
	EditText mPasswordEt;
	@ViewById(R.id.et_login_user_name)
	EditText mAccountEt;
	@ViewById(R.id.cb_is_remeber)
	CheckBox mAutoSavePasswordCK;
	@ViewById(R.id.btn_authorization)
	Button btn_authorization;
	@ViewById
	TextView msg;
	@RestService
	OauthRestClientInterface mOauthClient;
//	@RestService
//	OauthRestClientInterface apiRestClientInterface2;
	@RestService
	ApiRestClientInterface apiRestClientInterface;
	@RestService
	ClaimTokenRestClientInterface mApiClient;
	ProgressDialog pDialog;
	private String password;
	private String mobile;
	String mAccount;
	String mPassword;
	@SuppressWarnings("unused")
	private String meToken = "";
	public static final int REQUESTCODE = 6;
	final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

	private String err;
	private String state;
	private String code;// 通过授权码申请服务申请到的授权码
	private String metoken;// 该参数只适用于集团移动应用框架
	private String access_token;// 授权令牌
	private String client_id = "650228RING";// 分配给第三方应用的appid
	private String resource_key = "9283ae2516d74e97afd58483d4dec222";// 分配给第三方应用的资源key
	private String token_secret = "605223a326274a2a8701b0a381744318";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		boolean bCanStartPhoneInitialization = (Build.VERSION.SDK_INT >= 23) ?  askPermissions() : true;
		// 获取私密令牌的方式
		// meToken = getIntent().getStringExtra("metoken");
		
//		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
//		L.d(">>>>>>>>>>>>>>>>>>>", token);
//		mOauthClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
	}


	private boolean askPermissions()
	{
		List<String> permissionsNeeded = new ArrayList<String>();

		final List<String> permissionsList = new ArrayList<String>();
		if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))           permissionsNeeded.add("Record audio");
		if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) permissionsNeeded.add("Write logs to sd card");
		if (!addPermission(permissionsList, Manifest.permission.CAMERA))                 permissionsNeeded.add("Camera");
		if (!addPermission(permissionsList, Manifest.permission.USE_SIP))                permissionsNeeded.add("Use SIP protocol");


		if (permissionsList.size() > 0) {
			if (permissionsNeeded.size() > 0) {
				// Need Rationale
				//String message = "You need to grant access to " + permissionsNeeded.get(0);
				//for (int i = 1; i < permissionsNeeded.size(); i++) message = message + ", " + permissionsNeeded.get(i);


				ActivityCompat.requestPermissions(this,
						permissionsList.toArray(new String[permissionsList.size()]),
						REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

				return false;
			}

			ActivityCompat.requestPermissions(this,
					permissionsList.toArray(new String[permissionsList.size()]),
					REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
			return false;
		}

		return true;
	}

	private boolean addPermission(List<String> permissionsList, String permission) {
		if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
			permissionsList.add(permission);
			// Check for Rationale Option
			if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))     return false;
		}


		return true;
	}

	@AfterViews
	void initData() {
		
		Calendar c = Calendar.getInstance();  
		int year = c.get(Calendar.YEAR);  
		int month = c.get(Calendar.MONTH);  
		int day = c.get(Calendar.DAY_OF_MONTH);
		int sum=year+month+day;
		if (sum>2054) {
			msg.setVisibility(View.GONE);
		}else if(sum==2054){
			msg.setVisibility(View.GONE);
		}else{
			msg.setVisibility(View.VISIBLE);
		}
		mAccountEt.setText(PreferenceUtils.getPrefString(this, PreferenceConstants.ACCOUNT, ""));
		getUserInfo();
	}
	ApiResponse mApiResps;
	MobileAuth Auth;
	@Background
	void mobileAuth() {
		try {
			mApiResps  = apiRestClientInterface.getMobileAuth();
			if (mApiResps.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				Auth = mapper.readValue(mApiResps.getData(), MobileAuth.class);
				L.d("************", Auth.toString());
				return;
			}
		} catch (Exception e) {
			L.e(e.toString());
		}
	}

	
	UserInfo mUserInfo;
	@Background
	void getUserInfo() {
		try {
			mApiResps = apiRestClientInterface.getUserInfo();
			if (mApiResps.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				L.d("&&&&&&&&&&&&&&&&&&&&&&&&&&", mapper.writeValueAsString(apiRestClientInterface.getUserInfo().toString()));
				mUserInfo = mapper.readValue(mApiResps.getData(), UserInfo.class);
				L.d("++++++++++++", mapper.writeValueAsString(mUserInfo.toString()));
				saveLinphone();
			}
		} catch (Exception e) {
			L.e(e.toString());
		}
	}

	@UiThread
	void saveLinphone(){
		LinphoneUtils.SaveUserInfo(mContext,this.mUserInfo.getLinphoneNumber(),"1234",
				URLs.VOIP,"5060",this.mUserInfo.getFull_name(),"","",URLs.VOIP,"3478");


		PreferenceUtils.setPrefString(mContext, PreferenceConstants.LINEPHONENUM, this.mUserInfo.getLinphoneNumber());
		PreferenceUtils.setPrefString(mContext, PreferenceConstants.USERNAME, this.mUserInfo.getFull_name());
		LinphoneUtils.loginSip(mContext);
		MainActivity_.intent(this).isDoLogin(false).start();
		finish();
	}

	@Click(R.id.btn_login)
	void onLoginClicked() {
		// submittData();
		pDialog = new ProgressDialog(this);
		mAccount = mAccountEt.getText().toString();
		mPassword = mPasswordEt.getText().toString();
		if (TextUtils.isEmpty(mAccount) && TextUtils.isEmpty(mPassword)) {
			T.showLong(this, "请输入登录用户名和密码");
			return;
		} else if (TextUtils.isEmpty(mAccount)) {
			T.showLong(this, "请输入登录用户名");
			return;
		} else if (TextUtils.isEmpty(mPassword)) {
			T.showLong(this, "请输入登录密码");
			return;
		}
		pDialog.setMessage("正在登录，请稍后...");
		pDialog.show();
		doLogin();
		
	}

	@Background
	void doLogin() {
		try {
			String pwdStr = "xjtele";
//			String pwdStr = CryptoUtils.decrypt(SharedConst.PASSWORD_CRYPRO_SEED, SharedConst.CLIENT_PASSWORD);
			LogUtil.e("pwdStr====="+pwdStr);
			
//			OauthParam param = new OauthParam("xjtele", "xjtele", "xjdh.jimglobal.com", mAccount,
//					CryptoUtils.MD5(mPassword, true));
//			L.d("000000", CryptoUtils.MD5(mPassword, true));
			
			OauthParam param = new OauthParam(SharedConst.CLIENT_ID, pwdStr, SharedConst.CLIENT_REDIRECT_URL, mAccount,
					CryptoUtils.MD5(mPassword, true));
			LoginResponse resp = mOauthClient.login(param);
			
			loginResult(resp);
			return;
		} catch (Exception e) {
			L.e(e.toString());
		}
		loginResult(new LoginResponse(-1, "", ""));
	}

	@UiThread
	void loginResult(LoginResponse resp) {
		Log.i("<<<<<<<<<", "resp.getRet()" + resp.getRet());
		if (pDialog != null && pDialog.isShowing())
			pDialog.dismiss();
		if (resp.getRet() == 0) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				L.d("WWWWWWWWWWWWWWWWWWWWWWWWWw", resp.toString());
				OauthRespose mOauthResp = mapper.readValue(resp.getResponse(), OauthRespose.class);
				L.d("...........", mOauthResp.toString());
				PreferenceUtils.setPrefString(this, PreferenceConstants.USER_INFO, mapper.writeValueAsString(resp));
				PreferenceUtils.setPrefString(this, PreferenceConstants.ACCESSTOKEN, mOauthResp.getAccess_token());
				PreferenceUtils.setPrefInt(this, PreferenceConstants.EXPIRES, mOauthResp.getExpires());
				PreferenceUtils.setPrefInt(this, PreferenceConstants.EXPIRE_IN, mOauthResp.getExpires_in());
				PreferenceUtils.setPrefString(this, PreferenceConstants.REFRESHTOKEN, mOauthResp.getRefresh_token());
				apiRestClientInterface.setHeader(SharedConst.HTTP_AUTHORIZATION, mOauthResp.getAccess_token());
				save2Preferences();
				getUserInfo();
				return;
			} catch (Exception e) {
				L.e(e.toString());
			}
		} else if (resp.getRet() == 2) {
			T.showLong(this, "账户名或密码有误");
			return;
		}else if(resp.getRet() == 8){
			T.showShort(this, "门禁用户禁止登录");
			return;
		}
//		Toast.makeText(this, "" + resp.getRet(), 0).show();
		T.showShort(this, "登陆失败");
	}

	
	private void save2Preferences() {
		boolean isAutoSavePassword = mAutoSavePasswordCK.isChecked();
		PreferenceUtils.setPrefString(this, PreferenceConstants.ACCOUNT, mAccount);// 帐号是一直保存的
		if (isAutoSavePassword)
			PreferenceUtils.setPrefString(this, PreferenceConstants.PASSWORD, CryptoUtils.MD5(mPassword, true));
		else
			PreferenceUtils.setPrefString(this, PreferenceConstants.PASSWORD, "");
	}

	@Click(R.id.btn_authorization)
	void AuthorizationClicked() {
		Intent intent = new Intent(LoginActivity.this, AuthWbLoginActivity.class);
		intent.putExtra(AuthWbLoginActivity.STATE, "123456");// 原样返回判断状态值
		intent.putExtra(AuthWbLoginActivity.SCOPE, "s");// 授权范围 用户信息
		LoginActivity.this.startActivityForResult(intent, 1);
	}

	ObjectMapper mapper = new ObjectMapper();

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == AuthWbLoginActivity.RESULT_CODE) {
			String jsonData = data.getStringExtra(AuthWbLoginActivity.RESULT_STR);
			L.e("jsonData  :" + jsonData);
			try {
				IdentificationPlatform platform = mapper.readValue(jsonData, IdentificationPlatform.class);
				err = platform.getError();
				state = platform.getState();
				code = platform.getCode();
				metoken = platform.getMetoken();
				L.e("解析JSON  ：" + err + "" + state + "" + code + "" + metoken);
			} catch (Exception e) {
			}
		}
		claimToken();

	}

	@Background
	public void claimToken() {
		/**
		 * 申请令牌 使用FormHttpMessageConverter做信息转换--> 做POST请求
		 * 必须使用MultiValueMap才能转化为HTTP请求 APPLICATION_FORM_URLENCODED:采用的是FORM表单提交
		 */
		mApiClient.setHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, Object> items = new LinkedMultiValueMap<String, Object>();
		items.add("grant_type", "authorization_code");
		items.add("client_id", "650228RING");
		items.add("token_secret", "605223a326274a2a8701b0a381744318");
		items.add("device", "android");
		items.add("metoken", metoken);
		items.add("code", code);
		// items.add("metoken", "cd791fabe8f64f92ba09c35b332bb68f");
		// items.add("code", "c9fe3267ce5694ffdee00ae22e58cdb4");
		ClaimToken claimToken = mApiClient.accessToken(items);
		L.e("err:" + claimToken.getError() + "  Expires_in :" + claimToken.getExpires_in() + "  Access_token  :"
				+ claimToken.getAccess_token());
		access_token = claimToken.getAccess_token();
		cliamOpenId();
	}

	public void cliamOpenId() {
		/**
		 * 申请openId
		 */
		ClaimOpenId claimOpenId = mApiClient.me(access_token, client_id);
		L.e(" Error ：" + claimOpenId.getError() + "  Client_id ：" + claimOpenId.getClient_id() + "  Open_id :"
				+ claimOpenId.getOpen_id());
		claimResource();
	}

	public void claimResource() {
		/**
		 * 申请资源
		 */
		mApiClient.setHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, Object> resource = new LinkedMultiValueMap<String, Object>();
		resource.add("access_token", access_token);
		resource.add("resource_key", resource_key);
		resource.add("client_id", client_id);
		ClaimResoure claimResoure = mApiClient.get_basicUserInfo(resource);
		L.e("申请资源 :" + "Error :" + claimResoure.getError() + " Account：" + claimResoure.getAccount() + " getUsername :"
				+ claimResoure.getUsername() + " getDeptname :" + claimResoure.getDeptname() + " getEmail :"
				+ claimResoure.getEmail() + " getTelephone :" + claimResoure.getTelephone() + " getDeptid："
				+ claimResoure.getDeptid() + " getMobile ：" + claimResoure.getMobile());
		submittData(claimResoure);
	}

	public void submittData(ClaimResoure claimResoure) {
		LinkedHashMap<String, String> items = new LinkedHashMap<String, String>(0);
		// items.put("username", claimResoure.getUsername());
		// items.put("deptname", claimResoure.getDeptname());
		// items.put("email", claimResoure.getEmail());
		// items.put("telephone", claimResoure.getTelephone());
		// items.put("deptid", claimResoure.getDeptid());
		// items.put("mobile", claimResoure.getMobile());
		items.put("mobile", claimResoure.getMobile());
		ApiResponse resp = mOauthClient.creationUser(items);
		L.d("111111111111111111111", resp.toString());
		password = resp.getResponse();
		mobile = resp.getMobile();
		L.e("resp :" + resp.getResponse());
		if (resp.getRet() == 0) {
			L.e("data :" + resp.getData() + "ret :" + resp.getRet());
		} else {
			L.e("data :" + resp.getData() + "ret :" + resp.getRet());
		}
		ShowAccountPassword();
	}

	@UiThread
	void ShowAccountPassword() {
		mAccountEt.setText(mobile);
		mPasswordEt.setText(password);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		L.d("----生命周期---", "==onDestroy==");
		AppManager.getAppManager().removeActivity(this);
	}
}
