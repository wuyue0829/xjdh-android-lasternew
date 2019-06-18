package com.chinatelecom.xjdh.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.adapter.RstpAdapter;
import com.chinatelecom.xjdh.bean.ApiResponseUrl;
import com.chinatelecom.xjdh.bean.DevItem;
import com.chinatelecom.xjdh.bean.DevTypeItem;
import com.chinatelecom.xjdh.bean.RtspUrl;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.DividerItemDecoration;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.T;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@EActivity(R.layout.rstp_video)
public class RstpVideoActivity extends BaseActivity {

	@RestService
	ApiRestClientInterface mApiClient;
	@Extra("devTypeItem")
	DevTypeItem devTypeItem;
	@ViewById
	RecyclerView recycler;
	@ViewById(R.id.srl_alarm)
	SwipeRefreshLayout mSrlAlarm;
	private List<RtspUrl> list = new ArrayList<RtspUrl>();
	private List<Fragment> mListViews = new ArrayList<Fragment>();
	// private Handler handler = new Handler();
	private LinearLayoutManager mLinearLayoutManager;
	private RstpAdapter adapter;
	private String urls;
	ProgressDialog pDialog;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 10000:
				timer.cancel();
				pDialog.dismiss();
				if (list.size() > 0) {
					pDialog.dismiss();
					// T.showShort(getApplicationContext(), "数据请求成功");
				} else {
					pDialog.dismiss();
					T.showShort(getApplicationContext(), "请求超时");
					RstpVideoActivity.this.finish();

				}

				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("视频监控");
		adapter = new RstpAdapter(this);
		if (pDialog == null) {
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("加载数据中...");
			pDialog.setCancelable(true);
		}
	}

	@AfterViews
	void showView() {
		pDialog.show();
		getdata();
		mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		recycler.setLayoutManager(mLinearLayoutManager);
		recycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
		recycler.setAdapter(adapter);
		mSrlAlarm.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				mSrlAlarm.setRefreshing(false);
				pDialog.show();
				list.clear();
				adapter.clearList();
				getdata();
			}

		});

	}

	private void addView(String URL, String name) {
		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}
		RtspUrl rtspUrl = new RtspUrl(URL, name);
		list.add(rtspUrl);
		adapter.setItemList(list);
		adapter.notifyDataSetChanged();
	}
	

	private Timer timer;
	private MyTimerTask task;

	@Background()
	void getdata() {
		try {
			checkTimeOut();
			for (final DevItem i : devTypeItem.getDevlist()) {


				final ApiResponseUrl resp = mApiClient.GetCameraUrl(i.getData_id());
				L.d(">>>>>>>>>>>>>>" + resp.toString());
				handler.post(new Runnable() {
					@Override
					public void run() {
						addView(resp.getRtsp_url(), i.getName());
					}
				});
			}
		} catch (Exception ex) {
			L.e("Exception" + ex.toString());
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.finish();

	}

	private void checkTimeOut() {
		try {
			timer = new Timer();
			task = new MyTimerTask();
			timer.schedule(task, 10000);
		} catch (Exception e) {
			// TODO: handle exception
			L.e(e.toString());
			;
		}
	}

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			handler.sendEmptyMessage(10000);
		}

	}
}
