package com.chinatelecom.xjdh.ui;

import android.hardware.Camera;
import android.os.Bundle;

import com.chinatelecom.xjdh.R;
import com.portsip.PortSIPVideoRenderer;

public class CallVedioActivity extends BaseActivity{

    private Camera camera;
    private PortSIPVideoRenderer remote_video_view;

    private static final int FRONT = 1;//前置摄像头标记
    private static final int BACK = 2;//后置摄像头标记
    private int currentCameraType = -1;//当前打开的摄像头标记

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_vedio);

        remote_video_view = (PortSIPVideoRenderer) findViewById(R.id.remote_video_view);

    }
}
