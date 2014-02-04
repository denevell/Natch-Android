package org.denevell.droidnatch.threads.list.views;

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
import org.denevell.droidnatch.app.interfaces.ActivatingUiObject;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.EditTextHideKeyboard;
import org.denevell.droidnatch.threads.list.di.AddThreadServicesMapper;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceInput;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;
import org.denevell.droidnatch.threads.list.uievents.OpenNewThreadUiEvent;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class AddThreadEditText extends EditTextHideKeyboard implements
        ActivatingUiObject<AddPostResourceReturnData>,
        TextView.OnEditorActionListener {

    @Inject AddPostResourceInput addPostResourceInput;
    @Inject ServiceFetcher<AddPostResourceReturnData> addPostService;
    @Inject ScreenOpener screenOpener;
    private GenericUiObserver mCallback;

    public AddThreadEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        ObjectGraph.create(
                new ScreenOpenerMapper((FragmentActivity) context),
                new CommonMapper((Activity) context),
                new AddThreadServicesMapper()
        ).inject(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setOnEditorActionListener(this);
        Controller addThreadController =
                new UiEventThenServiceThenUiEvent<AddPostResourceReturnData>(
                        this,
                        addPostService,
                        null,
                        new OpenNewThreadUiEvent(screenOpener));
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
