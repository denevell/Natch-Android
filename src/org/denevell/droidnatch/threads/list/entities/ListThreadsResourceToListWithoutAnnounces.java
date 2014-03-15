package org.denevell.droidnatch.threads.list.entities;

import java.util.ArrayList;
import java.util.List;

import org.denevell.droidnatch.app.interfaces.TypeAdapter;

public class ListThreadsResourceToListWithoutAnnounces implements TypeAdapter<ListThreadsResource, List<ThreadResource>> {
    @Override
    public List<ThreadResource> convert(ListThreadsResource ob) {
        List<ThreadResource> threads = ob.getThreads();
  		List<ThreadResource> ret = new ArrayList<ThreadResource>();
        for (ThreadResource threadResource : threads) {
        	if(!threadResource.containsTag("announcements")) {
				ret.add(threadResource);
        	}
		}
        return ret;
    }
}