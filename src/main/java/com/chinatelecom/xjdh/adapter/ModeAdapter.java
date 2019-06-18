package com.chinatelecom.xjdh.adapter;

import com.chinatelecom.xjdh.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ModeAdapter extends BaseAdapter{
	
	private Context con;
	private String[] titles;
	private LayoutInflater inflater;
	
	public ModeAdapter(Context con,String[] titles) {
		super();
		// TODO Auto-generated constructor stub
		this.con=con;
		this.titles=titles;
		inflater = LayoutInflater.from(con);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titles.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return titles[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.mode_voiw, null);
			holder = new ViewHolder();
			holder.mode_name = (TextView) convertView.findViewById(R.id.mode_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mode_name.setText(titles[position]);
		return convertView;
	}
	
	class ViewHolder{
		TextView mode_name;
	}

}
