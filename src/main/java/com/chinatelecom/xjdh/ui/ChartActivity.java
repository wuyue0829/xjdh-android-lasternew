package com.chinatelecom.xjdh.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppManager;
import com.chinatelecom.xjdh.bean.AlarmChartsItem;
import com.chinatelecom.xjdh.bean.AlarmChartsResp;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.IntValueFormatter;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
/**
 * @author peter
 * 
 */
@EActivity(R.layout.activity_barchart)
public class ChartActivity extends BaseActivity {

	@RestService
	ApiRestClientInterface mApiClient;
	@ViewById(R.id.alarm_barchart_title)
	TextView mBarChartTitle;
	@ViewById(R.id.alarm_barchart)
	HorizontalBarChart mBarChart;
	String[] ALARM_CHART_LABELS = new String[] { "一级告警", "二级告警", "三级告警", "四级告警" };
	ArrayList<String> xVals = new ArrayList<String>();
	ProgressDialog pDialog;
	private Typeface mTf;

	@AfterViews
	void bindData() {
		setTitle("报表");
		pDialog = new ProgressDialog(this);
		pDialog.setMessage(getResources().getString(R.string.progress_loading_msg));
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, ""));
		mBarChart.setDescription("");
		mBarChart.setLogEnabled(false);
		mBarChart.setMaxVisibleValueCount(20);
		mBarChart.setDrawValuesForWholeStack(true);
		mBarChart.setPinchZoom(false);
		mBarChart.setDrawBarShadow(false);
		mBarChart.setDrawValueAboveBar(false);
//		设置字体(在assets目录下新建fonts目录，把TTF字体文件放到这里,//根据路径得到Typeface)
		mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
		XAxis xLabels = mBarChart.getXAxis();
		xLabels.setPosition(XAxisPosition.BOTTOM);
		xLabels.setDrawGridLines(false);
		xLabels.setTypeface(mTf);
		YAxis leftAxis = mBarChart.getAxisLeft();
		leftAxis.setTypeface(mTf);//设置字体
		leftAxis.setValueFormatter(new IntValueFormatter());
		leftAxis.setDrawGridLines(false);
		leftAxis.setSpaceTop(30f);
		mBarChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

			@Override
			public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
				BarEntry entry = (BarEntry) e;
				int stackIndex = h.getStackIndex();
				int xIndex = h.getXIndex();
				T.showLong(getApplicationContext(), xVals.get(xIndex) + "（" + ALARM_CHART_LABELS[stackIndex] + "）：" + (int) entry.getVals()[stackIndex] + "个");
			}

			@Override
			public void onNothingSelected() {

			}
		});

		Legend l = mBarChart.getLegend();
		l.setPosition(LegendPosition.RIGHT_OF_CHART_INSIDE);
		l.setFormSize(8f);
		l.setFormToTextSpace(4f);
		l.setTypeface(mTf);
		l.setYOffset(0f);
		l.setYEntrySpace(0f);
		pDialog.show();
		getChartsData();
	}
	@SuppressWarnings("deprecation")
	@UiThread
	void onPreferenceLogoutClicked() {
		final AlertDialog mExitDialog = new AlertDialog.Builder(ChartActivity.this).create();
		mExitDialog.setTitle("下线提示");
		mExitDialog.setIcon(R.drawable.index_btn_exit);
		mExitDialog.setMessage("您的账户已在另一个设备登录,请尝试重新登陆");
		mExitDialog.setButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mExitDialog.dismiss();
				String account = PreferenceUtils.getPrefString(ChartActivity.this, PreferenceConstants.ACCOUNT, "");
				PreferenceUtils.clearPreference(ChartActivity.this, PreferenceManager.getDefaultSharedPreferences(ChartActivity.this));
				PreferenceUtils.setPrefString(ChartActivity.this, PreferenceConstants.ACCOUNT, account);
				AppManager.getAppManager().finishAllActivity();
				LoginActivity_.intent(ChartActivity.this).start();
			}
		});
		mExitDialog.show();
	}
	@Background
	void getChartsData() {
		try {
			ApiResponse mApiResp = mApiClient.getAlarmChartsData();
			L.d("aaaaaaaaaaaaaaaaaaa" ,mApiResp.toString());
			if (mApiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				AlarmChartsResp alarmChartsResp = mapper.readValue(mApiResp.getData(), AlarmChartsResp.class);
				onResult(true, alarmChartsResp);
				return;
			}else if(mApiResp.getData().equals("Access token is not valid")){
				onPreferenceLogoutClicked();
			}
		} catch (Exception e) {
			L.e(e.toString());
		}
		onResult(false, null);
	}

	@UiThread
	void onResult(boolean isSuccess, AlarmChartsResp alarmChartsResp) {
		pDialog.dismiss();
		if (isSuccess) {
			mBarChartTitle.setText(alarmChartsResp.getTitle());

			ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
			int xIndex = 0;
			for (AlarmChartsItem item : alarmChartsResp.getAlarmChartsDataList()) {
				xVals.add(item.getName());
				yVals1.add(new BarEntry(new float[] { item.getAlarm_level_1(), item.getAlarm_level_2(), item.getAlarm_level_3(), item.getAlarm_level_4() },
						xIndex++));
			}
			BarDataSet barDataSet = new BarDataSet(yVals1, "");
			barDataSet.setColors(new int[] { getResources().getColor(R.color.red), getResources().getColor(R.color.orange),
					getResources().getColor(R.color.yellow), getResources().getColor(R.color.blue) });
			barDataSet.setStackLabels(ALARM_CHART_LABELS);
			ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
			dataSets.add(barDataSet);
			BarData data = new BarData(xVals, dataSets);
			data.setValueTypeface(mTf);
			data.setValueTextSize(12f);
			data.setValueFormatter(new IntValueFormatter());
			mBarChart.setData(data);
			mBarChart.invalidate();
			mBarChart.animateXY(3000, 3000);
		} else {
			T.showLong(this, "获取数据失败");
		}
	}
}
