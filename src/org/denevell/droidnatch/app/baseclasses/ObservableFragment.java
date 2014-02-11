package org.denevell.droidnatch.app.baseclasses;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;

import org.denevell.droidnatch.EventBus;

public class ObservableFragment extends Fragment {
    
    private static final String TAG = ObservableFragment.class.getSimpleName();
    public static class MenuItemHolder {
        public MenuItem item;
        public MenuItemHolder(MenuItem item) {
            this.item = item;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.v(TAG, "Context menu item selected");
        EventBus.getBus().post(new MenuItemHolder(item));
        return super.onContextItemSelected(item);
    }
    
}
