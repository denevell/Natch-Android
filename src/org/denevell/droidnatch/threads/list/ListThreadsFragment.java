package org.denevell.droidnatch.threads.list;

import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.natch.android.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ListThreadsFragment extends ObservableFragment {
	@SuppressWarnings("unused")
	private static final String TAG = ListThreadsFragment.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
       	getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        getActivity().setTitle(R.string.page_title_threads);
        View v = inflater.inflate(R.layout.threads_list_fragment, container, false);
        return v;
    }

}