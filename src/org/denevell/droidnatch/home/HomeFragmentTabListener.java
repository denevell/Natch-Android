package org.denevell.droidnatch.home;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.support.v4.view.ViewPager;

public class HomeFragmentTabListener implements ActionBar.TabListener {
	private ViewPager mVp;
	private int mPagerNumber;

	public HomeFragmentTabListener(ViewPager vp, int num) {
		mVp = vp;
		mPagerNumber = num;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mVp.setCurrentItem(mPagerNumber, true);
	}
	@Override public void onTabUnselected(Tab tab, FragmentTransaction ft) { }
	@Override public void onTabReselected(Tab tab, FragmentTransaction ft) { }

}