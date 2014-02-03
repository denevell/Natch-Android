package org.denevell.droidnatch.app.baseclasses;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ReceivingUiObject;
import org.denevell.droidnatch.app.interfaces.TypeAdapter;

import java.util.List;

public class ListViewResultDisplayer<T, U extends List<T>, S> implements 
         ReceivingUiObject<S>, ProgressIndicator{
    
    private static final String TAG = ListViewResultDisplayer.class.getSimpleName();
    private final TypeAdapter<S,U> mTypeAdapter;
    private ArrayAdapter<T> mListAdapter;
    private ListView mList;
    private Context mAppContext;
    private View mLoadingView;

    public ListViewResultDisplayer(
            ListView list, 
            ArrayAdapter<T> adapter, 
            View loadingView,
            Context appContext, 
            TypeAdapter<S, U> typeAdapter) {
        mListAdapter = adapter;
        mList = list;
        mLoadingView = loadingView;
        mAppContext = appContext;
        mTypeAdapter = typeAdapter;
    }

    /**
     * Used so when we start the loading view, we disable clicks
     * to views its hiding
     */
    private void toggleSiblingViews(boolean toggle) {
        if(mLoadingView.getParent()==null || !(mLoadingView.getParent() instanceof ViewGroup)) {
            Log.e(TAG, "Couldn't toggle sibling views since parent is null or not a ViewGroup.");
            return;
        }
        ViewGroup parent = ((ViewGroup)mLoadingView.getParent());
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = parent.getChildAt(i);
            if(v!=mLoadingView) {
                v.setClickable(toggle);
                v.setFocusable(toggle);
                v.setFocusableInTouchMode(toggle);
            }
        }
    }

    @Override
    public void success(S result) {
        Log.v(TAG, "Displaying results");
        mListAdapter.clear();
        U converted = mTypeAdapter.convert(result);
        mListAdapter.addAll(converted);
        mList.setAdapter(mListAdapter);
    }

    @Override
    public void fail(FailureResult fail) {
        String s = "Unknown error";
        if(fail!=null && fail.getErrorMessage()!=null) {
            s = fail.getErrorMessage();
        }
        Toast.makeText(mAppContext, s, Toast.LENGTH_LONG).show();

    }

    @Override
    public void start() {
        if(mLoadingView!=null) {
            toggleSiblingViews(false);
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void stop() {
        if(mLoadingView!=null) {
            mLoadingView.setVisibility(View.GONE);
            toggleSiblingViews(true);
        }
    }

}