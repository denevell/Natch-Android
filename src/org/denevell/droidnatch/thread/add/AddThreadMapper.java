package org.denevell.droidnatch.thread.add;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.BaseService;
import org.denevell.droidnatch.app.baseclasses.UiEventThenServiceCallController;
import org.denevell.droidnatch.app.baseclasses.VolleyRequestPUTImpl;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.GenericUiObservable;
import org.denevell.droidnatch.app.interfaces.ObjectStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.thread.add.adapters.AddTextEditTextGenericUiEvent;
import org.denevell.droidnatch.thread.add.entities.AddPostResourceInput;
import org.denevell.droidnatch.thread.add.entities.AddPostResourceReturnData;
import org.denevell.droidnatch.threads.list.ListThreadsMapper;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.droidnatch.threads.list.views.ListThreadsFragment;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;
import dagger.Module;
import dagger.Provides;

@Module(injects = {ListThreadsFragment.class}, complete = false)
public class AddThreadMapper {
    
    private static final String PROVIDES_ADD_THREAD = "addthread";
    private Activity mActivity;

    public AddThreadMapper(Activity activity) {
        mActivity = activity;
    }

    @Provides @Singleton @Named(PROVIDES_ADD_THREAD)
    public Controller providesLoginController(
            ServiceFetcher<AddPostResourceReturnData> service, 
            GenericUiObservable uiEvent, 
            ResultsDisplayer<List<ThreadResource>> listThreadsDisplayable,
            @Named(ListThreadsMapper.PROVIDES_LIST_THREADS) Controller listThreadsController,
            final AddPostResourceInput resourceInput) {
        UiEventThenServiceCallController controller = 
                new UiEventThenServiceCallController(
                        uiEvent, 
                        service,
                        listThreadsDisplayable,
                        listThreadsController);
        return controller;
    }

    @Provides @Singleton
    public ServiceFetcher<AddPostResourceReturnData> providesService(
            Context appContext, 
            ProgressIndicator progress, 
            ObjectStringConverter converter, 
            FailureResultFactory failureFactory, 
            VolleyRequest<AddPostResourceReturnData> volleyRequest,
            AddPostResourceInput resourceInput) {
        return new BaseService<AddPostResourceReturnData>(
                appContext, 
                volleyRequest,
                progress, 
                converter, 
                failureFactory, 
                AddPostResourceReturnData.class);
    }

    @Provides @Singleton
    public AddPostResourceInput providesThreadInput() {
        return new AddPostResourceInput();
    }
    
    @Provides @Singleton 
    public VolleyRequest<AddPostResourceReturnData> providesRequest(
            ObjectStringConverter reponseConverter,
            AddPostResourceInput body,
            Context appContext) {
        VolleyRequestPUTImpl<AddPostResourceReturnData> vollyRequest = 
                new VolleyRequestPUTImpl<AddPostResourceReturnData>(
                    reponseConverter, 
                    body);
        String url = appContext.getString(R.string.url_baseurl) + appContext.getString(R.string.url_addthread);
        vollyRequest.setUrl(url);
        return vollyRequest;
    } 

    @Provides @Singleton
    public GenericUiObservable providesEditTextUiEvent(
            EditText textInput, 
            AddPostResourceInput resourceInput) {
        GenericUiObservable genericUiEvent = 
                new AddTextEditTextGenericUiEvent(
                        textInput, 
                        resourceInput)
            .getGenericUiEvent();
        return genericUiEvent;
    }
    
    @Provides @Singleton
    public EditText providesEditText() {
        final EditText editText = (EditText) mActivity.findViewById(R.id.editText1);
        return editText;
    }

}
