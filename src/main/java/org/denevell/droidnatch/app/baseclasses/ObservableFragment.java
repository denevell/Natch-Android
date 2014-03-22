package org.denevell.droidnatch.app.baseclasses;

import org.denevell.droidnatch.EventBus;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ObservableFragment extends Fragment {
    
	private static final String TAG = ObservableFragment.class.getSimpleName();
    public static class MenuItemHolder {
        public MenuItem item;
        public MenuItemHolder(MenuItem item) {
            this.item = item;
        }
    }
    public static class ContextMenuItemHolder extends MenuItemHolder {
		public int position;
		public ContextMenuItemHolder(MenuItem item, int position) {
			super(item);
			this.position = position;
		}

    }
    public static class OptionMenuItemHolder extends MenuItemHolder {
		public OptionMenuItemHolder(MenuItem item) {
			super(item);
		}
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.v(TAG, "Context menu item selected");
        EventBus.getBus().post(new ContextMenuItemHolder(item, 
        		((AdapterContextMenuInfo)item.getMenuInfo()).position));
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        EventBus.getBus().post(new OptionMenuItemHolder(item));
        return super.onOptionsItemSelected(item);
    }
    
}