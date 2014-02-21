package org.denevell.droidnatch.app.views;

import java.util.ArrayList;
import java.util.List;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.HideKeyboard;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.interfaces.OnPressObserver;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.TypeAdapter;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

public class ClickableListView
		<ItemPressed, 
		ReceivingObjects, 
		AdapterItem, 
		AdapterItems extends List<AdapterItem>> 
	extends 
		ListView 
	implements
    	OnPressObserver<ItemPressed>,
        OnItemClickListener,
        OnScrollListener,
        Receiver<ReceivingObjects>{

    private static final String TAG = ClickableListView.class.getSimpleName();
    public static class LongPressListViewEvent {
        public final Object ob;
        public final int index;
		public final MenuItem menuItem;
        public LongPressListViewEvent(Object ob, MenuItem menuItem, int index) {
            this.ob = ob;
            this.menuItem = menuItem;
            this.index = index;
        }
    }
    public static interface AvailableItems<ReceivingObjects> {
    	int getTotalAvailableForList(ReceivingObjects ob);
	}

    private HideKeyboard mHideKeyboard;
    private ArrayList<OnPress<ItemPressed>> mPressListeners = new ArrayList<OnPressObserver.OnPress<ItemPressed>>();
	private ArrayList<Runnable> mPaginationFooterCallbacks = new ArrayList<Runnable>();
	private View mPaginationView;
	private int mTotalAvailableForList;
    private TypeAdapter<ReceivingObjects,AdapterItems> mTypeAdapter;
    private ArrayAdapter<AdapterItem> mListAdapter;
    private AvailableItems<ReceivingObjects> mAvailableItems;

    public ClickableListView(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        setOnItemClickListener(this);
        setOnScrollListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getBus().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getBus().unregister(this);
    }

    @Override
    public void addOnPressListener(OnPress<ItemPressed> callback) {
        mPressListeners.add(callback);
    }

	@SuppressWarnings("rawtypes")
    public ClickableListView setKeyboardHider(HideKeyboard kbh) {
        mHideKeyboard = kbh;
        return this;
    }
	
	@SuppressWarnings("rawtypes")
	public ClickableListView setAvailableItems(AvailableItems<ReceivingObjects> mAvailableItems) {
		this.mAvailableItems = mAvailableItems;
		return this;
	}

	@SuppressWarnings("rawtypes")
	public ClickableListView setListAdapter(ArrayAdapter<AdapterItem> listAdapter) {
		mListAdapter = listAdapter;
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public ClickableListView setTypeAdapter(TypeAdapter<ReceivingObjects, AdapterItems> adapter) {
		this.mTypeAdapter = adapter;
		return this;
	}

	@SuppressWarnings("rawtypes")
	public ClickableListView setPaginationView(View view) {
		mPaginationView = view;
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public ClickableListView addOnPaginationFooterVisibleCallback(Runnable runnable) {
		mPaginationFooterCallbacks.add(runnable);
		return this;
	}

    @Override
    public void setAdapter(ListAdapter adapter) {
    	Parcelable oldState = onSaveInstanceState();

        setPaginationFooterIfNeeded(adapter);

    	super.setAdapter(adapter);
    	if(oldState!=null) {
    		onRestoreInstanceState(oldState);
    	}
    }

	private void setPaginationFooterIfNeeded(ListAdapter adapter) {
		int adapterCount = adapter.getCount();
		if( getFooterViewsCount()==0 && 
				mTotalAvailableForList>adapterCount && 
				mPaginationView!=null) {
			addFooterView(mPaginationView);
        }
        if(getFooterViewsCount()!=0 && 
        		mTotalAvailableForList<=adapterCount && 
        		mPaginationView!=null) { 
			removeFooterView(mPaginationView);
        }
	}

    // On selection stuff

    @Subscribe
    public void onContextItemSelected(ObservableFragment.ContextMenuItemHolder item) {
        try {
            Log.v(TAG, "Long press issued");
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.item.getMenuInfo();
            int index = info.position;
            @SuppressWarnings("unchecked")
            ItemPressed tr = (ItemPressed) getAdapter().getItem(index);
            EventBus.getBus().post(new LongPressListViewEvent(tr, item.item, index));
        } catch (Exception e) {
            Log.e(TAG, "Couldn't process oncontextitemselected event.", e);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            if(mHideKeyboard!=null && parent!=null) mHideKeyboard.hideKeyboard(parent.getContext(), view);
            Log.v(TAG, "Press issued");
            @SuppressWarnings("unchecked")
            ItemPressed tr = (ItemPressed) getAdapter().getItem(position);
            for (OnPress<ItemPressed> listener: mPressListeners) {
                listener.onPress(tr);
            }
        } catch (Exception e) {
            Log.e(TAG, "Couldn't process onitemclick event.", e);
        }
    }
    
    // Scroll view stuff

	@Override public void onScrollStateChanged(AbsListView view, int scrollState) {}

	@Override
	public void onScroll(AbsListView view, 
			int firstVisibleItem,
			int visibleItemCount, 
			int totalItemCount) {
		int position = firstVisibleItem+(visibleItemCount);
		if (totalItemCount > 0 && position == totalItemCount) {
			if (view.getAdapter()!=null && view.getAdapter() instanceof HeaderViewListAdapter) {
				if(mPaginationFooterCallbacks!=null) {
					for (Runnable r: mPaginationFooterCallbacks) {
						r.run();
					}
				}
			}
		}
	}

	// Receiving objects stuff
	
	@Override
	public void success(ReceivingObjects result) {
		mTotalAvailableForList = getTotalAvailableForList(result);
        AdapterItems converted = mTypeAdapter.convert(result);
        mListAdapter.clear();
        mListAdapter.addAll(converted);
        setAdapter(mListAdapter);
	}

	@Override
	public void fail(FailureResult fail) {
        String s = "Unknown error";
        if(fail!=null && fail.getErrorMessage()!=null) {
            s = fail.getErrorMessage();
        }
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
	}

    private int getTotalAvailableForList(ReceivingObjects object) {
    	if(mAvailableItems==null) {
    		return 0;
    	} else {
    		return mAvailableItems.getTotalAvailableForList(object);
    	}
    }


}
