package org.denevell.droidnatch.home;

import java.util.ArrayList;

import org.denevell.droidnatch.notifications.AnnouncementsFragment;
import org.denevell.droidnatch.threads.list.ListThreadsFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
	
	private ArrayList<Fragment> mFragments;

	public HomeFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		mFragments = new ArrayList<Fragment>();
		mFragments.add(new ListThreadsFragment());
		mFragments.add(new AnnouncementsFragment());
	}
	
	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}
}