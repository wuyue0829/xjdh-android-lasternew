package com.chinatelecom.xjdh.adapter;

import java.util.List;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.CheckCityList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CityStationAdapter extends BaseAdapter {
	private Context con;
	private LayoutInflater inflater;
	private List<CheckCityList> data;
	public CityStationAdapter(Context con) {
		super();
		this.con = con;
		inflater = LayoutInflater.from(con);
		// TODO Auto-generated constructor stub
	}
	public void setData(List<CheckCityList> data) {
		this.data=data;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.station_stay_view, null);
			holder = new ViewHolder();
			holder.question = (TextView)convertView.findViewById(R.id.question);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.question.setText(data.get(position).getCity_name());
		return convertView;
	}

	class ViewHolder {
		TextView question;
	}

}
