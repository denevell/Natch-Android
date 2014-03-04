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
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.squareup.otto.Subscribe;

public class ReceivingClickingAutopaginatingListView
		<ReceivingObjects, 
		AdapterItem, 
		AdapterItems extends List<AdapterItem>> 
	extends 
		GridView
	implements
    	OnPressObserver<AdapterItem>,
        OnItemClickListener,
        OnScrollListener,
        Receiver<ReceivingObjects>{

    private static final String TAG = ReceivingClickingAutopaginatingListView.class.getSimpleName();
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
    private ArrayList<OnPress<AdapterItem>> mPressListeners = new ArrayList<OnPressObserver.OnPress<AdapterItem>>();
	private ArrayList<Runnable> mPaginationFooterCallbacks = new ArrayList<Runnable>();
	private int mTotalAvailableForList;
    private TypeAdapter<ReceivingObjects,AdapterItems> mTypeAdapter;
    private ArrayAdapter<AdapterItem> mListAdapter;
    private AvailableItems<ReceivingObjects> mAvailableItems;
    /**
     * Used so we can set a new adapter, and keep the old listview state.
     */
	private Parcelable mSavedListViewState;
	private View mErrorView;
	private int mMaxYOverscrollDistance;

    public ReceivingClickingAutopaginatingListView(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        setOnItemClickListener(this);
        setOnScrollListener(this);
    }

    public ReceivingClickingAutopaginatingListView(Context context, AttributeSet attrSet, int style) {
        super(context, attrSet, style);
        setOnItemClickListener(this);
        setOnScrollListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getBus().register(this);
        
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        final float density = metrics.density;

        mMaxYOverscrollDistance = (int) (density * 20);
        
    }

	private void findAndSetEmptyView() {
		if(getParent() instanceof ViewGroup) {
			View findViewById = ((ViewGroup) getParent()).findViewById(android.R.id.empty);
			if (findViewById != null) {
				setEmptyView(findViewById);
			}
        }
	}

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getBus().unregister(this);
    }

    @Override
    public void addOnPressListener(OnPress<AdapterItem> callback) {
        mPressListeners.add(callback);
    }

	@SuppressWarnings("rawtypes")
    public ReceivingClickingAutopaginatingListView setKeyboardHider(HideKeyboard kbh) {
        mHideKeyboard = kbh;
        return this;
    }
	
	@SuppressWarnings("rawtypes")
	public ReceivingClickingAutopaginatingListView setAvailableItems(AvailableItems<ReceivingObjects> mAvailableItems) {
		this.mAvailableItems = mAvailableItems;
		return this;
	}

	@SuppressWarnings("rawtypes")
	public ReceivingClickingAutopaginatingListView setListAdapter(ArrayAdapter<AdapterItem> listAdapter) {
		mListAdapter = listAdapter;
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public ReceivingClickingAutopaginatingListView setTypeAdapter(TypeAdapter<ReceivingObjects, AdapterItems> adapter) {
		this.mTypeAdapter = adapter;
		return this;
	}

	@SuppressWarnings("rawtypes")
	public ReceivingClickingAutopaginatingListView setPaginationView(View view) {
		//mPaginationView = view;
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public ReceivingClickingAutopaginatingListView addOnPaginationFooterVisibleCallback(Runnable runnable) {
		mPaginationFooterCallbacks.add(runnable);
		return this;
	}

	@SuppressWarnings("rawtypes")
	public ReceivingClickingAutopaginatingListView setErrorView(View v) {
		mErrorView = v;
		return this;
	}

	@SuppressWarnings("rawtypes")
	public ReceivingClickingAutopaginatingListView setErrorView(int layoutId) {
		mErrorView = LayoutInflater.from(getContext()).inflate(layoutId, this, false);
		return this;
	}

	@SuppressWarnings("rawtypes")
	public ReceivingClickingAutopaginatingListView setContenxtMenuListener(OnCreateContextMenuListener contextMenu) {
		setOnCreateContextMenuListener(contextMenu);
		return this;
	}

    @Override
    public void setAdapter(ListAdapter adapter) {
    	super.setAdapter(adapter);
        findAndSetEmptyView();
    	if(mSavedListViewState!=null) {
    		super.onRestoreInstanceState(mSavedListViewState);
    	}
    }
	
	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
		if (getAdapter() != null && clampedY
				&& mTotalAvailableForList > getAdapter().getCount()) {
			if (mPaginationFooterCallbacks != null) {
				for (Runnable r : mPaginationFooterCallbacks) {
					r.run();
				}
			}
		}
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
	}
	
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
				scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);
	}
	
    // On selection stuff

    @Subscribe
    public void onContextItemSelected(ObservableFragment.ContextMenuItemHolder item) {
        try {
            Log.v(TAG, "Long press issued");
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.item.getMenuInfo();
            int index = info.position;
            @SuppressWarnings("unchecked")
            AdapterItem tr = (AdapterItem) getAdapter().getItem(index);
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
            AdapterItem tr = (AdapterItem) getAdapter().getItem(position);
            for (OnPress<AdapterItem> listener: mPressListeners) {
                listener.onPress(tr);
            }
        } catch (Exception e) {
            Log.e(TAG, "Couldn't process onitemclick event.", e);
        }
    }
    
    // Save the old list view state.
    
    /**
     * Save the old listview state so we can set it 
     * on resetting the adapter.
     */
    @Override
    public Parcelable onSaveInstanceState() {
    	Bundle b = new Bundle();
    	b.putParcelable("oldstate", mSavedListViewState);
    	b.putParcelable("state", super.onSaveInstanceState());
    	return b;
    }
    
    /**
     * Get the old listview state out first, since we'll be
     * using this again when we reset the adapter
     */
    @Override
    public void onRestoreInstanceState(Parcelable state) {
    	Bundle bundle = (Bundle)state;
		mSavedListViewState = bundle.getParcelable("oldstate");
    	super.onRestoreInstanceState(bundle.getParcelable("state"));
    }
    
    // Scroll view stuff

	@Override public void onScrollStateChanged(AbsListView view, int scrollState) {}

	/**
	 * If we see the footer,
	 * And it's totally in focus, 
	 * Then call the callbacks.
	 */
	@Override
	public void onScroll(AbsListView view, 
			int firstVisibleItem,
			int visibleItemCount, 
			int totalItemCount) {
		if(getAdapter()!=null && getAdapter().getCount()>0) {
			mSavedListViewState = super.onSaveInstanceState();
		}
	}

	// Receiving objects stuff
	
	@Override
	public void success(ReceivingObjects result) {
        setVisibility(View.VISIBLE);
        if(mErrorView!=null) {
        	((ViewGroup)getParent()).removeView(mErrorView);
        }
		mTotalAvailableForList = getTotalAvailableForList(result);
        AdapterItems converted = mTypeAdapter.convert(result);
        mListAdapter.clear();
        mListAdapter.addAll(converted);
        setAdapter(mListAdapter);
	}

	@Override
	public void fail(FailureResult fail) {
        if(fail!=null && fail.getErrorMessage()!=null) {
            String s = fail.getErrorMessage();
            Log.w(TAG, "Error receiving list elements: " + s);
        }
        setVisibility(View.GONE);
        if(getEmptyView()!=null) {
        	getEmptyView().setVisibility(View.GONE);
        }
        if(mErrorView!=null) {
        	((ViewGroup)getParent()).addView(mErrorView);
        }
	}

    private int getTotalAvailableForList(ReceivingObjects object) {
    	if(mAvailableItems==null) {
    		return 0;
    	} else {
    		return mAvailableItems.getTotalAvailableForList(object);
    	}
    }


}
