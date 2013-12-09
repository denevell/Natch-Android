package org.denevell.droidnatch.addthread;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.addthread.entities.AddPostResourceInput;
import org.denevell.droidnatch.addthread.entities.AddPostResourceReturnData;
import org.denevell.droidnatch.addthread.views.TextEditablePostUpdater;
import org.denevell.droidnatch.app.baseclasses.BaseService;
import org.denevell.droidnatch.app.baseclasses.TextEditableEditText;
import org.denevell.droidnatch.app.baseclasses.VolleyRequestPUTImpl;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ResponseConverter;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.TextEditable;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;
import dagger.Module;
import dagger.Provides;

@Module(injects = {MainPageActivity.class}, complete = false)
public class AddThreadMapper {
    
    private Activity mActivity;

    public AddThreadMapper(Activity activity) {
        mActivity = activity;
    }

    @Provides @Singleton @Named("addthread")
    public Controller providesLoginController(
            ServiceFetcher<AddPostResourceReturnData> service, 
            TextEditable textInput) {
        AddThreadController controller = 
                new AddThreadController(
                        textInput, 
                        null, 
                        service);
        return controller;
    }

    @Provides @Singleton
    public ServiceFetcher<AddPostResourceReturnData> providesService(
            TextEditable textInput, 
            Context appContext, 
            ProgressIndicator progress, 
            ResponseConverter converter, 
            FailureResultFactory failureFactory, 
            @Named("addthreadrequest") VolleyRequest volleyRequest,
            AddPostResourceInput resourceInput) {
        return new BaseService<AddPostResourceReturnData>(
                appContext, 
                appContext.getString(R.string.url_baseurl) + appContext.getString(R.string.url_addthread), 
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
    
    @Provides @Singleton @Named("addthreadrequest")
    public VolleyRequest providesVolleyRequestPut(
            ResponseConverter reponseConverter,
            AddPostResourceInput body) {
        VolleyRequestPUTImpl vollyRequest = new VolleyRequestPUTImpl(
                reponseConverter, 
                body);
        return vollyRequest;
    } 

    @Provides @Singleton
    public TextEditable providesTextInput(final AddPostResourceInput resourceInput) {
        final EditText editText = (EditText) mActivity.findViewById(R.id.editText1);
        TextEditableEditText textInput = new TextEditablePostUpdater(editText, resourceInput);
        return textInput;
    }

}
