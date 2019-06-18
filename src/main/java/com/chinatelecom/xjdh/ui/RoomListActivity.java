package com.chinatelecom.xjdh.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.RoomItem;
import com.chinatelecom.xjdh.bean.SubstationItem;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.Contacts;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author peter
 * 
 */
@EActivity(R.layout.normal_list_views)
public class RoomListActivity extends BaseActivity {
	 private LatLng sLatLng;// 当前经纬度信息
	 private boolean isFirstLoc = true;
	    
	@ViewById(R.id.lv_items)
	ListView mLvRoom;
	
	private BaiduMap mBaidumap = null;
	private static final int MENU_FILTER_ID = Menu.FIRST;
//	@ViewById(R.id.tv_ref 
	@RestService
	ApiRestClientInterface mApiClient;
	@Extra("substation")
	SubstationItem substationItem;
	List<RoomItem> mRoomList = new ArrayList<>();
	SimpleAdapter mRoomAdapter;
	private LocationManager locationManager;// 位置管理类

	private String provider;// 位置提供器

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String token = PreferenceUtils.getPrefString(RoomListActivity.this, PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// 获取所有可用的位置提供器
		List<String> providerList = locationManager.getProviders(true);
		if (providerList.contains(LocationManager.GPS_PROVIDER)) {
			//优先使用gps
			provider = LocationManager.GPS_PROVIDER;
		} else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
			provider = LocationManager.NETWORK_PROVIDER;
		} else {
			// 没有可用的位置提供器
			Toast.makeText(RoomListActivity.this, "没有位置提供器可供使用", Toast.LENGTH_LONG)
					.show();
			return;
		}

		Location location = locationManager.getLastKnownLocation(provider);
		Log.i("wuyingjieLocation", "location=" + location + ",provider" + provider);
		if (location != null) {
			// 显示当前设备的位置信息
			String firstInfo = "第一次请求的信息";
			sLatLng = new LatLng(location.getLatitude(), location.getLongitude());
		} else {
			/*String info = "无法获得当前位置";
			Toast.makeText(this, info, 1).show();*/
			locationManager.requestLocationUpdates(provider, 10 * 1000, 1,
					locationListener);
		}

		// 更新当前位置
		locationManager.requestLocationUpdates(provider, 10 * 1000, 1,
				locationListener);
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_FILTER_ID:
			 final Dialog dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
			    View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_map, null);
			    Button gaode = (Button) inflate.findViewById(R.id.gaode);
			    Button baidu = (Button) inflate.findViewById(R.id.baidu);
			    Button cancel = (Button) inflate.findViewById(R.id.btn_cancel);
			    gaode.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						gaoDe();
						dialog.dismiss();
					}
				});
			    baidu.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						baidu();
						dialog.dismiss();
					}
				});
//		        share.setOnClickListener(new OnClickListener() {
	//	
//		        	@Override
//		        	public void onClick(View v) {
//		        		// TODO Auto-generated method stub
//		        		Intent intent = new Intent(RoomListActivity.this, RoutePlanDemo_.class);
//		        		intent.putExtra("longitude", substationItem.getLng());
//		        		intent.putExtra("latitude", substationItem.getLat());
//		        		intent.putExtra("adress", substationItem.getName());
//		        		startActivity(intent);
//		        		dialog.dismiss();
//		        	}
//		        });
		        cancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		        
		        
		        dialog.setContentView(inflate);
		        //获取当前Activity所在的窗体
		        Window dialogWindow = dialog.getWindow();
		        //设置Dialog从窗体底部弹出
		        dialogWindow.setGravity( Gravity.BOTTOM);
		        //获得窗体的属性
		        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		        lp.y = 20;//设置Dialog距离底部的距离
//		        将属性设置给窗体
		        dialogWindow.setAttributes(lp);
		        dialog.show();//显示对话框
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_FILTER_ID, 0, "筛选").setIcon(R.drawable.dh)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}
	

	@AfterViews
	void bindData() {
		setTitle("机房列表");
//		sub_name.setText(substationItem.getName());
		mRoomList.addAll(Arrays.asList(substationItem.getRoomlist()));
		List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
		int index = 1;
		for (RoomItem roomItem : mRoomList) {
			Map<String, Object> item = new HashMap<>();
			item.put("num", String.valueOf(index++));
			item.put("name", roomItem.getName());
			listItem.add(item);
		}
		
		mRoomAdapter = new SimpleAdapter(RoomListActivity.this, listItem, R.layout.area_list_item, new String[] { "num", "name" }, new int[] { R.id.tv_num, R.id.tv_info });
		mLvRoom.setAdapter(mRoomAdapter);
	}

	@ItemClick(R.id.lv_items)
	void onRoomItemClicked(int pos) {
		RoomDevListActivity_.intent(RoomListActivity.this).mRoomCode(mRoomList.get(pos).getId()).mRoomName(mRoomList.get(pos).getName()).start();
	}

	protected void onDestroy() {
		super.onDestroy();
		if (locationManager != null) {
			// 关闭程序时将监听器移除
			locationManager.removeUpdates(locationListener);
		}
		finish();

	};

	LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onLocationChanged(Location location) {
			// 设备位置发生改变时，执行这里的代码
			Log.i("wuyingjieLocation", "onLocationChanged" + location);
			String changeInfo = "隔10秒刷新的提示：\n 时间：" + sdf.format(new Date())
					+ ",\n当前的经度是：" + location.getLongitude() + ",\n 当前的纬度是："
					+ location.getLatitude();
			showLocation(location, changeInfo);
		}
	};

	/**
	 * 显示当前设备的位置信息
	 * 
	 * @param location
	 */
	private void showLocation(Location location, String changeInfo) {
		// TODO Auto-generated method stub
		String currentLocation = "当前的经度是：" + location.getLongitude() + ",\n"
				+ "当前的纬度是：" + location.getLatitude();
		sLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//		T.showLong(this, currentLocation);
//		positionText.setText(currentLocation);
//		T.showLong(this, changeInfo);
	}

	
	
	
	//调用外部高德地图方法

	private void gaoDe(){
	        if (Contacts.isAvilible(RoomListActivity.this, "com.autonavi.minimap")) {
	            try{  
	                Intent intent = Intent.getIntent("androidamap://navi?sourceApplication=慧医&poiname=我的目的地&lat="+substationItem.getLat()+"&lon="+substationItem.getLng()+"&dev=0");  
	                RoomListActivity.this.startActivity(intent);   
	            } catch (URISyntaxException e)  
	            {e.printStackTrace(); } 
	        }else{
	            Toast.makeText(RoomListActivity.this, "您尚未安装高德地图", Toast.LENGTH_LONG).show();
	            Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");  
	            Intent intent = new Intent(Intent.ACTION_VIEW, uri);   
	            RoomListActivity.this.startActivity(intent);
	        }

	    }

	    private void baidu(){
	        //起点经纬度
	    	
	    	LatLng eLatLng=new LatLng(substationItem.getLat(),substationItem.getLng());
	        RouteParaOption para = new RouteParaOption();
	        para.startName("从这里开始");
	        para.startPoint(sLatLng);
	        para.endName(substationItem.getName());//起点位置
	        para.endPoint(eLatLng);

	        try {
	            BaiduMapRoutePlan.openBaiduMapDrivingRoute(para, RoomListActivity.this);
//	            BaiduMapNavigation.openBaiduMapNavi(para, RoomListActivity.this);
	        } catch (BaiduMapAppNotSupportNaviException e) {
	            e.printStackTrace();  
	            AlertDialog.Builder builder = new AlertDialog.Builder(RoomListActivity.this);  
	            builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");  
	            builder.setTitle("提示");  

	            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
	                @Override  
	                public void onClick(DialogInterface dialog, int which) {  
	                    dialog.dismiss();  
	                    OpenClientUtil.getLatestBaiduMapApp(RoomListActivity.this);
	                    //BaiduMapNavigation.
	                }  
	            });  

	            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
	                @Override  
	                public void onClick(DialogInterface dialog, int which) {  
	                    dialog.dismiss();  
	                }  
	            });  

	            builder.create().show();  
	        }  
	    }
	    
	    public class MyLocationListener implements BDLocationListener {
	    	 
	        public void onReceiveLocation(BDLocation location) {
	        	sLatLng= new LatLng(location.getLatitude(), location.getLongitude());
	        }
	    }
}
