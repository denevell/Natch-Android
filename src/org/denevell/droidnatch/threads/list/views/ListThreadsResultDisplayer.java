package org.denevell.droidnatch.threads.list.views;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListThreadsResultDisplayer implements 
        ResultsDisplayer<ListThreadsResource> {
    
    private ArrayAdapter<ThreadResource> mListAdapter;
    private ListView mList;
    private Context mAppContext;
    private View mErrorView;

    public ListThreadsResultDisplayer(
            ListView list, 
            ArrayAdapter<ThreadResource> adapter, 
            View errorView,
            Context appContext) {
        mListAdapter = adapter;
        mList = list;
        mErrorView = errorView;
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

    @Override
    public void startLoading() {
        if(mErrorView!=null) {
            mErrorView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void stopLoading() {
        if(mErrorView!=null) {
            mErrorView.setVisibility(View.GONE);
        }
    }

}