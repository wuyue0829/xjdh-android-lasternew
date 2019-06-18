package com.chinatelecom.xjdh.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.adapter.ModeAdapter;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;

import android.os.Bundle;
import android.widget.ListView;
@EActivity(R.layout.activity_download_station)
public class ModeActivity extends BaseActivity {
	
	@ViewById(R.id.lv_stationName_grouping)
	ListView lv_stationName_grouping;
	@RestService
	ApiRestClientInterface mApiClient;
	private ModeAdapter adpter;
	private static final String[] TITLE = new String[] {"施工队拍照","工艺验收", "设备验收","新建故障","故障处理"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("局站验收");
		String token = PreferenceUtils.getPrefString(this,PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
	}
	
	@AfterViews
	void initData(){
		adpter = new ModeAdapter(this, TITLE);
		lv_stationName_grouping.setAdapter(adpter);
	}
	
	@ItemClick(R.id.lv_stationName_grouping)
	void showUI(int pos){
		if (pos == 0) {
			SecurityActivity_.intent(this).start();
		}else if (pos == 1) {
			ConstructionActivity_.intent(this).type1(1).start();
		}else if(pos == 2){
			EquipmentActivity_.intent(this).type2(2).start();
		}else if(pos == 3){
			AllStationActivity_.intent(this).start();
//			NewFaultActivity_.intent(this).start();
		}else if(pos == 4){
			OverhaulActivity_.intent(this).start();
		}
			
	}

}
