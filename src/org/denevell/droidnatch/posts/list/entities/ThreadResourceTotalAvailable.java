package org.denevell.droidnatch.posts.list.entities;

import org.denevell.droidnatch.app.views.ClickableListView.AvailableItems;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

public class ThreadResourceTotalAvailable implements AvailableItems<ThreadResource> {
	@Override
	public int getTotalAvailableForList(ThreadResource ob) {
		if (ob == null)
			return 0;
		else
			return (int) ob.getNumPosts();
	}
}
