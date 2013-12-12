package org.denevell.droidnatch.posts.list;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.OnPressObserver;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module(injects = {MainPageActivity.class}, complete = false)
public class ListPostsMapper {
    
    public ListPostsMapper(Context activity) {
    }

    @Provides @Singleton @Named("listposts")
    public Controller providesListsPostsController(
            OnPressObserver<ThreadResource> onPressObserver, 
            ScreenOpener screenCreator) {
        ListPostsController controller = new ListPostsController(
                onPressObserver, 
                screenCreator);
        return controller;
    }


}
