package com.chinatelecom.xjdh.ui;//package com.chinatelecom.xjdh.ui;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.androidannotations.annotations.AfterViews;
//import org.androidannotations.annotations.Click;
//import org.androidannotations.annotations.EActivity;
//import org.androidannotations.annotations.Extra;
//import org.androidannotations.annotations.ViewById;
//
//import com.chinatelecom.xjdh.R;
//
//import android.app.Activity;
//import android.content.pm.ActivityInfo;
//import android.content.res.Configuration;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//import io.vov.vitamio.MediaPlayer;
//import io.vov.vitamio.widget.MediaController;
//import io.vov.vitamio.widget.VideoView;
//
//@EActivity(R.layout.rtsp_view)
//public class RtspPlayerActivity extends Activity {
//	@ViewById
//	VideoView surface_view;
//	@Extra("URL")
//	String URL;
//	private MediaController mController;
//	@ViewById
//	FrameLayout mFlVideoGroup;
//	
//	@ViewById
//	ImageView mIvStart, back_img;
//	// 当前是否为全屏
//	@ViewById
//	LinearLayout lly;
//	private Boolean mIsFullScreen = false;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//	}
//
//	@AfterViews
//	void showView() {
//		mController = new MediaController(this, true, mFlVideoGroup);
//		mController.setVisibility(View.GONE);
//	}
//
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
//
//		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//			// 横屏
//			mIsFullScreen = true;
//			// 去掉系统通知栏
//			lly.setVisibility(View.GONE);
//			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//					WindowManager.LayoutParams.FLAG_FULLSCREEN);
//			// 调整mFlVideoGroup布局参数
//			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//					LinearLayout.LayoutParams.MATCH_PARENT);
//			mFlVideoGroup.setLayoutParams(params);
//			surface_view.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
//		} else {
//			mIsFullScreen = false;
//			lly.setVisibility(View.VISIBLE);
//			/* 清除flag,恢复显示系统状态栏 */
//			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//					LinearLayout.LayoutParams.MATCH_PARENT);
//			mFlVideoGroup.setLayoutParams(params);
//		}
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK && mIsFullScreen) {
//			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//			mController.setFullScreenIconState(false);
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//
//	@Click(R.id.back_img)
//	void exitPlay() {
//		this.finish();
//	}
//
//	@Click(R.id.mIvStart)
//	void playOnClick(View v) {
//		// TODO Auto-generated method stub
//		mIvStart.setVisibility(View.GONE);
//
//		if (URL == "") {
//			// Tell the user to provide a media file URL/path.
//			Toast.makeText(RtspPlayerActivity.this,
//					"Please edit VideoViewDemo Activity, and set path" + " variable to your media file URL/path",
//					Toast.LENGTH_LONG).show();
//			return;
//		} else {
//			/*
//			 * Alternatively,for streaming media you can use
//			 * mVideoView.setVideoURI(Uri.parse(URLstring));
//			 */
//			Map<String, String> headers = new HashMap<String, String>();
//			headers.put("max_analyze_duration", "1000") ;
//			surface_view.setVideoURI(Uri.parse(URL),headers);
//			surface_view.setMediaController(mController);
//			surface_view.requestFocus();
//
//			surface_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//				@Override
//				public void onPrepared(MediaPlayer mediaPlayer) {
//					// optional need Vitamio 4.0
//					mediaPlayer.setPlaybackSpeed(1.0f);
//				}
//			});
//		}
//		surface_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//			@Override
//			public void onCompletion(MediaPlayer mp) {
//				// 停止播放
//				surface_view.stopPlayback();
//				mIvStart.setVisibility(View.VISIBLE);
//			}
//		});
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		if (surface_view != null) {
//			// 清除缓存
//			surface_view.destroyDrawingCache();
//			// 停止播放
//			surface_view.stopPlayback();
//			surface_view = null;
//		}
//	}
//}
