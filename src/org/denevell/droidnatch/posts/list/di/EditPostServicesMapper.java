package org.denevell.droidnatch.posts.list.di;

import android.content.Context;

import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.networking.BaseService;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestPOST;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.posts.list.entities.EditPostResource;
import org.denevell.droidnatch.posts.list.entities.EditPostResourceReturnData;
import org.denevell.natch.android.R;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(complete = false, library=true)
public class EditPostServicesMapper {

    public EditPostServicesMapper() {
    }

    @Provides @Singleton
    public ServiceFetcher<EditPostResourceReturnData> providesService(
            ProgressIndicator progress,
            ObjectToStringConverter converter, 
            FailureResultFactory failureFactory, 
            VolleyRequest<EditPostResourceReturnData> volleyRequest) {
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
    public VolleyRequest<EditPostResourceReturnData> providesRequest(
            ObjectToStringConverter reponseConverter,
            EditPostResource body,
            Context appContext) {
        VolleyRequestPOST<EditPostResourceReturnData> volleyRequest =
                new VolleyRequestPOST<EditPostResourceReturnData>(
                    reponseConverter, 
                    body);
        volleyRequest.addHeader("AuthKey", Urls.getAuthKey());
        String url = Urls.getBasePath() + appContext.getString(R.string.url_edit_post);
        volleyRequest.setUrl(url);
        return volleyRequest;
    } 

}
