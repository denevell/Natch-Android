package org.denevell.droidnatch.posts.list.uievents;

import java.util.Map;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.baseclasses.networking.JsonVolleyRequest.LazyHeadersCallback;
import org.denevell.droidnatch.app.baseclasses.networking.ServiceBuilder;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.CanSetEntity;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.Finishable;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.ButtonWithProgress;
import org.denevell.droidnatch.posts.list.entities.EditPostResource;
import org.denevell.droidnatch.posts.list.entities.EditPostResourceReturnData;
import org.denevell.droidnatch.posts.list.entities.PostResource;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.newfivefour.android.manchester.R;

import dagger.ObjectGraph;

public class EditPostViewActivator extends LinearLayout implements
        Activator<EditPostResourceReturnData>, 
        View.OnClickListener, 
        Finishable,
        CanSetEntity {

    private ServiceFetcher<EditPostResource, EditPostResourceReturnData> mEditPostService;
    private GenericUiObserver mCallback;
    private ButtonWithProgress mButton;
    private TextView mContent;
    private Runnable mSuccessCallback;
    private PostResource mPost;

    public EditPostViewActivator(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.edit_post_layout, this, true);
        mButton = (ButtonWithProgress) findViewById(R.id.edit_post_button);
        mContent = (TextView) findViewById(R.id.edit_post_edittext);
    }

    private void inject() {
        ObjectGraph.create(
                new CommonMapper((Activity) getContext())
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
        Activity act = (Activity) getContext();

		String url = ShamefulStatics.getBasePath() + getContext().getString(R.string.url_edit_post);
		mEditPostService = new ServiceBuilder<EditPostResource, EditPostResourceReturnData>()
       		.addLazyHeader(new LazyHeadersCallback() {
				@Override public void run(Map<String, String> headersMap) {
					headersMap.put("AuthKey", ShamefulStatics.getAuthKey(getContext().getApplicationContext()));
				}
			})			
			.url(url)
			.entity(new EditPostResource())
			.method(Request.Method.POST)
        	.createJson(act, EditPostResourceReturnData.class);
        
		@SuppressWarnings("unchecked")
		Controller addThreadController =
                new UiEventThenServiceThenUiEvent<EditPostResourceReturnData>(
                        this,
                        mEditPostService,
                        null,
                        new Receiver<EditPostResourceReturnData>() {
                            @Override
                            public void success(EditPostResourceReturnData result) {
                                EventBus.getBus().post(new ListPostsViewStarter.CallControllerListPosts(false));
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
        if(mButton!=null) mButton.loadingStop();
        mContent.setText("");
        mContent.setError(null);
        if(mSuccessCallback!=null) mSuccessCallback.run();
    }

    @Override
    public void setFinishedCallback(Runnable runnable) {
        mSuccessCallback = runnable;
    }

    @Override
    public void fail(FailureResult f) {
        if(mButton!=null) mButton.loadingStop();
        if(f!=null && f.getErrorMessage()!=null) {
            mContent.setError(f.getErrorMessage());
        }
    }

    @Override
    public void onClick(View view) {
        mEditPostService.getBody().setContent(mContent.getText().toString());
		String url = ShamefulStatics.getBasePath() + getContext().getString(R.string.url_edit_post);
        mEditPostService.setUrl(url + "/" + mPost.getId());
        if(mButton!=null) mButton.loadingStart();
        mCallback.onUiEventActivated();
    }

    @Override
    public void setEntity(Object post) {
    	PostResource p = (PostResource) post;
        mPost = p;
    }
}
