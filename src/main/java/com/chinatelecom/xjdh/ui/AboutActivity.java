package com.chinatelecom.xjdh.ui;


import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.utils.UpdateManager;


@EActivity(R.layout.about_view)
public class AboutActivity extends BaseActivity{
	
	@Click(R.id.updata)
	void updataClick(){
		UpdateManager.getUpdateManager().checkAppUpdate(AboutActivity.this, true);
	}
	@Click(R.id.feed)
	void feedOnClick(){
		FeedBackActivity_.intent(AboutActivity.this).start();
	}

}
