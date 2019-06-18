package com.chinatelecom.xjdh.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> data;
	public MyFragmentPagerAdapter(FragmentManager fm,ArrayList<Fragment> data) {
		super(fm);
		this.data=data;
		// TODO Auto-generated constructor stub
	}
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

}
