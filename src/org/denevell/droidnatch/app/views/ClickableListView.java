package org.denevell.droidnatch.app.views;

import java.util.ArrayList;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.baseclasses.HideKeyboard;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.interfaces.OnPressObserver;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

public class ClickableListView<T> extends ListView implements
               OnPressObserver<T>,
               OnItemClickListener,
               OnScrollListener {

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

    private HideKeyboard mHideKeyboard;
    private ArrayList<OnPress<T>> mPressListeners = new ArrayList<OnPressObserver.OnPress<T>>();
	private ArrayList<Runnable> mPaginationFooterCallbacks = new ArrayList<Runnable>();

    public ClickableListView(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        setOnItemClickListener(this);
        setOnScrollListener(this);
    }

    public void setKeyboardHider(HideKeyboard kbh) {
        mHideKeyboard = kbh;
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
    public void addOnPressListener(OnPress<T> callback) {
        mPressListeners.add(callback);
    }

    @Subscribe
    public void onContextItemSelected(ObservableFragment.ContextMenuItemHolder item) {
        try {
            Log.v(TAG, "Long press issued");
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.item.getMenuInfo();
            int index = info.position;
            @SuppressWarnings("unchecked")
            T tr = (T) getAdapter().getItem(index);
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
            T tr = (T) getAdapter().getItem(position);
            for (OnPress<T> listener: mPressListeners) {
                listener.onPress(tr);
            }
        } catch (Exception e) {
            Log.e(TAG, "Couldn't process onitemclick event.", e);
        }
    }

	@Override public void onScrollStateChanged(AbsListView view, int scrollState) {}

	@Override
	public void onScroll(AbsListView view, 
			int firstVisibleItem,
			int visibleItemCount, 
			int totalItemCount) {
		int position = firstVisibleItem+(visibleItemCount);
		if (totalItemCount > 0 && position == totalItemCount) {
			if (view.getAdapter()!=null && view.getAdapter() instanceof HeaderViewListAdapter) {
				//HeaderViewListAdapter adapter = (HeaderViewListAdapter) view.getAdapter();
				//Toast.makeText(getContext(), "Can see footer!", Toast.LENGTH_LONG).show();
				if(mPaginationFooterCallbacks!=null) {
					for (Runnable r: mPaginationFooterCallbacks) {
						r.run();
					}
				}
			}
		}
	}

	public void addOnPaginationFooterVisiableCallback(Runnable runnable) {
		mPaginationFooterCallbacks.add(runnable);
	}

}
