package com.chinatelecom.xjdh.receiver;

import java.util.ArrayList;

import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.ReceiverAction;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.service.ScheduleService_;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author peter
 * 
 */
@EReceiver
public class AppBroadcastReceiver extends BroadcastReceiver {

	public static ArrayList<EventHandler> mListeners = new ArrayList<EventHandler>();

	@Override
	public void onReceive(Context context, Intent intent) {
	}

	@ReceiverAction("android.intent.action.BOOT_COMPLETED")
	void onReceiveBootCompleted(Context context) {
		if (PreferenceUtils.getPrefBoolean(context, context.getResources().getString(R.string.new_message_background), false)
				&& !PreferenceUtils.getPrefString(context, PreferenceConstants.ACCESSTOKEN, "").isEmpty())
			ScheduleService_.intent(context).start();
	}

	@ReceiverAction("android.net.conn.CONNECTIVITY_CHANGE")
	void onReceiveNetworkChange(Context ctx) {
		for (EventHandler handler : mListeners) {
			handler.onNetChange();
		}
	}

	public static abstract interface EventHandler {
		public abstract void onNetChange();
	}
}
