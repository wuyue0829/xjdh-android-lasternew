package com.chinatelecom.xjdh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.RtspUrl;
import com.chinatelecom.xjdh.ui.SurveillanceActivity_;
import com.chinatelecom.xjdh.utils.L;

import java.util.ArrayList;
import java.util.List;

public class RstpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private Context context;// 运行上下文
	private LayoutInflater inflater;
	public List<RtspUrl> jsonList = new ArrayList<RtspUrl>();

	// public List<String> pathList = new ArrayList<>();
	public RstpAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		// this.mVideoUrl = mVideoUrl;
		inflater = LayoutInflater.from(context);
	}

	public void setItemList(List<RtspUrl> pathList) {
		this.jsonList.clear();
		this.jsonList.addAll(pathList);
		this.notifyDataSetChanged();
	}
	public void clearList(){
		this.jsonList.clear();
		this.notifyDataSetChanged();
	}
	public RtspUrl getItem(int position) {
		// TODO Auto-generated method stub
		return jsonList.get(position);
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return jsonList.size();
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
		// TODO Auto-generated method stub
		ItemViewHolder newholder = (ItemViewHolder) holder;
		L.d("/////////" + jsonList.get(position).toString());
		newholder.tvType.setText(jsonList.get(position).getName());
		newholder.num.setText(String.valueOf(position +1));

	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.area_list_item, parent, false);
		ItemViewHolder mItemViewHolder = new ItemViewHolder(view);
		return mItemViewHolder;
	}

	class ItemViewHolder extends RecyclerView.ViewHolder {
		private TextView tvType,num;

		public ItemViewHolder(View itemView) {
			super(itemView);
			tvType = (TextView) itemView.findViewById(R.id.tv_info);
			num = (TextView) itemView.findViewById(R.id.tv_num);
			itemView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SurveillanceActivity_.intent(context).URL(jsonList.get(getPosition()).getUrl()).start();
				}
			});
		}
	}
}