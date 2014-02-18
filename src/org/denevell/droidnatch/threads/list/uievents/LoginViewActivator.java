package org.denevell.droidnatch.threads.list.uievents;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;
import org.denevell.natch.android.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class LoginViewActivator extends LinearLayout implements
        Activator<AddPostResourceReturnData>, View.OnClickListener {

    private GenericUiObserver mCallback;
    private Runnable mSuccessCallback;

    public LoginViewActivator(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.add_thread_layout, this, true);
    }

    private void inject() {
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        inject();
    }
    
    @Override
    public void setOnSubmitObserver(GenericUiObserver observer) {
        mCallback = observer;
    }

    @Override
    public void success(AddPostResourceReturnData result) {
        if(mSuccessCallback!=null) mSuccessCallback.run();
    }

    public void setSuccessCallback(Runnable runnable) {
        mSuccessCallback = runnable;
    }

    @Override
    public void fail(FailureResult f) {
        if(f!=null && f.getErrorMessage()!=null) {
        }
    }

    @Override
    public void onClick(View view) {
        mCallback.onUiEventActivated();
    }

}
