package org.denevell.droidnatch.app.baseclasses;

import org.denevell.droidnatch.EventBus;
import org.denevell.natch.android.R;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
/*    	View v = LayoutInflater
    			.from(getActivity().getApplicationContext())
    			.inflate(R.layout.login_layout, null);
    	new AlertDialog.Builder(getActivity())
    	.setView(v)
    	.create()
    	.show();*/
    	DialogFragment df = new Df();
		df.show(getActivity().getSupportFragmentManager(), "sdf");
        return super.onOptionsItemSelected(item);
    }
    
    public static class Df extends DialogFragment {
    	
    	@Override
    	public Dialog onCreateDialog(Bundle savedInstanceState) {
    		Dialog onCreateDialog = super.onCreateDialog(savedInstanceState);
    		return onCreateDialog;
    	}
    	
    	@Override
    	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    		View view = LayoutInflater
    				.from(getActivity().getApplicationContext())
    				.inflate(R.layout.login_layout, container, false);
    		return view;
    	}
    	
    	@Override
    	public void onDismiss(DialogInterface dialog) {
    		Log.d(TAG, "Dismiss");
    		super.onDismiss(dialog);
    		dialog.cancel();
    		dialog.dismiss();
    	}
    	
    }    
    
}