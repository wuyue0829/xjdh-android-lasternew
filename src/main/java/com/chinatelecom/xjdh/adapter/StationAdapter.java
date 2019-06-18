package com.chinatelecom.xjdh.adapter;

import java.util.List;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.model.StationTable;
import com.chinatelecom.xjdh.ui.CollectActivity_;
//import com.chinatelecom.xjdh.ui.UpLoadActivity_;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author peter
 * 
 */
public class StationAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater inflater;
	private List<StationTable> data;

	public StationAdapter(Context mContext) {
		super();
		this.mContext = mContext;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder horder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.station_adapter_pattern, null);
			horder = new ViewHolder();
			horder.tv_station_name = (TextView) convertView.findViewById(R.id.tv_station_name);
			horder.tv_station_name_value = (TextView) convertView.findViewById(R.id.tv_station_name_value);
			horder.btn_collect = (Button) convertView.findViewById(R.id.btn_collect);
			horder.btn_upload = (Button) convertView.findViewById(R.id.btn_upload);
			/**
			 * 采集
			 */
			horder.btn_collect.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CollectActivity_.intent(mContext).start();

				}
			});
			
			/**
			 * 上传数据中
			 */
			horder.btn_upload.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//UpLoadActivity_.intent(mContext).start();
				}
			});
			convertView.setTag(horder);
		} else {
			horder = (ViewHolder) convertView.getTag();
		}
		horder.tv_station_name.setText("局站名称:" + data.get(position).getId());
		horder.tv_station_name_value.setText(data.get(position).getStationName().toString());
		return convertView;
	}

	class ViewHolder {
		TextView tv_station_name;// 局站名称
		TextView tv_station_name_value;// 局站名称直
		Button btn_collect;// 采集
		Button btn_upload;// 上传
	}

}