package org.denevell.droidnatch.app.baseclasses;

import javax.inject.Singleton;

import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.posts.list.uievents.AddPostTextEditActivator;
import org.denevell.droidnatch.posts.list.uievents.EditPostViewActivator;
import org.denevell.droidnatch.posts.list.uievents.EditThreadViewActivator;
import org.denevell.droidnatch.posts.list.uievents.ListPostsViewStarter;
import org.denevell.droidnatch.posts.list.uievents.LongClickDeletePostActivator;
import org.denevell.droidnatch.posts.list.uievents.LongClickDeleteThreadActivator;
import org.denevell.droidnatch.posts.list.uievents.PreviousScreenReceiver;
import org.denevell.droidnatch.threads.list.ListThreadsFragment;
import org.denevell.droidnatch.threads.list.uievents.AddThreadViewActivator;
import org.denevell.droidnatch.threads.list.uievents.ListThreadsViewStarter;
import org.denevell.droidnatch.threads.list.uievents.LoginViewActivator;
import org.denevell.droidnatch.threads.list.uievents.LongClickDeleteActivator;
import org.denevell.droidnatch.threads.list.uievents.OpenNewThreadReceiver;
import org.denevell.droidnatch.threads.list.uievents.RegisterViewActivator;

import android.app.Activity;
import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module(injects = {
		LoginViewActivator.class,
		RegisterViewActivator.class,
		AddPostTextEditActivator.class,
		AddThreadViewActivator.class,
        PreviousScreenReceiver.class,
        EditPostViewActivator.class,
        EditThreadViewActivator.class,
        LongClickDeletePostActivator.class,
        LongClickDeleteActivator.class,
        ListPostsViewStarter.class,
        ListThreadsFragment.class,
        ListThreadsViewStarter.class,
        OpenNewThreadReceiver.class,
        LongClickDeleteThreadActivator.class},
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
