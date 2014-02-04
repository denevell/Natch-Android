package org.denevell.droidnatch.threads.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.natch.android.R;

public class ListThreadsFragment extends ObservableFragment {
    private static final String TAG = ListThreadsFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        try {
            getActivity().setTitle(R.string.page_title_threads);
            View v = inflater.inflate(R.layout.list_threads_fragment, container, false);
            return v;
        } catch (Exception e) {
            Log.e(TAG, "Failed to start di mapper", e);
            return null;
        }
    }

}