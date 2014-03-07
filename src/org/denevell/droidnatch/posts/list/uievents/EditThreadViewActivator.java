package org.denevell.droidnatch.posts.list.uievents;

import java.util.Map;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.baseclasses.networking.ServiceBuilder;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestImpl.LazyHeadersCallback;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.CanSetEntity;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.Finishable;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
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

import com.android.volley.Request;

import dagger.ObjectGraph;

public class EditThreadViewActivator extends LinearLayout implements
        Activator<EditPostResourceReturnData>, 
        View.OnClickListener,
        Finishable,
        CanSetEntity {

    private ServiceFetcher<EditPostResource, EditPostResourceReturnData> mEditPostService;
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
                new CommonMapper((Activity) getContext())
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
        Activity act = (Activity) getContext();

		String url = ShamefulStatics.getBasePath() + getContext().getString(R.string.url_edit_thread);
		mEditPostService = new ServiceBuilder<EditPostResource, EditPostResourceReturnData>()
       		.addLazyHeader(new LazyHeadersCallback() {
				@Override public void run(Map<String, String> headersMap) {
					headersMap.put("AuthKey", ShamefulStatics.getAuthKey(getContext().getApplicationContext()));
				}
			})			
			.url(url)
			.entity(new EditPostResource())
			.method(Request.Method.POST)
        	.create(act, EditPostResourceReturnData.class); 
        
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
    	setEnabled(true);
        mContent.setText("");
        mContent.setError(null);
        if(mSuccessCallback!=null) mSuccessCallback.run();
        if(getContext() instanceof Activity) {
            ((Activity)getContext()).setTitle(mSubject.getText().toString());
        }
    }

    @Override
    public void setFinishedCallback(Runnable runnable) {
        mSuccessCallback = runnable;
    }

    @Override
    public void fail(FailureResult f) {
    	setEnabled(true);
        if(f!=null && f.getErrorMessage()!=null) {
            mSubject.setError(f.getErrorMessage());
        }
    }

    @Override
    public void onClick(View view) {
        mEditPostService.getRequest().getBody().setContent(mContent.getText().toString());
        mEditPostService.getRequest().getBody().setSubject(mSubject.getText().toString());
        mEditPostService.getRequest().setUrl(mEditPostService.getRequest().getRequest().getUrl() + "/" + mPost.getId());
        mCallback.onUiEventActivated();
    	setEnabled(false);
    }

    @Override
    public void setEntity(Object post) {
    	PostResource p = (PostResource) post;
        mPost = p;
    }
    
}
