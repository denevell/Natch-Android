package org.denevell.droidnatch.app.baseclasses;

import java.util.ArrayList;
import java.util.List;

import org.denevell.droidnatch.app.interfaces.ContextItemSelected;
import org.denevell.droidnatch.app.interfaces.ContextItemSelectedObserver;

import android.support.v4.app.Fragment;
import android.view.MenuItem;

public class ObservableFragment extends Fragment 
    implements ContextItemSelectedObserver {
    
    private List<ContextItemSelected> contextItemSelected = new ArrayList<ContextItemSelected>();
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        for (ContextItemSelected selectedCallback: contextItemSelected) {
            selectedCallback.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
    }
    
    @Override
    public void addContextItemSelectedCallback(ContextItemSelected contextItem) {
        contextItemSelected.add(contextItem);
    }        

}
