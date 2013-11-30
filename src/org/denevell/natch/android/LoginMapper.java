package org.denevell.natch.android;

import org.denevell.natch.android.service.LoginService;
import org.denevell.natch.android.views.LoginResultDisplayer;
import org.denevell.natch.android.views.ProgressIndicator;

import android.app.Activity;
import android.content.Context;
import android.widget.Button;
import dagger.Module;
import dagger.Provides;

@Module(injects = {MainActivity.class})
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
        return new LoginService(context, progress, displayer);
    }

    @Provides
    Button provideLoginButton() {
        return (Button) mActivity.findViewById(R.id.button1);
    }

}
