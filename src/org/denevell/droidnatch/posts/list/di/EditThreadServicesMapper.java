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
import org.denevell.droidnatch.posts.list.entities.EditPostResource;
import org.denevell.droidnatch.posts.list.entities.EditPostResourceReturnData;
import org.denevell.natch.android.R;

import android.content.Context;

import com.android.volley.Request;

import dagger.Module;
import dagger.Provides;

@Module(complete = false, library=true)
public class EditThreadServicesMapper {

    public EditThreadServicesMapper() {
    }

    @Provides @Singleton
    public ServiceFetcher<EditPostResourceReturnData> providesService(
            ProgressIndicator progress,
            ObjectToStringConverter converter, 
            FailureResultFactory failureFactory, 
            VolleyRequest<EditPostResource, EditPostResourceReturnData> volleyRequest) {
        return new BaseService<EditPostResourceReturnData>(
                volleyRequest,
                progress, 
                converter, 
                failureFactory, 
                EditPostResourceReturnData.class);
    }

    @Provides @Singleton
    public EditPostResource providesThreadInput() {
        EditPostResource editPostResource = new EditPostResource();
        return editPostResource;
    }
    
    @Provides @Singleton 
    public VolleyRequest<EditPostResource, EditPostResourceReturnData> providesRequest(
            ObjectToStringConverter reponseConverter,
            EditPostResource body,
            Context appContext) {
        VolleyRequestImpl<EditPostResource, EditPostResourceReturnData> volleyRequest =
                new VolleyRequestImpl<EditPostResource, EditPostResourceReturnData>(
                    reponseConverter, 
                    body,
                    Request.Method.POST);
        volleyRequest.addLazyHeader(new LazyHeadersCallback() {
			@Override
			public void run(Map<String, String> headersMap) {
				headersMap.put("AuthKey", Urls.getAuthKey());
			}
		});
        String url = Urls.getBasePath() + appContext.getString(R.string.url_edit_thread);
        volleyRequest.setUrl(url);
        return volleyRequest;
    } 

}
