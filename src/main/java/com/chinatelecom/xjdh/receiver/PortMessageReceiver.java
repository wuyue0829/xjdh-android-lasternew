package com.chinatelecom.xjdh.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PortMessageReceiver extends BroadcastReceiver
{
    public interface BroadcastListener {void OnBroadcastReceiver(Intent intent);}
    @Override
    public void onReceive(Context context, Intent intent) {
        if(broadcastReceiver!=null) {
            broadcastReceiver.OnBroadcastReceiver(intent);
        }
    }
    public BroadcastListener broadcastReceiver;
}