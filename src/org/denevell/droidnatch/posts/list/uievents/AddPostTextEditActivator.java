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
import org.denevell.droidnatch.app.views.EditTextHideKeyboard;
import org.denevell.droidnatch.app.views.ReceivingClickingAutopaginatingListView;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.posts.list.ListPostsMapper;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceInput;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.volley.Request;

import dagger.ObjectGraph;

public class AddPostTextEditActivator extends EditTextHideKeyboard implements
        Activator<AddPostResourceReturnData>, OnEditorActionListener {
    
    private GenericUiObserver mCallback;
    private ServiceFetcher<AddPostResourceInput, AddPostResourceReturnData> addPostService;
    @Inject ReceivingClickingAutopaginatingListView<ThreadResource, PostResource, List<PostResource>> mListView;
	//protected boolean mExpanded;
    
    public AddPostTextEditActivator(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        setOnEditorActionListener(this);
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
        		.create(act, AddPostResourceReturnData.class); 
        
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
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(event!=null && event.getAction()==KeyEvent.ACTION_DOWN) {
            return true; // Natsty hack to ui automator doesn't call this twice
        }
        
        addPostService.getRequest().getBody().setSubject("-");
        addPostService.getRequest().getBody().setContent(v.getText().toString());
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
