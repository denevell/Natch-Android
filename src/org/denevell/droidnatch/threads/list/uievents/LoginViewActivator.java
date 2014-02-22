package org.denevell.droidnatch.threads.list.uievents;

import javax.inject.Inject;

import org.denevell.droidnatch.AppWideMapper;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Finishable;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.threads.list.ListThreadsMapper;
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
import android.widget.TextView;
import dagger.ObjectGraph;

public class LoginViewActivator extends LinearLayout implements
		Activator<LoginResourceReturnData>, View.OnClickListener, Finishable {

	public static class RefreshOptionsMenuReceiver implements Receiver<LoginResourceReturnData> {
		@SuppressWarnings("unused")
		private static final String TAG = RefreshOptionsMenuReceiver.class .getSimpleName();
		private FragmentActivity mActivity;
		public RefreshOptionsMenuReceiver(FragmentActivity act) {
			mActivity = act;
		}
		@Override public void success(LoginResourceReturnData obj) {
			if (mActivity != null) {
				mActivity.supportInvalidateOptionsMenu();
			}
		}
		@Override public void fail(FailureResult r) { }
	}

	public static class UpdateLoginInfoReceiver implements Receiver<LoginResourceReturnData> {
		@SuppressWarnings("unused") private static final String TAG = RefreshOptionsMenuReceiver.class .getSimpleName();
		private TextView mUsername;
		public UpdateLoginInfoReceiver(TextView username) {
			mUsername = username;
		}
		@Override public void success(LoginResourceReturnData result) {
			Urls.setAuthKey(result.getAuthKey());
			Urls.setUsername(mUsername.getText().toString());
		}
		@Override public void fail(FailureResult r) { }
	}

	private GenericUiObserver mCallback;
	private Runnable mSuccessCallback;
	private Button mButton;
	private EditText mUsername;
	private EditText mPassword;
	@Inject ServiceFetcher<LoginResourceInput, LoginResourceReturnData> mLoginService;

	public LoginViewActivator(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.login_layout, this, true);
	}

	private void inject() {
		Activity activity = (Activity) getContext();
		ObjectGraph.create(
				new CommonMapper(activity),
				AppWideMapper.getInstance(),
				new ListThreadsMapper(activity)
				).inject(
				this);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		inject();
		FragmentActivity act = (FragmentActivity) getContext();
		mButton = (Button) findViewById(R.id.login_button);
		mButton.setOnClickListener(this);
		mUsername = (EditText) findViewById(R.id.login_username_edittext);
		mPassword = (EditText) findViewById(R.id.login_password_edittext);

		new UiEventThenServiceThenUiEvent<LoginResourceReturnData>(
				this,
				mLoginService, 
				null, 
				new RefreshOptionsMenuReceiver(act),
				new UpdateLoginInfoReceiver(mUsername)).setup();
	}

	@Override
	public void setOnSubmitObserver(GenericUiObserver observer) {
		mCallback = observer;
	}

	@Override
	public void success(LoginResourceReturnData result) {
		if (mSuccessCallback != null)
			mSuccessCallback.run();
	}

	@Override
	public void fail(FailureResult f) {
		if (f != null && f.getErrorMessage() != null) {
			mUsername.setError("Login failed.");
		}
	}

	@Override
	public void setFinishedCallback(Runnable runnable) {
		mSuccessCallback = runnable;
	}

	@Override
	public void onClick(View view) {
		mLoginService.getRequest().getBody()
				.setPassword(mPassword.getText().toString());
		mLoginService.getRequest().getBody()
				.setUsername(mUsername.getText().toString());
		mCallback.onUiEventActivated();
	}

}
