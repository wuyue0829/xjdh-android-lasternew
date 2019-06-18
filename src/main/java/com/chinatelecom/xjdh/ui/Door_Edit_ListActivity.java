package com.chinatelecom.xjdh.ui;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.adapter.WebviewFragmentAdapter;
import com.chinatelecom.xjdh.bean.DevItem;
import com.chinatelecom.xjdh.bean.DevQuestion;
import com.chinatelecom.xjdh.bean.DevTypeItem;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.view.DoorView_;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

@EActivity(R.layout.activity_realtime)
public class Door_Edit_ListActivity extends BaseActivity {
	@ViewById(R.id.webview_indicator)
	com.viewpagerindicator.TitlePageIndicator mWebviewIndicator;
	@ViewById(R.id.webview_pager)
	ViewPager mWebviewPager;

	@Extra("devTypeItem")
	DevQuestion devTypeItem;
	WebviewFragmentAdapter mPagerAdapter;
	ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pDialog = new ProgressDialog(this);
		pDialog.setMessage(getResources().getString(R.string.progress_loading_msg));
	}

	@AfterViews
	void bindData() {
		setTitle(devTypeItem.getName());
		myAdapter = new WebPagerAdapter();
		for (DevItem e : devTypeItem.getDevList()) {
			L.d("================", e.toString());
			DoorView_ newDv = (DoorView_)DoorView_.build(this);
			newDv.SetPara(e.getName(), e.getData_id(), e.getCan_open());
			mListViews.add(newDv);
		}
		mWebviewPager.setAdapter(myAdapter);
		mWebviewIndicator.setViewPager(mWebviewPager);
		mWebviewIndicator.setFooterIndicatorStyle(IndicatorStyle.Triangle);
	}

	private List<DoorView_> mListViews = new ArrayList<DoorView_>();
	private WebPagerAdapter myAdapter;

	private class WebPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return devTypeItem.getDevList()[position].getName();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}
	}
}
