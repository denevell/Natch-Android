package org.denevell.droidnatch.threads.list.uievents;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Finishable;
import org.denevell.droidnatch.threads.list.entities.LoginResourceReturnData;
import org.denevell.natch.android.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Need to refactor this code, separate out logout for starters.
 * @author user
 *
 */
public class LoginViewActivator extends LinearLayout implements Activator<LoginResourceReturnData>, View.OnClickListener, Finishable {

	public static class LoginUpdatedEvent { }

/*	private final class LogoutActivatorImplementation implements Activator<LogoutResourceReturnData> {
		private ButtonWithProgress mLogoutButton;
		public LogoutActivatorImplementation(ButtonWithProgress logoutButton) {
			mLogoutButton = logoutButton;
		}
		@Override public void setOnSubmitObserver(GenericUiObserver observer) {}
		@Override public void success(LogoutResourceReturnData result) {
			logout();
			if(mLogoutButton!=null) mLogoutButton.loadingStop();
		}
		@Override public void fail(FailureResult r) {
			logout();
			if(mLogoutButton!=null) mLogoutButton.loadingStop();
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
	private ButtonWithProgress mButton;
	private EditText mUsername;
	private EditText mPassword;
	@Inject ServiceFetcher<LoginResourceInput, LoginResourceReturnData> mLoginService;
	@Inject ServiceFetcher<Void, LogoutResourceReturnData> mLogoutService;
*/
	public LoginViewActivator(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.login_layout, this, true);
	}
/*
	private void inject() {
		Activity activity = (Activity) getContext();
		ObjectGraph.create(
				new CommonMapper(activity),
				PaginationMapper.getInstance(),
				new ListThreadsMapper(activity)
				).inject(
				this);
	}*/

	@SuppressWarnings("unchecked")
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
/*		inject();
		final FragmentActivity act = (FragmentActivity) getContext();
		String username = ShamefulStatics.getUsername(getContext().getApplicationContext());
		if(username!=null && username.length()>0) {
			View v = findViewById(R.id.threads_list_add_thread_pane_scrollview);
			v.setVisibility(View.GONE);
			final ButtonWithProgress logoutButton = (ButtonWithProgress) findViewById(R.id.logout_button);
			logoutButton.setVisibility(View.VISIBLE);
			Receiver<LogoutResourceReturnData> receivers = null;
			final UiEventThenServiceThenUiEvent<LogoutResourceReturnData> controller = new UiEventThenServiceThenUiEvent<LogoutResourceReturnData>(
					new LogoutActivatorImplementation(logoutButton),
					mLogoutService, 
					null,
					receivers)
				.setup();
			logoutButton.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View v) {
					controller.onUiEventActivated();
					logoutButton.loadingStart();
				}
			});

		} else {
			mButton = (ButtonWithProgress) findViewById(R.id.login_button);
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
		}*/
	}
/*
*/
	@Override
	public void setOnSubmitObserver(GenericUiObserver observer) {
		//mCallback = observer;
	}

	@Override
	public void success(LoginResourceReturnData result) {
/*		if(mButton!=null) mButton.loadingStop();
		if (mSuccessCallback != null)
			mSuccessCallback.run();*/
	}

	@Override
	public void fail(FailureResult f) {
/*		if(mButton!=null) mButton.loadingStop();
		if(f!=null && f.getStatusCode()==400) {
        	mUsername.setError(getContext().getString(R.string.register_400_error));
		}
		if (f != null && f.getErrorMessage() != null) {
			mUsername.setError("Login failed.");
		}*/
	}

	@Override
	public void setFinishedCallback(Runnable runnable) {
		//mSuccessCallback = runnable;
	}
	@Override
	public void onClick(View view) {
		//mLoginService.getRequest().getBody().setPassword(mPassword.getText().toString());
		//mLoginService.getRequest().getBody().setUsername(mUsername.getText().toString());
		//if(mButton!=null) mButton.loadingStart();
		//mCallback.onUiEventActivated();
	}
/*
	private void logout() {
		ShamefulStatics.logout(getContext().getApplicationContext());
		if(getContext()!=null) {
			if(getContext() instanceof FragmentActivity) {
				((FragmentActivity)getContext()).supportInvalidateOptionsMenu();
			}
		}
		success(null);
		EventBus.getBus().post(new LoginUpdatedEvent());
	}*/

}
