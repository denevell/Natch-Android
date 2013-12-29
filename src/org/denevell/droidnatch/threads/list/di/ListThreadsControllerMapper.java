package org.denevell.droidnatch.threads.list.di;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.controllers.ServiceCallThenDisplayController;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.OnPressObserver.OnPress;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.threads.list.ListThreadsFragment;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResourceToArrayList;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

import dagger.Module;
import dagger.Provides;

@Module(injects = {ListThreadsFragment.class}, complete=false, library=true)
public class ListThreadsControllerMapper {
    
    public static final String PROVIDES_LIST_THREADS = "list_threads";
    protected static final String TAG = ListThreadsControllerMapper.class.getSimpleName();

    public ListThreadsControllerMapper() {
    }
    
    @Provides @Singleton @Named(PROVIDES_LIST_THREADS)
    public Controller providesController(
            ServiceFetcher<ListThreadsResource> listThreadsService, 
            ResultsDisplayer<List<ThreadResource>> resultsPane, 
            // We're taking in the OnPress simply so it's constructed.
            @Named(ListThreadsResultsDisplayableMapper.PROVIDES_LIST_THREADS_LIST_CLICK) OnPress<ThreadResource> listClickListener) {
        ServiceCallThenDisplayController<ListThreadsResource, List<ThreadResource>> controller = 
                new ServiceCallThenDisplayController<ListThreadsResource, List<ThreadResource>>(
                    listThreadsService, 
                    resultsPane,
                    new ListThreadsResourceToArrayList()
                    );
        return controller;
    }

}
