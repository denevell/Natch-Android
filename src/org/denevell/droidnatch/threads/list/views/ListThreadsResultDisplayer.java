package org.denevell.droidnatch.threads.list.views;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

import android.content.Context;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListThreadsResultDisplayer implements 
        ResultsDisplayer<ListThreadsResource>, 
        OnCreateContextMenuListener
        {
    
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
        mList.setOnCreateContextMenuListener(this);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, 0, 0, "Delete");
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