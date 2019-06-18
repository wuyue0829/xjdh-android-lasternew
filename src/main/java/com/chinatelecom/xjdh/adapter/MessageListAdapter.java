package com.chinatelecom.xjdh.adapter;

import java.util.List;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.MessageItem;
import com.chinatelecom.xjdh.utils.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author peter
 * 
 */
public class MessageListAdapter extends BaseAdapter {
	private List<MessageItem> listItems;
	private Context context;// 运行上下文
	private LayoutInflater listContainer;// 视图容器

	static class ListItemView { // 自定义控件集合
		public TextView title;
		public TextView date;
		public TextView send_by;
	}

	public MessageListAdapter(Context context) {
		super();
	}

	public MessageListAdapter(Context context, List<MessageItem> listItems) {
		super();
		this.listItems = listItems;
		this.context = context;
		this.listContainer = LayoutInflater.from(context);
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
			convertView = listContainer.inflate(R.layout.message_list_item, null);
			listItemView = new ListItemView();
			listItemView.title = (TextView) convertView.findViewById(R.id.tv_mesg_content);
			listItemView.date = (TextView) convertView.findViewById(R.id.tv_mesg_added_datetime);
			listItemView.send_by = (TextView) convertView.findViewById(R.id.tv_mesg_send_by);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		MessageItem mesgItem = listItems.get(position);
		listItemView.title.setText(StringUtils.cutArticle(mesgItem.getTitle(), 40));
		listItemView.date.setText(StringUtils.friendly_time(mesgItem.getAdded_datetime()));
		listItemView.send_by.setText(mesgItem.getSend_by());
		return convertView;
	}

	public void setListItems(List<MessageItem> items) {
		this.listItems = items;
	}
}