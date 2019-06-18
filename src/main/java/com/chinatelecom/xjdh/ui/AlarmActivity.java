package com.chinatelecom.xjdh.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.adapter.AlarmListAdapter;
import com.chinatelecom.xjdh.app.AppManager;
import com.chinatelecom.xjdh.bean.AlarmItem;
import com.chinatelecom.xjdh.bean.AlarmResp;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.CityItem;
import com.chinatelecom.xjdh.bean.CountyItem;
import com.chinatelecom.xjdh.bean.DevModelItem;
import com.chinatelecom.xjdh.bean.RoomItem;
import com.chinatelecom.xjdh.bean.SignalItem;
import com.chinatelecom.xjdh.bean.SubstationItem;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.service.ScheduleService;
import com.chinatelecom.xjdh.service.ScheduleService.onNewAlarmServiceListener;
import com.chinatelecom.xjdh.service.ScheduleService_;
import com.chinatelecom.xjdh.utils.FileUtils;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.StringUtils;
import com.chinatelecom.xjdh.utils.T;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author peter
 * 
 */
@EActivity(R.layout.activity_alarm)
public class AlarmActivity extends BaseActivity {
	@ViewById(R.id.lv_alarm)
	ListView mLvAlarm;
	@ViewById(R.id.srl_alarm)
	SwipeRefreshLayout mSrlAlarm;
	@ViewById(R.id.sv_alarm_filter)
	ScrollView mSvAlarmFilter;
	@ViewById(R.id.sp_city)
	Spinner mSpCity;
	@ViewById(R.id.sp_county)
	Spinner mSpCounty;
	@ViewById(R.id.sp_substation)
	Spinner mSpSubstation;
	@ViewById(R.id.sp_room)
	Spinner mSpRoom;
	@ViewById(R.id.sp_level)
	Spinner mSpLevel;
	@ViewById(R.id.sp_model)
	Spinner mSpModel;
	@ViewById(R.id.sp_signal)
	Spinner mSpSignal;
	@ViewById(R.id.et_start_datetime)
	EditText mEtStartDatetime;
	@ViewById(R.id.et_end_datetime)
	EditText mEtEndDatetime;
	@RestService
	ApiRestClientInterface mApiClient;
	LinearLayout footerView;
	TextView footerMsg;
	ProgressDialog pDialog;
	private static final int MENU_FILTER_ID = Menu.FIRST;
	private HashMap<String, String> alarmLevelList = new LinkedHashMap<String, String>();
	private List<CityItem> cityList = new ArrayList<CityItem>(0);
	private List<DevModelItem> modelList = new ArrayList<DevModelItem>(0);
	private List<SignalItem> signalList = new ArrayList<SignalItem>(0);
	List<AlarmItem> alarmList = new ArrayList<AlarmItem>(0);
	private AlarmListAdapter mAlarmListAdapter;
	private ArrayAdapter<String> mCityAdapter;
	private ArrayAdapter<String> mCountyAdapter;
	private ArrayAdapter<String> mSubstationtyAdapter;
	private ArrayAdapter<String> mRoomAdapter;
	private ArrayAdapter<String> mModelAdapter;
	private ArrayAdapter<String> mSignalAdapter;
	private int selCity = 0;
	private int selCounty = 0;
	private int selSubstation = 0;
	private int selRoom = 0;
	private int selModel = 0;
	private int selSignal = 0;
	private int selLevel = 0;
	private DatePickerDialog mDpDlg;
	private int selDateField = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent(ScheduleService.ALARM_ACTIVITY_RECEIVER_ACTION);
		sendBroadcast(intent);
		setTitle("告警处理");
		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
		mAlarmListAdapter = new AlarmListAdapter(this, alarmList);
		if (pDialog == null) {
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("加载数据中...");
			pDialog.setCancelable(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_FILTER_ID:
			if (mSvAlarmFilter.getVisibility() == View.VISIBLE)
				mSvAlarmFilter.setVisibility(View.GONE);
			else {
				mSvAlarmFilter.setVisibility(View.VISIBLE);
				ObjectMapper mapper = new ObjectMapper();
				/**
				 * cityList全部数据
				 */
				if (cityList.size() == 0) {
					String cityData = new String(FileUtils.getFileData(this, SharedConst.FILE_AREA_JSON));
					try {
						List<CityItem> l = mapper.readValue(cityData, new TypeReference<List<CityItem>>() {
						});
						cityList.addAll(l);
						List<String> cities = new ArrayList<>();
						cities.add("全网");
						for (CityItem cityItem : cityList) {
							cities.add(cityItem.getName());
						}
						mCityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
						mCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						mSpCity.setAdapter(mCityAdapter);
					} catch (Exception e) {
						L.e(e.toString());
					}
				}
				if (modelList.size() == 0) {
					String modelData = new String(FileUtils.getFileData(this, SharedConst.FILE_DEV_JSON));
					try {
						List<DevModelItem> l = mapper.readValue(modelData, new TypeReference<List<DevModelItem>>() {
						});
						modelList.addAll(l);
						List<String> models = new ArrayList<>();
						models.add("所有类型");
						for (int index = 0; index < modelList.size(); index++) {
							models.add(modelList.get(index).getVal());
						}
						mModelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, models);
						mModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						mSpModel.setAdapter(mModelAdapter);
					} catch (Exception e) {
						L.e(e.toString());
					}
				}
				if (signalList.size() == 0) {
					String signalData = new String(FileUtils.getFileData(this, SharedConst.FILE_SIGNAL_JSON));
					try {
						List<SignalItem> l = mapper.readValue(signalData, new TypeReference<List<SignalItem>>() {
						});
						signalList.addAll(l);
						List<String> signals = new ArrayList<>();
						signals.add("所有类型");
						for (int index = 0; index < signalList.size(); index++) {
							signals.add(signalList.get(index).getVal());
						}
						mSignalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, signals);
						mSignalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						mSpSignal.setAdapter(mSignalAdapter);
					} catch (Exception e) {
						L.e(e.toString());
					}
				}
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_FILTER_ID, 0, "筛选").setIcon(R.drawable.icon_filter)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}
	private int totalItemCount;
	private int lastViewItem;
	@AfterViews
	void bindData() {
		ScheduleService.setOnNewAlarmServiceListener(new onNewAlarmServiceListener() {

			@Override
			public void onHasNewAlarm(String latestId) {
				long latest = Long.parseLong(latestId);
				long currentMax = alarmList.size() > 0 ? Long.parseLong(alarmList.get(0).getId()) : 0;
//				updateTvRefreshVisibility(latest > currentMax);
				// L.i("Refreshing alarm latestId:" + latestId + " currentMax:"
				// + currentMax);
			}
		});
//		mLvAlarm.addFooterView(footerView);
		mLvAlarm.setAdapter(mAlarmListAdapter);
		mLvAlarm.setOnScrollListener(new OnScrollListener() {
			private int totalItemCount;
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (totalItemCount == lastViewItem && scrollState == SCROLL_STATE_IDLE) {
					pDialog.show();
					getData(false);
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				lastViewItem = firstVisibleItem + visibleItemCount;
				this.totalItemCount = totalItemCount;
			}
		});
		
		Calendar c = Calendar.getInstance();
		mEtStartDatetime
				.setText(c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DAY_OF_MONTH));
		mEtEndDatetime
				.setText(c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH));

		alarmLevelList.put("0", "所有级别");
		alarmLevelList.put("1", "一级");
		alarmLevelList.put("2", "二级");
		alarmLevelList.put("3", "三级");
		alarmLevelList.put("4", "四级");

		ArrayAdapter<String> levelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
				new ArrayList<String>(alarmLevelList.values()));
		levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpLevel.setAdapter(levelAdapter);
		mSpLevel.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View v, int position, long arg3) {
				selLevel = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		mSpCity.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View v, int position, long arg3) {
				selCity = position;
				List<String> counties = new ArrayList<>();
				counties.add("所有分局");
				if (position > 0) {
					CountyItem[] countyList = cityList.get(selCity - 1).getCountylist();
					for (CountyItem e : countyList) {
						counties.add(e.getName());
					}
				}
				mCountyAdapter = new ArrayAdapter<>(AlarmActivity.this, android.R.layout.simple_spinner_item, counties);
				mCountyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				mSpCounty.setAdapter(mCountyAdapter);
				selCounty = 0;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}
		});

		mSpCounty.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View v, int position, long arg3) {
				selCounty = position;
				List<String> substations = new ArrayList<String>();
				substations.add("所有局站");
				if (position > 0) {
					CountyItem countyObj = cityList.get(selCity - 1).getCountylist()[selCounty - 1];
					for (SubstationItem e : countyObj.getSubstationlist()) {
						substations.add(e.getName());
					}
				}
				mSubstationtyAdapter = new ArrayAdapter<>(AlarmActivity.this, android.R.layout.simple_spinner_item,
						substations);
				mSubstationtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				mSpSubstation.setAdapter(mSubstationtyAdapter);
				selSubstation = 0;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}
		});
		mSpSubstation.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View v, int position, long arg3) {
				selSubstation = position;
				List<String> rooms = new ArrayList<>();
				rooms.add("所有机房");
				if (position > 0) {
					SubstationItem substationObj = cityList.get(selCity - 1).getCountylist()[selCounty - 1]
							.getSubstationlist()[selSubstation - 1];
					for (RoomItem e : substationObj.getRoomlist()) {
						rooms.add(e.getName());
					}
				}
				mRoomAdapter = new ArrayAdapter<>(AlarmActivity.this, android.R.layout.simple_spinner_item, rooms);
				mRoomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				mSpRoom.setAdapter(mRoomAdapter);
				selRoom = 0;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}
		});
		mSpRoom.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View v, int position, long arg3) {
				selRoom = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}
		});
		mSpSignal.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View v, int position, long arg3) {
				selSignal = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}
		});

		mSpModel.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View v, int position, long arg3) {
				selModel = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}
		});
		if (mDpDlg == null) {
			mDpDlg = new DatePickerDialog(this, mDateSetListener, 2015, 1, 1);
			mDpDlg.setCanceledOnTouchOutside(true);
		}
		/**
		 * 第一次刷新数据
		 */
		if (alarmList.size() == 0) {
			L.d("bindData pdialog show");
			pDialog.show();
			getData(true);
		}
		mSrlAlarm.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				mSrlAlarm.setRefreshing(false);
				alarmList.clear();
				pDialog.show();
				getData(true);
			}
		});
		
		
	}

	@Touch(R.id.et_start_datetime)
	void onStartDatetimeClicked() {
		selDateField = R.id.et_start_datetime;
		mDpDlg.setTitle("选择开始时间");
		if (mEtStartDatetime.getText().toString().length() > 0) {
			Date date = StringUtils.toDate2(mEtStartDatetime.getText().toString());
			mDpDlg.updateDate(1900 + date.getYear(), date.getMonth(), date.getDate());
		} else {
			Calendar c = Calendar.getInstance();
			c.get(Calendar.DATE);
			mDpDlg.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		}
		if (!mDpDlg.isShowing())
			mDpDlg.show();

	}

	@Touch(R.id.et_end_datetime)
	void onEndDatetimeClicked() {
		selDateField = R.id.et_end_datetime;
		mDpDlg.setTitle("选择结束时间");
		if (mEtEndDatetime.getText().toString().length() > 0) {
			Date date = StringUtils.toDate2(mEtEndDatetime.getText().toString());
			mDpDlg.updateDate(1900 + date.getYear(), date.getMonth(), date.getDate());
		} else {
			Calendar c = Calendar.getInstance();
			mDpDlg.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		}
		if (!mDpDlg.isShowing())
			mDpDlg.show();
	}

	private OnDateSetListener mDateSetListener = new OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			if (selDateField == R.id.et_start_datetime) {
				Date startDate = StringUtils.toDate2(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
				Date endDate = StringUtils.toDate2(mEtEndDatetime.getText().toString());
				if (startDate.getTime() > endDate.getTime())
					mEtEndDatetime.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
				mEtStartDatetime.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
			} else if (selDateField == R.id.et_end_datetime) {
				Date endDate = StringUtils.toDate2(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
				Date startDate = StringUtils.toDate2(mEtStartDatetime.getText().toString());
				if (startDate.getTime() > endDate.getTime())
					T.showShort(AlarmActivity.this, "结束时间不能小于开始时间");
				else
					mEtEndDatetime.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
			}
		}
	};

	@Click(R.id.btn_confirm)
	void onBtnConfirmClicked() {
		pDialog.show();
		mSvAlarmFilter.setVisibility(View.GONE);
		alarmList.clear();
		mAlarmListAdapter.notifyDataSetChanged();
		getData(true);
	}
	
	@SuppressWarnings("deprecation")
	@UiThread
	void onPreferenceLogoutClicked() {
		final AlertDialog mExitDialog = new AlertDialog.Builder(AlarmActivity.this).create();
		mExitDialog.setTitle("下线提示");
		mExitDialog.setIcon(R.drawable.index_btn_exit);
		mExitDialog.setMessage("您的账户已在另一个设备登录,请尝试重新登陆");
		mExitDialog.setButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mExitDialog.dismiss();
				String account = PreferenceUtils.getPrefString(AlarmActivity.this, PreferenceConstants.ACCOUNT, "");
				PreferenceUtils.clearPreference(AlarmActivity.this, PreferenceManager.getDefaultSharedPreferences(AlarmActivity.this));
				PreferenceUtils.setPrefString(AlarmActivity.this, PreferenceConstants.ACCOUNT, account);
				AppManager.getAppManager().finishAllActivity();
				LoginActivity_.intent(AlarmActivity.this).start();
			}
		});
		mExitDialog.show();
	}

	@Background
	void getData(boolean isRefreshing) {
		try {
			String citycode = "";
			String countycode = "";
			String substationId = "";
			String roomId = "";
			if (selCity > 0) {
				citycode = cityList.get(selCity - 1).getCity_code();
				if (selCounty > 0) {
					countycode = cityList.get(selCity - 1).getCountylist()[selCounty - 1].getCode();
					if (selSubstation > 0) {
						substationId = cityList.get(selCity - 1).getCountylist()[selCounty - 1]
								.getSubstationlist()[selSubstation - 1].getCode();
						if (selRoom > 0) {
							roomId = cityList.get(selCity - 1).getCountylist()[selCounty - 1]
									.getSubstationlist()[selSubstation - 1].getRoomlist()[selRoom - 1].getId();
						}
					}
				}
			}
			String level = String.valueOf(selLevel);
			String model = selModel > 0 ? modelList.get(selModel - 1).getKey() : "";
			String signal = selSignal > 0 ? signalList.get(selSignal - 1).getVal() : "";
			String startdatetime = mEtStartDatetime.getText().toString();
			String enddatetime = mEtEndDatetime.getText().toString();
			String lastId = (isRefreshing && alarmList.size() > 0) ? alarmList.get(0).getId() : "-1";
//			ScheduleService.SetRequestParams(citycode, countycode, substationId, roomId, level, model, startdatetime,
//					enddatetime, String.valueOf(isRefreshing ? 0 : alarmList.size()),
//					String.valueOf(SharedConst.DEFAULT_PAGE_SIZE), lastId);
			L.d("00000000", citycode+"==="+countycode+"==="+substationId+"==="+roomId+"==="+level+"==="+model+"==="+startdatetime+"==="+enddatetime+"==="+lastId);
			ApiResponse apiResp = mApiClient.getAlarmList(citycode, countycode, substationId, roomId, level, model,signal,startdatetime, enddatetime, String.valueOf(isRefreshing ? 0 : alarmList.size()),String.valueOf(SharedConst.DEFAULT_PAGE_SIZE), lastId);
			L.d("------*****", apiResp.toString());
			// 请求加载数据
			if (apiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				AlarmResp alarmResp = mapper.readValue(apiResp.getData(), AlarmResp.class);
				List<AlarmItem> l = Arrays.asList(alarmResp.getAlarmlist());
				if (isRefreshing) {
					alarmList.clear();
					alarmList.addAll(l);
				} else {
					alarmList.addAll(l);
				}
				updateAlarmListView(isRefreshing, l.size() > 0, l.size() < 10);
			} else if(apiResp.getData().equals("Access token is not valid")){
				onPreferenceLogoutClicked();
			}else{
				updateAlarmListView(isRefreshing, true, true);
			}
		} catch (Exception e) {
			L.d("000000000000000",e.toString());
			updateAlarmListView(isRefreshing, false, false);
		}
	}

	@UiThread
	void updateAlarmListView(boolean isRefreshing, boolean isSuccess, boolean isLoadAll) {
		mSrlAlarm.setRefreshing(false);
		if (pDialog.isShowing() && pDialog.isShowing()) {
			L.d("updateAlarmListView pdialog dismiss");
			pDialog.dismiss();
		}

//		if (isRefreshing) {
//			if (isSuccess)
//				tvRefresh.setVisibility(View.GONE);
//			else
//				tvRefresh.setText("加载失败，点击重试");
//		}
		

//		if (!isLoadAll) {
//			footerMsg.setText("点击加载更多");
//			footerMsg.setClickable(true);
//		} else {
//			T.showLong(this, "已经加载全部");
//			footerMsg.setText("已经加载全部");
//			footerMsg.setClickable(false);
//		}
		if (isSuccess) {
			mAlarmListAdapter.notifyDataSetChanged();
		}
	}

//	@Click(R.id.tv_refresh)
//	void onTvRefreshClicked() {
//		pDialog.show();
//		getData(true);
//	}
//
//	@UiThread
//	void updateTvRefreshVisibility(boolean isVisiable) {
//		tvRefresh.setVisibility(isVisiable ? View.VISIBLE : View.GONE);
//	}

	@Override
	protected void onDestroy() {
		ScheduleService_.SetRequestParams("", "", "", "", "", "", "", "", "", "","", "");
		if (AppManager.getAppManager().getActivityStackSize() > 1) {
			super.onDestroy();
		} else {
			MainActivity_.intent(this).start();
			super.onDestroy();
		}
	}

	@ItemClick(R.id.lv_alarm)
	void onAlarmListViewClicked(int position) {
		if (position != mAlarmListAdapter.getCount()) {
			AlarmDetailActivity_.intent(this).alarmItem(alarmList.get(position)).start();
		}
	}

	@Override
	public void onBackPressed() {
		if (mSvAlarmFilter.getVisibility() == View.VISIBLE) {
			mSvAlarmFilter.setVisibility(View.GONE);
			return;
		}
		super.onBackPressed();
	}
}
