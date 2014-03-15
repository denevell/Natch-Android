
package org.denevell.droidnatch.posts.list.uievents;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.PaginationMapper;
import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.baseclasses.networking.ServiceBuilder;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestImpl.LazyHeadersCallback;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.ButtonWithProgress;
import org.denevell.droidnatch.app.views.ReceivingClickingAutopaginatingListView;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.posts.list.ListPostsMapper;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceInput;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import com.newfivefour.android.manchester.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.android.volley.Request;

import dagger.ObjectGraph;

public class AddPostViewActivator extends FrameLayout implements
        Activator<AddPostResourceReturnData>, OnClickListener {
    
    private static final String TAG = AddPostViewActivator.class.getSimpleName();
	private GenericUiObserver mCallback;
    private ServiceFetcher<AddPostResourceInput, AddPostResourceReturnData> addPostService;
    @Inject ReceivingClickingAutopaginatingListView<ThreadResource, PostResource, List<PostResource>> mListView;
	private ButtonWithProgress mButton;
	private EditText mEditText;
	//protected boolean mExpanded;
    
    public AddPostViewActivator(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        LayoutInflater.from(context).inflate(R.layout.post_add_layout, this, true);
        mButton = (ButtonWithProgress) findViewById(R.id.post_add_button);
        mButton.setOnClickListener(this);
        mEditText = (EditText) findViewById(R.id.post_add_edittext);
    }

    private void inject(Activity activity, String threadId) {
        ObjectGraph.create(
        		PaginationMapper.getInstance(),
        		new ListPostsMapper(activity, threadId),
                new CommonMapper((Activity) getContext())
        ).inject(this);
    }

    public void setup(Bundle arguments) {
        Activity act = (Activity) getContext();
        String threadId = arguments.getString(ListPostsFragment.BUNDLE_KEY_THREAD_ID);
        inject(act, threadId);

        AddPostResourceInput entity = new AddPostResourceInput();
		entity.setThreadId(threadId);

        String url = ShamefulStatics.getBasePath()+getContext().getString(R.string.url_add_post);
		addPostService = new ServiceBuilder<AddPostResourceInput, AddPostResourceReturnData>()
        		.url(url)
        		.method(Request.Method.PUT)
        		.entity(entity)
        		.addLazyHeader(new LazyHeadersCallback() {
					@Override public void run(Map<String, String> headersMap) {
						headersMap.put("AuthKey", ShamefulStatics.getAuthKey(getContext().getApplicationContext()));
					}
				})
        		.create(null, AddPostResourceReturnData.class); 
        
		@SuppressWarnings("unchecked")
        UiEventThenServiceThenUiEvent<AddPostResourceReturnData> addPostController =
                new UiEventThenServiceThenUiEvent<AddPostResourceReturnData> (
                        this,
                        addPostService,
                        null,
                        new Receiver<AddPostResourceReturnData>() {
                            @Override public void success(AddPostResourceReturnData result) {
                                EventBus.getBus().post(new ListPostsViewStarter.CallControllerListPosts(true));
                            }
                            @Override public void fail(FailureResult r) { }
                        });
        addPostController.setup();
//        setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Animation anim=AnimationUtils.loadAnimation(getContext(), R.anim.expand_edittext);
//				setWidth(getMeasuredWidth());
//				if(!mExpanded) {
//					setHeight(getMeasuredHeight()*2);
//					setSingleLine(false);
//					setMaxLines(6);
//					setMinLines(3);
//				} else {
//					setHeight(getMeasuredHeight()/2);
//				}
//				startAnimation(anim);
//				mExpanded = !mExpanded;
//			}
//		});
    }

	@Override
	public void onClick(View v) {
		if(mEditText!=null && mEditText.getText().length()>0) {
			addPostService.getRequest().getBody().setSubject("-");
			addPostService.getRequest().getBody().setContent(mEditText.getText().toString());
			mCallback.onUiEventActivated();
			if(mButton!=null) mButton.loadingStart();
		} else {
			Log.e(TAG, "Edit text was null...");
		}
	}

    
    @Override
    public void setOnSubmitObserver(GenericUiObserver observer) {
        mCallback = observer;
    }

    @Override
    public void success(AddPostResourceReturnData result) {
    	if(mButton!=null) mButton.loadingStop();
        if(mEditText!=null) {
        	mEditText.setText("");
        	mEditText.setError(null);
        }
    }

    @Override
    public void fail(FailureResult f) {
    	if(mButton!=null) mButton.loadingStop();
    	if(f.getStatusCode()==403 || f.getStatusCode()==401) {
    		try {
    			FragmentActivity act = (FragmentActivity) getContext();
    			act.startActionMode(new ListPostsMapper.NotLoggedInActionMenuImplementation());
			} catch (Exception e) {
				Log.e(TAG, "Couldn't open action menu for login / reg");
				if(mEditText!=null) mEditText.setError("Exception while adding post...");
			} 
    	} else if(f!=null && f.getErrorMessage()!=null) {
            if(mEditText!=null) mEditText.setError(f.getErrorMessage());
        }
    }

}
