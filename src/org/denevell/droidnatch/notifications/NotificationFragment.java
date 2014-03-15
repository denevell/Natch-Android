package org.denevell.droidnatch.notifications;

import org.denevell.natch.android.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NotificationFragment extends Fragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
       	getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        getActivity().setTitle(R.string.page_title_threads);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.notifications_fragment, container, false);
	}
	
	@Override
	public void onResume() {
		super.onResume();
       	getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
	}

}
