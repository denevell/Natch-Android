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
    
    private ArrayAdapter<ThreadResource> mListAdapter;
    private ListView mList;
    private Context mAppContext;

    public ListThreadsResultDisplayer(
            ListView list, 
            ArrayAdapter<ThreadResource> adapter, 
            Context appContext) {
        mListAdapter = adapter;
        mList = list;
        mAppContext = appContext;
    }

    public void onSuccess(final ListThreadsResource success) {
        mListAdapter.clear();
        mListAdapter.addAll(success.getThreads());
        mList.setAdapter(mListAdapter);
    }

    public void onFail(final FailureResult fail) {
        String s = "Unknown error";
        if(fail!=null && fail.getErrorMessage()!=null) {
            s = fail.getErrorMessage();
        }
        Toast.makeText(mAppContext, s, Toast.LENGTH_LONG).show();
    }

}