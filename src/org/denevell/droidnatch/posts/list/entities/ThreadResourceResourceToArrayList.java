package org.denevell.droidnatch.posts.list.entities;

import java.util.List;

import org.denevell.droidnatch.app.interfaces.TypeAdapter;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

public class ThreadResourceResourceToArrayList implements
        TypeAdapter<ThreadResource, List<PostResource>> {

    @Override
    public List<PostResource> convert(ThreadResource ob) {
        return ob.getPosts();
    }

}
