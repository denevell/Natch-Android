package org.denevell.droidnatch.threads.list;

import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.natch.android.R;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class ListThreadsFragment extends ObservableFragment {
    private static final String TAG = ListThreadsFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        try {
            getActivity().setTitle(R.string.page_title_threads);
            setHasOptionsMenu(true);
            View v = inflater.inflate(R.layout.threads_list_fragment, container, false);
            return v;
        } catch (Exception e) {
            Log.e(TAG, "Failed to start di mapper", e);
            return null;
        }
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        new ListThreadsOptionsMenu().create(menu, inflater);
    }

}