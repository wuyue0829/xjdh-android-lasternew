package com.chinatelecom.xjdh.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppContext;
import com.chinatelecom.xjdh.app.AppManager;
import com.chinatelecom.xjdh.app.AppStart_;
import com.chinatelecom.xjdh.linphone.CallManager;
import com.chinatelecom.xjdh.linphone.Ring;
import com.chinatelecom.xjdh.linphone.Session;
import com.chinatelecom.xjdh.receiver.PortMessageReceiver;
import com.chinatelecom.xjdh.service.PortSipService;
import com.portsip.PortSIPVideoRenderer;
import com.portsip.PortSipErrorcode;
import com.portsip.PortSipSdk;


public class CallInComeActivity extends Activity implements PortMessageReceiver.BroadcastListener, View.OnClickListener {

    private TextView tv_name;
    public PortMessageReceiver receiver = null;
    AppContext application;
    long mSessionid;
    private ImageView decline;
    private ImageView accept;
    private TextView tv_time;
    private TextView tv_state;
    private Session session;
    private Context mContent;
    private PortSIPVideoRenderer localRenderScreen1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_income);
        receiver = new PortMessageReceiver();
        mContent = this;
        IntentFilter filter = new IntentFilter();
        filter.addAction(PortSipService.REGISTER_CHANGE_ACTION);
        filter.addAction(PortSipService.CALL_CHANGE_ACTION);
        filter.addAction(PortSipService.PRESENCE_CHANGE_ACTION);
        registerReceiver(receiver, filter);
        receiver.broadcastReceiver =this;
        Intent intent = getIntent();
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_state = (TextView) findViewById(R.id.tv_state);
        mSessionid = intent.getLongExtra("incomingSession", PortSipErrorcode.INVALID_SESSION_ID);
        session = CallManager.Instance().findSessionBySessionID(mSessionid);
        if(mSessionid==PortSipErrorcode.INVALID_SESSION_ID||session ==null||session.state!= Session.CALL_STATE_FLAG.INCOMING){
            this.finish();
        }
        tv_name.setText(session.DisplayName);
        application = (AppContext) getApplication();
        application.mIsVideo = session.HasVideo;
        initView();
    }


    private void initView(){
        decline = (ImageView) findViewById(R.id.decline);
        accept = (ImageView) findViewById(R.id.accept);
        localRenderScreen1 = (PortSIPVideoRenderer)findViewById(R.id.local_video_view1);
        tv_time.setVisibility(View.GONE);
        tv_state.setVisibility(View.VISIBLE);
        if(application.mIsVideo){
            tv_state.setText("对方发来视频邀请");
            localRenderScreen1.setVisibility(View.VISIBLE);
            application.mEngine.setLocalVideoWindow(localRenderScreen1);
            application.mEngine.displayLocalVideo(true);
        }else{
            tv_state.setText("对方发来语音邀请");
            localRenderScreen1.setVisibility(View.GONE);
        }
        decline.setOnClickListener(this);
        accept.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(application.mEngine!=null){
            Session currentLine = CallManager.Instance().findSessionBySessionID(mSessionid);
            switch (v.getId()){
                case R.id.decline:
                    Ring.getInstance(this).stop();
                    if (currentLine.state == Session.CALL_STATE_FLAG.INCOMING) {
                        application.mEngine.rejectCall(currentLine.SessionID, 486);
                        currentLine.Reset();
                        Toast.makeText(this,"已挂断",Toast.LENGTH_SHORT).show();
                        if(AppManager.getAppManager().isHavaActivity(AppStart_.class)){
                            finish();
                        }else{
                            startActivity(new Intent(this, AppStart_.class));
                            finish();
                        }
                    }
                    break;
                case R.id.accept:
                    if (currentLine.state != Session.CALL_STATE_FLAG.INCOMING) {
                        return;
                    }
                    Ring.getInstance(this).stopRingTone();
                    currentLine.state = Session.CALL_STATE_FLAG.CONNECTED;
                    if(session.HasVideo){
                        application.mEngine.answerCall(mSessionid,true);
                    }else{
                        application.mEngine.answerCall(mSessionid,false);
                    }

                    if(application.mConference){
                        application.mEngine.joinToConference(currentLine.SessionID);
                    }
                    break;
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
            //通话一段时间后挂断了电话
            case "已接通":
                application.mEngine.displayLocalVideo(false);
                application.mEngine.setLocalVideoWindow(null);
                Intent intent = new Intent(this,CallActivity.class);
                intent.putExtra("callName",session.DisplayName);
                intent.putExtra("state","income");
                if(application.mIsVideo){
                    intent.putExtra("isVedio","isVedio");
                }
                startActivity(intent);
                finish();
                break;
            case "通话结束":
                Toast.makeText(this,"对方已挂断",Toast.LENGTH_LONG).show();
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

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
           /* if(AppManager.getAppManager().isHavaActivity(MainActivity_.class)){
                finish();
            }else{
                startActivity(new Intent(mContent, AppStart_.class));*/
            finish();
//            }
        }
    };


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        long sessionid = intent.getLongExtra("incomingSession", PortSipErrorcode.INVALID_SESSION_ID);
        Session session = CallManager.Instance().findSessionBySessionID(sessionid);
        if(mSessionid!=PortSipErrorcode.INVALID_SESSION_ID&&session !=null){
            mSessionid = sessionid;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        PortSipSdk portSipLib = application.mEngine;
        if(localRenderScreen1!=null){
            if(portSipLib!=null) {
//                portSipLib.displayLocalVideo(false);
                portSipLib.setLocalVideoWindow(null);
            }
            localRenderScreen1.release();
        }
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
}
