package org.denevell.droidnatch.app.baseclasses;

import java.util.List;

import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListViewResultDisplayer<T, U extends List<T>> implements 
        ResultsDisplayer<U> {
    
    private static final String TAG = ListViewResultDisplayer.class.getSimpleName();
    private ArrayAdapter<T> mListAdapter;
    private ListView mList;
    private Context mAppContext;
    private View mLoadingView;

    public ListViewResultDisplayer(
            ListView list, 
            ArrayAdapter<T> adapter, 
            View loadingView,
            Context appContext) {
        mListAdapter = adapter;
        mList = list;
        mLoadingView = loadingView;
        mAppContext = appContext;
    }

    public void onSuccess(final U success) {
        Log.v(TAG, "Displaying results");
        mListAdapter.clear();
        mListAdapter.addAll(success);
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
        if(mLoadingView!=null) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void stopLoading() {
        if(mLoadingView!=null) {
            mLoadingView.setVisibility(View.GONE);
        }
    }

}