package org.denevell.droidnatch.posts.list.di;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.controllers.ServiceCallThenDisplayController;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.posts.list.entities.ListPostsResource;
import org.denevell.droidnatch.posts.list.entities.ListPostsResourceToArrayList;
import org.denevell.droidnatch.posts.list.entities.PostResource;

import dagger.Module;
import dagger.Provides;

@Module(injects = {ListPostsFragment.class}, complete = false)
public class ListPostsControllerMapper {
   
    public static final String PROVIDES_LIST_POSTS = "list_posts";

    public ListPostsControllerMapper() {
    }
    
    @Provides @Named(PROVIDES_LIST_POSTS) @Singleton
    public Controller providesController(
            ServiceFetcher<ListPostsResource> listPostsService, 
            ResultsDisplayer<List<PostResource>> resultsPane) {
        ServiceCallThenDisplayController<ListPostsResource, List<PostResource>> controller = 
                new ServiceCallThenDisplayController<ListPostsResource, List<PostResource>>(
                listPostsService, 
                resultsPane,
                new ListPostsResourceToArrayList());
        return controller;
    }    
    
}
