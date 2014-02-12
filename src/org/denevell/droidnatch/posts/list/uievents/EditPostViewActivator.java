package org.denevell.droidnatch.posts.list.uievents;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.posts.list.di.EditPostServicesMapper;
import org.denevell.droidnatch.posts.list.entities.EditPostResource;
import org.denevell.droidnatch.posts.list.entities.EditPostResourceReturnData;
import org.denevell.natch.android.R;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class EditPostViewActivator extends LinearLayout implements
        Activator<EditPostResourceReturnData>, View.OnClickListener {

    @Inject EditPostResource mEditPostResourceInput;
    @Inject VolleyRequest<EditPostResourceReturnData> mVolleyRequest;
    @Inject ServiceFetcher<EditPostResourceReturnData> mEditPostService;
    private GenericUiObserver mCallback;
    private Button mButton;
    private TextView mContent;
    private Runnable mSuccessCallback;
    private long mPostId;

    public EditPostViewActivator(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.edit_post_layout, this, true);
        mButton = (Button) findViewById(R.id.edit_post_button);
        mContent = (TextView) findViewById(R.id.edit_post_edittext);
    }

    private void inject() {
        ObjectGraph.create(
                new CommonMapper((Activity) getContext()),
                new EditPostServicesMapper()
        ).inject(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mButton.setOnClickListener(this);
        inject();
        Controller addThreadController =
                new UiEventThenServiceThenUiEvent<EditPostResourceReturnData>(
                        this,
                        mEditPostService,
                        null,
                        new Receiver() {
                            @Override
                            public void success(Object result) {
                                EventBus.getBus().post(new ListPostsViewStarter.CallControllerListPosts());
                            }
                            @Override public void fail(FailureResult r) { }
                        });
        addThreadController.setup();
    }

    public Parcelable getInstanceState() {
        Bundle b = new Bundle();
        b.putParcelable("content", mContent.onSaveInstanceState());
        b.putParcelable("state", onSaveInstanceState());
        return b;
    }

    public void setInstanceState(Parcelable p) {
        if(p instanceof Bundle) {
            Bundle b = (Bundle) p;
            mContent.onRestoreInstanceState(b.getParcelable("content"));
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
    public void success(EditPostResourceReturnData result) {
        mContent.setText("");
        mContent.setError(null);
        if(mSuccessCallback!=null) mSuccessCallback.run();
    }

    public void setSuccessCallback(Runnable runnable) {
        mSuccessCallback = runnable;
    }

    @Override
    public void fail(FailureResult f) {
        if(f!=null && f.getErrorMessage()!=null) {
            mContent.setError(f.getErrorMessage());
        }
    }

    @Override
    public void onClick(View view) {
        mEditPostResourceInput.setContent(mContent.getText().toString());
        mVolleyRequest.setUrl(mVolleyRequest.getRequest().getUrl() + "/" + mPostId);
        mCallback.onUiEventActivated();
    }

    public void setPostId(long postId) {
        mPostId = postId;
    }
}
