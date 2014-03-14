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
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import dagger.ObjectGraph;

public class LogoutViewActivator extends LinearLayout implements Activator<LogoutResourceReturnData>, View.OnClickListener, Finishable {

	private GenericUiObserver mCallback;
	private Runnable mSuccessCallback;
	private ButtonWithProgress mButton;
	@Inject ServiceFetcher<Void, LogoutResourceReturnData> mLogoutService;

	public LogoutViewActivator(Context context, AttributeSet attrs) {
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
		mButton = (ButtonWithProgress) findViewById(R.id.logout_button);
		mButton.setOnClickListener(this);
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
		if(mButton!=null) mButton.loadingStop();
		if (mSuccessCallback != null)
			mSuccessCallback.run();
	}

	@Override
	public void fail(FailureResult f) {
		logout();
		if(mButton!=null) mButton.loadingStop();
	}

	@Override
	public void setFinishedCallback(Runnable runnable) {
		mSuccessCallback = runnable;
	}

	@Override
	public void onClick(View view) {
		if(mButton!=null) mButton.loadingStart();
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
