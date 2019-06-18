package com.chinatelecom.xjdh.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.chinatelecom.xjdh.R;

public class RtspVideoPlayActivity extends BaseActivity implements MediaPlayer.OnErrorListener{

    private VideoView videoView;
    private ProgressBar progressBar;
    private MediaController mediaController;
    private int intPositionWhenPause;
    private String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rtsp_video_play);
        Intent intent = getIntent();
        videoUrl = intent.getStringExtra("videoUrl");
        Log.v("URL:::::::::after", videoUrl);
        videoView = (VideoView) findViewById(R.id.videoView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        initVideoView();
    }



    public void initVideoView(){
        mediaController = new MediaController(this);
        //设置videoview的控制条
        videoView.setMediaController(mediaController);
        //设置显示控制条
        mediaController.show(0);
        //设置播放完成以后监听

        //设置发生错误监听，如果不设置videoview会向用户提示发生错误
        videoView.setOnErrorListener(this);
        //设置在视频文件在加载完毕以后的回调函数
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
            }
        });
        //设置videoView的点击监听

        //设置网络视频路径
//        Uri uri=Uri.parse("http://123.150.52.227/0725695b00000000-1415769042-1960016430/data5/vkplx.video.qq.com/flv/74/164/a0015193bxf.p203.1.mp4");
//        videoView.setVideoURI(uri);


    }

    /**
     * 视频播放发生错误时调用的回调函数
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what){
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.e("textwyj","发生未知错误");

                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.e("textwyj","媒体服务器死机");
                break;
            default:
                Log.e("textwyj","onError+"+what);
                break;
        }
        switch (extra){
            case MediaPlayer.MEDIA_ERROR_IO:
                //io读写错误
                Log.e("textwyj","文件或网络相关的IO操作错误");
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                //文件格式不支持
                Log.e("textwyj","比特流编码标准或文件不符合相关规范");
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                //一些操作需要太长时间来完成,通常超过3 - 5秒。
                Log.e("text","操作超时");
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                //比特流编码标准或文件符合相关规范,但媒体框架不支持该功能
                Log.e("textwyj","比特流编码标准或文件符合相关规范,但媒体框架不支持该功能");
                break;
            default:
                Log.e("textwyj","onError+"+extra);
                break;
        }
        //如果未指定回调函数， 或回调函数返回假，VideoView 会通知用户发生了错误。
        return false;
    }

    public void setVideoViewLayoutParams(int paramsType){
        //全屏模式
        if(1==paramsType) {
            //设置充满整个父布局
            RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            //设置相对于父布局四边对齐
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            //为VideoView添加属性
            videoView.setLayoutParams(LayoutParams);
        }else{
            //窗口模式
            //获取整个屏幕的宽高
            DisplayMetrics DisplayMetrics=new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(DisplayMetrics);
            //设置窗口模式距离边框50
            int videoHeight=DisplayMetrics.heightPixels;
            int videoWidth=DisplayMetrics.widthPixels;
            RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(videoWidth,videoHeight);
            //设置居中
            LayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            //为VideoView添加属性
            videoView.setLayoutParams(LayoutParams);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //设置为窗口模式播放
        setVideoViewLayoutParams(0);
        /*File videoFile = new File("/mnt/sdcard/play.mp4");
        if (videoFile.exists()) {
            videoView.setVideoPath(videoFile.getAbsolutePath());
            //启动视频播放
            videoView.start();
            //设置获取焦点
            videoView.setFocusable(true);
        }*/
        Uri uri = Uri.parse(videoUrl);
        videoView.setVideoURI(uri);
        //启动视频播放
        videoView.start();
        //设置获取焦点
        videoView.setFocusable(true);
    }

    /**
     * 页面暂停效果处理
     */
    @Override
    protected  void onPause() {
        super.onPause();
        //如果当前页面暂定则保存当前播放位置，并暂停
        intPositionWhenPause=videoView.getCurrentPosition();
        //停止回放视频文件
        videoView.stopPlayback();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("1111111111111", "orientation changed");/*
        if(newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_PORTRAIT){
            setVideoViewLayoutParams(0);
        }else{
            setVideoViewLayoutParams(1);
        }*/
        setVideoViewLayoutParams(0);
    }

    /**
     * 页面从暂停中恢复
     */
    @Override
    protected void onResume() {
        super.onResume();
        //跳转到暂停时保存的位置
        if(intPositionWhenPause>=0){
            videoView.seekTo(intPositionWhenPause);
            //初始播放位置
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=videoView){
            videoView=null;
        }
    }
}
