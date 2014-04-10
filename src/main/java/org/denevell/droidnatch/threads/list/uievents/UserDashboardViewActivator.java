package org.denevell.droidnatch.threads.list.uievents;

import javax.inject.Inject;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Activator.GenericUiObserver;
import org.denevell.droidnatch.app.interfaces.Finishable;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.ButtonWithProgress;
import org.denevell.droidnatch.threads.list.ChangePasswordInput;
import org.denevell.droidnatch.threads.list.ListThreadsMapper;
import org.denevell.droidnatch.threads.list.entities.LogoutResourceReturnData;
import org.denevell.droidnatch.threads.list.uievents.LoginViewActivator.LoginUpdatedEvent;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.newfivefour.android.manchester.R;

import dagger.ObjectGraph;

public class UserDashboardViewActivator extends LinearLayout implements Finishable {

	private GenericUiObserver mLogoutServiceCallback;
	private GenericUiObserver mChangePasswordServiceCallback;
	private Runnable mSuccessCallback;
	private ButtonWithProgress mLogoutButton;
	@Inject ServiceFetcher<Void, LogoutResourceReturnData> mLogoutService;
	@Inject ServiceFetcher<ChangePasswordInput, Void> mChangePasswordService;
	private EditText mChangePasswordEditText;
	private EditText mConfirmPasswordEditText;
	private ButtonWithProgress mChangePasswordButton;

	public UserDashboardViewActivator(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.user_dashboard_layout, this, true);
	}

	private void inject() {
		Activity activity = (Activity) getContext();
		ObjectGraph.create(
				new CommonMapper(activity),
				new ListThreadsMapper(activity)
				).inject(this);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		inject();
		setupLogoutService();
		setupChangePasswordService();
	}

	@SuppressWarnings("unchecked")
	private void setupLogoutService() {
		// Setup logout service
		mLogoutButton = (ButtonWithProgress) findViewById(R.id.logout_button);
		mLogoutButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				if(mLogoutButton!=null) mLogoutButton.loadingStart();
				mLogoutServiceCallback.onUiEventActivated();
			}
		});
		new UiEventThenServiceThenUiEvent<LogoutResourceReturnData>(
				new Activator<LogoutResourceReturnData>() {
					@Override public void setOnSubmitObserver(org.denevell.droidnatch.app.interfaces.Activator.GenericUiObserver observer) {
						mLogoutServiceCallback = observer;
					}
					@Override public void success(LogoutResourceReturnData result) {
						logout();
						if(mLogoutButton!=null) mLogoutButton.loadingStop();
						if (mSuccessCallback != null) mSuccessCallback.run();
					}
					@Override public void fail(FailureResult r) {
						logout();
                        if(mLogoutButton!=null) mLogoutButton.loadingStop();
                        if (mSuccessCallback != null) mSuccessCallback.run();
                    }
				},
				mLogoutService, null).setup();
	}

	@SuppressWarnings("unchecked")
	private void setupChangePasswordService() {
		mChangePasswordEditText = (EditText) findViewById(R.id.change_password_change_password_edittext);
		mConfirmPasswordEditText = (EditText) findViewById(R.id.change_password_confirm_edittext);
		mChangePasswordButton = (ButtonWithProgress) findViewById(R.id.change_password_button);
		mChangePasswordButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				if(mChangePasswordButton!=null) mChangePasswordButton.loadingStart();
				mChangePasswordService.getBody().setPassword(mChangePasswordEditText.getText().toString());
				mChangePasswordServiceCallback.onUiEventActivated();
			}
		});
		new UiEventThenServiceThenUiEvent<Void>(
				new Activator<Void>() {
					@Override public void setOnSubmitObserver(org.denevell.droidnatch.app.interfaces.Activator.GenericUiObserver observer) {
						mChangePasswordServiceCallback = observer;
					}
					@Override public void success(Void result) {
						if(mChangePasswordButton!=null) mChangePasswordButton.loadingStop();
						// Show success somehow
					}
					@Override public void fail(FailureResult r) {
                        if(mChangePasswordButton!=null) mChangePasswordButton.loadingStop();
						// Show failure 
                    }
				},
				mChangePasswordService, null).setup();
	}

	@Override
	public void setFinishedCallback(Runnable runnable) {
		mSuccessCallback = runnable;
	}

	private void logout() {
		ShamefulStatics.logout(getContext().getApplicationContext());
		if(getContext()!=null) {
			if(getContext() instanceof FragmentActivity) {
				((FragmentActivity)getContext()).supportInvalidateOptionsMenu();
			}
		}
		EventBus.getBus().post(new LoginUpdatedEvent());
	}

}
