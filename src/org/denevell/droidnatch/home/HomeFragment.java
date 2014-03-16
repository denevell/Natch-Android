package org.denevell.droidnatch.home;

import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.threads.list.ListThreadsOptionsMenu;
import org.denevell.natch.android.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
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
			if(position==1) {
				
			}
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
		pager.setAdapter(new HomeFragmentPagerAdapter(getChildFragmentManager()));
		pager.setOnPageChangeListener(new FragmentSwipeListener());

		Tab tab = ab.newTab().setText(R.string.home_chat_tab).setTabListener(new HomeFragmentTabListener(pager, 0));
		ab.addTab(tab);
		tab = ab.newTab().setText(R.string.home_announcements_tab).setTabListener(new HomeFragmentTabListener(pager, 1));
		ab.addTab(tab);
		return view;
	}
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        new ListThreadsOptionsMenu().create(menu, inflater, getActivity().getApplicationContext());
    }

}