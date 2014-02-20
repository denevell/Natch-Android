package org.denevell.droidnatch.posts.list.uievents;

import javax.inject.Inject;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.posts.list.di.EditThreadServicesMapper;
import org.denevell.droidnatch.posts.list.entities.EditPostResource;
import org.denevell.droidnatch.posts.list.entities.EditPostResourceReturnData;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import dagger.ObjectGraph;

public class EditThreadViewActivator extends LinearLayout implements
        Activator<EditPostResourceReturnData>, 
        View.OnClickListener {

    @Inject VolleyRequest<EditPostResource, EditPostResourceReturnData> mVolleyRequest;
    @Inject ServiceFetcher<EditPostResourceReturnData> mEditPostService;
    private GenericUiObserver mCallback;
    private Button mButton;
    private TextView mContent;
    private TextView mSubject;
    private Runnable mSuccessCallback;
    private PostResource mPost;

    public EditThreadViewActivator(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.edit_thread_layout, this, true);
        mButton = (Button) findViewById(R.id.edit_thread_button);
        mContent = (TextView) findViewById(R.id.edit_thread_content_edittext);
        mSubject = (TextView) findViewById(R.id.edit_thread_subject_edittext);
    }

    private void inject() {
        ObjectGraph.create(
                new CommonMapper((Activity) getContext()),
                new EditThreadServicesMapper()
        ).inject(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(mContent.getText().length()==0) {
        	mContent.setText(mPost.getContent());
        }
        if(mSubject.getText().length()==0) {
        	mSubject.setText(mPost.getSubject());
        }
        mButton.setOnClickListener(this);
        inject();
        @SuppressWarnings("unchecked")
		Controller addThreadController =
                new UiEventThenServiceThenUiEvent<EditPostResourceReturnData>(
                        this,
                        mEditPostService,
                        null,
                        new Receiver<EditPostResourceReturnData>() {
                            @Override
                            public void success(EditPostResourceReturnData result) {
                                EventBus.getBus().post(new ListPostsViewStarter.CallControllerListPosts());
                            }
                            @Override public void fail(FailureResult r) { }
                        });
        addThreadController.setup();
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
        if(getContext() instanceof Activity) {
            ((Activity)getContext()).setTitle(mSubject.getText().toString());
        }
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
        mVolleyRequest.getBody().setContent(mContent.getText().toString());
        mVolleyRequest.getBody().setSubject(mSubject.getText().toString());
        mVolleyRequest.setUrl(mVolleyRequest.getRequest().getUrl() + "/" + mPost.getId());
        mCallback.onUiEventActivated();
    }

    public void setPost(PostResource post) {
        mPost = post;
    }
    
}
