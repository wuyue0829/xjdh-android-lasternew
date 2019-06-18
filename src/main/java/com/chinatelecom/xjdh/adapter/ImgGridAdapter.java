package com.chinatelecom.xjdh.adapter;

import java.util.ArrayList;

import com.chinatelecom.xjdh.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImgGridAdapter extends BaseAdapter {
	
		private LayoutInflater inflater;
		ArrayList<String> mImageUrlList = new ArrayList<String>();
		private Context con;
		public ImgGridAdapter(Context con,ArrayList<String> mImageUrlList) {
			this.mImageUrlList = mImageUrlList;
			inflater = LayoutInflater.from(con);
		}

		@Override
		public int getCount() {
			return mImageUrlList.size();
		}

		@Override
		public Object getItem(int position) {
			return mImageUrlList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder=null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.lv_item_img,
						null);
				holder = new ViewHolder();
				holder.img = (ImageView) convertView
						.findViewById(R.id.lv_item_iv_img);
//				LayoutParams p = holder.img.getLayoutParams();
//				p.width = p.height = mGetScreenWidth() / 3 - 20;
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			Picasso.with(con).load(mImageUrlList.get(position)).into(holder.img);
//			Picasso.with(con).load(URLs.HTTP+URLs.HOST+imgPath[position]).into(holder.img);
			return convertView;
		}
		private class ViewHolder {
			ImageView img;
		}
		
//		private int mGetScreenWidth() {
//			DisplayMetrics dm = new DisplayMetrics();
//			((Activity) con).getWindowManager().getDefaultDisplay().getMetrics(dm);
//			return dm.widthPixels;
//		}
	}
