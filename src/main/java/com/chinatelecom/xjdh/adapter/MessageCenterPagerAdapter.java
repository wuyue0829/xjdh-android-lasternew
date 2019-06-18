package com.chinatelecom.xjdh.adapter;

import java.util.ArrayList;
import java.util.List;

import com.chinatelecom.xjdh.fragment.MessageCenterFragment_;
import com.chinatelecom.xjdh.utils.SharedConst;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * @author peter
 * 
 */
public class MessageCenterPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	FragmentManager fm;
	private List<String> mTagList = new ArrayList<String>();

	public MessageCenterPagerAdapter(FragmentManager fm) {
		super(fm);
		this.fm = fm;
		mFragmentList.add(MessageCenterFragment_.newInstance(SharedConst.MESG_TYPE_PERSONAL));
		mFragmentList.add(MessageCenterFragment_.newInstance(SharedConst.MESG_TYPE_SYSTEM));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		mTagList.add(makeFragmentName(container.getId(), (int) getItemId(position)));
		return super.instantiateItem(container, position);
	}

	public static String makeFragmentName(int viewId, int index) {
		return "android:switcher:" + viewId + ":" + index;
	}

	@Override
	public Fragment getItem(int position) {
		return mFragmentList.get(position);
	}

	public void update(int position) {
		Fragment fragment = fm.findFragmentByTag(mTagList.get(position));
		if (fragment != null) {
			((MessageCenterFragment_) fragment).update();
		}
	}

	@Override
	public int getCount() {
		return SharedConst.CONTENT_MAP.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return SharedConst.CONTENT_MAP.get(position + 1);
	}

}
