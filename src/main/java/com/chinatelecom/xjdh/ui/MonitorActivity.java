package com.chinatelecom.xjdh.ui;//package com.chinatelecom.xjdh.ui;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.androidannotations.annotations.AfterViews;
//import org.androidannotations.annotations.Background;
//import org.androidannotations.annotations.Click;
//import org.androidannotations.annotations.EActivity;
//import org.androidannotations.annotations.ItemClick;
//import org.androidannotations.annotations.UiThread;
//import org.androidannotations.annotations.ViewById;
//import org.androidannotations.annotations.rest.RestService;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.type.TypeReference;
//
//import com.chinatelecom.xjdh.R;
//import com.chinatelecom.xjdh.adapter.SubstationListAdapter;
//import com.chinatelecom.xjdh.bean.ApiResponse;
//import com.chinatelecom.xjdh.bean.CityItem;
//import com.chinatelecom.xjdh.bean.CountyItem;
//import com.chinatelecom.xjdh.bean.SubstationItem;
//import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
//import com.chinatelecom.xjdh.utils.FileUtils;
//import com.chinatelecom.xjdh.utils.L;
//import com.chinatelecom.xjdh.utils.PreferenceConstants;
//import com.chinatelecom.xjdh.utils.PreferenceUtils;
//import com.chinatelecom.xjdh.utils.SharedConst;
//import com.chinatelecom.xjdh.utils.T;
//
//import android.app.ProgressDialog;
//import android.os.Bundle;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.ScrollView;
//import android.widget.Spinner;
//import android.widget.TextView;
//
///**
// * @author peter
// * 
// */
//@EActivity(R.layout.normal_list_view)
//public class MonitorActivity extends BaseActivity {
//	@ViewById(R.id.lv_items)
//	ListView mLvSubstation;
//	@ViewById(R.id.tv_new_message)
//	TextView mTvRefresh;
//	@ViewById(R.id.srl_alarm)
//	SwipeRefreshLayout mSrlAlarm;
//	@ViewById(R.id.sv_alarm_filter)
//	ScrollView mSvAlarmFilter;
//	@ViewById(R.id.city_spinners) // 所属分公司
//	Spinner city_spinner;
//	@ViewById(R.id.et_station_names) // 局站名称
//	EditText et_station_name;
//	@ViewById(R.id.county_spinners) // 局站名称下拉列表
//	Spinner county_spinner;
//	private List<CityItem> cityList = new ArrayList<CityItem>(0);
//	// private List<SubstationItem> substationItems = new
//	// ArrayList<SubstationItem>(0);
//	private List<SubstationItem> mSubstationList = new ArrayList<SubstationItem>();
//	private SubstationListAdapter mSubstationAdapter;
//	private static final int MENU_FILTER_ID = Menu.FIRST;
//	private ArrayAdapter<String> mCityAdapter;
//	private ArrayAdapter<String> mCountyAdapter;
//	private String cityName = "all";// 选择的所属公司
//	private String sbarea = "all";// 所属分区
//	private int selCity = 0;
//	@RestService
//	ApiRestClientInterface mApiClient;
//	ProgressDialog pDialog;
//	private String station;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setTitle("实时监控");
//		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
//		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
//		pDialog = new ProgressDialog(this);
//		pDialog.setMessage(getResources().getString(R.string.progress_loading_msg));
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case MENU_FILTER_ID:
//			if (mSvAlarmFilter.getVisibility() == View.VISIBLE) {
//				mSvAlarmFilter.setVisibility(View.GONE);
//			} else {
//				mSvAlarmFilter.setVisibility(View.VISIBLE);
//				ObjectMapper mapper = new ObjectMapper();
//				/**
//				 * cityList全部数据
//				 */
//				if (true) {
//					String cityData = new String(FileUtils.getFileData(this, SharedConst.FILE_AREA_JSON));
//					L.d("@@@@@@@@@@@@@@@@@@@@22", cityData.toString());
//
//					try {
//						List<CityItem> l = mapper.readValue(cityData, new TypeReference<List<CityItem>>() {
//						});
//						cityList.clear();
//						cityList.addAll(l);
//						List<String> cities = new ArrayList<>();
//						cities.add("all");
//						for (CityItem cityItem : cityList) {
//							cities.add(cityItem.getName());
//						}
//						city_spinner.setPrompt("请选择所属公司");
//						mCityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
//						mCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//						city_spinner.setAdapter(mCityAdapter);
//					} catch (Exception e) {
//						L.e(e.toString());
//					}
//					city_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//						@Override
//						public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
//							// TODO Auto-generated method stub
//							cityName = city_spinner.getSelectedItem().toString();// 得到city的内容
//							// Toast.makeText(MonitorActivity.this, "你选择了" +
//							// cityName, 0).show();
//							selCity = position;
//							List<String> counties = new ArrayList<>();
//							counties.add("all");
//							if (position > 0) {
//								CountyItem[] countyList = cityList.get(selCity - 1).getCountylist();
//								for (CountyItem e : countyList) {
//									counties.add(e.getName());
//								}
//							}
//							mCountyAdapter = new ArrayAdapter<>(MonitorActivity.this,
//									android.R.layout.simple_spinner_item, counties);
//							mCountyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//							county_spinner.setAdapter(mCountyAdapter);
//						}
//
//						@Override
//						public void onNothingSelected(AdapterView<?> arg0) {
//							// TODO Auto-generated method stub
//
//						}
//					});
//					/*
//					 * if (true) { String CountyData = new
//					 * String(FileUtils.getFileData(this,
//					 * SharedConst.FILE_AREA_JSON)); try { List<CityItem> l =
//					 * mapper.readValue(cityData, new
//					 * TypeReference<List<CityItem>>() { }); //
//					 * substationItems.addAll(l); List<String> counties = new
//					 * ArrayList<>(); counties.add("全局"); for (CityItem cityItem
//					 * : l) { for (CountyItem countyitem :
//					 * cityItem.getCountylist()) { for (SubstationItem
//					 * substationItem : countyitem.getSubstationlist()) {
//					 * counties.add(substationItem.getName()); } } }
//					 * 
//					 * county_spinner.setPrompt("请选择所属局站"); mCountyAdapter = new
//					 * ArrayAdapter<>(this,
//					 * android.R.layout.simple_spinner_item, counties);
//					 * mCountyAdapter.setDropDownViewResource(android.R.layout.
//					 * simple_spinner_dropdown_item);
//					 * county_spinner.setAdapter(mCountyAdapter); } catch
//					 * (Exception e) { L.e(e.toString()); }
//					 * county_spinner.setOnItemSelectedListener(new
//					 * OnItemSelectedListener() {
//					 * 
//					 * @Override public void onItemSelected(AdapterView<?> arg0,
//					 * View arg1, int arg2, long arg3) {
//					 * Toast.makeText(MonitorActivity.this, "你选择了"+stationName,
//					 * 0).show(); stationName =
//					 * county_spinner.getSelectedItem().toString(); }
//					 * 
//					 * @Override public void onNothingSelected(AdapterView<?>
//					 * arg0) { // TODO Auto-generated method stub
//					 * 
//					 * } }); }
//					 */
//					county_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//						@Override
//						public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
//							sbarea = county_spinner.getSelectedItem().toString();
//							// Toast.makeText(MonitorActivity.this, "你选择了" +
//							// stationName, 0).show();
//						}
//
//						@Override
//						public void onNothingSelected(AdapterView<?> arg0) {
//							// TODO Auto-generated method stub
//
//						}
//					});
//				}
//			}
//			break;
//
//		default:
//			break;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(0, MENU_FILTER_ID, 0, "筛选").setIcon(R.drawable.icon_filter)
//				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	@AfterViews
//	void bindData() {
//		mTvRefresh.setText("加载失败，下拉刷新");
//		if (mSubstationList.size() == 0) {
//			pDialog.show();
//			getData(sbarea, cityName);
//		}
//		/**
//		 * 下拉刷新
//		 */
//		mSrlAlarm.setOnRefreshListener(new OnRefreshListener() {
//
//			@Override
//			public void onRefresh() {
//				mSrlAlarm.setRefreshing(false);
//				pDialog.show();
//				getData(sbarea, cityName);
//			}
//		});
//		mSubstationAdapter = new SubstationListAdapter(this, mSubstationList);
//		mLvSubstation.setAdapter(mSubstationAdapter);
//	}
//	ApiResponse apiResp;
//	@Background
//	void getData(String station, String city) {
//		int tryCount = 0;
//		do {
//			try {
//
//				apiResp = mApiClient.getAreaData(city, station, sbarea);
//				if (apiResp.getRet() == 0) {
//					FileUtils.setToData(this, SharedConst.FILE_AREA_JSON, apiResp.getData().getBytes());
//					ObjectMapper mapper = new ObjectMapper();
//					// 复杂泛型和首字母大写的json转换
//					List<CityItem> l = mapper.readValue(apiResp.getData(), new TypeReference<List<CityItem>>() {
//					});
//					cityList.clear();
//					cityList.addAll(l);
//					mSubstationList.clear();
//					for (CityItem cityItem : l) {
//						for (CountyItem countyitem : cityItem.getCountylist()) {
//							for (SubstationItem substationItem : countyitem.getSubstationlist()) {
//								substationItem.setName(cityItem.getName() + "->" + countyitem.getName() + "->"
//										+ substationItem.getName());
//								mSubstationList.add(substationItem);
//							}
//						}
//					}
//					break;
//				}
//			} catch (Exception e) {
//				L.e(e.toString());
//			}
//		} while (tryCount++ < 5);
//		updateSubstationListview(tryCount < 5);
//	}
//
//	@UiThread
//	void updateSubstationListview(boolean isSuccess) {
//		pDialog.dismiss();
//		T.showLong(this, isSuccess ? "加载完成" : "加载失败");
//		mTvRefresh.setVisibility(isSuccess ? View.GONE : View.VISIBLE);
//		mSubstationAdapter.notifyDataSetChanged();
//	}
//
//	@Click(R.id.tv_new_message)
//	void onTvRefreshClicked() {
//		pDialog.show();
//		getData(sbarea, cityName);
//	}
//
//	@ItemClick(R.id.lv_items)
//	void onSustationListViewClicked(SubstationItem item) {
//		RoomListActivity_.intent(this).substationItem(item).start();
//	}
//
//	/**
//	 * 确认
//	 */
//	@Click(R.id.btn_confirms)
//	void confirmClicked() {
//		pDialog.show();
//		mSvAlarmFilter.setVisibility(View.GONE);
//		mSubstationList.clear();
//		mSubstationAdapter.notifyDataSetChanged();
//		int tryCount = 0;
//		station = "all";
////		station = et_station_name.getText().toString();
//
//		searchData(tryCount, station, sbarea, cityName);
//	}
//
//	/**
//	 * 搜索
//	 * 
//	 * @param tryCount
//	 */
//	@Background
//	public void searchData(int tryCount, String station, String sbarea, String city) {
//		do {
//			try {
//
////				ApiResponse apiResp = mApiClient.getAreaData(city, sbarea, station);
//				L.e(" data------------------- :" + apiResp.getData() + "  ret :" + apiResp.getRet());
//				if (apiResp.getRet() == 0) {
//					ObjectMapper mapper = new ObjectMapper();
//					// 复杂泛型和首字母大写的json转换
//					List<CityItem> l = mapper.readValue(apiResp.getData(), new TypeReference<List<CityItem>>() {
//					});
//					cityList.clear();
//					cityList.addAll(l);
//					mSubstationList.clear();
//					for (CityItem cityItem : l) {
//						for (CountyItem countyitem : cityItem.getCountylist()) {
//							for (SubstationItem substationItem : countyitem.getSubstationlist()) {
//								if (city.equals(cityItem.getName()) && sbarea.equals(countyitem.getName())) {
//									substationItem.setName(cityItem.getName() + "->" + countyitem.getName() + "->"
//											+ substationItem.getName());
//									mSubstationList.add(substationItem);
//								}
//							}
//						}
//					}
//					break;
//				}
//			} catch (Exception e) {
//				L.e(e.toString());
//			}
//		} while (tryCount++ < 1);
////		et_station_name.setText("");
//		updateSubstationListview(tryCount < 1);
//	}
//
//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		if (mSvAlarmFilter.getVisibility() == View.VISIBLE) {
//			mSvAlarmFilter.setVisibility(View.GONE);
//			return;
//		} else {
//			super.onBackPressed();
//		}
//	}
//}
