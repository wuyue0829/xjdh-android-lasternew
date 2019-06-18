package com.chinatelecom.xjdh.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppContext;
import com.chinatelecom.xjdh.app.AppManager;
import com.chinatelecom.xjdh.utils.ToolBarHelper;
import com.portsip.PortSipSdk;

import java.util.ArrayList;

public abstract class BaseActivity extends AppCompatActivity {
	private ToolBarHelper mToolBarHelper;
	public Toolbar toolbar;
	public Context mContext;
	public Activity mActivity;
	public PortSipSdk mEngine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
		mContext = this;
		mEngine = ((AppContext)getApplicationContext()).mEngine;
		mActivity = this;
	}

	@Override
	public void setContentView(int layoutResID) {
		if (this.getClass().equals(LoginActivity_.class)) {
			super.setContentView(layoutResID);
			return;
		}
		mToolBarHelper = new ToolBarHelper(this, layoutResID);
		toolbar = mToolBarHelper.getToolBar();
		super.setContentView(mToolBarHelper.getContentView());
		setSupportActionBar(toolbar);
		onCreateCustomToolBar(toolbar);
		if (!this.getClass().equals(MainActivity_.class)) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(false);
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		}
	}

	public void onCreateCustomToolBar(Toolbar toolbar) {
		toolbar.setContentInsetsRelative(0, 0);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}

	@Override
	protected void onResume() {
		if (mListeners.size() > 0)
			for (BackPressHandler handler : mListeners) {
				handler.activityOnResume();
			}
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mListeners.size() > 0)
			for (BackPressHandler handler : mListeners) {
				handler.activityOnPause();
			}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (!this.getClass().equals(MainActivity_.class)) {
				finish();
				return true;
			} else {
				return super.onOptionsItemSelected(item);
			}
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	


	public static ArrayList<BackPressHandler> mListeners = new ArrayList<BackPressHandler>();

	public static abstract interface BackPressHandler {
		public abstract void activityOnResume();

		public abstract void activityOnPause();
	}
}
