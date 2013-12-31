package org.denevell.droidnatch.thread.add.di;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceCallController;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.GenericUiObservable;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.thread.add.entities.AddPostResourceInput;
import org.denevell.droidnatch.thread.add.entities.AddPostResourceReturnData;
import org.denevell.droidnatch.thread.add.uievents.AddThreadTextEditUiEvent;
import org.denevell.droidnatch.threads.list.ListThreadsFragment;
import org.denevell.droidnatch.threads.list.di.ListThreadsControllerMapper;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.widget.EditText;
import dagger.Module;
import dagger.Provides;

@Module(injects = {ListThreadsFragment.class}, complete = false)
public class AddThreadControllerMapper {
    
    public static final String PROVIDES_ADD_THREAD = "addthread";
    private Activity mActivity;

    public AddThreadControllerMapper(Activity activity) {
        mActivity = activity;
    }

    @Provides @Singleton @Named(PROVIDES_ADD_THREAD)
    public Controller providesLoginController(
            ServiceFetcher<AddPostResourceReturnData> service, 
            GenericUiObservable uiEvent, 
            ResultsDisplayer<List<ThreadResource>> listThreadsDisplayable,
            @Named(ListThreadsControllerMapper.PROVIDES_LIST_THREADS) Controller listThreadsController) {
        UiEventThenServiceCallController controller = 
                new UiEventThenServiceCallController(
                        uiEvent, 
                        service,
                        listThreadsDisplayable,
                        listThreadsController);
        return controller;
    }

    @Provides @Singleton
    public GenericUiObservable providesEditTextUiEvent(
            AddPostResourceInput resourceInput) {
        EditText editText = (EditText) mActivity.findViewById(R.id.editText1);
        return new AddThreadTextEditUiEvent(
                        editText, 
                        resourceInput);
    }
    
}
