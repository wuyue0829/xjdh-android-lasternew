package com.chinatelecom.xjdh.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.adapter.WebviewFragmentAdapter;
import com.chinatelecom.xjdh.bean.DevItem;
import com.chinatelecom.xjdh.bean.DevTypeItem;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.URLs;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author peter
 * 
 */
@EActivity(R.layout.activity_realtime)
public class RealtimeActivity extends BaseActivity {
	@ViewById(R.id.webview_indicator)
	TitlePageIndicator mWebviewIndicator;
	@ViewById(R.id.webview_pager)
	ViewPager mWebviewPager;
	@Extra("model")
	String model;
	@Extra("devTypeItem")
	DevTypeItem devTypeItem;
	WebviewFragmentAdapter mPagerAdapter;
	ProgressDialog pDialog;
	double nLenStart = 0; 
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
		for (DevItem e : devTypeItem.getDevlist()) {
			String loadurl = URLs.WAP_BASE_URL + "/loadrealtime?data_id=" + e.getData_id() + "&model=" + devTypeItem.getType()
			+ "&access_token="+ PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
			L.d("555555555555", loadurl);
			addView(mListViews,
					loadurl);
		}
		mWebviewPager.setAdapter(myAdapter);
		mWebviewIndicator.setViewPager(mWebviewPager);
		mWebviewIndicator.setFooterIndicatorStyle(IndicatorStyle.Triangle);
	}

	private List<WebView> mListViews = new ArrayList<WebView>();
	private WebPagerAdapter myAdapter;

	@SuppressLint("SetJavaScriptEnabled")
	private void addView(List<WebView> viewList, String url) {
		final WebView webView = new WebView(this);
		webView.getSettings().setJavaScriptEnabled(true);
		// 设置可以支持缩放 
		webView.getSettings().setSupportZoom(true); 
		// 设置出现缩放工具 
		webView.getSettings().setBuiltInZoomControls(true);
		//扩大比例的缩放
		webView.getSettings().setUseWideViewPort(true);  
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.setWebViewClient(new WebViewClient() {
			@SuppressLint("NewApi")
			@Override
			public void onPageFinished(WebView view, String url) {
				pDialog.dismiss();
				super.onPageFinished(view, url);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					if (0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
						WebView.setWebContentsDebuggingEnabled(true);
					}
				}
			}

			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
				WebResourceResponse response = null;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					if (url.contains("bootstrap.css")) {
						return getWebResourceResponseFromAsset("text/css", "css/bootstrap.css");
					} else if (url.contains("bootstrap-responsive.css")) {
						return getWebResourceResponseFromAsset("text/css", "css/bootstrap-responsive.css");
					} else if (url.contains("jquery.js")) {
						return getWebResourceResponseFromAsset("application/x-javascript", "js/jquery.js");
					} else {
						return super.shouldInterceptRequest(view, url);
					}

				}
				return response;
			}

			private WebResourceResponse getWebResourceResponseFromAsset(String mimeType, String fileName) {
				try {
					return getUtf8EncodedWebResourceResponse(mimeType, getAssets().open(fileName));
				} catch (IOException e) {
					return null;
				}
			}

			private WebResourceResponse getUtf8EncodedWebResourceResponse(String mimeType, InputStream data) {
				return new WebResourceResponse(mimeType, "UTF-8", data);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				pDialog.show();
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (Uri.parse(url).getHost().equals(Uri.parse(url).getHost())) {
					return false;
				}
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);
				return true;
			}
		});
		webView.loadUrl(url);
		viewList.add(webView);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy(); 
		this.finish();
//		for (WebView webView : mListViews) {
//			webView.destroy();
//		}
		
	}

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
			if (devTypeItem.getName().equals("开关电源")) {
				return devTypeItem.getDevlist()[position].getDev_group();
			}else{
				return devTypeItem.getDevlist()[position].getName();
			}
				
			
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
