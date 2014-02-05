package org.denevell.droidnatch.app.baseclasses;

import android.app.Activity;
import android.content.Context;

import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.posts.list.views.AddPostTextEditGenericUiEvent;
import org.denevell.droidnatch.threads.list.views.AddThreadEditTextActivator;
import org.denevell.droidnatch.threads.list.views.ListThreadsView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = {AddThreadEditTextActivator.class, ListThreadsView.class, AddPostTextEditGenericUiEvent.class},
        complete = false,
        library = true)
public class CommonMapper {
    
    private Activity mActivityContext;
    private Context mAppContext;
    
    public CommonMapper(Activity c) {
        mActivityContext = c;
        mAppContext = c.getApplicationContext();
    }

    @Provides @Singleton
    public Context providesAppContext() {
        return mAppContext;
    }
    
    @Provides @Singleton
    public ProgressIndicator providesProgress() {
        ProgressBarIndicator progress = new ProgressBarIndicator(mActivityContext);
        return progress;
    }

    @Provides
    public ObjectToStringConverter providesResponseConverter() {
        return new JsonConverter();
    }
    
    @Provides
    public FailureResultFactory providesFailureFactory() {
        return new FailureResultFactory() {
            @Override
            public FailureResult newInstance(int statusCode, String errorMessage, String errorCode) {
                return new FailureResult(errorCode, errorMessage, statusCode);
            }
        };
    }
    
}
