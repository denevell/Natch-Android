package org.denevell.droidnatch.threads.list.entities;

import org.denevell.droidnatch.app.views.ReceivingClickingAutopaginatingListView.AvailableItems;

public class ListThreadsResourceTotalAvailable implements AvailableItems<ListThreadsResource> {
	@Override
	public int getTotalAvailableForList(ListThreadsResource ob) {
		if (ob == null) return 0;
     	int toRemove = 0;
        for (ThreadResource threadResource : ob.getThreads()) {
        	if(threadResource.containsTag("announcements")) {
				toRemove++;
        	}
		}
		return (int) ob.getNumOfThreads()-toRemove;
	}
}
