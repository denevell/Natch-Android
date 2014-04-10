package org.denevell.droidnatch.threads.list.uievents;

import javax.inject.Inject;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Finishable;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.ButtonWithProgress;
import org.denevell.droidnatch.threads.list.ListThreadsMapper;
import org.denevell.droidnatch.threads.list.entities.LogoutResourceReturnData;
import org.denevell.droidnatch.threads.list.uievents.LoginViewActivator.LoginUpdatedEvent;

import com.newfivefour.android.manchester.R;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import dagger.ObjectGraph;

public class UserDashboardViewActivator extends LinearLayout implements Activator<LogoutResourceReturnData>, View.OnClickListener, Finishable {

	private GenericUiObserver mCallback;
	private Runnable mSuccessCallback;
	private ButtonWithProgress mLogoutButton;
	@Inject ServiceFetcher<Void, LogoutResourceReturnData> mLogoutService;
	private EditText mChangePasswordEditText;
	private EditText mConfirmPasswordEditText;
	private Button mChangePasswordButton;

	public UserDashboardViewActivator(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.logout_layout, this, true);
	}

	private void inject() {
		Activity activity = (Activity) getContext();
		ObjectGraph.create(
				new CommonMapper(activity),
				new ListThreadsMapper(activity)
				).inject(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		inject();
		mLogoutButton = (ButtonWithProgress) findViewById(R.id.logout_button);
		mLogoutButton.setOnClickListener(this);
		mChangePasswordEditText = (EditText) findViewById(R.id.change_password_change_password_edittext);
		mConfirmPasswordEditText = (EditText) findViewById(R.id.change_password_confirm_edittext);
		mChangePasswordButton = (Button) findViewById(R.id.change_password_button);
		new UiEventThenServiceThenUiEvent<LogoutResourceReturnData>(
				this,
				mLogoutService, 
				null) .setup();
	}

	@Override
	public void setOnSubmitObserver(GenericUiObserver observer) {
		mCallback = observer;
	}

	@Override
	public void success(LogoutResourceReturnData result) {
		logout();
		if(mLogoutButton!=null) mLogoutButton.loadingStop();
		if (mSuccessCallback != null)
			mSuccessCallback.run();
	}

	@Override
	public void fail(FailureResult f) {
		logout();
		if(mLogoutButton!=null) mLogoutButton.loadingStop();
		if (mSuccessCallback != null)
			mSuccessCallback.run();
	}

	@Override
	public void setFinishedCallback(Runnable runnable) {
		mSuccessCallback = runnable;
	}

	@Override
	public void onClick(View view) {
		if(mLogoutButton!=null) mLogoutButton.loadingStart();
		mCallback.onUiEventActivated();
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
