package com.chinatelecom.xjdh.ui;

import java.util.LinkedHashMap;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppContext;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.StringUtils;
import com.chinatelecom.xjdh.utils.T;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author peter
 * 
 */
@EActivity(R.layout.activity_feedback)
public class FeedBackActivity extends BaseActivity {
	@RestService
	ApiRestClientInterface mApiClient;
	@ViewById(R.id.et_feedback_content)
	EditText mEtFeedBack;
	@ViewById(R.id.btn_feedback_submit)
	Button mBtnFeedBackSubmit;
	ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
	}

	@AfterViews
	void bindData() {
		setTitle("意见反馈");
		pDialog = new ProgressDialog(this);
		pDialog.setMessage(getResources().getString(R.string.progress_loading_msg));
	}

	@Click(R.id.btn_feedback_submit)
	void onBtnFeedBackClicked() {
		if (StringUtils.isEmpty(mEtFeedBack.getText().toString())) {
			T.showShort(this, "请填写反馈信息...");
			return;
		}
		if (AppContext.getInstance().isNetworkConnected()) {
			uploadFeedBack();
			pDialog.show();
		} else {
			T.showLong(this, getResources().getString(R.string.network_err_msg));
		}
	}

	@Background
	void uploadFeedBack() {
		int times = 0;
		boolean isSuccess = false;
		while (times < 5) {
			mBtnFeedBackSubmit.setClickable(false);
			LinkedHashMap<String, String> items = new LinkedHashMap<String, String>(0);
			items.put("content", mEtFeedBack.getText().toString());
			try {
				ApiResponse resp = mApiClient.addFeedback(items);
				isSuccess = resp.getRet() == 0;
				if (isSuccess)
					break;
			} catch (Exception e) {
				L.e(e.toString());
			}
			times++;
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				L.e(e1.toString());
			}

		}
		resolveAddMessageResult(isSuccess);
	}

	@UiThread
	void resolveAddMessageResult(boolean isSuccess) {
		pDialog.dismiss();
		mBtnFeedBackSubmit.setClickable(true);
		if (isSuccess) {
			T.showShort(this, "反馈成功,感谢您对我们工作的支持...");
			finish();
		} else {
			T.showShort(this, "不好意思，反馈提交失败，请重试");
		}
	}
}
