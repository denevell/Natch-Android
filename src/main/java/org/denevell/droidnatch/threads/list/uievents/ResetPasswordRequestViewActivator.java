package org.denevell.droidnatch.threads.list.uievents;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.views.ButtonWithProgress;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.newfivefour.android.manchester.R;

public class ResetPasswordRequestViewActivator extends LinearLayout implements Activator<Void>, View.OnClickListener {

	private GenericUiObserver mCallback;
	private Runnable mSuccessCallback;
	private ButtonWithProgress mButton;
	private EditText mUsername;

	public ResetPasswordRequestViewActivator(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.reset_password_layout, this, true);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
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
        	mUsername.setError(getContext().getString(R.string.register_400_error));
		}
		if (f != null && f.getErrorMessage() != null) {
			mUsername.setError("Login failed.");
		}
	}

	@Override
	public void onClick(View view) {
		if(mButton!=null) mButton.loadingStart();
		mCallback.onUiEventActivated();
	}

}
