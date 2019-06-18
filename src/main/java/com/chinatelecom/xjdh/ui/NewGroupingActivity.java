package com.chinatelecom.xjdh.ui;

import java.sql.SQLException;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemLongClick;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.db.DatabaseHelper;
import com.chinatelecom.xjdh.model.GroupingName;
import com.chinatelecom.xjdh.utils.L;
import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

@EActivity(R.layout.activity_new_grouping)
public class NewGroupingActivity extends Activity {

	@ViewById(R.id.lv_new_grouping)
	ListView lv_new_grouping;
	@ViewById(R.id.btn_new_grouping)
	Button btn_new_grouping;

	@Extra
	String stationName;// 局站名成
	private List<GroupingName> data;
	private newGroupingAdapter adapter;
	private ProgressDialog pDialog;
	@OrmLiteDao(helper = DatabaseHelper.class, model = GroupingName.class)
	Dao<GroupingName, Integer> sDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("上传数据中...");
	}

	@AfterViews
	void initData() {
		adapter = new newGroupingAdapter(this);
		getData();
		adapter.setData(data);
		lv_new_grouping.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	/**
	 * 从GroupingName表中获取数据
	 */
	private void getData() {
		// TODO Auto-generated method stub
		try {
			// select groupingName from GroupingName where stationName="乌鲁木齐"
			data.clear();
			data = sDao.queryBuilder().where().eq("stationName", stationName).query();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 新建分组
	 */
	@Click(R.id.btn_new_grouping)
	void newGroupingClicked() {
		GroupingNameActivity_.intent(this).stationName(stationName).start();
	}

	/**
	 * 长按删除数据
	 */
	@ItemLongClick(R.id.lv_new_grouping)
	void deleteDataClicked(final int positon) {
		Builder builder = new Builder(NewGroupingActivity.this);
		builder.setTitle("温馨提示");
		builder.setMessage("你确定删除数据？");
		builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});
		builder.setPositiveButton(" 是的", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				try {
					sDao.delete(data.get(positon));
					data.clear();
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

	public class newGroupingAdapter extends BaseAdapter {
		private Context mContext;
		private LayoutInflater inflater;
		private List<GroupingName> data;

		public newGroupingAdapter(Context Context) {
			super();
			this.mContext = Context;
			this.inflater = LayoutInflater.from(mContext);
		}

		public void setData(List<GroupingName> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data != null ? data.size() : 0;
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
				convertView = inflater.inflate(R.layout.new_grouping_adapter_pattern, null);
				horder = new ViewHolder();
				horder.tv_grouping_name = (TextView) convertView.findViewById(R.id.tv_grouping_name);
				horder.tv_grouping_name_value = (TextView) convertView.findViewById(R.id.tv_grouping_name_value);
				horder.btn_collect = (Button) convertView.findViewById(R.id.btn_collect);
				horder.btn_upload = (Button) convertView.findViewById(R.id.btn_upload);
				/**
				 * 采集
				 */
				horder.btn_collect.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						CollectActivity_.intent(mContext).newGrouping(data.get(position).getGroupingName())
								.stationName(stationName).start();
					}
				});
				/**
				 * 上传图片
				 */
				horder.btn_upload.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						pDialog.show();
						upLoadImag();
					}

					@Background
					private void upLoadImag() {
						// TODO Auto-generated method stub

					}
				});
				convertView.setTag(horder);
			} else {
				horder = (ViewHolder) convertView.getTag();
			}
			if (data != null) {
				horder.tv_grouping_name.setText("分组名称 :");
				horder.tv_grouping_name_value.setText(data.get(position).getGroupingName());
			}
			return convertView;
		}

		public class ViewHolder {
			TextView tv_grouping_name;// 分组名称
			TextView tv_grouping_name_value;// 分组名称直
			Button btn_collect;// 采集
			Button btn_upload;// 上传
		}
	}

	/**
	 * 刷新数据
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			data = sDao.queryBuilder().where().eq("stationName", stationName).query();
			adapter.setData(data);
			lv_new_grouping.setAdapter(adapter);
		} catch (SQLException e) {
			e.printStackTrace();
			L.e("----------" + e);
		}
	}

	@Click(R.id.img_btn_reback)
	void rebackClicked() {
		this.finish();
	}
}
