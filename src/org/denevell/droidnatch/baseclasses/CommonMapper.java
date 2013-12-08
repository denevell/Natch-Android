package org.denevell.droidnatch.baseclasses;

import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.JsonConverter;
import org.denevell.droidnatch.app.baseclasses.ProgressBarIndicator;
import org.denevell.droidnatch.app.baseclasses.VolleyRequestGET;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ResponseConverter;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.app.interfaces.VolleyRequestPUT;

import android.app.Activity;
import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module(complete = false, 
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
    public VolleyRequest providesVolleyRequest() {
        return new VolleyRequestGET();
    }

    @Provides 
    public VolleyRequestPUT providesVolleyRequestPut(ResponseConverter reponseConverter) {
        return new org.denevell.droidnatch.app.baseclasses.VolleyRequestPUT(reponseConverter);
    }
    
    @Provides
    public ResponseConverter providesResponseConverter() {
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
