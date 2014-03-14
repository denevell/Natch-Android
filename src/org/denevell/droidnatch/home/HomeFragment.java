package org.denevell.droidnatch.home;

import java.util.ArrayList;

import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.notifications.NotificationFragment;
import org.denevell.droidnatch.threads.list.ListThreadsFragment;
import org.denevell.droidnatch.threads.list.ListThreadsOptionsMenu;
import org.denevell.natch.android.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends ObservableFragment {
	
	public class FragmentSwipeListener implements OnPageChangeListener {
		@Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
		@Override public void onPageScrollStateChanged(int state) { }
		@Override
		public void onPageSelected(int position) {
			getActivity().getActionBar().setSelectedNavigationItem(position);
		}
	}

	public class TabListener implements ActionBar.TabListener {
		private ViewPager mVp;
		private int mPagerNumber;

		public TabListener(ViewPager vp, int num) {
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

	public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		
		private ArrayList<Fragment> mFragments;

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
			mFragments = new ArrayList<Fragment>();
			mFragments.add(new ListThreadsFragment());
			mFragments.add(new NotificationFragment());
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_fragment, container, false);

		ActionBar ab = getActivity().getActionBar();
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ab.setDisplayShowTitleEnabled(true);
		ab.removeAllTabs();

		ViewPager pager = (ViewPager) view.findViewById(R.id.home_fragment_viewpager);
		pager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager()));
		pager.setOnPageChangeListener(new FragmentSwipeListener());

		Tab tab = ab.newTab().setText(R.string.home_chat_tab).setTabListener(new TabListener(pager, 0));
		ab.addTab(tab);
		tab = ab.newTab().setText(R.string.home_announcements_tab).setTabListener(new TabListener(pager, 1));
		ab.addTab(tab);
		return view;
	}
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        new ListThreadsOptionsMenu().create(menu, inflater, getActivity().getApplicationContext());
    }

}