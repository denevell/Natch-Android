package org.denevell.droidnatch.threads.list.entities;

import org.denevell.droidnatch.app.views.ClickableListView.AvailableItems;

public class ListThreadsResourceTotalAvailable implements AvailableItems<ListThreadsResource> {
	@Override
	public int getTotalAvailableForList(ListThreadsResource ob) {
		if (ob == null)
			return 0;
		else
			return (int) ob.getNumOfThreads();
	}
}
