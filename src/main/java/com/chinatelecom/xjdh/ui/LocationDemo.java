package com.chinatelecom.xjdh.ui;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.chinatelecom.xjdh.R;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * @author peter
 * 
 */
@EActivity(R.layout.activity_location)
public class LocationDemo extends BaseActivity {
	private List<BDLocation> list = new ArrayList<BDLocation>();
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	@ViewById(R.id.bmapView)
	MapView mMapView;
	BaiduMap mBaiduMap;

	// UI相关
	@ViewById(R.id.btn_location)
	ImageButton requestLocButton;
	boolean isFirstLoc = true;// 是否首次定位

	/**
	 * 
	 */
	@AfterViews
	public void bindData() {
		setTitle("地图");
		mCurrentMode = LocationMode.NORMAL;
		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				switch (mCurrentMode) {
				case NORMAL:
					requestLocButton.setImageResource(R.drawable.main_icon_follow);
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap.setMyLocationConfigeration(
							new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
					break;
				case COMPASS:
					requestLocButton.setImageResource(R.drawable.main_icon_location);
					mCurrentMode = LocationMode.NORMAL;
					isFirstLoc = true;
					mBaiduMap.setMyLocationConfigeration(
							new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
					MapStatus.Builder mapStatusBuilder = new MapStatus.Builder();
					mapStatusBuilder.overlook(0);
					mapStatusBuilder.rotate(0);
					mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatusBuilder.build()));
					break;
				case FOLLOWING:
					requestLocButton.setImageResource(R.drawable.main_icon_compass);
					mCurrentMode = LocationMode.COMPASS;
					mBaiduMap.setMyLocationConfigeration(
							new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
					break;
				}
			}
		};
		requestLocButton.setOnClickListener(btnClickListener);

		// 地图初始化
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		mMapView.showZoomControls(false);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;

			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);

			if (isFirstLoc) {
				isFirstLoc = false;
				MapStatus.Builder mapStatusBuilder = new MapStatus.Builder();
				mapStatusBuilder.zoom(17f);
				LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
				mapStatusBuilder.target(ll);
				mapStatusBuilder.overlook(0);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatusBuilder.build()));
				list.add(location);
			}

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode);
		// inte
	}

}
