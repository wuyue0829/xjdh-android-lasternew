package com.chinatelecom.xjdh.adapter;

import java.util.List;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.Technology;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DetailAdapter extends BaseAdapter {
	
	private Context con;
	private LayoutInflater inflater;
	private List<Technology> data;
	public DetailAdapter(Context con) {
		super();
		this.con = con;
		inflater = LayoutInflater.from(con);
	}
	
	public void setData(List<Technology> data) {
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
		Holder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.question_item, null);
			holder = new Holder();
			holder.title = (TextView) convertView
					.findViewById(R.id.question);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.title.setText(data.get(position).getContent());
		return convertView;
	}
	class Holder {
		TextView title;
	}
}

 

