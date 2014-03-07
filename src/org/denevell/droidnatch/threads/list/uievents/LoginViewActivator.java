package org.denevell.droidnatch.threads.list.uievents;

import javax.inject.Inject;

import org.denevell.droidnatch.PaginationMapper;
import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.ShamefulStatics;
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
import org.denevell.droidnatch.threads.list.entities.LogoutResourceReturnData;
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

public class LoginViewActivator extends LinearLayout implements Activator<LoginResourceReturnData>, View.OnClickListener, Finishable {

	public static class LoginUpdatedEvent { }

	private final class LogoutActivatorImplementation implements Activator<LogoutResourceReturnData> {
		@Override public void setOnSubmitObserver(GenericUiObserver observer) {}
		@Override public void success(LogoutResourceReturnData result) {
			logout();
		}
		@Override public void fail(FailureResult r) {
			logout();
		}
	}

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
	private Button mButton;
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
		String username = ShamefulStatics.getUsername(getContext().getApplicationContext());
		if(username!=null && username.length()>0) {
			View v = findViewById(R.id.threads_list_add_thread_pane_scrollview);
			v.setVisibility(View.GONE);
			Button logoutButton = (Button) findViewById(R.id.logout_button);
			logoutButton.setVisibility(View.VISIBLE);
			Receiver<LogoutResourceReturnData> receivers = null;
			final UiEventThenServiceThenUiEvent<LogoutResourceReturnData> controller = new UiEventThenServiceThenUiEvent<LogoutResourceReturnData>(
					new LogoutActivatorImplementation(),
					mLogoutService, 
					null,
					receivers)
				.setup();
			logoutButton.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View v) {
					controller.onUiEventActivated();
				}
			});

		} else {
			mButton = (Button) findViewById(R.id.login_button);
			mButton.setOnClickListener(this);
			mUsername = (EditText) findViewById(R.id.login_username_edittext);
			mPassword = (EditText) findViewById(R.id.login_password_edittext);

			new UiEventThenServiceThenUiEvent<LoginResourceReturnData>(
					this,
					mLoginService, 
					null,
					new RefreshOptionsMenuReceiver(act),
					new UpdateLoginInfoReceiver(mUsername))
						.setup();
		}
	}

	@Override
	public void setOnSubmitObserver(GenericUiObserver observer) {
		mCallback = observer;
	}

	@Override
	public void success(LoginResourceReturnData result) {
		setEnabled(true);
		if (mSuccessCallback != null)
			mSuccessCallback.run();
	}

	@Override
	public void fail(FailureResult f) {
		setEnabled(true);
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
		mCallback.onUiEventActivated();
		setEnabled(false);
	}

	private void logout() {
		ShamefulStatics.logout(getContext().getApplicationContext());
		if(getContext()!=null) {
			if(getContext() instanceof FragmentActivity) {
				((FragmentActivity)getContext()).supportInvalidateOptionsMenu();
			}
		}
		success(null);
		EventBus.getBus().post(new LoginUpdatedEvent());
	}

}
