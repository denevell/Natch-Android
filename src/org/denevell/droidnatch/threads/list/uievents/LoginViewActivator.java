package org.denevell.droidnatch.threads.list.uievents;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.baseclasses.networking.ServiceBuilder;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Finishable;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.threads.list.entities.LoginResourceInput;
import org.denevell.droidnatch.threads.list.entities.LoginResourceReturnData;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;

import dagger.ObjectGraph;

public class LoginViewActivator extends LinearLayout implements
        Activator<LoginResourceReturnData>, 
        View.OnClickListener,
        Finishable {

    public class LoginSuccessfulEvent {
		public LoginResourceReturnData result;
		public LoginSuccessfulEvent(LoginResourceReturnData result) {
			this.result = result;
		}

	}

	private GenericUiObserver mCallback;
    private Runnable mSuccessCallback;
	private Button mButton;
	private EditText mUsername;
	private EditText mPassword;
	private ServiceFetcher<LoginResourceInput, LoginResourceReturnData> mLoginService;

    public LoginViewActivator(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.login_layout, this, true);
    }

    private void inject() {
        ObjectGraph.create(
                new CommonMapper((Activity) getContext())
        ).inject(this);
    }

    @SuppressWarnings("unchecked")
	@Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        inject();
		Activity act = (Activity) getContext();
        mButton  = (Button) findViewById(R.id.login_button);
        mButton.setOnClickListener(this);
        mUsername = (EditText) findViewById(R.id.login_username_edittext);
        mPassword = (EditText) findViewById(R.id.login_password_edittext);
        
        String url = Urls.getBasePath()+getContext().getString(R.string.url_login);
		mLoginService = new ServiceBuilder<LoginResourceInput, LoginResourceReturnData>()
        		.url(url)
        		.method(Request.Method.POST)
        		.entity(new LoginResourceInput())
        		.create(act, LoginResourceReturnData.class);
        
        Receiver<LoginResourceReturnData> things = null;
		new UiEventThenServiceThenUiEvent<LoginResourceReturnData>(
                this,
                mLoginService,
                null,
                things).setup();
    }
    
    @Override
    public void setOnSubmitObserver(GenericUiObserver observer) {
        mCallback = observer;
    }

    @Override
    public void success(LoginResourceReturnData result) {
        if(mSuccessCallback!=null) mSuccessCallback.run();
        Urls.setAuthKey(result.getAuthKey());
        if(result!=null && result.isSuccessful()) {
        	Urls.setUsername(mUsername.getText().toString());
        	EventBus.getBus().post(new LoginSuccessfulEvent(result));
        	Context context = getContext();
			if(context instanceof FragmentActivity) {
        		((FragmentActivity)context).supportInvalidateOptionsMenu();
        	}
        }
    }

    @Override
    public void fail(FailureResult f) {
        if(f!=null && f.getErrorMessage()!=null) {
        	mUsername.setError("Login failed.");
        }
    }

	@Override
	public void setFinishedCallback(Runnable runnable) {
        mSuccessCallback = runnable;
	}

    @Override
    public void onClick(View view) {
        mLoginService.getRequest().getBody().setPassword(mPassword.getText().toString());
        mLoginService.getRequest().getBody().setUsername(mUsername.getText().toString());
        mCallback.onUiEventActivated();
    }

}