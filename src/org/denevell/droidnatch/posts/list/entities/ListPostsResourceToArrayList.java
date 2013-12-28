package org.denevell.droidnatch.posts.list.entities;

import java.util.List;

import org.denevell.droidnatch.app.interfaces.TypeAdapter;

public class ListPostsResourceToArrayList implements
        TypeAdapter<ListPostsResource, List<PostResource>> {

    @Override
    public List<PostResource> convert(ListPostsResource ob) {
        return ob.getPosts();
    }

}
