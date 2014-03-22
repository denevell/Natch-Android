package org.denevell.droidnatch.threads.list.entities;

import java.util.List;

import org.denevell.droidnatch.app.interfaces.TypeAdapter;

public class ListThreadsToList implements TypeAdapter<ListThreadsResource, List<ThreadResource>> {
        @Override 
        public List<ThreadResource> convert(ListThreadsResource ob) {
            return ob.getThreads();
        }
}
