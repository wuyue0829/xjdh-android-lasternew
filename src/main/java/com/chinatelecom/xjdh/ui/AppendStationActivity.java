package com.chinatelecom.xjdh.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.CityItem;
import com.chinatelecom.xjdh.db.DatabaseHelper;
import com.chinatelecom.xjdh.model.StationTable;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.FileUtils;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

@EActivity(R.layout.activity_append_station)
public class AppendStationActivity extends Activity {
	private StationTable stationTable;
	private ArrayAdapter<String> mCityAdapter;// 局站名称适配器
	private List<CityItem> cityList = new ArrayList<CityItem>();
	private String city = "all";
	private String station = "all";
	private String stationNames = "all";
	private String stationName;// 局站名称

	@RestService
	ApiRestClientInterface mApiClient;
	@ViewById(R.id.et_append_station)
	EditText et_append_station;
	@ViewById(R.id.station_name_spinner) // 局站名称
	Spinner station_name_spinner;
	@OrmLiteDao(helper = DatabaseHelper.class, model = StationTable.class)
	Dao<StationTable, Integer> sDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
	}

	@AfterViews
	void initData() {
		stationTable = new StationTable();
		getApiData();
		spinnerShowData();
	}

	@Background
	public void getApiData() {
		ApiResponse apiResp = mApiClient.getAreaData(city, station,stationNames);
		L.v("第一个。。。。。。。。。。。。。" + apiResp.getData());
		if (apiResp.getRet() == 0) {
			FileUtils.setToData(this, SharedConst.FILE_AREA_JSON, apiResp.getData().getBytes());
		}

	}

	private void spinnerShowData() {
		ObjectMapper mapper = new ObjectMapper();
		String cityData = new String(FileUtils.getFileData(this, SharedConst.FILE_AREA_JSON));
		try {
			List<CityItem> l = mapper.readValue(cityData, new TypeReference<List<CityItem>>() {
			});
			cityList.clear();
			cityList.addAll(l);
			List<String> cities = new ArrayList<>();
			cities.add("全网");
			for (CityItem cityItem : cityList) {
				cities.add(cityItem.getName());
			}
			station_name_spinner.setPrompt("请选择所属局站");
			mCityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
			mCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			station_name_spinner.setAdapter(mCityAdapter);
		} catch (Exception e) {
			L.e(e.toString());
		}
		station_name_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				stationName = station_name_spinner.getSelectedItem().toString();// 得到station_name_spinner的内容
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
	 * 保存数据
	 */
	@Click(R.id.btn_save)
	void saveData() {
		judge();
	}

	private void judge() {
	/*	if (et_append_station.getText().toString() == null || et_append_station.getText().toString().equals("")) {
			et_append_station.setError("局站名称不能为空");
			return;
		}*/
		getData();
		Toast.makeText(AppendStationActivity.this, "保存成功" + stationTable.getStationName(), 0).show();
		this.finish();
	}

	private void getData() {

		try {
			stationTable.setStationName(et_append_station.getText().toString());
			stationTable.setStationName(stationName);
			sDao.createOrUpdate(stationTable);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
