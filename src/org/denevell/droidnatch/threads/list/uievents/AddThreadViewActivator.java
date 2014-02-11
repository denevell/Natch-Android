package org.denevell.droidnatch.threads.list.uievents;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.ScreenOpenerMapper;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.threads.list.di.AddThreadServicesMapper;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceInput;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;
import org.denevell.natch.android.R;

import java.util.Arrays;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class AddThreadViewActivator extends LinearLayout implements
        Activator<AddPostResourceReturnData>, View.OnClickListener {

    @Inject AddPostResourceInput addPostResourceInput;
    @Inject ServiceFetcher<AddPostResourceReturnData> addPostService;
    @Inject ScreenOpener screenOpener;
    private GenericUiObserver mCallback;
    private Button mButton;
    private TextView mSubject;
    private TextView mContent;
    private TextView mTags;
    private Runnable mSuccessCallback;

    public AddThreadViewActivator(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.add_thread_layout, this, true);
        mButton = (Button) findViewById(R.id.add_thread_button);
        mSubject = (TextView) findViewById(R.id.add_thread_subject_edittext);
        mContent = (TextView) findViewById(R.id.add_thread_content_edittext);
        mTags = (TextView) findViewById(R.id.add_thread_tags_edittext);
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
        mButton.setOnClickListener(this);
        inject();
        Controller addThreadController =
                new UiEventThenServiceThenUiEvent<AddPostResourceReturnData>(
                        this,
                        addPostService,
                        null,
                        new OpenNewThreadReceiver(screenOpener));
        addThreadController.setup();
    }

    public Parcelable getInstanceState() {
        Bundle b = new Bundle();
        b.putParcelable("subject", mSubject.onSaveInstanceState());
        b.putParcelable("content", mContent.onSaveInstanceState());
        b.putParcelable("tags", mTags.onSaveInstanceState());
        b.putParcelable("state", onSaveInstanceState());
        return b;
    }

    public void setInstanceState(Parcelable p) {
        if(p instanceof Bundle) {
            Bundle b = (Bundle) p;
            mSubject.onRestoreInstanceState(b.getParcelable("subject"));
            mContent.onRestoreInstanceState(b.getParcelable("content"));
            mTags.onRestoreInstanceState(b.getParcelable("tags"));
            onRestoreInstanceState(b.getParcelable("state"));
        } else {
            onRestoreInstanceState(p);
        }

    }

    @Override
    public void setOnSubmitObserver(GenericUiObserver observer) {
        mCallback = observer;
    }

    @Override
    public void success(AddPostResourceReturnData result) {
        mSubject.setText("");
        mSubject.setError(null);
        mContent.setText("");
        mContent.setError(null);
        mTags.setText("");
        mTags.setError(null);
        if(mSuccessCallback!=null) mSuccessCallback.run();
    }

    public void setSuccessCallback(Runnable runnable) {
        mSuccessCallback = runnable;
    }

    @Override
    public void fail(FailureResult f) {
        if(f!=null && f.getErrorMessage()!=null) {
            mSubject.setError(f.getErrorMessage());
        }
    }

    @Override
    public void onClick(View view) {
        addPostResourceInput.setContent(mContent.getText().toString());
        addPostResourceInput.setSubject(mSubject.getText().toString());
        String[] tags = mTags.getText().toString().split(",");
        addPostResourceInput.setTags(Arrays.asList(tags));
        mCallback.onUiEventActivated();
    }

}
