package org.denevell.droidnatch.threads.list.uievents;

import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.baseclasses.networking.ServiceBuilder;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Finishable;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.threads.list.entities.RegisterResourceInput;
import org.denevell.droidnatch.threads.list.entities.RegisterResourceReturnData;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;

import dagger.ObjectGraph;

public class RegisterViewActivator extends LinearLayout implements
        Activator<RegisterResourceReturnData>, 
        View.OnClickListener,
        Finishable {

	private GenericUiObserver mCallback;
    private Runnable mSuccessCallback;
	private Button mButton;
	private EditText mUsername;
	private EditText mPassword;
	private ServiceFetcher<RegisterResourceInput, RegisterResourceReturnData> mRegisterService;

    public RegisterViewActivator(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.register_layout, this, true);
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
        mButton  = (Button) findViewById(R.id.register_button);
        mButton.setOnClickListener(this);
        mUsername = (EditText) findViewById(R.id.register_username_edittext);
        mPassword = (EditText) findViewById(R.id.register_password_edittext);
        
        String url = Urls.getBasePath()+getContext().getString(R.string.url_register);
		mRegisterService = new ServiceBuilder<RegisterResourceInput, RegisterResourceReturnData>()
        		.url(url)
        		.method(Request.Method.PUT)
        		.entity(new RegisterResourceInput())
        		.create(act, RegisterResourceReturnData.class);
        
        Receiver<RegisterResourceReturnData> things = null;
		new UiEventThenServiceThenUiEvent<RegisterResourceReturnData>(
                this,
                mRegisterService,
                null,
                things).setup();
    }
    
    @Override
    public void setOnSubmitObserver(GenericUiObserver observer) {
        mCallback = observer;
    }

    @Override
    public void success(RegisterResourceReturnData result) {
        if(mSuccessCallback!=null) mSuccessCallback.run();
    }

	@Override
	public void setFinishedCallback(Runnable runnable) {
        mSuccessCallback = runnable;
	}

    @Override
    public void fail(FailureResult f) {
        if(f!=null && f.getErrorMessage()!=null) {
        	mUsername.setError("Register failed.");
        }
    }

    @Override
    public void onClick(View view) {
        mRegisterService.getRequest().getBody().setPassword(mPassword.getText().toString());
        mRegisterService.getRequest().getBody().setUsername(mUsername.getText().toString());
        mCallback.onUiEventActivated();
    }

}
