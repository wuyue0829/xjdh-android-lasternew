package com.chinatelecom.xjdh.utils;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author peter
 * 
 */
public class DialogUtils {
	public static int getScreenWidth(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public static int getScreenHeight(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/**
	 * 得到自定义的progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字

		if (msg != null)
			tipTextView.setText(msg);// 设置加载信息

		final Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		// loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		v.findViewById(R.id.img_close).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadingDialog.dismiss();
			}
		});
		return loadingDialog;
	}

	@SuppressWarnings("deprecation")
	public static void buildExitDialog(final Context ctx) {
		final AlertDialog mExitDialog = new AlertDialog.Builder(ctx).create();
		mExitDialog.setTitle("温馨提示");
		mExitDialog.setIcon(R.drawable.index_btn_exit);
		mExitDialog.setMessage("确定退出？\n退出后将无法接收推送消息。");
		mExitDialog.setButton("退出", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mExitDialog.dismiss();
				AppManager.getAppManager().AppExit(ctx);
			}
		});
		mExitDialog.setButton3("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mExitDialog.dismiss();
			}
		});
		mExitDialog.setButton2("后台运行", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mExitDialog.dismiss();
				Activity activity = (Activity) ctx;
				activity.moveTaskToBack(true);
			}
		});
		mExitDialog.setCanceledOnTouchOutside(true);
		mExitDialog.show();
	}
}
