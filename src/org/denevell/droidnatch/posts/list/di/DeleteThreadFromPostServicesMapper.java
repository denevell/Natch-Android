package org.denevell.droidnatch.posts.list.di;

import java.util.Map;

import javax.inject.Named;
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
import org.denevell.droidnatch.threads.list.entities.DeletePostResourceReturnData;
import org.denevell.natch.android.R;

import android.content.Context;

import com.android.volley.Request;

import dagger.Module;
import dagger.Provides;

@Module(complete = false, library=true)
public class DeleteThreadFromPostServicesMapper {

    public static final String DELETE_THREAD_FROM_POST_SERVICE = "delete thread from post service";
    public static final String DELETE_THREAD_FROM_VOLLEY_REQUEST = "delete thread from volley request";

    public DeleteThreadFromPostServicesMapper() {
    }
  
    @Provides @Named(DELETE_THREAD_FROM_POST_SERVICE)
    public ServiceFetcher<DeletePostResourceReturnData> providesService(
            ProgressIndicator progress,
            ObjectToStringConverter converter, 
            FailureResultFactory failureFactory,
            @Named(DELETE_THREAD_FROM_VOLLEY_REQUEST) VolleyRequest<Void, DeletePostResourceReturnData> volleyRequest) {
        return new BaseService<DeletePostResourceReturnData>(
                volleyRequest,
                progress, 
                converter, 
                failureFactory, 
                DeletePostResourceReturnData.class);
    }

    @Provides @Singleton @Named(DELETE_THREAD_FROM_VOLLEY_REQUEST)
    public VolleyRequest<Void, DeletePostResourceReturnData> providesVolleyRequestDelete(
            ObjectToStringConverter reponseConverter,
            Context appContext) {
        String url = Urls.getBasePath() + appContext.getString(R.string.url_del);
        VolleyRequestImpl<Void, DeletePostResourceReturnData> vollyRequest = 
        		new VolleyRequestImpl<Void, DeletePostResourceReturnData>(null, null, 
                        Request.Method.DELETE);
        vollyRequest.setUrl(url);
        vollyRequest.addLazyHeader(new LazyHeadersCallback() {
			@Override
			public void run(Map<String, String> headersMap) {
				headersMap.put("AuthKey", Urls.getAuthKey());
			}
		});
        return vollyRequest;
    } 

}