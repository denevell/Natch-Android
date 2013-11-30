package org.denevell.natch.android;

import org.denevell.natch.android.service.LoginService;
import org.denevell.natch.android.views.LoginButton;
import org.denevell.natch.android.views.LoginResultDisplayer;
import org.denevell.natch.android.views.ProgressIndicator;

import android.app.Activity;
import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module(injects = {LoginButton.class})
public class LoginMapper {
    
    private Activity mActivity;

    public LoginMapper(Activity activity) {
        mActivity = activity;
    }
    
    @Provides
    LoginService provideLoginService() {
        ProgressIndicator progress = new ProgressIndicator(mActivity);
        Context context = mActivity.getApplicationContext();
        LoginResultDisplayer displayer = new LoginResultDisplayer(mActivity.getApplicationContext());
        String url = context.getString(R.string.url_baseurl) + context.getString(R.string.url_threads);
        return new LoginService(context, url, progress, displayer);
    }

}
