package com.chinatelecom.xjdh.adapter;

import java.util.List;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.adapter.AlarmListAdapter.ListItemView;
import com.chinatelecom.xjdh.bean.AlarmItem;
import com.chinatelecom.xjdh.utils.StringUtils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PreAlarmListAdapter extends BaseAdapter {
	private List<AlarmItem> listItems;
	private Context context;// 运行上下文
	private LayoutInflater listContainer;// 视图容器
	
	
	public PreAlarmListAdapter(List<AlarmItem> listItems, Context context) {
		super();
		this.listItems = listItems;
		this.context = context;
		this.listContainer = LayoutInflater.from(context);
	}

	public void setListItems(List<AlarmItem> listItems) {
		this.listItems = listItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return  listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListItemView listItemView = null;
		if (convertView == null) {
			convertView = listContainer.inflate(R.layout.alarm_list_item, null);
			listItemView = new ListItemView();
			listItemView.content = (TextView) convertView.findViewById(R.id.tv_list_item_content);
			listItemView.date = (TextView) convertView.findViewById(R.id.tv_list_item_date);
			listItemView.area = (TextView) convertView.findViewById(R.id.tv_list_item_area);
			listItemView.status = (TextView) convertView.findViewById(R.id.tv_list_item_status);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		AlarmItem alarmItem = listItems.get(position);
		listItemView.date.setText(StringUtils.friendly_time(alarmItem.getAdded_datetime()));
		listItemView.content.setText(StringUtils.cutArticle(alarmItem.getSubject(), 40));
		listItemView.area.setText(alarmItem.getCity() + "-" + alarmItem.getCounty() + "-" + alarmItem.getSubstation_name());
		if (alarmItem.getStatus().equalsIgnoreCase("unresolved")) {
			listItemView.status.setText("未处理");
			listItemView.status.setBackgroundColor(Color.RED);
		} else if (alarmItem.getStatus().equalsIgnoreCase("sloving")) {
			listItemView.status.setText("处理中");
			listItemView.status.setBackgroundColor(Color.YELLOW);
		} else if (alarmItem.getStatus().equalsIgnoreCase("solved")) {
			listItemView.status.setText("处理完成");
			listItemView.status.setBackgroundColor(Color.GREEN);
		}
		Drawable drawableLevel = null;
		switch (alarmItem.getLevel()) {
		case 1:
		default:
			drawableLevel = context.getResources().getDrawable(R.drawable.icon_level1);
			break;
		case 2:
			drawableLevel = context.getResources().getDrawable(R.drawable.icon_level2);
			break;
		case 3:
			drawableLevel = context.getResources().getDrawable(R.drawable.icon_level3);
			break;
		case 4:
			drawableLevel = context.getResources().getDrawable(R.drawable.icon_level4);
			break;
		}
		drawableLevel.setBounds(0, 0, drawableLevel.getMinimumWidth(), drawableLevel.getMinimumHeight());
		listItemView.area.setCompoundDrawables(null, null, drawableLevel, null);
		return convertView;
	}


}
