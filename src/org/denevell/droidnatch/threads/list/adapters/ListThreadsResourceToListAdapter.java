package org.denevell.droidnatch.threads.list.adapters;

import java.util.List;

import org.denevell.droidnatch.app.interfaces.TypeAdapter;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

public class ListThreadsResourceToListAdapter implements TypeAdapter<ListThreadsResource, List<ThreadResource>> {
    @Override
    public List<ThreadResource> convert(ListThreadsResource ob) {
        return ob.getThreads();
    }
}