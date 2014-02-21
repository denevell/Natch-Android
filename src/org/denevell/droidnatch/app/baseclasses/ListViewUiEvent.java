package org.denevell.droidnatch.app.baseclasses;

import java.util.List;

import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.TypeAdapter;

import android.content.Context;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListViewUiEvent<T, U extends List<T>, S> implements
        Receiver<S>, ProgressIndicator{
    
    public interface AvailableItems<S> {
    	int getTotalAvailableForList(S ob);
	}

	private static final String TAG = ListViewUiEvent.class.getSimpleName();
    private final TypeAdapter<S,U> mTypeAdapter;
    private ArrayAdapter<T> mListAdapter;
    private ListView mList;
    private Context mAppContext;
    private View mLoadingView;
	private AvailableItems<S> mAvailableItems;
	private View mPaginationButton;

    public ListViewUiEvent(
            ListView list,
            ArrayAdapter<T> adapter,
            View loadingView,
            Context appContext,
            TypeAdapter<S, U> typeAdapter,
            AvailableItems<S> availableItems,
            View paginationButton) {
        mListAdapter = adapter;
        mList = list;
        mLoadingView = loadingView;
        mAppContext = appContext;
        mTypeAdapter = typeAdapter;
        mAvailableItems = availableItems;
        mPaginationButton = paginationButton;
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
        
        Parcelable oldState = mList.onSaveInstanceState();
        
        mListAdapter.clear();
        U converted = mTypeAdapter.convert(result);
        mListAdapter.addAll(converted);
        int availableForList = totalAvailableForList(result);
        if(mList.getFooterViewsCount()==0 && availableForList>converted.size() && mPaginationButton!=null) {
			mList.addFooterView(mPaginationButton);
        }
        if(mList.getFooterViewsCount()!=0 && availableForList<=converted.size() && mPaginationButton!=null) { 
			mList.removeFooterView(mPaginationButton);
        }
        mList.setAdapter(mListAdapter);
        
        if(oldState!=null) {
        	mList.onRestoreInstanceState(oldState);
        }
    }
    
    private int totalAvailableForList(S object) {
    	if(mAvailableItems==null) {
    		return 0;
    	} else {
    		return mAvailableItems.getTotalAvailableForList(object);
    	}
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