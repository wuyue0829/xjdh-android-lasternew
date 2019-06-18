package com.chinatelecom.xjdh.app;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.telephony.TelephonyManager;

import com.baidu.mapapi.SDKInitializer;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.Update;
import com.hss01248.dialog.StyledDialog;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.portsip.PortSipSdk;

import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.rest.RestService;
import org.xutils.DbManager;
import org.xutils.x;

import java.util.List;

/**
 * @author peter
 * 
 */
@EApplication
public class AppContext extends Application {
	public static final int NUM_PAGE = 6;// 总共有多少页
	public static int NUM = 20;// 每页20个表情,还有最后一个删除button
	private static AppContext mApplication;
	private TelephonyManager mTelephonyMgr;
	public PortSipSdk mEngine;
	public boolean mConference= false;
	public boolean mUseFrontCamera= false;
	public boolean mIsVideo = false;

	public synchronized static AppContext getInstance() {
		return mApplication;
	}
	public static Context getAppContext() {
		return mApplication;
	}


	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	@SuppressWarnings("unused")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;

		mEngine = new PortSipSdk();
		mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		L.isDebug = PreferenceUtils.getPrefBoolean(this, PreferenceConstants.ISNEEDLOG, true);
				if (Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}
		x.Ext.init(this);
		x.Ext.setDebug(true);
		StyledDialog.init(this);
		initImageLoader(getApplicationContext());
		SDKInitializer.initialize(this);
		//创建默认的ImageLoader配置参数
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration
				.createDefault(this);
		
		//Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(configuration);

	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(50 * 1024 * 1024)
				.tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	public String getPhoneImei() {
		return mTelephonyMgr.getDeviceId();
	}

	public static boolean isApplicationBackground(final Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 用来判断服务是否运行.
	 * 
	 * @param className
	 *            判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(100);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	@RestService
	ApiRestClientInterface mApiClient;

	public Update getUpdateInfo() {
		Update u = null;// = new ApiRestClientInterface_(this);
		try {
			u = mApiClient.getUpdateInfo();
			L.d("-------------", u.getDownloadUrl());
		} catch (Exception e) {
			L.e("get update info", e.toString());
		}
		return u;
	}

	public DbManager getDbManager() {
		DbManager.DaoConfig daoconfig = new DbManager.DaoConfig();
		daoconfig.setDbName("donghuan.db");
		daoconfig.setDbVersion(1);
		return x.getDb(daoconfig);
	}

}
