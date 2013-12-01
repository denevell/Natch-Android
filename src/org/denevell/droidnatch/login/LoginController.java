package org.denevell.droidnatch.login;

import org.denevell.droidnatch.baseclasses.FailureResult;
import org.denevell.droidnatch.interfaces.Clickable;
import org.denevell.droidnatch.interfaces.Controller;
import org.denevell.droidnatch.interfaces.ResultsDisplayable;
import org.denevell.droidnatch.interfaces.ServiceCallable;
import org.denevell.droidnatch.interfaces.ServiceCallable.Failure;
import org.denevell.droidnatch.interfaces.ServiceCallable.Success;

public class LoginController implements Controller {
    
    private ServiceCallable<String> mLoginService;
    private Clickable mLoginButton;
    private ResultsDisplayable<String> mResultsDisplayable;

    public LoginController(ServiceCallable<String> loginService, Clickable loginButton, ResultsDisplayable<String> resultsDisplayable) {
        mLoginService = loginService;
        mLoginButton = loginButton;
        mResultsDisplayable = resultsDisplayable;
    }
    
    public void go() {
        mLoginButton.setOnClick(new Runnable() {
            @Override
            public void run() {
                mLoginService.go();
            }
        });
        mLoginService.setSuccessCallback(new Success<String>() {
            @Override
            public void success(String success) {
                mResultsDisplayable.onSuccess(success);
            }
        });
        mLoginService.setFailureCallback(new Failure() {
            @Override
            public void fail(FailureResult f) {
                mResultsDisplayable.onFail(f);
            }
        });
    }

}
