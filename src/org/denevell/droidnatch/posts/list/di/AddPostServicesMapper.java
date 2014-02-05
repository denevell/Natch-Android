package org.denevell.droidnatch.posts.list.di;

import android.content.Context;
import android.os.Bundle;

import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.networking.BaseService;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestPUTImpl;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.posts.list.views.AddPostTextEditGenericUiEvent;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceInput;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;
import org.denevell.natch.android.R;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = {ListPostsFragment.class, AddPostTextEditGenericUiEvent.class}, complete = false, library=true)
public class AddPostServicesMapper {
    
    private Bundle mBundle;

    public AddPostServicesMapper(Bundle activity) {
        mBundle = activity;
    }

    @Provides @Singleton
    public ServiceFetcher<AddPostResourceReturnData> providesService(
            ProgressIndicator progress,
            ObjectToStringConverter converter, 
            FailureResultFactory failureFactory, 
            VolleyRequest<AddPostResourceReturnData> volleyRequest) {
        return new BaseService<AddPostResourceReturnData>(
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
        vollyRequest.addHeader("AuthKey", Urls.getAuthKey());
        String url = Urls.getBasePath() + appContext.getString(R.string.url_add_post);
        vollyRequest.setUrl(url);
        return vollyRequest;
    } 

}
