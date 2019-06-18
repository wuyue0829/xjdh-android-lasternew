package com.chinatelecom.xjdh.ui;

import java.sql.SQLException;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ItemLongClick;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;

import com.baidu.location.BDLocation;
import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.db.DatabaseHelper;
import com.chinatelecom.xjdh.model.FileNameGPSTable;
import com.chinatelecom.xjdh.model.StationTable;
import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

@EActivity(R.layout.activity_station_collect)
public class StationCollectActivity extends Activity {

	private StationAdapter adapter;
	private List<StationTable> data;
	private ProgressDialog pDialog;
	@ViewById(R.id.lv_station_list)
	ListView lv_station_list;
	@Extra
	BDLocation location;
	@OrmLiteDao(helper = DatabaseHelper.class, model = StationTable.class)
	Dao<StationTable, Integer> sDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("上传数据中...");
		try {
			sDaoGps.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@OrmLiteDao(helper = DatabaseHelper.class, model = FileNameGPSTable.class)
	Dao<FileNameGPSTable, Integer> sDaoGps;

	@AfterViews
	void initData() {
		adapter = new StationAdapter(this);
		getData();
		adapter.setData(data);
		lv_station_list.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	/**
	 * 从StationTable中获取数据
	 */
	private void getData() {
		try {
			data = sDao.queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 设备列表
	 */
	@ItemClick(R.id.lv_station_list)
	void showItemClicked(int position) {
		// Toast.makeText(this, "选择第" + position + "设备", 0).show();
		NewGroupingActivity_.intent(this).stationName(data.get(position).getStationName()).start();
	}

	/**
	 * 删除数据
	 */
	@ItemLongClick(R.id.lv_station_list)
	void deleteClicked(final int position) {
		Builder builder = new Builder(StationCollectActivity.this);
		builder.setTitle("温馨提示");
		builder.setMessage("你确定删除数据？");
		builder.setNeutralButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});
		builder.setPositiveButton("是的", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				try {
					sDao.delete(data.get(position));
					data = sDao.queryForAll();
					adapter.setData(data);
					adapter.notifyDataSetChanged();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		builder.create().show();
	}

	/**
	 * 添加局站
	 */
	@Click(R.id.btn_add_station)
	void myAddStationClicked() {
		AppendStationActivity_.intent(StationCollectActivity.this).start();
	}

	@Click(R.id.img_btn_reback)
	void rebackClicked() {
		this.finish();
	}

	/**
	 * 刷新数据
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			// data.clear();
			data = sDao.queryForAll();
			adapter.setData(data);
			lv_station_list.setAdapter(adapter);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class StationAdapter extends BaseAdapter {
		private Context mContext;
		private LayoutInflater inflater;
		private List<StationTable> data;

		public StationAdapter(Context Context) {
			super();
			this.mContext = Context;
			inflater = LayoutInflater.from(mContext);// 得到布局填充器对象
		}

		public void setData(List<StationTable> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data != null ? data.size() : 0;

			// return 3;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder horder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.station_adapter_pattern, null);
				horder = new ViewHolder();
				horder.tv_station_name = (TextView) convertView.findViewById(R.id.tv_station_name);
				horder.tv_station_name_value = (TextView) convertView.findViewById(R.id.tv_station_name_value);
				horder.btn_collect = (Button) convertView.findViewById(R.id.btn_collect);
				horder.btn_upload = (Button) convertView.findViewById(R.id.btn_upload);
				/*	*//**
						 * 采集
						 */
				/*
				 * horder.btn_collect.setOnClickListener(new OnClickListener() {
				 * 
				 * @Override public void onClick(View v) {
				 * CollectActivity_.intent(mContext).stationName(data.get(
				 * position).getStationName()).start(); } });
				 * 
				 *//**
					 * 上传数据中
					 */
				/*
				 * horder.btn_upload.setOnClickListener(new OnClickListener() {
				 * 
				 * @Override public void onClick(View v) { pDialog.show();
				 * DoPictureUpload(position,
				 * fileNameGPSTables.get(position).getLongitude(),
				 * fileNameGPSTables.get(position).getLatitude(),
				 * fileNameGPSTables.get(position).getStationName()); }
				 * 
				 *//**
					 * 上传图片
					 *//*
					 * @Background private void DoPictureUpload(int position,
					 * String longitude, String latitude, String stationName) {
					 * // TODO Auto-generated method stub L.e("经度：" +
					 * fileNameGPSTables.get(position).getLongitude());
					 * L.e("维度：" +
					 * fileNameGPSTables.get(position).getLatitude()); L.e("维度："
					 * + fileNameGPSTables.get(position).getStationName()); }
					 * });
					 */
				convertView.setTag(horder);
			} else {
				horder = (ViewHolder) convertView.getTag();
			}
			horder.tv_station_name.setText("局站名称 :");
			horder.tv_station_name_value.setText(data.get(position).getStationName().toString());
			return convertView;
		}

		public class ViewHolder {
			TextView tv_station_name;// 局站名称
			TextView tv_station_name_value;// 局站名称直
			Button btn_collect;// 采集
			Button btn_upload;// 上传
		}

	}
}
