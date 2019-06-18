package com.chinatelecom.xjdh.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.CityItem;
import com.chinatelecom.xjdh.bean.CountyItem;
import com.chinatelecom.xjdh.bean.RoomItem;
import com.chinatelecom.xjdh.bean.SubstationItem;
import com.chinatelecom.xjdh.ui.AlarmActivity;
import com.chinatelecom.xjdh.ui.AllStationActivity_;
import com.chinatelecom.xjdh.utils.FileUtils;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.SharedConst;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
@EFragment(R.layout.new_data_view)
public class AllStationFragment extends Fragment{
	
	private HashMap<String, String> alarmLevelList = new LinkedHashMap<String, String>();
	private List<CityItem> cityList = new ArrayList<CityItem>(0);
	@ViewById
	Spinner spCity,spPren,spSub,spRoom;
	
	private int selCity = 0;
	private int selCounty = 0;
	private int selSubstation = 0;
	private int selRoom = 0;
	
	private ArrayAdapter<String> mCityAdapter;
	private ArrayAdapter<String> mCountyAdapter;
	private ArrayAdapter<String> mSubstationtyAdapter;
	private ArrayAdapter<String> mRoomAdapter;
	
	@AfterViews
	void showView(){
		ObjectMapper mapper = new ObjectMapper();
		if (cityList.size() == 0) {
			String cityData = new String(FileUtils.getFileData(getActivity(), SharedConst.FILE_AREA_JSON));
			try {
				List<CityItem> l = mapper.readValue(cityData, new TypeReference<List<CityItem>>() {
				});
				cityList.addAll(l);
				List<String> cities = new ArrayList<>();
				cities.add("全网");
				for (CityItem cityItem : cityList) {
					cities.add(cityItem.getName());
				}
				
				ArrayAdapter<String> stationAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,cities);
				stationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spCity.setAdapter(stationAdapter);
				
			} catch (Exception e) {
				L.e(e.toString());
			}
		}
		
		spCity.setOnItemSelectedListener(new OnItemSelectedListener() {

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
				mCountyAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, counties);
				mCountyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spPren.setAdapter(mCountyAdapter);
				selCounty = 0;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}
		});

		spPren.setOnItemSelectedListener(new OnItemSelectedListener() {

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
				mSubstationtyAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
						substations);
				mSubstationtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spSub.setAdapter(mSubstationtyAdapter);
				selSubstation = 0;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}
		});
		spSub.setOnItemSelectedListener(new OnItemSelectedListener() {

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
				mRoomAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, rooms);
				mRoomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spRoom.setAdapter(mRoomAdapter);
				selRoom = 0;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}
		});
		spRoom.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View v, int position, long arg3) {
				selRoom = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}
		});

	}
	
	@Click(R.id.btn_search)
	void btn_searchClick(){
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
		AllStationActivity_ activity = (AllStationActivity_)getActivity();
		activity.onSearchClicked(citycode, countycode, substationId, roomId);
	}
}
