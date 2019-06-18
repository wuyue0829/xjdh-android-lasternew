package com.chinatelecom.xjdh.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.rest.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.ApiResponseUpLoad;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

@EActivity(R.layout.fault_detail_view)
public class FaultDetailActivity extends BaseActivity {
	@Extra
	String content;
	@Extra
	int ID;
	@ViewById
	TextView describe;
	@ViewById
	EditText etMemo;
	@RestService
	ApiRestClientInterface mApiClient;
	public static final int ACTIVICE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
	}

	@AfterViews
	void showView() {
		describe.setText(content);
	}
	
	@Background
	public void DoWorkerUpload(int memoID, int activice_con) {
		try {
			MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
			formData.add("memoID", String.valueOf(memoID));
			formData.add("active", String.valueOf(activice_con));
			mApiClient.setHeader("Content-Type", MediaType.MULTIPART_FORM_DATA);
			ApiResponseUpLoad resp = mApiClient.updateMemo(formData);

			L.e("上传照片........" + resp.getRet() + "" + resp.getData() + "formData"
					+ formData.get(SharedConst.UPLOAD_IMG));
			L.e("NewGrouping :" + resp.getNewGrouping());
			if (resp.getRet() == 0) {
				ShowResponse(resp);
				return;
			}
		} catch (Exception ex) {
			ApiResponseUpLoad resp = new ApiResponseUpLoad();
			resp.setRet(1);
			resp.setData("请求失败");
			ShowResponse(resp);
			L.e("Exception" + ex.toString());
			L.e("Exception" + resp.getRet());
		}
		}

	@Click(R.id.submit)
	void submitClick() {
		DoWorkerUpload(ID, ACTIVICE);
	}

	@UiThread
	public void ShowResponse(ApiResponseUpLoad resp) {
		if (resp.getRet() == 0) {
			T.showLong(this, "上传成功");
			finish();
		} else {
			T.showLong(this, resp.getData());
		}
	}

}
