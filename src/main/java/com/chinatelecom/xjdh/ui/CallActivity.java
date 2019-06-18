package com.chinatelecom.xjdh.ui;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppContext;
import com.chinatelecom.xjdh.app.AppManager;
import com.chinatelecom.xjdh.app.AppStart_;
import com.chinatelecom.xjdh.linphone.CallManager;
import com.chinatelecom.xjdh.linphone.Session;
import com.chinatelecom.xjdh.receiver.PortMessageReceiver;
import com.chinatelecom.xjdh.service.PortSipService;
import com.portsip.PortSIPVideoRenderer;
import com.portsip.PortSipErrorcode;
import com.portsip.PortSipSdk;


import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class CallActivity extends Activity implements PortMessageReceiver.BroadcastListener, View.OnClickListener , SensorEventListener {

    public PortMessageReceiver receiver = null;
    private String callName;
    private long baseTimer;
    private RelativeLayout rl_jingying;
    private RelativeLayout rl_guaduan;
    private RelativeLayout rl_mianti;
    private TextView tv_speak_steat;
    AppContext application;
    private TextView tv_state;
    private TextView tv_time;
    private boolean isUnregisterRecever = false;
    private TextView tv_name;
    private ImageView speaker;
    private ImageView micro;
    private PortSipSdk mEngine;
    private String isIncome;
    private String isVedio;
    private Sensor mSensor;
    private PowerManager.WakeLock mWakeLock;
    private SensorManager sensorManager;
    private PowerManager mPowerManager;
    private boolean creameType;
    private RelativeLayout llLocalView;
    private PortSIPVideoRenderer localRenderScreen = null;
    private PortSIPVideoRenderer remoteRenderScreen = null;
    private PortSIPVideoRenderer localRenderScreen1 = null;
    private PortSIPVideoRenderer sipVideoRenderer =null;
    private PortSIPVideoRenderer.ScalingType scalingType = PortSIPVideoRenderer.ScalingType.SCALE_ASPECT_BALANCED;// SCALE_ASPECT_FIT or SCALE_ASPECT_FILL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_call);
        mEngine = ((AppContext)getApplication()).mEngine;

        initView();
        initData();
    }

    /**
     * 初始化界面
     */
    private void initView(){
        localRenderScreen = (PortSIPVideoRenderer)findViewById(R.id.local_video_view);
        remoteRenderScreen = (PortSIPVideoRenderer)findViewById(R.id.remote_video_view);
        localRenderScreen1 = (PortSIPVideoRenderer)findViewById(R.id.local_video_view1);

        rl_jingying = (RelativeLayout) findViewById(R.id.rl_jingying);
        rl_guaduan = (RelativeLayout) findViewById(R.id.rl_guaduan);
        rl_mianti = (RelativeLayout) findViewById(R.id.rl_mianti);
        llLocalView = (RelativeLayout) findViewById(R.id.llLocalView);
        tv_speak_steat = (TextView) findViewById(R.id.tv_speak_steat);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_time = (TextView) findViewById(R.id.tv_time);
        speaker = (ImageView) findViewById(R.id.speaker);
        micro = (ImageView) findViewById(R.id.micro);
        tv_name = (TextView) findViewById(R.id.tv_name);
        rl_jingying.setOnClickListener(this);
        rl_guaduan.setOnClickListener(this);
        rl_mianti.setOnClickListener(this);
        application = (AppContext) getApplication();
        scalingType = PortSIPVideoRenderer.ScalingType.SCALE_ASPECT_FIT;//
        remoteRenderScreen.setScalingType(scalingType);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        register();
        callName = getIntent().getStringExtra("callName");
        if(null != getIntent().getStringExtra("state")){
            isIncome = getIntent().getStringExtra("state");
        }
        if(null != getIntent().getStringExtra("isVedio")){
            isVedio = getIntent().getStringExtra("isVedio");
        }

        CallManager.Instance().setSpeakerOn(mEngine,false);
        receiver = new PortMessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PortSipService.REGISTER_CHANGE_ACTION);
        filter.addAction(PortSipService.CALL_CHANGE_ACTION);
        filter.addAction(PortSipService.PRESENCE_CHANGE_ACTION);
        registerReceiver(receiver, filter);
        receiver.broadcastReceiver =this;
        tv_name.setText(callName);

        if(null != isIncome && isIncome.length()>0){
            if(null != isVedio && isVedio.length()>0){
                speaker.setImageDrawable(getResources().getDrawable(R.mipmap.btn_camera));
                tv_speak_steat.setText("切换摄像头");
                updateVideo(mEngine);
            }else{
                localRenderScreen.setVisibility(View.GONE);
                remoteRenderScreen.setVisibility(View.GONE);
                localRenderScreen1.setVisibility(View.GONE);
            }
            tv_state.setVisibility(View.GONE);
            tv_time.setVisibility(View.VISIBLE);
            startTimeChronometer();
            if (mSensor != null)
                sensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        }else{
            tv_state.setText("正在呼叫，等待对方接听");
            tv_time.setVisibility(View.GONE);

            if( CallManager.Instance().getCurrentSession().HasVideo){
                localRenderScreen.setVisibility(View.GONE);
                remoteRenderScreen.setVisibility(View.GONE);
                localRenderScreen1.setVisibility(View.VISIBLE);
                mEngine.setLocalVideoWindow(localRenderScreen1);
                mEngine.displayLocalVideo(true); // display Local video
                speaker.setImageDrawable(getResources().getDrawable(R.mipmap.btn_camera));
                tv_speak_steat.setText("切换摄像头");
            }else{
                localRenderScreen.setVisibility(View.GONE);
                remoteRenderScreen.setVisibility(View.GONE);
                localRenderScreen1.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void OnBroadcastReceiver(Intent intent) {
        String action = intent == null ? "" : intent.getAction();
        if (PortSipService.CALL_CHANGE_ACTION.equals(action)) {
            long sessionId = intent.getLongExtra(PortSipService.EXTRA_CALL_SEESIONID, Session.INVALID_SESSION_ID);
            String status = intent.getStringExtra(PortSipService.EXTRA_CALL_DESCRIPTION);
            callStateChange(status);
        }
    }


    private void callStateChange(String status){
        switch (status){
            //对方点击了接通或者挂断按钮
            case "对方已应答":
                break;
            case "已接通":
                //唤醒设备
                if (mWakeLock.isHeld()){
                    mWakeLock.release();
                }
                tv_state.setVisibility(View.GONE);
                tv_time.setVisibility(View.VISIBLE);
                if(null == isIncome || isIncome.length()<=0){
                    if( CallManager.Instance().getCurrentSession().HasVideo){
                        updateVideo(mEngine);
                    }
                }
                startTimeChronometer();
                if (mSensor != null)
                    sensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

                break;
            //通话一段时间后挂断了电话
            case "通话结束":
                Toast.makeText(this,"对方已挂断",Toast.LENGTH_LONG).show();
                if(null == isIncome || isIncome.length()<=0){
                    if( CallManager.Instance().getCurrentSession().HasVideo){
                        updateVideo(mEngine);
                    }
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessageDelayed(1000,2000);
                    }
                }).start();
                break;
            case "呼叫错误":
                Toast.makeText(this,"呼叫发生错误",Toast.LENGTH_LONG).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessageDelayed(1000,2000);
                    }
                }).start();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Session currentLine = CallManager.Instance().getCurrentSession();
        switch (v.getId()){
            case R.id.rl_jingying:
                if (currentLine.Mute) {
                    mEngine.muteSession(currentLine.SessionID, false,
                            false, false, false);
                    currentLine.Mute = false;
                    micro.setImageDrawable(getResources().getDrawable(R.mipmap.jingyin_disabled));
                } else {
                    mEngine.muteSession(currentLine.SessionID, true,
                            true, true, true);
                    currentLine.Mute = true;
                    micro.setImageDrawable(getResources().getDrawable(R.mipmap.jingyin_nor));
                }
                break;
            case R.id.rl_guaduan:
                mEngine.hangUp(currentLine.SessionID);
                currentLine.Reset();
                CallManager.Instance().setSpeakerOn(mEngine,false);
                Toast.makeText(this,"通话结束",Toast.LENGTH_LONG).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessageAtTime(1000,1000);
                    }
                }).start();
                break;
            case R.id.rl_mianti:
                if(currentLine.HasVideo){
                    SetCamera(mEngine,creameType);
                    creameType = !creameType;
                }else{
                    if(CallManager.Instance().setSpeakerOn(mEngine,!CallManager.Instance().isSpeakerOn())){
                        speaker.setImageDrawable(getResources().getDrawable(R.mipmap.mianti_selected));
                    }else {
                        speaker.setImageDrawable(getResources().getDrawable(R.mipmap.mianti_nor));
                    }
                }

                break;

        }
    }

    /**
     * 通话时间监听器
     */
    private void startTimeChronometer(){
        @SuppressLint("HandlerLeak") final Handler startTimehandler = new Handler(){
            public void handleMessage(android.os.Message msg) {
                if (null != tv_time) {
                    CallActivity.this.baseTimer += 1000;
                    tv_time.setText((String) msg.obj);
                }
            }
        };
        new Timer("通话计时器").scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int time = (int)CallActivity.this.baseTimer/1000;
                String hh = new DecimalFormat("00").format(time / 3600);
                String mm = new DecimalFormat("00").format(time % 3600 / 60);
                String ss = new DecimalFormat("00").format(time % 60);
                String timeFormat = new String(hh + ":" + mm + ":" + ss);
                Message msg = new Message();
                msg.obj = timeFormat;
                startTimehandler.sendMessage(msg);
            }

        }, 0, 1000L);
    }

    /**
     * 返回误触
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return true;
        }
        return false;
    }

    /**
     * 销毁广播
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
        sensorManager.unregisterListener(this);
        //释放息屏
        if (mWakeLock.isHeld())
            mWakeLock.release();
        mWakeLock = null;
        mPowerManager = null;
        if(!AppManager.getAppManager().isHavaActivity(AppStart_.class)){
            startActivity(new Intent(this, AppStart_.class));
        }
        PortSipSdk portSipLib = application.mEngine;
        if(localRenderScreen!=null){
            if(portSipLib!=null) {
                portSipLib.displayLocalVideo(false);
            }
            localRenderScreen.release();
        }

        if(localRenderScreen1!=null){
            localRenderScreen1.release();
        }

        CallManager.Instance().setRemoteVideoWindow(application.mEngine,-1,null);//set
        if(remoteRenderScreen!=null){
            remoteRenderScreen.release();
        }

        stopVideo(mEngine);
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 5000:
                    updateVideo(mEngine);
                    break;
                case 1000:
                    finish();
                    break;
            }

        }
    };


    @SuppressLint("InvalidWakeLockTag")
    private void register(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        //息屏设置
        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "pengdi");
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] == 0.0) {
            //关闭屏幕
            if (!mWakeLock.isHeld())
                mWakeLock.acquire();

        } else {
            //唤醒设备
            if (mWakeLock.isHeld())
                mWakeLock.release();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void stopVideo(PortSipSdk portSipLib)
    {
        Session cur = CallManager.Instance().getCurrentSession();
        if(portSipLib!=null) {
            portSipLib.displayLocalVideo(false);
            portSipLib.setLocalVideoWindow(null);
            CallManager.Instance().setRemoteVideoWindow(portSipLib,cur.SessionID,null);
            CallManager.Instance().setConferenceVideoWindow(portSipLib,null);
        }
    }


    public void updateVideo(final PortSipSdk portSipLib)
    {
        isUnregisterRecever = true;
//        unregisterReceiver(receiver);
        localRenderScreen.setVisibility(View.VISIBLE);
        remoteRenderScreen.setVisibility(View.VISIBLE);
        localRenderScreen1.setVisibility(View.GONE);
        CallManager callManager = CallManager.Instance();
        callManager.setSpeakerOn(mEngine,true);
        if(null == isIncome){
            mEngine.displayLocalVideo(false);
            mEngine.setLocalVideoWindow(null);
            localRenderScreen1.release();
        }
        if (application.mConference)
        {
            callManager.setConferenceVideoWindow(portSipLib,remoteRenderScreen);
        }else {
            Session cur = CallManager.Instance().getCurrentSession();
            if (cur != null && !cur.IsIdle()
                    && cur.SessionID != PortSipErrorcode.INVALID_SESSION_ID
                    && cur.HasVideo) {
                portSipLib.setVideoNackStatus(true);
                portSipLib.setRtpCallback(true);
                callManager.setRemoteVideoWindow(portSipLib,cur.SessionID, remoteRenderScreen);
                if(null != isVedio && isVedio.length()>0){
                    portSipLib.setLocalVideoWindow(localRenderScreen);
                    portSipLib.displayLocalVideo(true); // display Local video
                    localRenderScreen.setZOrderOnTop(true);
                }else{
                    portSipLib.displayLocalVideo(false);
                    portSipLib.setLocalVideoWindow(null);
//                    portSipLib.setLocalVideoWindow(localRenderScreen);
//                    portSipLib.displayLocalVideo(true); // display Local video
                }

                portSipLib.sendVideo(cur.SessionID, true);
            } else {
                portSipLib.displayLocalVideo(false);
                callManager.setRemoteVideoWindow(portSipLib,cur.SessionID, null);
                portSipLib.setLocalVideoWindow(null);
            }
        }
    }



    private void SetCamera(PortSipSdk portSipLib,boolean userFront)
    {
        if (userFront)
        {
            portSipLib.setVideoDeviceId(1);
        }
        else
        {
            portSipLib.setVideoDeviceId(0);
        }
    }
}
