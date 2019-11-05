package com.chinatelecom.xjdh.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppContext;
import com.chinatelecom.xjdh.linphone.CallManager;
import com.chinatelecom.xjdh.linphone.Session;
import com.chinatelecom.xjdh.view.RingsView;


public class BroadcastVoiceActivity extends BaseActivity {

    private RingsView rv_rings_circle;
    private TextView tv_title1;
    private String titleShow;
    private long mSessionid;
    AppContext application;
    private Session currentLine;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
        setTitle("语音广播");
        titleShow = getIntent().getExtras().getString("titleShow");
        mSessionid = getIntent().getExtras().getLong("mSessionid");
        rv_rings_circle = (RingsView)findViewById(R.id.rv_rings_circle);
        tv_title1 = (TextView) findViewById(R.id.tv_title1);
        tv_title1.setText(titleShow);
        application = (AppContext) getApplication();
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setSpeakerphoneOn(true);

        currentLine = CallManager.Instance().findSessionBySessionID(mSessionid);
        currentLine.state = Session.CALL_STATE_FLAG.CONNECTED;
        mEngine.answerCall(mSessionid,false);
        mEngine.muteSession(currentLine.SessionID, true,
                true, true, true);
        currentLine.Mute = true;
        if(application.mConference){
            application.mEngine.joinToConference(currentLine.SessionID);
        }
        CallManager.Instance().setSpeakerOn(mEngine,true);
        rv_rings_circle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        rv_rings_circle.stop();
                        mEngine.muteSession(currentLine.SessionID, false,
                                true, false, true);
                        currentLine.Mute = true;
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        mEngine.muteSession(currentLine.SessionID, false,
                                false, false, false);
                        currentLine.Mute = false;
                        rv_rings_circle.start();

                        break;
                    default:
                        break;
                }
                return true;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEngine.hangUp(currentLine.SessionID);
        currentLine.Reset();
        CallManager.Instance().setSpeakerOn(mEngine,false);
        Toast.makeText(this,"已退出广播",Toast.LENGTH_SHORT).show();
    }
}
