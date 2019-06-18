package com.chinatelecom.xjdh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.SubstationItem;
import com.chinatelecom.xjdh.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * @author peter
 * 
 */
public class SubstationListAdapter extends BaseAdapter {
	private List<SubstationItem> listItems = new ArrayList<>();
	private Context context;// 运行上下文
	private LayoutInflater inflater;// 视图容器
	

	public SubstationListAdapter(Context context) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	
	public void updateListView(List<SubstationItem> mSubstationList) {
		// TODO Auto-generated method stub
		L.d("saslkjml","mSubstationAdapter.upd");
		this.listItems = mSubstationList;
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return listItems.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListItemView listItemView = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.area_list_item, null);
			listItemView = new ListItemView();
			listItemView.num = (TextView) convertView.findViewById(R.id.tv_num);
			listItemView.roomName = (TextView) convertView.findViewById(R.id.tv_info);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		SubstationItem substationItem = listItems.get(position);
		L.d("00000000000000", substationItem.toString());
		listItemView.num.setText(String.valueOf(position +1));
		listItemView.roomName.setText(substationItem.getName());
		return convertView;
	}

	public void setListItems(List<SubstationItem> items) {
		this.listItems.addAll(items);
	}
	public void addLists(List<SubstationItem> items) {
		listItems.addAll(items); 
	    }
	
	
	class ListItemView { // 自定义控件集合
		 TextView num,roomName;
	}
	
}