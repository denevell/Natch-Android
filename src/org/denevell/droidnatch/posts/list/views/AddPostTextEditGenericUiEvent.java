package org.denevell.droidnatch.posts.list.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.EditTextHideKeyboard;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.posts.list.di.AddPostServicesMapper;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceInput;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class AddPostTextEditGenericUiEvent extends EditTextHideKeyboard implements
        Activator,OnEditorActionListener {
    
    private EditText mEditText;
    private GenericUiObserver mCallback;
    @Inject ServiceFetcher<AddPostResourceReturnData> addPostService;
    @Inject AddPostResourceInput addPostResourceInput;

    public AddPostTextEditGenericUiEvent(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        setOnEditorActionListener(this);
    }

    private void inject(Bundle arguments) {
        ObjectGraph.create(
                new CommonMapper((Activity) getContext()),
                new AddPostServicesMapper(arguments)
        ).inject(this);
    }

    public void setup(Bundle arguments) {
        inject(arguments);
        UiEventThenServiceThenUiEvent addPostController =
                new UiEventThenServiceThenUiEvent (
                        this,
                        addPostService,
                        null,
                        new Receiver() {
                            @Override
                            public void success(Object result) {
                                EventBus.getBus().post(new ListPostsFragment.CallControllerListPosts());
                            }
                            @Override public void fail(FailureResult r) { }
                        });
        addPostController.setup().go();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(event!=null && event.getAction()==KeyEvent.ACTION_DOWN) {
            return true; // Natsty hack to ui automator doesn't call this twice
        }
        addPostResourceInput.setSubject("-");
        addPostResourceInput.setContent(v.getText().toString());
        mCallback.onUiEventActivated();
        return true;
    }

    @Override
    public void setOnSubmitObserver(GenericUiObserver observer) {
        mCallback = observer;
    }

    @Override
    public void success(Object result) {
        setText("");
    }

    @Override
    public void fail(FailureResult f) {
        if(f!=null && f.getErrorMessage()!=null) {
            setError(f.getErrorMessage());
        }
    }

}
