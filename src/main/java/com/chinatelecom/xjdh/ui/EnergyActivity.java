package com.chinatelecom.xjdh.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.fragment.FirstLoadFragment_;
import com.chinatelecom.xjdh.fragment.ForeLoadFragment_;
import com.chinatelecom.xjdh.fragment.SecondLoadFragment_;
import com.chinatelecom.xjdh.fragment.ThreeLoadFragment_;
import com.chinatelecom.xjdh.view.MyViewPagerIndicator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

@EActivity(R.layout.activity_energy)
public class EnergyActivity extends BaseActivity{
	@ViewById
	MyViewPagerIndicator indicator;
	@ViewById
	ViewPager viewpager;
	
	@Extra 
	String model;
	FragmentPagerAdapter adapter;
	FirstLoadFragment_ oneFragment;
	SecondLoadFragment_ twoFragment;
	ThreeLoadFragment_ threeFragment;
	ForeLoadFragment_ foreFragment; 
	
	private static final String[] TITLE = new String[] { "第一路", "第二路","第三路","第四路"};
	
	class TabPageIndicatorAdapter extends FragmentPagerAdapter {
		public TabPageIndicatorAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getItemPosition(Object obj)
		{
			return POSITION_NONE;
		}
		
		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				// 待办事项
				return (Fragment) oneFragment;
			} 
			else if (position == 1) {
				// 工单详情
				return (Fragment) twoFragment;
			} else if (position == 2) {
				//派单记录
				return (Fragment) threeFragment;
			} else if (position == 3) {
				//工单事件
				return (Fragment) foreFragment; 
			}
			else {
				Fragment fragment = new Fragment();
				return fragment;
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLE[position % TITLE.length];
		}

		@Override
		public int getCount() {
			return TITLE.length;
		}
	}
	
	@AfterViews
	void initsClick() {
		
		oneFragment = new FirstLoadFragment_();
		oneFragment.setData(model);

		twoFragment = new SecondLoadFragment_();
		twoFragment.setData(model);

		threeFragment = new ThreeLoadFragment_();
		threeFragment.setData(model);
		
		foreFragment = new ForeLoadFragment_();
		foreFragment.setData(model);

		adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
		viewpager.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		indicator.setTabItemTitles(TITLE);
		indicator.setViewPager(viewpager, 0);

	}
}

