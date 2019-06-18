package com.chinatelecom.xjdh.ui;//package com.chinatelecom.xjdh.ui;
//
//import java.text.DecimalFormat;
//import java.text.NumberFormat;
//import java.util.Locale;
//
//import org.androidannotations.annotations.AfterViews;
//import org.androidannotations.annotations.Click;
//import org.androidannotations.annotations.EActivity;
//import org.androidannotations.annotations.Extra;
//import org.androidannotations.annotations.ViewById;
//
//import com.chinatelecom.xjdh.R;
//import com.mediedictionary.playerlibrary.PlayerView;
//import com.mediedictionary.playerlibrary.PlayerView.OnChangeListener;
//
//import android.app.Activity;
//import android.content.res.Configuration;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Handler.Callback;
//import android.text.TextUtils;
//import android.os.Message;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.SeekBar;
//import android.widget.SeekBar.OnSeekBarChangeListener;
//import android.widget.TextView;
//import android.widget.Toast;
//
//@EActivity(R.layout.activity_player)
//public class RtspPlayersActivity extends BaseActivity implements OnChangeListener, OnSeekBarChangeListener, Callback  {
//	private static final int SHOW_PROGRESS = 0;
//	private static final int ON_LOADED = 1;
//	private static final int HIDE_OVERLAY = 2;
//	@ViewById
//	PlayerView pv_video;
//	@Extra("URL")
//	String URL;
//	@ViewById
//	TextView tv_title,tv_time,tv_length,tv_buffer;
//	@ViewById
//	SeekBar sb_video;
//	@ViewById
//	ImageButton ib_lock,ib_backward,ib_play,ib_forward,ib_size;
//	@ViewById
//	LinearLayout  rl_loading,ll_overlay;
//	@ViewById
//	RelativeLayout rl_title;
//	private Handler mHandler;
//	 //当前是否为全屏
//    private Boolean mIsFullScreen = false;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		mHandler = new Handler(this);
//	}
//
//	@AfterViews
//	void showView() {
//		if (TextUtils.isEmpty(URL)) {
//			Toast.makeText(this, "error:no url in intent!", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		//第二步：设置参数，毫秒为单位
//		pv_video.setNetWorkCache(20000);
//
//				//第三步:初始化播放器
//		pv_video.initPlayer(URL);
//
//				//第四步:设置事件监听，监听缓冲进度等
//		pv_video.setOnChangeListener(this);
//
//				//第五步：开始播放
//		pv_video.start();
//
//				//init view
//		tv_title.setText(URL);
//		showLoading();
//		hideOverlay();
//	}
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if (event.getAction() == MotionEvent.ACTION_UP) {
//			if (ll_overlay.getVisibility() != View.VISIBLE) {
//				showOverlay();
//			} else {
//				hideOverlay();
//			}
//		}
//		return false;
//	}
//	
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		pv_video.changeSurfaceSize();
//		super.onConfigurationChanged(newConfig);
//	}
//
//	@Override
//	public void onPause() {
//		hideOverlay();
//		pv_video.stop();
//		super.onPause();
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//	}
//
//	@Override
//	public void onBufferChanged(float buffer) {
//		if (buffer >= 100) {
//			hideLoading();
//		} else {
//			showLoading();
//		}
//		tv_buffer.setText("正在缓冲中..." + (int) buffer + "%");
//	}
//
//	private void showLoading() {
//		rl_loading.setVisibility(View.VISIBLE);
//
//	}
//
//	private void hideLoading() {
//		rl_loading.setVisibility(View.GONE);
//	}
//
//	@Override
//	public void onLoadComplet() {
//		mHandler.sendEmptyMessage(ON_LOADED);
//	}
//
//	@Override
//	public void onError() {
//		Toast.makeText(getApplicationContext(), "Player Error Occur！", Toast.LENGTH_SHORT).show();
//		finish();
//	}
//
//	@Override
//	public void onEnd() {
//		finish();
//	}
//	
//	private void showOverlay() {
//		rl_title.setVisibility(View.VISIBLE);
//		ll_overlay.setVisibility(View.VISIBLE);
//		mHandler.sendEmptyMessage(SHOW_PROGRESS);
//		mHandler.removeMessages(HIDE_OVERLAY);
//		mHandler.sendEmptyMessageDelayed(HIDE_OVERLAY, 5 * 1000);
//	}
//
//	private void hideOverlay() {
//		rl_title.setVisibility(View.GONE);
//		ll_overlay.setVisibility(View.GONE);
//		mHandler.removeMessages(SHOW_PROGRESS);
//	}
//
//	private int setOverlayProgress() {
//		if (pv_video == null) {
//			return 0;
//		}
//		int time = (int) pv_video.getTime();
//		int length = (int) pv_video.getLength();
//		boolean isSeekable = pv_video.canSeekable() && length > 0;
//		ib_forward.setVisibility(isSeekable ? View.VISIBLE : View.GONE);
//		ib_backward.setVisibility(isSeekable ? View.VISIBLE : View.GONE);
//		sb_video.setMax(length);
//		sb_video.setProgress(time);
//		if (time >= 0) {
//			tv_time.setText(millisToString(time, false));
//		}
//		if (length >= 0) {
//			tv_length.setText(millisToString(length, false));
//		}
//		return time;
//	}
//
//	@Override
//	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//		if (fromUser && pv_video.canSeekable()) {
//			pv_video.setTime(progress);
//			setOverlayProgress();
//		}
//	}
//
//	@Override
//	public void onStartTrackingTouch(SeekBar seekBar) {
//
//	}
//
//	@Override
//	public void onStopTrackingTouch(SeekBar seekBar) {
//
//	}
//
//	@Override
//	public boolean handleMessage(Message msg) {
//		switch (msg.what) {
//		case SHOW_PROGRESS:
//			setOverlayProgress();
//			mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS, 20);
//			break;
//		case ON_LOADED:
//			showOverlay();
//			hideLoading();
//			break;
//		case HIDE_OVERLAY:
//			hideOverlay();
//			break;
//		default:
//			break;
//		}
//		return false;
//	}
//
//	private String millisToString(long millis, boolean text) {
//		boolean negative = millis < 0;
//		millis = java.lang.Math.abs(millis);
//		int mini_sec = (int) millis % 1000;
//		millis /= 1000;
//		int sec = (int) (millis % 60);
//		millis /= 60;
//		int min = (int) (millis % 60);
//		millis /= 60;
//		int hours = (int) millis;
//
//		String time;
//		DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		format.applyPattern("00");
//
//		DecimalFormat format2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		format2.applyPattern("000");
//		if (text) {
//			if (millis > 0)
//				time = (negative ? "-" : "") + hours + "h" + format.format(min) + "min";
//			else if (min > 0)
//				time = (negative ? "-" : "") + min + "min";
//			else
//				time = (negative ? "-" : "") + sec + "s";
//		} else {
//			if (millis > 0)
//				time = (negative ? "-" : "") + hours + ":" + format.format(min) + ":" + format.format(sec) + ":" + format2.format(mini_sec);
//			else
//				time = (negative ? "-" : "") + min + ":" + format.format(sec) + ":" + format2.format(mini_sec);
//		}
//		return time;
//	}
//
//	@Click({R.id.ib_lock,R.id.ib_forward,R.id.ib_play,R.id.ib_backward,R.id.ib_size})
//	void btnOnClick(View v) {
//		switch (v.getId()) {
//		case R.id.ib_lock:
//			break;
//		case R.id.ib_forward:
//			pv_video.seek(10000);
//			break;
//		case R.id.ib_play:
//			if (pv_video.isPlaying()) {
//				pv_video.pause();
//				ib_play.setBackgroundResource(R.drawable.ic_play);
//			} else {
//				pv_video.play();
//				ib_play.setBackgroundResource(R.drawable.ic_pause);
//			}
//			break;
//
//		case R.id.ib_backward:
//			pv_video.seek(-10000);
//			break;
//		case R.id.ib_size:
//			break;
//		default:
//			break;
//		}
//	}
//}
