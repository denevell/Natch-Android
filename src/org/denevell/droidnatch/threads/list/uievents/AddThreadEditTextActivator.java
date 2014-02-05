package org.denevell.droidnatch.threads.list.uievents;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.TextView;

import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.ScreenOpenerMapper;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.EditTextHideKeyboard;
import org.denevell.droidnatch.threads.list.di.AddThreadServicesMapper;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceInput;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;
import org.denevell.droidnatch.threads.list.uievents.OpenNewThreadReceiver;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class AddThreadEditTextActivator extends EditTextHideKeyboard implements
        Activator<AddPostResourceReturnData>,
        TextView.OnEditorActionListener {

    @Inject AddPostResourceInput addPostResourceInput;
    @Inject ServiceFetcher<AddPostResourceReturnData> addPostService;
    @Inject ScreenOpener screenOpener;
    private GenericUiObserver mCallback;

    public AddThreadEditTextActivator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void inject() {
        ObjectGraph.create(
                new ScreenOpenerMapper((FragmentActivity) getContext()),
                new CommonMapper((Activity) getContext()),
                new AddThreadServicesMapper()
        ).inject(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        inject();
        setOnEditorActionListener(this);
        Controller addThreadController =
                new UiEventThenServiceThenUiEvent<AddPostResourceReturnData>(
                        this,
                        addPostService,
                        null,
                        new OpenNewThreadReceiver(screenOpener));
        addThreadController.setup();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(event!=null && event.getAction()==KeyEvent.ACTION_DOWN) {
            return true; // Natsty hack to ui automator doesn't call this twice
        }
        addPostResourceInput.setContent("-");
        addPostResourceInput.setSubject(v.getText().toString());
        mCallback.onUiEventActivated();
        return true;
    }

    @Override
    public void setOnSubmitObserver(GenericUiObserver observer) {
        mCallback = observer;
    }

    @Override
    public void success(AddPostResourceReturnData result) {
        setText("");
    }

    @Override
    public void fail(FailureResult f) {
        if(f!=null && f.getErrorMessage()!=null) {
            setError(f.getErrorMessage());
        }
    }
}
