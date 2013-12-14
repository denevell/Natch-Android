package org.denevell.droidnatch.app.baseclasses;

import java.util.ArrayList;
import java.util.List;

import org.denevell.droidnatch.app.interfaces.ContextItemSelected;
import org.denevell.droidnatch.app.interfaces.ContextItemSelectedObserver;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;

public class ObservableFragment extends Fragment 
    implements ContextItemSelectedObserver {
    
    private static final String TAG = ObservableFragment.class.getSimpleName();
    private List<ContextItemSelected> mContextItemSelected = new ArrayList<ContextItemSelected>();
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.v(TAG, "Context menu item selected");
        for (ContextItemSelected selectedCallback: mContextItemSelected) {
            selectedCallback.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
    }
    
    @Override
    public void addContextItemSelectedCallback(ContextItemSelected contextItem) {
        Log.v(TAG, "Adding Context menu callback");
        mContextItemSelected.add(contextItem);
    }        
    
    @Override
    public void onResume() {
        super.onResume();
        mContextItemSelected.clear();
    }

}
