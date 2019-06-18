package com.chinatelecom.xjdh.adapter;

import java.util.List;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.UserDetailListItem;
import com.chinatelecom.xjdh.utils.URLs;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author peter
 * 
 */
public class UserDetailListAdapter extends BaseAdapter {
	private List<UserDetailListItem> listItems;
	private Context context;// 运行上下文
	private LayoutInflater listContainer;// 视图容器
	DisplayImageOptions options;

	static class ListItemView { // 自定义控件集合
		public TextView columnText;
		public TextView columnTextVal;
		public ImageView columnImgVal;
	}

	public UserDetailListAdapter(Context context) {
		super();
	}

	public UserDetailListAdapter(Context context, List<UserDetailListItem> listItems) {
		super();
		this.listItems = listItems;
		this.context = context;
		this.listContainer = LayoutInflater.from(context);
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.sc_publish_spinner).showImageForEmptyUri(R.drawable.login_username)
				.showImageOnFail(R.drawable.login_username).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
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
			convertView = listContainer.inflate(R.layout.user_detail_item, null);
			listItemView = new ListItemView();
			listItemView.columnText = (TextView) convertView.findViewById(R.id.user_detail_column_text);
			listItemView.columnTextVal = (TextView) convertView.findViewById(R.id.user_detail_column_val);
			listItemView.columnImgVal = (ImageView) convertView.findViewById(R.id.user_detail_column_image);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		UserDetailListItem item = listItems.get(position);
		listItemView.columnText.setText(item.getColumnText());
		if (item.getType() == 1) {
			listItemView.columnTextVal.setVisibility(View.VISIBLE);
			listItemView.columnImgVal.setVisibility(View.GONE);
			listItemView.columnTextVal.setText(item.getColumnVal());
		} else {
			listItemView.columnTextVal.setVisibility(View.GONE);
			listItemView.columnImgVal.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(URLs.USER_IMAGE_URL + "/" + item.getColumnVal(), listItemView.columnImgVal, options);
		}
		return convertView;
	}

	public void setListItems(List<UserDetailListItem> items) {
		this.listItems = items;
	}
}