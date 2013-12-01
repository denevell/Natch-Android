package org.denevell.droidnatch.listthreads.views;

import org.denevell.droidnatch.baseclasses.FailureResult;
import org.denevell.droidnatch.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.listthreads.entities.ListThreadsResource;
import org.denevell.droidnatch.listthreads.entities.ThreadResource;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListThreadsResultDisplayer implements ResultsDisplayer<ListThreadsResource> {
    
    private Context mAppContext;
    private ArrayAdapter<ThreadResource> mListAdapter;
    private ListView mList;

    public ListThreadsResultDisplayer(
            Context appContext, 
            ListView list, 
            ArrayAdapter<ThreadResource> adapter) {
        mAppContext = appContext;
        mListAdapter = adapter;
        mList = list;
    }

    public void onSuccess(final ListThreadsResource success) {
        mListAdapter.clear();
        mListAdapter.addAll(success.getThreads());
        mList.setAdapter(mListAdapter);
    }

    public void onFail(final FailureResult fail) {
        Toast.makeText(mAppContext, fail.errorMessage, Toast.LENGTH_LONG) .show();
    }

}