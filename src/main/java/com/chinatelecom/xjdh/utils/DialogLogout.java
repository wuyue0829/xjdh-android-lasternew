package com.chinatelecom.xjdh.utils;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppManager;
import com.chinatelecom.xjdh.ui.LoginActivity_;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.PreferenceManager;

public class DialogLogout {
	private static AlertDialog dialog;

	@SuppressWarnings("deprecation")
	public static void showDialog(final Context context) {
		if (null == dialog) {
			dialog = new AlertDialog.Builder(context).create();
			dialog.setTitle("下线提示");
			dialog.setIcon(R.drawable.index_btn_exit);
			dialog.setMessage("您的账户已在另一个设备登录,请尝试重新登陆");
			dialog.setButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					String account = PreferenceUtils.getPrefString(context, PreferenceConstants.ACCOUNT, "");
					PreferenceUtils.clearPreference(context, PreferenceManager.getDefaultSharedPreferences(context));
					PreferenceUtils.setPrefString(context, PreferenceConstants.ACCOUNT, account);
					AppManager.getAppManager().finishAllActivity();
					LoginActivity_.intent(context).start();
				}
			});
			dialog.show();
		}
	}
}
