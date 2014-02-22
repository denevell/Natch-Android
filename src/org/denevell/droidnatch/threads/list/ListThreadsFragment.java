package org.denevell.droidnatch.threads.list;

import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.service.NewThreadPollingService;
import org.denevell.natch.android.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class ListThreadsFragment extends ObservableFragment {
	private static final String TAG = ListThreadsFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        try {
            getActivity().setTitle(R.string.page_title_threads);
            setHasOptionsMenu(true);
            View v = inflater.inflate(R.layout.threads_list_fragment, container, false);
            return v;
        } catch (Exception e) {
            Log.e(TAG, "Failed to start di mapper", e);
            return null;
        }
    }

    private final class HandlerExtension extends Handler {
		private Context mContext;

		private HandlerExtension(Looper looper, Context appContext) {
			super(looper);
			mContext = appContext;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String string = msg.obj.toString();
			Context applicationContext = mContext;
			Toast.makeText(applicationContext, string, Toast.LENGTH_SHORT).show();
		}
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
		Handler handler = new HandlerExtension(getActivity().getMainLooper(),
				getActivity().getApplicationContext());
    	Messenger messenger = new Messenger(handler);

    	Intent serviceIntent = new Intent(getActivity(), NewThreadPollingService.class);
    	serviceIntent.putExtra("messenger", messenger);
		getActivity().startService(serviceIntent);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        new ListThreadsOptionsMenu().create(menu, inflater);
    }

}