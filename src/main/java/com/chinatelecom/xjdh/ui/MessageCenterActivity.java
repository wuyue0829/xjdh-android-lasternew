package com.chinatelecom.xjdh.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.adapter.MessageCenterPagerAdapter;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

/**
 * @author peter
 * 
 */
@EActivity(R.layout.activity_message_center)
public class MessageCenterActivity extends BaseActivity implements OnPageChangeListener {

	@ViewById(R.id.title_indicator)
	TitlePageIndicator mTitlePageIndicator;
	@ViewById(R.id.message_pager)
	ViewPager mMessagePager;
	MessageCenterPagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPagerAdapter = new MessageCenterPagerAdapter(getSupportFragmentManager());
		setTitle("消息中心");
	}

	@AfterViews
	void bindData() {
		mMessagePager.setAdapter(mPagerAdapter);
		mTitlePageIndicator.setViewPager(mMessagePager);
		mTitlePageIndicator.setFooterIndicatorStyle(IndicatorStyle.Triangle);
		mMessagePager.setCurrentItem(0);
		mTitlePageIndicator.setOnPageChangeListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		mPagerAdapter.update(position);
	}

}
