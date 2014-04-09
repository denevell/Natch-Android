package org.denevell.droidnatch.threads.list.uievents;

import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.baseclasses.networking.ServiceBuilder;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.ButtonWithProgress;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.newfivefour.android.manchester.R;

public class ResetPasswordRequestViewActivator extends LinearLayout implements Activator<Void>, View.OnClickListener {

	private GenericUiObserver mCallback;
	private Runnable mSuccessCallback;
	private ButtonWithProgress mButton;
	private ServiceFetcher<Void, Void> mService;
	private EditText mRecoveryEmail;

	public ResetPasswordRequestViewActivator(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.reset_password_layout, this, true);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		
		mRecoveryEmail = (EditText) findViewById(R.id.password_reset_recovery_email_editext);
		mButton = (ButtonWithProgress) findViewById(R.id.password_reset_button);
		mButton.setOnClickListener(this);
		// Setup service
		String url = ShamefulStatics.getBasePath() + getContext().getString(R.string.url_reset_password);
		mService = new ServiceBuilder<Void, Void>()
				.url(url).method(Request.Method.POST)
				.create((Activity) getContext(), Void.class);
		Receiver<Void>[] args = null;
		new UiEventThenServiceThenUiEvent<Void>(this,
				mService,
				null,
				args).setup();
	}

	@Override
	public void setOnSubmitObserver(GenericUiObserver observer) {
		mCallback = observer;
	}

	@Override
	public void success(Void result) {
		if(mButton!=null) mButton.loadingStop();
		if (mSuccessCallback != null)
			mSuccessCallback.run();
	}

	@Override
	public void fail(FailureResult f) {
		if(mButton!=null) mButton.loadingStop();
		if(f!=null && f.getStatusCode()==400) {
        	mRecoveryEmail.setError(getContext().getString(R.string.pw_reset_400_error));
		}
		if (f != null && f.getErrorMessage() != null) {
			mRecoveryEmail.setError("Password reset request failed.");
		}
	}

	@Override
	public void onClick(View view) {
		if(mRecoveryEmail==null || mRecoveryEmail.getText()==null || mService==null) {
			return;
		}
		String recoveryEmail = mRecoveryEmail.getText().toString();
		if(mButton!=null) mButton.loadingStart();
		String url = ShamefulStatics.getBasePath() 
				+ getContext().getString(R.string.url_reset_password);
		mService.setUrl( url + recoveryEmail);
		mCallback.onUiEventActivated();
	}

}
