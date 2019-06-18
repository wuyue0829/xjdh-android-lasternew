package com.chinatelecom.xjdh.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.fragment.DoingStationFragment_;
import com.chinatelecom.xjdh.fragment.DoneStationFragment_;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.view.MyViewPagerIndicators;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
@EActivity(R.layout.manage_main)
public class EquipmentActivity extends BaseActivity {

	@Extra
	int type2;
	@ViewById
	MyViewPagerIndicators indicator;
	@ViewById
	ViewPager viewpager;
	DoingStationFragment_ pendingFragment;
	DoneStationFragment_ acceptedFragment;
	@RestService
	ApiRestClientInterface mApiClient;

	FragmentPagerAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("局站验收");
		String token = PreferenceUtils.getPrefString(this,PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
	}
	private static final String[] TITLE = new String[] {"验收", "编辑"};

	class TabPageIndicatorAdapter extends FragmentPagerAdapter {
		public TabPageIndicatorAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getItemPosition(Object obj) {
			return POSITION_NONE;
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				// 待办事项
				return (Fragment) pendingFragment;
			} else if (position == 1) {
				// 工单详情
				return (Fragment) acceptedFragment;
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
	public void ShowView() {
		pendingFragment = new DoingStationFragment_();
		pendingFragment.SetData(2);
		acceptedFragment = new DoneStationFragment_();

		adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
		viewpager.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		indicator.setTabItemTitles(TITLE);
		indicator.setViewPager(viewpager, 0);
	}
}