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
import org.denevell.droidnatch.posts.list.di.EditPostServicesMapper;
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

public class EditPostViewActivator extends LinearLayout implements
        Activator<EditPostResourceReturnData>, View.OnClickListener {

    @Inject ServiceFetcher<EditPostResource, EditPostResourceReturnData> mEditPostService;
    private GenericUiObserver mCallback;
    private Button mButton;
    private TextView mContent;
    private Runnable mSuccessCallback;
    private PostResource mPost;

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
        if(mContent.getText().length()==0) {
        	mContent.setText(mPost.getContent());
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
        mEditPostService.getRequest().getBody().setContent(mContent.getText().toString());
        mEditPostService.getRequest().setUrl(mEditPostService.getRequest().getRequest().getUrl() + "/" + mPost.getId());
        mCallback.onUiEventActivated();
    }

    public void setPost(PostResource post) {
        mPost = post;
    }
}
