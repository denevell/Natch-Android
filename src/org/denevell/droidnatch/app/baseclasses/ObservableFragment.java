package org.denevell.droidnatch.app.baseclasses;

import org.denevell.droidnatch.EventBus;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;

public class ObservableFragment extends Fragment {
    

	private static final String TAG = ObservableFragment.class.getSimpleName();
    public static class MenuItemHolder {
        public MenuItem item;
        public MenuItemHolder(MenuItem item) {
            this.item = item;
        }
    }
    public class FragmentStopped {
		public Fragment fragment;
		public FragmentStopped(Fragment observableFragment) {
			fragment = observableFragment;
		}
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.v(TAG, "Context menu item selected");
        EventBus.getBus().post(new MenuItemHolder(item));
        return super.onContextItemSelected(item);
    }
    
    @Override
    public void onDestroyView() {
        EventBus.getBus().post(new FragmentStopped(this));
    	super.onDestroyView();
    }
    
}
