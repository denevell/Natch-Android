package org.denevell.droidnatch.listthreads.views;

import org.denevell.droidnatch.baseclasses.FailureResult;
import org.denevell.droidnatch.interfaces.PopupDisplayer;
import org.denevell.droidnatch.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.listthreads.entities.ListThreadsResource;
import org.denevell.droidnatch.listthreads.entities.ThreadResource;

import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListThreadsResultDisplayer implements ResultsDisplayer<ListThreadsResource> {
    
    private ArrayAdapter<ThreadResource> mListAdapter;
    private ListView mList;
    private PopupDisplayer mPopupDisplayer;

    public ListThreadsResultDisplayer(
            ListView list, 
            ArrayAdapter<ThreadResource> adapter, 
            PopupDisplayer popupDisplayer) {
        mListAdapter = adapter;
        mList = list;
        mPopupDisplayer = popupDisplayer;
    }

    public void onSuccess(final ListThreadsResource success) {
        mListAdapter.clear();
        mListAdapter.addAll(success.getThreads());
        mList.setAdapter(mListAdapter);
    }

    public void onFail(final FailureResult fail) {
        mPopupDisplayer.displayString(fail.errorMessage);
    }

}