package com.chinatelecom.xjdh.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chinatelecom.xjdh.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

@EActivity(R.layout.activity_video_vlc)
public class SurveillanceActivity extends Activity implements SurfaceHolder.Callback, IVLCVout.Callback, IVLCVout.OnNewVideoLayoutListener {
	@ViewById
	SurfaceView mVideoSurface;
    @ViewById
    View video_loading;
	@Extra("URL")
	String URL;


	private SurfaceHolder mSurfaceHolder;

	private LibVLC libvlc;
    private MediaPlayer mMediaPlayer = null;
    private int mVideoHeight = 0;
    private int mVideoWidth = 0;
    private int mVideoVisibleHeight = 0;
    private int mVideoVisibleWidth = 0;
    private int mVideoSarNum = 0;
    private int mVideoSarDen = 0;
    
	@ViewById
    LinearLayout lly;
	private Boolean mIsFullScreen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        Log.i("zhangonCreat", "come in");
	}

	@AfterViews
	void showView() {
        Log.i("zhangshowView", "come in");
		mSurfaceHolder = mVideoSurface.getHolder();
		mSurfaceHolder.setFormat(PixelFormat.RGBX_8888);
//		mSurfaceHolder.addCallback(this);
		createPlayer(URL);
	}

	@Override
    protected void onResume() {
        super.onResume();
    }
 

	@Override
	public void onPause() {
		super.onPause();
        Log.i("zhangonPause", "come in");
		releasePlayer();
        this.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
        Log.i("zhangonDestory", "come in");
		releasePlayer();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// 横屏
			mIsFullScreen = true;
			// 去掉系统通知栏
			lly.setVisibility(View.GONE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setViewFullScreen();
			// 调整mFlVideoGroup布局参数
		} else {
			mIsFullScreen = false;
			lly.setVisibility(View.INVISIBLE);
			// 清除flag,恢复显示系统状态栏
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            updateVideoSurfaces();
		}
	}

	@Click(R.id.back_img)
	void exitPlay() {
		releasePlayer();
		this.finish();
	}

    private void updateVideoSurfaces() {
        int sw = getWindow().getDecorView().getWidth();
        int sh = getWindow().getDecorView().getHeight();

        // sanity check
        if (sw * sh == 0) {
            return;
        }

        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        //竖屏
        if(isPortrait){
            lly.setVisibility(View.VISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if(sw > sh){
                //横屏以后再竖屏
                mMediaPlayer.getVLCVout().setWindowSize(sh, sw);
            }else{
                mMediaPlayer.getVLCVout().setWindowSize(sw, sh);
            }
        }else{
            lly.setVisibility(View.GONE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mMediaPlayer.getVLCVout().setWindowSize(sw, sh);
    }
        Log.i("windowSize", "width" + sw + ",videoHeight" + sh);

        /*LayoutParams lp = mVideoSurface.getLayoutParams();
        if (mVideoWidth * mVideoHeight == 0) {
            *//* Case of OpenGL vouts: handles the placement of the video using MediaPlayer API *//*
            *//*lp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoSurface.setLayoutParams(lp);
            lp = mVideoSurfaceFrame.getLayoutParams();
            lp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoSurfaceFrame.setLayoutParams(lp);
            changeMediaPlayerLayout(sw, sh);*//*
            return;
        }

        if (lp.width == lp.height && lp.width == LayoutParams.MATCH_PARENT) {
            *//* We handle the placement of the video using Android View LayoutParams *//*
            mMediaPlayer.setAspectRatio(null);
            mMediaPlayer.setScale(0);
        }

        double dw = sw, dh = sh;
        final boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        if (sw > sh && isPortrait ||  sw <sh && !isPortrait) {
            dw = sh;
            dh = sw;
        }

        // compute the aspect ratio
        double ar, vw;
        if (mVideoSarDen == mVideoSarNum) {
            *//* No indication about the density, assuming 1:1 *//*
            vw = mVideoVisibleWidth;
            ar = (double)mVideoVisibleWidth / (double)mVideoVisibleHeight;
        } else {
            *//* Use the specified aspect ratio *//*
            vw = mVideoVisibleWidth * (double)mVideoSarNum / mVideoSarDen;
            ar = vw / mVideoVisibleHeight;
        }

        // compute the display aspect ratio
        double dar = dw / dh;*/

        /*switch (CURRENT_SIZE) {
            case SURFACE_BEST_FIT:
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_FIT_SCREEN:
                if (dar >= ar)
                    dh = dw / ar; *//* horizontal *//*
                else
                    dw = dh * ar; *//* vertical *//*
                break;
            case SURFACE_FILL:
                break;
            case SURFACE_16_9:
                ar = 16.0 / 9.0;
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_4_3:
                ar = 4.0 / 3.0;
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_ORIGINAL:
                dh = mVideoVisibleHeight;
                dw = vw;
                break;
        }

         set display size
        lp.width  = (int) Math.ceil(dw * mVideoWidth / mVideoVisibleWidth);
        lp.height = (int) Math.ceil(dh * mVideoHeight / mVideoVisibleHeight);
        mVideoSurface.setLayoutParams(lp);
        if (mSubtitlesSurface != null)
            mSubtitlesSurface.setLayoutParams(lp);

        // set frame size (crop if necessary)
        lp = mVideoSurfaceFrame.getLayoutParams();
        lp.width = (int) Math.floor(dw);
        lp.height = (int) Math.floor(dh);
        mVideoSurfaceFrame.setLayoutParams(lp);

        mVideoSurface.invalidate();
        if (mSubtitlesSurface != null)
            mSubtitlesSurface.invalidate();*/
    }

    private static final int HANDLER_SURFACE_SIZE = 3;
    
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
                switch (msg.what) {
                case HANDLER_SURFACE_SIZE:
                        video_loading.setVisibility(View.GONE);
                        break;
                }
        }
};

    /*************
     * Player
     *************/

    private void createPlayer(String media) {
        releasePlayer();
        try {
            // Create LibVLC
            // TODO: make this more robust, and sync with audio demo
            ArrayList<String> options = new ArrayList<String>();
            //options.add("--subsdec-encoding <encoding>");
            options.add("--aout=opensles");
            options.add("--audio-time-stretch"); // time stretching
            options.add("--rtsp-tcp"); // time stretching

            Log.d("zhang", "URL" + URL);
            options.add("--network-caching=1000"); // time stretching
            //options.add("-vvv"); // verbosity
            libvlc = new LibVLC(this, options);
            mSurfaceHolder.setKeepScreenOn(true);

            // Create media player
            mMediaPlayer = new MediaPlayer(libvlc);
            mMediaPlayer.setEventListener(mPlayerListener);

            // Set up video output
            final IVLCVout vout = mMediaPlayer.getVLCVout();
            vout.setVideoView(mVideoSurface);
            //vout.setSubtitlesView(mSurfaceSubtitles);
            vout.addCallback(this);
            vout.attachViews(this);

            Media m = new Media(libvlc, Uri.parse(media));
            mMediaPlayer.setMedia(m);
            Log.d("zhang", "before play");
            mMediaPlayer.play();
            Log.d("zhang", "after play");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating player!", Toast.LENGTH_LONG).show();
        }
    }

    // TODO: handle this cleaner
    private void releasePlayer() {
    	Log.d("zhang", "Release Player called");
        if (libvlc == null)
            return;
    	Log.d("zhang", "Release Player called before stop");
        mMediaPlayer.stop();
        Log.d("zhang", "Release Player called after stop");
        final IVLCVout vout = mMediaPlayer.getVLCVout();
        Log.d("zhang", "Release Player called after stop 1");
        vout.removeCallback(this);
        vout.detachViews();
        Log.d("zhang", "Release Player called after stop 2");
        mSurfaceHolder = null;
        libvlc.release();
        libvlc = null;

        mVideoWidth = 0;
        mVideoHeight = 0;
    	Log.d("zhang", "Release Player out");
    }

    /*************
     * Events
     *************/

    private MediaPlayer.EventListener mPlayerListener = new MyPlayerListener(this);

    @Override
    public void onNewVideoLayout(IVLCVout vout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
        Log.i("zhangOnNewVideoLayout", "come in");
        mVideoWidth = width;
        mVideoHeight = height;
        mVideoVisibleWidth = visibleWidth;
        mVideoVisibleHeight = visibleHeight;
        mVideoSarNum = sarNum;
        mVideoSarDen = sarDen;
        updateVideoSurfaces();
//        changeSurfaceSize();
    }

    @Override
    public void onSurfacesCreated(IVLCVout vout) {

    }

    @Override
    public void onSurfacesDestroyed(IVLCVout vout) {

    }

    private static class MyPlayerListener implements MediaPlayer.EventListener {
        private static final String TAG = null;
		private WeakReference<SurveillanceActivity> mOwner;

        public MyPlayerListener(SurveillanceActivity owner) {
            mOwner = new WeakReference<SurveillanceActivity>(owner);
        }

        @Override
        public void onEvent(MediaPlayer.Event event) {
        	SurveillanceActivity player = mOwner.get();

            switch(event.type) {
                case MediaPlayer.Event.EndReached:
                    Log.d(TAG, "MediaPlayerEndReached");
                    player.releasePlayer();
                    break;
                case MediaPlayer.Event.Playing:
	                	player.mHandler.removeMessages(HANDLER_SURFACE_SIZE);
	                	player.mHandler.sendEmptyMessage(HANDLER_SURFACE_SIZE);
                    break;
                case MediaPlayer.Event.Paused:
                case MediaPlayer.Event.Stopped:
	                	player.releasePlayer();
	                	break;
                default:
                	  
                    break;
            }
        }
    }



	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

    public void setViewFullScreen(){
        //全屏模式
        //获取整个屏幕的宽高
        DisplayMetrics DisplayMetrics=new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(DisplayMetrics);
        int videoHeight=DisplayMetrics.heightPixels;
        int videoWidth=DisplayMetrics.widthPixels;
        // sanity check
        if (videoHeight * videoWidth == 0) {
            return;
        }
        mMediaPlayer.getVLCVout().setWindowSize(videoWidth, videoHeight);
        Log.i("windowSize", "width" + videoWidth + ",videoHeight" + videoHeight);
    }
}