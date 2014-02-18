package org.denevell.droidnatch.threads.list.uievents;

import javax.inject.Inject;

import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.threads.list.di.LoginServicesMapper;
import org.denevell.droidnatch.threads.list.entities.LoginResourceInput;
import org.denevell.droidnatch.threads.list.entities.LoginResourceReturnData;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import dagger.ObjectGraph;

public class LoginViewActivator extends LinearLayout implements
        Activator<LoginResourceReturnData>, View.OnClickListener {

    private GenericUiObserver mCallback;
    private Runnable mSuccessCallback;
	private Button mButton;
	private EditText mUsername;
	private EditText mPassword;
	@Inject LoginResourceInput mLoginInput;
	@Inject ServiceFetcher<LoginResourceReturnData> mLoginService;

    public LoginViewActivator(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.login_layout, this, true);
    }

    private void inject() {
        ObjectGraph.create(
                new CommonMapper((Activity) getContext()),
                new LoginServicesMapper()
        ).inject(this);
    }

    @SuppressWarnings("unchecked")
	@Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        inject();
        mButton  = (Button) findViewById(R.id.login_button);
        mButton.setOnClickListener(this);
        mUsername = (EditText) findViewById(R.id.login_username_edittext);
        mPassword = (EditText) findViewById(R.id.login_password_edittext);
        new UiEventThenServiceThenUiEvent<LoginResourceReturnData>(
                this,
                mLoginService,
                null,
                null).setup();
    }
    
    @Override
    public void setOnSubmitObserver(GenericUiObserver observer) {
        mCallback = observer;
    }

    @Override
    public void success(LoginResourceReturnData result) {
        if(mSuccessCallback!=null) mSuccessCallback.run();
        Urls.setAuthKey(result.getAuthKey());
    }

    public void setSuccessCallback(Runnable runnable) {
        mSuccessCallback = runnable;
    }

    @Override
    public void fail(FailureResult f) {
        if(f!=null && f.getErrorMessage()!=null) {
        	mUsername.setError("Login failed.");
        }
    }

    @Override
    public void onClick(View view) {
        mLoginInput.setPassword(mPassword.getText().toString());
        mLoginInput.setUsername(mUsername.getText().toString());
        mCallback.onUiEventActivated();
    }

}
