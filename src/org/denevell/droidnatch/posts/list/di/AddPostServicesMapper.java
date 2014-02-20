package org.denevell.droidnatch.posts.list.di;

import java.util.Map;

import javax.inject.Singleton;

import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.networking.BaseService;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestImpl;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestImpl.LazyHeadersCallback;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceInput;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;
import org.denevell.natch.android.R;

import android.content.Context;
import android.os.Bundle;

import com.android.volley.Request;

import dagger.Module;
import dagger.Provides;

@Module(complete = false, library=true)
public class AddPostServicesMapper {
    
    private Bundle mBundle;

    public AddPostServicesMapper(Bundle activity) {
        mBundle = activity;
    }

    @Provides @Singleton
    public ServiceFetcher<AddPostResourceInput, AddPostResourceReturnData> providesService(
            ProgressIndicator progress,
            ObjectToStringConverter converter, 
            FailureResultFactory failureFactory, 
            VolleyRequest<AddPostResourceInput, AddPostResourceReturnData> volleyRequest) {
        return new BaseService<AddPostResourceInput, AddPostResourceReturnData>(
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
    public VolleyRequest<AddPostResourceInput, AddPostResourceReturnData> providesRequest(
            ObjectToStringConverter reponseConverter,
            AddPostResourceInput body,
            Context appContext) {
        VolleyRequestImpl<AddPostResourceInput, AddPostResourceReturnData> vollyRequest = 
                new VolleyRequestImpl<AddPostResourceInput, AddPostResourceReturnData>(
                    reponseConverter, 
                    body,
                    Request.Method.PUT);
        vollyRequest.addLazyHeader(new LazyHeadersCallback() {
			@Override
			public void run(Map<String, String> headersMap) {
				headersMap.put("AuthKey", Urls.getAuthKey());
			}
		});
        String url = Urls.getBasePath() + appContext.getString(R.string.url_add_post);
        vollyRequest.setUrl(url);
        return vollyRequest;
    } 

}
