package org.denevell.droidnatch.posts.list.adapters;

import java.util.List;

import org.denevell.droidnatch.app.interfaces.TypeAdapter;
import org.denevell.droidnatch.posts.list.entities.ListPostsResource;
import org.denevell.droidnatch.posts.list.entities.PostResource;

public class ListPostsResourceToListAdapter implements
        TypeAdapter<ListPostsResource, List<PostResource>> {

    @Override
    public List<PostResource> convert(ListPostsResource ob) {
        return ob.getPosts();
    }

}
