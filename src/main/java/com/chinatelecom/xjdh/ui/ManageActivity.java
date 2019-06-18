package com.chinatelecom.xjdh.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.fragment.DoingFragment_;
import com.chinatelecom.xjdh.fragment.DoneFragment_;
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
public class ManageActivity extends BaseActivity {
	@Extra
	int station_code;
	@ViewById
	MyViewPagerIndicators indicator;
	@ViewById
	ViewPager viewpager;
	DoingFragment_ doingFragment;
	DoneFragment_ doneFragment;
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
	private static final String[] TITLE = new String[] {"待验收", "已验收"};

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
				return (Fragment) doingFragment;
			} else if (position == 1) {
				// 工单详情
				return (Fragment) doneFragment;
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
		doingFragment = new DoingFragment_();
		doingFragment.SetData(station_code);
		doneFragment = new DoneFragment_();
		doneFragment.SetData(station_code);

		adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
		viewpager.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		indicator.setTabItemTitles(TITLE);
		indicator.setViewPager(viewpager, 0);
	}

}
