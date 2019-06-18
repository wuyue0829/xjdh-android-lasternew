package com.chinatelecom.xjdh.fragment;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.PreferenceByKey;
import org.androidannotations.annotations.PreferenceClick;
import org.androidannotations.annotations.PreferenceScreen;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppManager;
import com.chinatelecom.xjdh.ui.AboutActivity_;
import com.chinatelecom.xjdh.ui.FeedBackActivity_;
import com.chinatelecom.xjdh.ui.LoginActivity_;
import com.chinatelecom.xjdh.utils.DialogUtils;
import com.chinatelecom.xjdh.utils.FileUtils;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.UpdateManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

/**
 * @author peter
 * 
 */
@PreferenceScreen(R.xml.preferences)
@EFragment
public class SettingFragment extends PreferenceFragment {
	private Dialog latestOrFailDialog;
	@PreferenceByKey(R.string.new_message_background)
	SwitchPreference spfBackground;

	@SuppressWarnings("deprecation")
	@PreferenceClick(R.string.logout)
	void onPreferenceLogoutClicked() {
		final AlertDialog mExitDialog = new Builder(getActivity()).create();
		mExitDialog.setTitle("温馨提示");
		mExitDialog.setIcon(R.drawable.index_btn_exit);
		mExitDialog.setMessage("确定注销？");
		mExitDialog.setButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mExitDialog.dismiss();
				String account = PreferenceUtils.getPrefString(getActivity(), PreferenceConstants.ACCOUNT, "");
				PreferenceUtils.clearPreference(getActivity(), PreferenceManager.getDefaultSharedPreferences(getActivity()));
				PreferenceUtils.setPrefString(getActivity(), PreferenceConstants.ACCOUNT, account);
				try {
					FileUtils.setToData(getActivity(), SharedConst.FILE_AREA_JSON, new byte[] {});
					FileUtils.setToData(getActivity(), SharedConst.FILE_MODEL_JSON, new byte[] {});
				} catch (Exception e) {
					L.e(e.toString());
				}
				AppManager.getAppManager().finishAllActivity();
				LoginActivity_.intent(getActivity()).start();
				getActivity().finish();
			}
		});
		mExitDialog.setButton2("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mExitDialog.dismiss();
			}
		});
		mExitDialog.show();
	}

//	@PreferenceClick(R.string.app_update)
//	void onPreferenceUpdateClicked() {
//		UpdateManager.getUpdateManager().checkAppUpdate(getActivity(), true);
//	}
//
//	@PreferenceClick(R.string.app_feedback)
//	void onPreferenceFeedbackClicked() {
//		FeedBackActivity_.intent(getActivity()).start();
//	}

	@PreferenceClick(R.string.app_exit)
	void onPreferenceExitClicked() {
		DialogUtils.buildExitDialog(getActivity());
	}

	@PreferenceClick(R.string.app_about)
	void onPreferenceAboutClicked() {
		AboutActivity_.intent(getActivity()).start();
	}
}
