package org.denevell.droidnatch.login;

import java.util.ArrayList;
import java.util.List;

import org.denevell.droidnatch.baseclasses.ProgressIndicator;
import org.denevell.droidnatch.interfaces.Clickable;
import org.denevell.droidnatch.interfaces.Controller;
import org.denevell.droidnatch.interfaces.ResultsDisplayable;
import org.denevell.droidnatch.interfaces.ServiceCallable;
import org.denevell.droidnatch.login.service.LoginService;
import org.denevell.droidnatch.login.views.LoginButton;
import org.denevell.droidnatch.login.views.LoginResultDisplayer;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module(injects = {LoginActivity.class})
public class LoginMapper {
    
    private Activity mActivity;

    public LoginMapper(Activity activity) {
        mActivity = activity;
    }
    
    // controllers
    
    @SuppressWarnings("serial")
    @Provides
    public List<Controller> provideControllers() {
        return new ArrayList<Controller>() {{
            add(providesLoginController());
        }};
    }

    public Controller providesLoginController() {
        LoginController controller = new LoginController(
                provideLoginService(), 
                provideLoginButton(), 
                provideLoginResultPane());
        return controller;
    }
    
    // others
    
    public Clickable provideLoginButton() {
        LoginButton b = (LoginButton) mActivity.findViewById(R.id.button1);
        return b;
    }

    public ResultsDisplayable<String> provideLoginResultPane() {
        LoginResultDisplayer displayer = new LoginResultDisplayer(mActivity.getApplicationContext());
        return displayer;
    }
    
    public ServiceCallable<String> provideLoginService() {
        ProgressIndicator progress = new ProgressIndicator(mActivity);
        Context context = mActivity.getApplicationContext();
        String url = context.getString(R.string.url_baseurl) + context.getString(R.string.url_threads);
        return new LoginService(context, url, progress);
    }

}
