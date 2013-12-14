package org.denevell.droidnatch.threads.list.views;

import javax.inject.Inject;
import javax.inject.Named;

import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.baseclasses.ScreenOpenerMapper;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.thread.add.AddThreadMapper;
import org.denevell.droidnatch.thread.delete.DeleteThreadMapper;
import org.denevell.droidnatch.threads.list.ListThreadsMapper;
import org.denevell.natch.android.R;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dagger.ObjectGraph;

public class ListThreadsFragment extends ObservableFragment {
    
    private static final String TAG = ListThreadsFragment.class.getSimpleName();
    @Inject @Named("list_threads") Controller mController;
    @Inject @Named("addthread") Controller mControllerAddThread;
    @Inject @Named("deletethread") Controller mControllerDeleteThread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.list_threads_fragment, container, false);
        return v;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        try {
            ObjectGraph.create(
                    new ScreenOpenerMapper(getActivity()),
                    new CommonMapper(getActivity()),
                    new DeleteThreadMapper(this),
                    new ListThreadsMapper(getActivity()),
                    new AddThreadMapper(getActivity())
                    )
                    .inject(this);
            mController.go();
            mControllerAddThread.go();
            mControllerDeleteThread.go();
        } catch (Exception e) {
            Log.e(TAG, "Failed to start di mapper", e);
            return;
        }    
    }


}