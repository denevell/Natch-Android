package org.denevell.droidnatch.post.add;

import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.networking.BaseService;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestPUTImpl;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.thread.add.entities.AddPostResourceInput;
import org.denevell.droidnatch.thread.add.entities.AddPostResourceReturnData;
import org.denevell.natch.android.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import dagger.Module;
import dagger.Provides;

@Module(injects = {ListPostsFragment.class}, complete = false, library=true)
public class AddPostServicesMapper {
    
    private Bundle mBundle;

    public AddPostServicesMapper(Fragment activity) {
        mBundle = activity.getArguments();        
    }

    @Provides @Singleton
    public ServiceFetcher<AddPostResourceReturnData> providesService(
            Context appContext, 
            ProgressIndicator progress, 
            ObjectToStringConverter converter, 
            FailureResultFactory failureFactory, 
            VolleyRequest<AddPostResourceReturnData> volleyRequest) {
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
        String threadId = mBundle.getString(ListPostsFragment.BUNDLE_KEY_THREAD_ID);
        AddPostResourceInput addPostResourceInput = new AddPostResourceInput();
        addPostResourceInput.setThreadId(threadId);
        return addPostResourceInput;
    }
    
    @Provides @Singleton 
    public VolleyRequest<AddPostResourceReturnData> providesRequest(
            ObjectToStringConverter reponseConverter,
            AddPostResourceInput body,
            Context appContext) {
        VolleyRequestPUTImpl<AddPostResourceReturnData> vollyRequest = 
                new VolleyRequestPUTImpl<AddPostResourceReturnData>(
                    reponseConverter, 
                    body);
        String url = appContext.getString(R.string.url_baseurl) + appContext.getString(R.string.url_add_post);
        vollyRequest.setUrl(url);
        return vollyRequest;
    } 

}
