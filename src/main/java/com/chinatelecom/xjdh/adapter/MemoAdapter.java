package com.chinatelecom.xjdh.adapter;

import java.util.List;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.Country;
import com.chinatelecom.xjdh.bean.MemoList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MemoAdapter extends BaseAdapter {
	private Context con;
	private LayoutInflater inflater;
	private List<MemoList> data;
	public MemoAdapter(Context con) {
		super();
		this.con = con;
		inflater = LayoutInflater.from(con);
		// TODO Auto-generated constructor stub
	}
	public void setData(List<MemoList> data) {
		this.data = data;
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
			convertView = inflater.inflate(R.layout.memo_view, null);
			holder = new ViewHolder();
			holder.room = (TextView)convertView.findViewById(R.id.room);
			holder.device = (TextView)convertView.findViewById(R.id.device);
			holder.time = (TextView)convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
			holder.room.setText(data.get(position).getRoomName());
			holder.device.setText(data.get(position).getDeviceName());
			holder.time.setText(data.get(position).getRecord());
		
		return convertView;
	}

	class ViewHolder {
		TextView room,device,time;
	}

}
