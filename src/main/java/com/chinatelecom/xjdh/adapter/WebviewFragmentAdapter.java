package com.chinatelecom.xjdh.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chinatelecom.xjdh.bean.WebviewItem;
import com.chinatelecom.xjdh.fragment.WebviewFragment_;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * @author peter
 * 
 */
public class WebviewFragmentAdapter extends FragmentStatePagerAdapter {
	List<WebviewItem> listItem = new ArrayList<WebviewItem>();
	Context ctx;
	Map<Integer, Fragment> mapFragment = new HashMap<Integer, Fragment>();

	public WebviewFragmentAdapter(FragmentManager fm, Context ctx, List<WebviewItem> listItem) {
		super(fm);
		this.listItem = listItem;
		this.ctx = ctx;
	}

	@Override
	public Fragment getItem(int position) {
		String url = listItem.get(position).url;
		if (mapFragment.containsKey(position))
			return mapFragment.get(position);
		else {
			Fragment f = WebviewFragment_.newInstance(url);
			mapFragment.put(position, f);
			return f;
		}

	}

	@Override
	public CharSequence getPageTitle(int position) {
		return listItem.get(position).title;
	}

	@Override
	public int getCount() {
		return listItem.size();
	}

}
