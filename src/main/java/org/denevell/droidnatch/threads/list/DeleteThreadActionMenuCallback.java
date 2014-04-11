package org.denevell.droidnatch.threads.list;

import java.util.List;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment.ContextMenuItemHolder;
import org.denevell.droidnatch.app.views.ReceivingClickingAutopaginatingListView;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ActionMode.Callback;

import com.newfivefour.android.manchester.R;

final class DeleteThreadActionMenuCallback implements Callback {
	/**
	 * 
	 */
	private final ListThreadsMapper listThreadsMapper;
	private final ReceivingClickingAutopaginatingListView<ListThreadsResource, ThreadResource, List<ThreadResource>> listView;
	private final int position;
	private View selectedView;

	DeleteThreadActionMenuCallback(
			ListThreadsMapper listThreadsMapper, ReceivingClickingAutopaginatingListView<ListThreadsResource, ThreadResource, List<ThreadResource>> listView, 
			int position, 
			View selectedView) {
		this.listThreadsMapper = listThreadsMapper;
		this.selectedView = selectedView;
		this.listView = listView;
		this.position = position;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		ReceivingClickingAutopaginatingListView.sCurrentActionMode = mode;
		return false;
	}

	@Override 
	public void onDestroyActionMode(ActionMode mode) { 
		if(this.selectedView!=null) this.selectedView.setSelected(false);
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		if(position==0) return false; // 0 is the header
		try {
			ThreadResource ob = (ThreadResource) listView.getAdapter() .getItem(position);
			String author = ob.getAuthor();
			String username = ShamefulStatics.getUsername(this.listThreadsMapper.mActivity .getApplicationContext());
			if (!ShamefulStatics.emptyUsername(this.listThreadsMapper.mActivity.getApplicationContext())
					&& author != null
					&& !author.equals(username)) {
				mode.getMenuInflater().inflate(
						R.menu.not_yours_context_option_menu, menu);
			} else if (ShamefulStatics.emptyUsername(this.listThreadsMapper.mActivity .getApplicationContext())) {
				mode.getMenuInflater().inflate( R.menu.please_login_context_option_menu, menu);
			} else {
				mode.getMenuInflater().inflate( R.menu.list_threads_context_option_menu, menu);
			}
			return true;
		} catch (Exception e) {
			Log.d(ListThreadsMapper.TAG, "Couldnt open action menu", e);
			return false;
		}
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		Log.d(ListThreadsMapper.TAG, "Sending delete thread from action item");
		EventBus.getBus().post(new ContextMenuItemHolder(item, position, listView));
		mode.finish();
		return true;
	}
}