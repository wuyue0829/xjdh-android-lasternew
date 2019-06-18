package com.chinatelecom.xjdh.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.webkit.WebView;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.ui.BaseActivity;
import com.chinatelecom.xjdh.ui.LoginActivity_;
import com.chinatelecom.xjdh.ui.MainActivity_;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;

import org.androidannotations.annotations.EActivity;

/**
 * @author peter
 * 
 */
@EActivity
public class AppStart extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.start, null);
		setContentView(view);
		AppManager.getAppManager().addActivity(this);
		WebView.setWebContentsDebuggingEnabled(true);
		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(1000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				redirectTo();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

		});
	}

	/**
	 * 跳转到...
	 */
	private void redirectTo() {
		String password = PreferenceUtils.getPrefString(this, PreferenceConstants.PASSWORD, "");
		String account = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCOUNT, "");
		if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(account)) {
			MainActivity_.intent(this).isDoLogin(true).start();
		} else {
			LoginActivity_.intent(this).start();
		}
		finish();
	}

}
