package com.chinatelecom.xjdh.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppContext;
import com.chinatelecom.xjdh.app.AppStart_;
import com.chinatelecom.xjdh.linphone.CallManager;
import com.chinatelecom.xjdh.linphone.Contact;
import com.chinatelecom.xjdh.linphone.ContactManager;
import com.chinatelecom.xjdh.linphone.Ring;
import com.chinatelecom.xjdh.linphone.Session;
import com.chinatelecom.xjdh.receiver.MessageWrap;
import com.chinatelecom.xjdh.ui.CallInComeActivity;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.portsip.OnPortSIPEvent;
import com.portsip.PortSipEnumDefine;
import com.portsip.PortSipErrorcode;
import com.portsip.PortSipSdk;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;

import java.util.Random;
import java.util.UUID;

public class PortSipService extends Service implements OnPortSIPEvent {
    public static final String ACTION_SIP_REGIEST = "PortSip.AndroidSample.Test.REGIEST";
    public static final String ACTION_SIP_UNREGIEST = "PortSip.AndroidSample.Test.UNREGIEST";
    public static final String ACTION_PUSH_MESSAGE = "PortSip.AndroidSample.Test.PushMessageIncoming";

    public static final String INSTANCE_ID = "instanceid";

    public static final String USER_NAME = "user name";
    public static final String USER_PWD = "user pwd";
    public static final String SVR_HOST = "svr host";
    public static final String SVR_PORT = "svr port";

    public static final String USER_DOMAIN = "user domain";
    public static final String USER_DISPALYNAME = "user dispalay";
    public static final String USER_AUTHNAME = "user authname";
    public static final String STUN_HOST = "stun host";
    public static final String STUN_PORT = "stun port";

    public static final String TRANS = "trans type";
    public static final String SRTP = "srtp type";

    protected PowerManager.WakeLock mCpuLock;
    public static final String REGISTER_CHANGE_ACTION = "PortSip.AndroidSample.Test.RegisterStatusChagnge";
    public static final String CALL_CHANGE_ACTION = "PortSip.AndroidSample.Test.CallStatusChagnge";
    public static final String PRESENCE_CHANGE_ACTION = "PortSip.AndroidSample.Test.PRESENCEStatusChagnge";

    public static String EXTRA_REGISTER_STATE = "RegisterStatus";
    public static String EXTRA_CALL_SEESIONID = "SessionID";
    public static String EXTRA_CALL_DESCRIPTION = "Description";

    private final String APPID = "PortSip.Android.Test";
    private PortSipSdk mEngine;
    private AppContext applicaton;
    private final int SERVICE_NOTIFICATION  = 31414;
    private String ChannelID = "PortSipService";
    @Override
    public void onCreate() {
        super.onCreate();
        applicaton = (AppContext) getApplicationContext();
        mEngine = applicaton.mEngine;
        showServiceNotifiCation();
    }

    private void showServiceNotifiCation(){
        Intent intent = new Intent(this, AppStart_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0/*requestCode*/, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder;
        builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.logo)
                .setContentTitle("新疆动环监控")
                .setContentText("音视频服务正在运行")
                .setContentIntent(contentIntent)
                .build();// getNotification()
        startForeground(SERVICE_NOTIFICATION,builder.build());
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            if (ACTION_PUSH_MESSAGE.equals(intent.getAction()) && !CallManager.Instance().regist) {
                registerToServer();
            } else if (ACTION_SIP_REGIEST.equals(intent.getAction()) && !CallManager.Instance().regist) {
                registerToServer();
            } else if (ACTION_SIP_UNREGIEST.equals(intent.getAction()) && CallManager.Instance().regist) {
                unregisterToServer();
            }
        }
        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mEngine.destroyConference();

        if (mCpuLock != null) {
            mCpuLock.release();
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void registerToServer() {
        SharedPreferences prefences = PreferenceManager.getDefaultSharedPreferences(this);
        Random rm = new Random();
        int localport = 5060 + rm.nextInt(60000);

        int transtype = prefences.getInt(TRANS, 2);
        int srtptype = prefences.getInt(SRTP, 0);

        String registerTips = "音视频初始化失败";
        int result = 0;
        mEngine.DeleteCallManager();
        mEngine.CreateCallManager(applicaton);
        mEngine.setOnPortSIPEvent(this);
        String dataPath = getExternalFilesDir(null).getAbsolutePath();

        result = mEngine.initialize(PortSipEnumDefine.ENUM_TRANSPORT_TCP, "0.0.0.0", localport,
                PortSipEnumDefine.ENUM_LOG_LEVEL_DEBUG, dataPath,
                8, "PortSIP SDK for Android", 0, 0, dataPath, "", false, null);
        if(result == PortSipErrorcode.ECoreErrorNone) {
            //init failed
            registerTips = "ECoreWrongLicenseKey";
            result = mEngine.setLicenseKey("LicenseKey");
            if (result != PortSipErrorcode.ECoreWrongLicenseKey) {

                mEngine.setAudioDevice(PortSipEnumDefine.AudioDevice.SPEAKER_PHONE);
                mEngine.setVideoDeviceId(1);
                mEngine.setSrtpPolicy(srtptype);
                ConfigPresence(this, prefences, mEngine);

                mEngine.enable3GppTags(true);

                String name = prefences.getString(USER_NAME, "");
                String displayname = prefences.getString(USER_DISPALYNAME, "");
                String authname = prefences.getString(USER_AUTHNAME, "");
                String domain = prefences.getString(USER_DOMAIN, "");

                String pwd = prefences.getString(USER_PWD, "");
                String host = prefences.getString(SVR_HOST, "");
                String port = prefences.getString(SVR_PORT, "5060");
                String stunhost = prefences.getString(STUN_HOST, "");
                String stunport = prefences.getString(STUN_PORT, "3478");
                registerTips = "服务器未查询到登录用户信息";
                result = -1;
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd)
                        && !TextUtils.isEmpty(host) && !TextUtils.isEmpty(port)) {

                    int sipServerPort;
                    int stunServerPort;
                    sipServerPort = Integer.parseInt(port);
                    stunServerPort = Integer.parseInt(stunport);
                    result = mEngine.setUser(name, displayname, authname, pwd,
                            domain, host, sipServerPort, stunhost, stunServerPort, null, 5060);
                    registerTips = "音视频登录失败";
                    if (result == PortSipErrorcode.ECoreErrorNone) {
                        mEngine.setInstanceId(getInstanceID());
                        registerTips = "registerServer failed";
                        result = mEngine.registerServer(90, 0);
                    }
                }
            }
        }

        if(result!=PortSipErrorcode.ECoreErrorNone){
            Intent broadIntent = new Intent(REGISTER_CHANGE_ACTION);
            broadIntent.putExtra(EXTRA_REGISTER_STATE,registerTips);
            sendPortSipMessage(registerTips + result, broadIntent);
            CallManager.Instance().regist = false;
            CallManager.Instance().resetAll();
            keepCpuRun(false);
        }
    }

    public static void ConfigPresence(Context context, SharedPreferences prefences, PortSipSdk sdk) {
        sdk.clearAudioCodec();
        if (prefences.getBoolean(context.getString(R.string.MEDIA_G722), false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_G722);
        }
        if (prefences.getBoolean(context.getString(R.string.MEDIA_PCMA), true)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_PCMA);
        }
        if (prefences.getBoolean(context.getString(R.string.MEDIA_PCMU), true)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_PCMU);
        }
        if (prefences.getBoolean(context.getString(R.string.MEDIA_G729), true)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_G729);
        }

        if (prefences.getBoolean(context.getString(R.string.MEDIA_GSM), false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_GSM);
        }
        if (prefences.getBoolean(context.getString(R.string.MEDIA_ILBC), false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_ILBC);
        }
        if (prefences.getBoolean(context.getString(R.string.MEDIA_AMR), false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_AMR);
        }
        if (prefences.getBoolean(context.getString(R.string.MEDIA_AMRWB), false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_AMRWB);
        }
        if (prefences.getBoolean(context.getString(R.string.MEDIA_SPEEX), false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_SPEEX);
        }
        if (prefences.getBoolean(context.getString(R.string.MEDIA_SPEEXWB), false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_SPEEXWB);
        }
        if (prefences.getBoolean(context.getString(R.string.MEDIA_ISACWB), false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_ISACWB);
        }
        if (prefences.getBoolean(context.getString(R.string.MEDIA_ISACSWB), false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_ISACSWB);
        }
//        if (prefences.getBoolean(context.getString(R.string.MEDIA_G7221), false)) {
//            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_G7221);
//        }
        if (prefences.getBoolean(context.getString(R.string.MEDIA_OPUS), false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_OPUS);
        }

        sdk.clearVideoCodec();
        if (prefences.getBoolean(context.getString(R.string.MEDIA_H264), true)) {
            sdk.addVideoCodec(PortSipEnumDefine.ENUM_VIDEOCODEC_H264);
        }
        if (prefences.getBoolean(context.getString(R.string.MEDIA_VP8), true)) {
            sdk.addVideoCodec(PortSipEnumDefine.ENUM_VIDEOCODEC_VP8);
        }
        if (prefences.getBoolean(context.getString(R.string.MEDIA_VP9), true)) {
            sdk.addVideoCodec(PortSipEnumDefine.ENUM_VIDEOCODEC_VP9);
        }

        sdk.enableAEC(prefences.getBoolean(context.getString(R.string.MEDIA_AEC), true));
        sdk.enableAGC(prefences.getBoolean(context.getString(R.string.MEDIA_AGC), true));
        sdk.enableCNG(prefences.getBoolean(context.getString(R.string.MEDIA_CNG), true));
        sdk.enableVAD(prefences.getBoolean(context.getString(R.string.MEDIA_VAD), true));
        sdk.enableANS(prefences.getBoolean(context.getString(R.string.MEDIA_ANS), true));

        boolean foward = prefences.getBoolean(context.getString(R.string.str_fwopenkey), false);
        boolean fowardBusy = prefences.getBoolean(context.getString(R.string.str_fwbusykey), false);
        String fowardto = prefences.getString(context.getString(R.string.str_fwtokey), null);
        if (foward && !TextUtils.isEmpty(fowardto)) {
            sdk.enableCallForward(fowardBusy, fowardto);
        }

        sdk.enableReliableProvisional(prefences.getBoolean(context.getString(R.string.str_pracktitle), false));

        String resolution = prefences.getString((context.getString(R.string.str_resolution)), "720P");
        int width = 352;
        int height = 288;
        if (resolution.equals("QCIF")) {
            width = 176;
            height = 144;
        } else if (resolution.equals("CIF")) {
            width = 352;
            height = 288;
        } else if (resolution.equals("VGA")) {
            width = 640;
            height = 480;
        } else if (resolution.equals("720P")) {
            width = 1280;
            height = 720;
        } else if (resolution.equals("1080P")) {
            width = 1920;
            height = 1080;
        }

        sdk.setVideoResolution(width, height);
    }

    private int getTransType(int select) {
        switch (select) {
            case 0:
                return PortSipEnumDefine.ENUM_TRANSPORT_UDP;
            case 1:
                return PortSipEnumDefine.ENUM_TRANSPORT_TLS;
            case 2:
                return PortSipEnumDefine.ENUM_TRANSPORT_TCP;
            case 3:
                return PortSipEnumDefine.ENUM_TRANSPORT_PERS_UDP;
            case 4:
                return PortSipEnumDefine.ENUM_TRANSPORT_PERS_TCP;
        }
        return PortSipEnumDefine.ENUM_TRANSPORT_UDP;
    }

    String getInstanceID() {
        SharedPreferences prefences = PreferenceManager.getDefaultSharedPreferences(this);

        String insanceid = prefences.getString(INSTANCE_ID, "");
        if (TextUtils.isEmpty(insanceid)) {
            insanceid = UUID.randomUUID().toString();
            prefences.edit().putString(INSTANCE_ID, insanceid).commit();
        }
        return insanceid;
    }


    public void unregisterToServer() {

        mEngine.unRegisterServer();
        mEngine.DeleteCallManager();
        CallManager.Instance().regist = false;
    }



    //--------------------
    @Override
    public void onRegisterSuccess(String statusText, int statusCode, String sipMessage) {
        CallManager.Instance().regist = true;
        Intent broadIntent = new Intent(REGISTER_CHANGE_ACTION);
        broadIntent.putExtra(EXTRA_REGISTER_STATE,statusText);
        sendPortSipMessage(PreferenceUtils.getPrefString(this, PreferenceConstants.USERNAME, "")+"音视频在线", broadIntent);
        keepCpuRun(true);
        LogUtil.e("服务注册成功");
    }

    @Override
    public void onRegisterFailure(String statusText, int statusCode, String sipMessage) {
        Intent broadIntent = new Intent(REGISTER_CHANGE_ACTION);
        broadIntent.putExtra(EXTRA_REGISTER_STATE, statusText);
        sendPortSipMessage("音视频离线" + statusCode, broadIntent);
        CallManager.Instance().regist=false;
        CallManager.Instance().resetAll();
        LogUtil.e("服务注册失败");
        keepCpuRun(false);
    }

    @Override
    public void onInviteIncoming(final long sessionId,
                                 String callerDisplayName,
                                 String caller,
                                 String calleeDisplayName,
                                 String callee,
                                 String audioCodecNames,
                                 String videoCodecNames,
                                 boolean existsAudio,
                                 boolean existsVideo,
                                 String sipMessage) {

        LogUtil.e("有来电");
        if(CallManager.Instance().findIncomingCall()!=null){
            applicaton.mEngine.rejectCall(sessionId,486);//busy
            return;
        }
        Session session = CallManager.Instance().findIdleSession();
        session.state = Session.CALL_STATE_FLAG.INCOMING;
        session.HasVideo = existsVideo;
        session.SessionID = sessionId;
        session.Remote = caller;
        session.DisplayName = callerDisplayName;
        if(caller.contains("sip:3")){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        EventBus.getDefault().post(MessageWrap.getInstance((sessionId)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();

        }else{
            Intent activityIntent = new Intent(this, CallInComeActivity.class);
            activityIntent.putExtra("incomingSession",sessionId);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(activityIntent);
            Intent broadIntent = new Intent(CALL_CHANGE_ACTION);
            broadIntent.putExtra(EXTRA_CALL_SEESIONID, sessionId);

            String description = "来电";
            broadIntent.putExtra(EXTRA_CALL_DESCRIPTION, description);
            sendPortSipMessage(description, broadIntent);
            Ring.getInstance(this).startRingTone();
        }

    }

    @Override
    public void onInviteTrying(long sessionId) {
        LogUtil.e("正在响铃");
    }

    @Override
    public void onInviteSessionProgress(
            long sessionId,
            String audioCodecNames,
            String videoCodecNames,
            boolean existsEarlyMedia,
            boolean existsAudio,
            boolean existsVideo,
            String sipMessage) {
    }

    @Override
    public void onInviteRinging(long sessionId, String statusText, int statusCode, String sipMessage) {
        LogUtil.e("正在正在响铃");
    }

    @Override
    public void onInviteAnswered(long sessionId,
                                 String callerDisplayName,
                                 String caller,
                                 String calleeDisplayName,
                                 String callee,
                                 String audioCodecNames,
                                 String videoCodecNames,
                                 boolean existsAudio,
                                 boolean existsVideo,
                                 String sipMessage) {
        Session session = CallManager.Instance().findSessionBySessionID(sessionId);


        if (session != null) {
            session.state = Session.CALL_STATE_FLAG.CONNECTED;
            session.HasVideo = existsVideo;

            Intent broadIntent = new Intent(CALL_CHANGE_ACTION);
            broadIntent.putExtra(EXTRA_CALL_SEESIONID, sessionId);

            String description ="对方已应答";
            broadIntent.putExtra(EXTRA_CALL_DESCRIPTION, description);

            sendPortSipMessage(description, broadIntent);
        }
        LogUtil.e("对方已应答");
        Ring.getInstance(this).stopRingBackTone();
    }

    @Override
    public void onInviteFailure(long sessionId, String reason, int code, String sipMessage) {
        Session session = CallManager.Instance().findSessionBySessionID(sessionId);
        if (session != null) {
            session.state = Session.CALL_STATE_FLAG.FAILED;
            session.SessionID = sessionId;

            Intent broadIntent = new Intent(CALL_CHANGE_ACTION);
            broadIntent.putExtra(EXTRA_CALL_SEESIONID, sessionId);
            String description ="呼叫错误";
            broadIntent.putExtra(EXTRA_CALL_DESCRIPTION, description);

            sendPortSipMessage(description, broadIntent);
        }
        LogUtil.e("呼叫错误");
        Ring.getInstance(this).stopRingBackTone();
    }

    @Override
    public void onInviteUpdated(
            long sessionId,
            String audioCodecNames,
            String videoCodecNames,
            boolean existsAudio,
            boolean existsVideo,
            String sipMessage) {
        Session session = CallManager.Instance().findSessionBySessionID(sessionId);

        if (session != null) {
            session.state = Session.CALL_STATE_FLAG.CONNECTED;
            session.HasVideo = existsVideo;

            Intent broadIntent = new Intent(CALL_CHANGE_ACTION);
            broadIntent.putExtra(EXTRA_CALL_SEESIONID, sessionId);
            String description = "OnInviteUpdated";
            broadIntent.putExtra(EXTRA_CALL_DESCRIPTION, description);

            sendPortSipMessage(description, broadIntent);
        }
    }

    @Override
    public void onInviteConnected(long sessionId) {
        Session session = CallManager.Instance().findSessionBySessionID(sessionId);
        if (session != null) {
            session.state = Session.CALL_STATE_FLAG.CONNECTED;
            session.SessionID = sessionId;

            if (applicaton.mConference)
            {
                applicaton.mEngine.joinToConference(session.SessionID);
                applicaton.mEngine.sendVideo(session.SessionID, true);
            }

            Intent broadIntent = new Intent(CALL_CHANGE_ACTION);
            broadIntent.putExtra(EXTRA_CALL_SEESIONID, sessionId);
            String description = "已接通";
            broadIntent.putExtra(EXTRA_CALL_DESCRIPTION, description);

            sendPortSipMessage(description, broadIntent);
        }

        CallManager.Instance().setSpeakerOn(applicaton.mEngine,CallManager.Instance().isSpeakerOn());
    }

    @Override
    public void onInviteBeginingForward(String forwardTo) {
    }

    @Override
    public void onInviteClosed(long sessionId) {
        Session session = CallManager.Instance().findSessionBySessionID(sessionId);
        if (session != null) {
            session.state = Session.CALL_STATE_FLAG.CLOSED;
            session.SessionID = sessionId;

            Intent broadIntent = new Intent(CALL_CHANGE_ACTION);
            broadIntent.putExtra(EXTRA_CALL_SEESIONID, sessionId);
            String description = "通话结束";
            broadIntent.putExtra(EXTRA_CALL_DESCRIPTION, description);

            sendPortSipMessage(description, broadIntent);
        }
        Ring.getInstance(this).stopRingTone();
    }

    @Override
    public void onDialogStateUpdated(String BLFMonitoredUri,
                                     String BLFDialogState,
                                     String BLFDialogId,
                                     String BLFDialogDirection) {
        String text = "The user ";
        text += BLFMonitoredUri;
        text += " dialog state is updated: ";
        text += BLFDialogState;
        text += ", dialog id: ";
        text += BLFDialogId;
        text += ", direction: ";
        text += BLFDialogDirection;
    }

    @Override
    public void onRemoteUnHold(
            long sessionId,
            String audioCodecNames,
            String videoCodecNames,
            boolean existsAudio,
            boolean existsVideo) {
    }

    @Override
    public void onRemoteHold(long sessionId) {
    }

    @Override
    public void onReceivedRefer(
            long sessionId,
            long referId,
            String to,
            String referFrom,
            String referSipMessage) {
    }

    @Override
    public void onReferAccepted(long sessionId) {
        Session session = CallManager.Instance().findSessionBySessionID(sessionId);
        if (session != null) {
            session.state = Session.CALL_STATE_FLAG.CLOSED;
            session.SessionID = sessionId;

            Intent broadIntent = new Intent(CALL_CHANGE_ACTION);
            broadIntent.putExtra(EXTRA_CALL_SEESIONID, sessionId);
            String description = "onReferAccepted";
            broadIntent.putExtra(EXTRA_CALL_DESCRIPTION, description);

            sendPortSipMessage(description, broadIntent);
        }
        Ring.getInstance(this).stopRingTone();
    }

    @Override
    public void onReferRejected(long sessionId, String reason, int code) {
    }

    @Override
    public void onTransferTrying(long sessionId) {
    }

    @Override
    public void onTransferRinging(long sessionId) {
    }

    @Override
    public void onACTVTransferSuccess(long sessionId) {
        Session session = CallManager.Instance().findSessionBySessionID(sessionId);
        if (session != null) {
            session.state = Session.CALL_STATE_FLAG.CLOSED;
            session.SessionID = sessionId;

            Intent broadIntent = new Intent(CALL_CHANGE_ACTION);
            broadIntent.putExtra(EXTRA_CALL_SEESIONID, sessionId);
            String description = "Transfer succeeded, call closed";
            broadIntent.putExtra(EXTRA_CALL_DESCRIPTION, description);

            sendPortSipMessage(description, broadIntent);
            // Close the call after succeeded transfer the call
            mEngine.hangUp(sessionId);
        }
    }

    @Override
    public void onACTVTransferFailure(long sessionId, String reason, int code) {
        Session session = CallManager.Instance().findSessionBySessionID(sessionId);
        if (session != null) {
            Intent broadIntent = new Intent(CALL_CHANGE_ACTION);
            broadIntent.putExtra(EXTRA_CALL_SEESIONID, sessionId);
            String description =  "Transfer failure!";
            broadIntent.putExtra(EXTRA_CALL_DESCRIPTION, description);

            sendPortSipMessage(description, broadIntent);

        }
    }

    @Override
    public void onReceivedSignaling(long sessionId, String signaling) {
    }

    @Override
    public void onSendingSignaling(long sessionId, String signaling) {
    }

    @Override
    public void onWaitingVoiceMessage(
            String messageAccount,
            int urgentNewMessageCount,
            int urgentOldMessageCount,
            int newMessageCount,
            int oldMessageCount) {
    }

    @Override
    public void onWaitingFaxMessage(
            String messageAccount,
            int urgentNewMessageCount,
            int urgentOldMessageCount,
            int newMessageCount,
            int oldMessageCount) {
    }

    @Override
    public void onRecvDtmfTone(long sessionId, int tone) {
    }

    @Override
    public void onRecvOptions(String optionsMessage) {
    }

    @Override
    public void onRecvInfo(String infoMessage) {
    }

    @Override
    public void onRecvNotifyOfSubscription(long sessionId, String notifyMessage, byte[] messageData, int messageDataLength) {
    }

    //Receive a new subscribe
    @Override
    public void onPresenceRecvSubscribe(
            long subscribeId,
            String fromDisplayName,
            String from,
            String subject) {
        Contact contact = ContactManager.Instance().FindContactBySipAddr(from);
        if (contact == null) {
            contact = new Contact();
            contact.SipAddr = from;
            ContactManager.Instance().AddContact(contact);
        }

        contact.SubRequestDescription = subject;
        contact.SubId = subscribeId;
        switch (contact.state) {
            case ACCEPTED://This subscribe has accepted
                applicaton.mEngine.presenceAcceptSubscribe(subscribeId);
                break;
            case REJECTED://This subscribe has rejected
                applicaton.mEngine.presenceRejectSubscribe(subscribeId);
                break;
            case UNSETTLLED:
                break;
            case UNSUBSCRIBE:
                contact.state = Contact.SUBSCRIBE_STATE_FLAG.UNSETTLLED;
                break;
        }
        Intent broadIntent = new Intent(PRESENCE_CHANGE_ACTION);
        sendPortSipMessage("OnPresenceRecvSubscribe", broadIntent);
    }

    //update online status
    @Override
    public void onPresenceOnline(String fromDisplayName, String from, String stateText) {
        Contact contact = ContactManager.Instance().FindContactBySipAddr(from);
        if (contact == null) {

        } else {
            contact.SubDescription = stateText;
        }

        Intent broadIntent = new Intent(PRESENCE_CHANGE_ACTION);
        sendPortSipMessage("OnPresenceRecvSubscribe", broadIntent);
    }

    //update offline status
    @Override
    public void onPresenceOffline(String fromDisplayName, String from) {
        Contact contact = ContactManager.Instance().FindContactBySipAddr(from);
        if (contact == null) {

        } else {
            contact.SubDescription = "Offline";
        }

        Intent broadIntent = new Intent(PRESENCE_CHANGE_ACTION);
        sendPortSipMessage("OnPresenceRecvSubscribe", broadIntent);
    }

    @Override
    public void onRecvMessage(
            long sessionId,
            String mimeType,
            String subMimeType,
            byte[] messageData,
            int messageDataLength) {
    }

    @Override
    public void onRecvOutOfDialogMessage(
            String fromDisplayName,
            String from,
            String toDisplayName,
            String to,
            String mimeType,
            String subMimeType,
            byte[] messageData,
            int messageDataLengthsipMessage,
            String sipMessage) {
        if ("text".equals(mimeType) && "plain".equals(subMimeType)) {
            Toast.makeText(this,"you have a mesaage from: "+from+ "  "+new String(messageData),Toast.LENGTH_SHORT).show();
        }else{
        }
    }

    @Override
    public void onSendMessageSuccess(long sessionId, long messageId) {
    }

    @Override
    public void onSendMessageFailure(long sessionId, long messageId, String reason, int code) {
    }

    @Override
    public void onSendOutOfDialogMessageSuccess(long messageId,
                                                String fromDisplayName,
                                                String from,
                                                String toDisplayName,
                                                String to) {
    }

    @Override
    public void onSendOutOfDialogMessageFailure(
            long messageId,
            String fromDisplayName,
            String from,
            String toDisplayName,
            String to,
            String reason,
            int code) {
    }

    @Override
    public void onSubscriptionFailure(long subscribeId, int statusCode) {
    }

    @Override
    public void onSubscriptionTerminated(long subscribeId) {
    }

    @Override
    public void onPlayAudioFileFinished(long sessionId, String fileName) {
    }

    @Override
    public void onPlayVideoFileFinished(long sessionId) {
    }

    @Override
    public void onReceivedRTPPacket(
            long sessionId,
            boolean isAudio,
            byte[] RTPPacket,
            int packetSize) {
    }

    @Override
    public void onSendingRTPPacket(long l, boolean b, byte[] bytes, int i) {

    }

    @Override
    public void onAudioRawCallback(
            long sessionId,
            int callbackType,
            byte[] data,
            int dataLength,
            int samplingFreqHz) {
    }

    @Override
    public void onVideoRawCallback(long l, int i, int i1, int i2, byte[] bytes, int i3) {

    }


    //--------------------
    public void sendPortSipMessage(String message, Intent broadIntent) {
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, AppStart_.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification.Builder builder;
        builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.logo)
                .setContentText(message)
                .setContentIntent(contentIntent)
                .build();// getNotification()

//        mNotifyMgr.notify(1, builder.build());

        sendBroadcast(broadIntent);
    }

    public int outOfDialogRefer(int replaceSessionId, String replaceMethod, String target, String referTo) {
        return 0;
    }

    public void keepCpuRun(boolean keepRun) {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (keepRun == true) { //open
            if (mCpuLock == null) {
                if ((mCpuLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SipSample:CpuLock.")) == null) {
                    return;
                }
                mCpuLock.setReferenceCounted(false);
            }

            synchronized (mCpuLock) {
                if (!mCpuLock.isHeld()) {
                    mCpuLock.acquire();
                }
            }
        } else {//close
            if (mCpuLock != null) {
                synchronized (mCpuLock) {
                    if (mCpuLock.isHeld()) {
                        mCpuLock.release();
                    }
                }
            }
        }
    }

}

