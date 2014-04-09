package org.denevell.droidnatch.threads.list.uievents;

import javax.inject.Inject;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.PaginationMapper;
import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Finishable;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.ButtonWithProgress;
import org.denevell.droidnatch.app.views.DialoguePopupOnMenuActivator;
import org.denevell.droidnatch.threads.list.ListThreadsMapper;
import org.denevell.droidnatch.threads.list.entities.LoginResourceInput;
import org.denevell.droidnatch.threads.list.entities.LoginResourceReturnData;
import org.denevell.droidnatch.threads.list.entities.LogoutResourceReturnData;

import com.newfivefour.android.manchester.R;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import dagger.ObjectGraph;

public class LoginViewActivator extends LinearLayout implements Activator<LoginResourceReturnData>, View.OnClickListener, Finishable {

	public static class LoginUpdatedEvent { }

	@SuppressWarnings("rawtypes")
	public static class RefreshOptionsMenuReceiver implements Receiver {
		@SuppressWarnings("unused")
		private static final String TAG = RefreshOptionsMenuReceiver.class .getSimpleName();
		private FragmentActivity mActivity;
		public RefreshOptionsMenuReceiver(FragmentActivity act) {
			mActivity = act;
		}
		@Override public void success(Object obj) {
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
			ShamefulStatics.setAuthKey(result.getAuthKey(), mUsername.getContext().getApplicationContext());
			ShamefulStatics.setUsername(mUsername.getText().toString(), mUsername.getContext().getApplicationContext());
			EventBus.getBus().post(new LoginUpdatedEvent());
		}
		@Override public void fail(FailureResult r) { }
	}

	private GenericUiObserver mCallback;
	private Runnable mSuccessCallback;
	private ButtonWithProgress mButton;
	private EditText mUsername;
	private EditText mPassword;
	@Inject ServiceFetcher<LoginResourceInput, LoginResourceReturnData> mLoginService;
	@Inject ServiceFetcher<Void, LogoutResourceReturnData> mLogoutService;

	public LoginViewActivator(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.login_layout, this, true);
	}

	private void inject() {
		Activity activity = (Activity) getContext();
		ObjectGraph.create(
				new CommonMapper(activity),
				PaginationMapper.getInstance(),
				new ListThreadsMapper(activity)
				).inject(
				this);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		inject();
		final FragmentActivity act = (FragmentActivity) getContext();
		mButton = (ButtonWithProgress) findViewById(R.id.login_button);
		mButton.setOnClickListener(this);
		mUsername = (EditText) findViewById(R.id.login_username_edittext);
		mPassword = (EditText) findViewById(R.id.login_password_edittext);
		View forgottenPasswordLink = findViewById(R.id.login_forgotten_password_link_textview);
		forgottenPasswordLink.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				DialoguePopupOnMenuActivator df = new DialoguePopupOnMenuActivator(getContext());
				df.setLayout(R.layout.reset_password_dialogue_layout);
				df.createDialogue(null);
			}
		});
		new UiEventThenServiceThenUiEvent<LoginResourceReturnData>(this,
				mLoginService, null, new UpdateLoginInfoReceiver(mUsername),
				new RefreshOptionsMenuReceiver(act)).setup();
	}

	@Override
	public void setOnSubmitObserver(GenericUiObserver observer) {
		mCallback = observer;
	}

	@Override
	public void success(LoginResourceReturnData result) {
		if(mButton!=null) mButton.loadingStop();
		if (mSuccessCallback != null)
			mSuccessCallback.run();
	}

	@Override
	public void fail(FailureResult f) {
		if(mButton!=null) mButton.loadingStop();
		if(f!=null && f.getStatusCode()==400) {
        	mUsername.setError(getContext().getString(R.string.register_400_error));
		}
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
		mLoginService.getRequest().getBody().setPassword(mPassword.getText().toString());
		mLoginService.getRequest().getBody().setUsername(mUsername.getText().toString());
		if(mButton!=null) mButton.loadingStart();
		mCallback.onUiEventActivated();
	}

}
