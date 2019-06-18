package com.chinatelecom.xjdh.adapter;

import java.util.ArrayList;
import java.util.List;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.StationList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class SearchAdapter extends BaseAdapter implements Filterable{
	private List<StationList> listItems = new ArrayList<>();
	private List<StationList> lists= new ArrayList<>();
	private Context mContext;// 运行上下文
	private ArrayFilter mFilter;
	private LayoutInflater inflater;// 视图容器
	
	public SearchAdapter(Context context) {
		super();
		this.mContext = context;
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<StationList> data) {
		this.listItems = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.station_item, null);
			holder = new ViewHolder();
			holder.tv_station_name = (TextView) convertView.findViewById(R.id.tv_show_stationName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_station_name.setText(lists.get(position).getName());
		return convertView;
	}
	public class ViewHolder {
		TextView tv_station_name;// 局站名称
	}
	
	private class ArrayFilter extends Filter {

	    @Override
	    protected FilterResults performFiltering(CharSequence constraint) {
	        FilterResults results = new FilterResults();
	        if (listItems == null) {
	        	listItems = new ArrayList<StationList>(lists);
	        }
	        //如果没有过滤条件则不过滤
	        if (constraint == null || constraint.length() == 0) {
	            results.values = listItems;
	            results.count = listItems.size();
	        } else {
	            List<StationList> retList = new ArrayList<StationList>();
	            //过滤条件
	            String str = constraint.toString().toLowerCase();
	            //循环变量数据源，如果有属性满足过滤条件，则添加到result中
	            for (StationList station : listItems) {
	                if ( station.getName().contains(str)) {
	                    retList.add(station);
	                }
	            }
	            results.values = retList;
	            results.count = retList.size();
	        }
	        return results;
	    }

	    //在这里返回过滤结果
	    @Override
	    protected void publishResults(CharSequence constraint,
	            FilterResults results) {
//	      notifyDataSetInvalidated()，会重绘控件（还原到初始状态）
//	      notifyDataSetChanged()，重绘当前可见区域
	        lists = (List<StationList>) results.values;
	        if(results.count>0){
	            notifyDataSetChanged();
	        }else{
	            notifyDataSetInvalidated();
	        }
	    }

	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		  if(mFilter==null){
			  mFilter = new ArrayFilter();
	        }
	        return mFilter;
	}

}
