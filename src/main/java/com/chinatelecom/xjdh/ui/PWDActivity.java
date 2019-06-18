package com.chinatelecom.xjdh.ui;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.JsonResponse;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

@EActivity(R.layout.pwd_main)
public class PWDActivity extends BaseActivity {
	@RestService
	ApiRestClientInterface apiRestClientInterface;
	@ViewById
	EditText pwd_old,pwd_new,pwd_agin;
	
	String mpwd_old;
	String mpwd_new;
	String mpwd_agin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 获取私密令牌的方式
		// meToken = getIntent().getStringExtra("metoken");
		setTitle("修改密码");
		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
		apiRestClientInterface.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
//		String password = PreferenceUtils.getPrefString(this, PreferenceConstants.PASSWORD, "");
//		mpwd_old = password;
	}

//	@AfterViews
//	void bindData(){
//		pwd_old.setText(mpwd_old);
//	}
	
	@Click(R.id.pwd_canncle)
	void pwdCanncle(){
		finish();
	}

	@Click(R.id.pwd_arrage)
	void pwdArrage(){
		
		mpwd_old =pwd_old.getText().toString();
		mpwd_new = pwd_new.getText().toString();
		mpwd_agin = pwd_agin.getText().toString();
		L.d("==========",mpwd_old);
		if (pwd_old.getText().toString().length() < 6) {
			T.showLong(getApplicationContext(), "密码最少6个字符");
			return;
		}
		if (pwd_new.getText().toString().length() < 6) {
			T.showLong(getApplicationContext(), "密码最少6个字符");
			return;
		}
		if (pwd_agin.getText().toString().length() < 6) {
			T.showLong(getApplicationContext(), "密码最少6个字符");
			return;
		}
		if (TextUtils.isEmpty(mpwd_old) 
				&&TextUtils.isEmpty(mpwd_new)
				&& TextUtils.isEmpty(mpwd_agin)
				) {
			T.showLong(this, "密码不能为空");
			return;
		}
		postChangePwd(mpwd_old,mpwd_new,mpwd_agin);
	}
	
	@Background
	void postChangePwd(String old,String news,String agin){
		try {
			ObjectMapper mapper = new ObjectMapper();
			
//			ChangePwd param = new ChangePwd(mpwd_old,mpwd_new,mpwd_agin);
			JsonResponse resp = apiRestClientInterface.changepasswd(old,news,agin);
			L.d("22222222222", mapper.writeValueAsString(resp));
			changeResult(resp);
			return;
		} catch (Exception e) {
			L.e(e.toString());
		}
	}
	@UiThread
	void changeResult(JsonResponse resp){
		if (resp.getRet() == 0) {
		   T.showShort(this, resp.getMsg());
		   finish();
		}else{
			T.showShort(this, resp.getMsg());
		}
	}
	
}
