package org.denevell.natch.android;

import android.app.Activity;
import android.content.Context;
import android.widget.Button;
import dagger.Module;
import dagger.Provides;

@Module(injects = {MainActivity.class})
public class ObjectMapper {
    
    private Activity mActivity;

    public ObjectMapper(Activity activity) {
        mActivity = activity;
    }
    
    @Provides
    LoginService provideLoginService() {
        ProgressIndicator progress = new ProgressIndicator(mActivity);
        Context context = mActivity.getApplicationContext();
        LoginResultDisplayer displayer = new LoginResultDisplayer(mActivity.getApplicationContext());
        return new LoginService(context, progress, displayer);
    }

    @Provides
    Button provideLoginButton() {
        return (Button) mActivity.findViewById(R.id.button1);
    }


}
