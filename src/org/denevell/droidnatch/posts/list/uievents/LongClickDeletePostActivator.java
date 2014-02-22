package org.denevell.droidnatch.posts.list.uievents;

import java.util.Map;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.baseclasses.networking.ServiceBuilder;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestImpl.LazyHeadersCallback;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.ReceivingClickingAutopaginatingListView;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.threads.list.entities.DeletePostResourceReturnData;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.android.volley.Request;
import com.squareup.otto.Subscribe;

import dagger.ObjectGraph;

public class LongClickDeletePostActivator extends View
        implements Activator<DeletePostResourceReturnData> {
    
    @SuppressWarnings("unused")
    private static final String TAG = LongClickDeletePostActivator.class.getSimpleName();
    private GenericUiObserver mCallback;
	private ServiceFetcher<Void, DeletePostResourceReturnData> mService;

    public LongClickDeletePostActivator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
                ObjectGraph.create(
                        new CommonMapper((Activity) getContext())
                ).inject(this);
                
        String url = Urls.getBasePath() + getContext().getString(R.string.url_del);
		Activity act = (Activity) getContext();
		mService 
        	= new ServiceBuilder<Void, DeletePostResourceReturnData>()
        		.url(url)
        		.method(Request.Method.DELETE)
        		.addLazyHeader(new LazyHeadersCallback() {
					@Override
					public void run(Map<String, String> headersMap) {
						headersMap.put("AuthKey", Urls.getAuthKey());
					}
				 })
        		.create(act, DeletePostResourceReturnData.class);
                
                
        @SuppressWarnings("unchecked")
		UiEventThenServiceThenUiEvent<DeletePostResourceReturnData> deletePostController =
                new UiEventThenServiceThenUiEvent<DeletePostResourceReturnData> (
                        this,
                        mService,
                        null,
                        new Receiver<DeletePostResourceReturnData>() {
                            @Override
                            public void success(DeletePostResourceReturnData result) {
                                EventBus.getBus().post(new ListPostsViewStarter.CallControllerListPosts());
                            }
                            @Override public void fail(FailureResult r) { }
                        });
        deletePostController.setup();
        EventBus.getBus().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getBus().unregister(this);
    }

    @Subscribe
    public void onLongPress(ReceivingClickingAutopaginatingListView.LongPressListViewEvent obj) {
        if(obj.ob instanceof PostResource 
        		&& obj.menuItem.getTitle().toString().equals("Delete post")) {
            PostResource pr = (PostResource) obj.ob;
            String url = Urls.getBasePath() + getContext().getString(R.string.url_del);
            mService.getRequest().setUrl(url + pr.getId());
            mCallback.onUiEventActivated();
        }
    }

    @Override
    public void setOnSubmitObserver(GenericUiObserver observer) {
        mCallback = observer;
    }

    @Override
    public void success(DeletePostResourceReturnData result) {

    }

    @Override
    public void fail(FailureResult r) {

    }
}