package com.chinatelecom.xjdh.linphone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.chinatelecom.xjdh.service.PortSipService;
import com.chinatelecom.xjdh.ui.CallActivity;
import com.portsip.PortSipSdk;

public class LinphoneUtils {


    /**
     * 登录sip通话
     * @param context
     */
    public static void loginSip(Context context){
        Intent onLineIntent = new Intent(context, PortSipService.class);
        onLineIntent.setAction(PortSipService.ACTION_SIP_REGIEST);
        context.startService(onLineIntent);
    }

    /**
     * 保存相关登录信息
     * @param context
     * @param etUsername
     * @param etPassword
     * @param etSipServer
     * @param etSipServerPort
     * @param etDisplayname
     * @param etUserdomain
     * @param etAuthName
     * @param etStunServer
     * @param etStunPort
     */
    public static void SaveUserInfo(Context context,String etUsername,String etPassword,
                              String etSipServer,String etSipServerPort,String etDisplayname,
                              String etUserdomain,String etAuthName,String etStunServer,
                              String etStunPort) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(PortSipService.USER_NAME, etUsername);
        editor.putString(PortSipService.USER_PWD, etPassword);
        editor.putString(PortSipService.SVR_HOST, etSipServer);
        editor.putString(PortSipService.SVR_PORT, etSipServerPort);

        editor.putString(PortSipService.USER_DISPALYNAME, etDisplayname);
        editor.putString(PortSipService.USER_DOMAIN, etUserdomain);
        editor.putString(PortSipService.USER_AUTHNAME, etAuthName);
        editor.putString(PortSipService.STUN_HOST, etStunServer);
        editor.putString(PortSipService.STUN_PORT, etStunPort);
        editor.commit();
    }

    /**
     * 呼叫电话
     * @param mEngine
     * @param callTo
     * @param isVideo
     */
    public static void call(Context context,PortSipSdk mEngine,String callTo,boolean isVideo,String callName,boolean isJian){
        //default send video
        long sessionId = mEngine.call(callTo, true, isVideo);
        if (sessionId <= 0) {
            return;
        }

        if(isVideo){
            mEngine.sendVideo(sessionId, isVideo);
            mEngine.setVideoDeviceId(1);
        }
        Session currentLine = CallManager.Instance().getCurrentSession();
        currentLine.Remote = callTo;
        currentLine.SessionID = sessionId;
        currentLine.state = Session.CALL_STATE_FLAG.TRYING;
        if(isVideo){
            currentLine.HasVideo = true;
        }else{
            currentLine.HasVideo = false;
        }
        Intent intent = new Intent(context,CallActivity.class);
        intent.putExtra("callName",callName);
        if(isVideo){
            intent.putExtra("isVedio","isVedio");
        }
        if(isJian){
            intent.putExtra("isJian","isJian");
        }else{
            intent.putExtra("isJian","");
        }

        context.startActivity(intent);
    }



    /**
     * 呼叫电话
     * @param mEngine
     * @param callTo
     * @param isVideo
     */
    public static void callChat(PortSipSdk mEngine,String callTo,boolean isVideo){
        //default send video
        long sessionId = mEngine.call(callTo, true, isVideo);
        if (sessionId <= 0) {
            return;
        }

        if(isVideo){
            mEngine.sendVideo(sessionId, isVideo);
            mEngine.setVideoDeviceId(1);
        }
        Session currentLine = CallManager.Instance().getCurrentSession();
        currentLine.Remote = callTo;
        currentLine.SessionID = sessionId;
        currentLine.state = Session.CALL_STATE_FLAG.TRYING;
        if(isVideo){
            currentLine.HasVideo = true;
        }else{
            currentLine.HasVideo = false;
        }
    }
}
