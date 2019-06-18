package com.chinatelecom.xjdh.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppContext;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author peter
 * 
 */
@EActivity(R.layout.webview)
public class WebViewActivity extends BaseActivity {
	@ViewById(R.id.webview_main)
	WebView webview;
	@ViewById(R.id.tv_webview_reload)
	TextView mTvWebviewReload;
	@Extra
	String originalUrl;
	@Extra
	String title;
	ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(title);
	
		pDialog = new ProgressDialog(this);
		pDialog.setMessage(getResources().getString(R.string.progress_loading_msg));
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@AfterViews
	void bindData() {
		webview.getSettings().setJavaScriptEnabled(true);   
		// 设置可以支持缩放   
		webview.getSettings().setSupportZoom(true);   
		// 设置出现缩放工具   
//		webview.getSettings().setBuiltInZoomControls(true);  
		//扩大比例的缩放  
		webview.getSettings().setUseWideViewPort(true);  
		//自适应屏幕  
		webview.setWebViewClient(new NoAdWebViewClient(this,webview));
		webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);  
		webview.getSettings().setLoadWithOverviewMode(true); 
		webview.setWebViewClient(new WebViewClient() {
			@SuppressLint("NewApi")
			@Override
			public void onPageFinished(WebView view, String url) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					if (0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
						WebView.setWebContentsDebuggingEnabled(true);
					}
				}
				mTvWebviewReload.setVisibility(View.GONE);
				webview.setVisibility(View.VISIBLE);
				pDialog.dismiss();
				super.onPageFinished(view, url);
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

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				webview.setVisibility(View.GONE);
				mTvWebviewReload.setVisibility(View.VISIBLE);
				if (AppContext.getInstance().isNetworkConnected()) {
					mTvWebviewReload.setText("网络好像出问题了，点击重试");
				} else {
					mTvWebviewReload.setText("加载失败，点击重试");
				}
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			private WebResourceResponse getUtf8EncodedWebResourceResponse(String mimeType, InputStream data) {
				return new WebResourceResponse(mimeType, "UTF-8", data);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (Uri.parse(url).getHost().equals(Uri.parse(originalUrl).getHost())) {
					return false;
				}
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);
				return true;
			}
		});
		loadWebviewContent();
	}

	@Click(R.id.tv_webview_reload)
	void loadWebviewContent() {
		webview.loadUrl(originalUrl);
		pDialog.show();
	}
}
