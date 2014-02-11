package org.denevell.droidnatch.threads.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.threads.list.di.AddThreadUiEventMapper;
import org.denevell.natch.android.R;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.ObjectGraph;

public class ListThreadsFragment extends ObservableFragment {
    private static final String TAG = ListThreadsFragment.class.getSimpleName();

    @Inject @Named(AddThreadUiEventMapper.PROVIDES_ADD_THREAD_POPUP) Receiver<Void> addThreadUi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        try {
            getActivity().setTitle(R.string.page_title_threads);
            setHasOptionsMenu(true);
            View v = inflater.inflate(R.layout.list_threads_fragment, container, false);
            ObjectGraph.create(new AddThreadUiEventMapper(getActivity())).inject(this);
            return v;
        } catch (Exception e) {
            Log.e(TAG, "Failed to start di mapper", e);
            return null;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_threads_options_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        addThreadUi.success(null);
        return super.onOptionsItemSelected(item);
    }
}