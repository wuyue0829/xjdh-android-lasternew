package com.chinatelecom.xjdh.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.fragment.SettingFragment_;

import android.os.Bundle;
/**
 * @author peter
 * 
 */
@EActivity(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("设置");
	}

	@AfterViews
	void bindData() {
		getFragmentManager().beginTransaction().replace(R.id.setting_fragment, new SettingFragment_()).commit();
	}
}
