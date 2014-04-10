package org.denevell.droidnatch.posts.list.uievents;

import java.util.Map;

import javax.inject.Inject;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.ScreenOpenerMapper;
import org.denevell.droidnatch.app.baseclasses.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.baseclasses.networking.ServiceBuilder;
import org.denevell.droidnatch.app.baseclasses.networking.JsonVolleyRequest.LazyHeadersCallback;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.ReceivingClickingAutopaginatingListView;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.threads.list.entities.DeletePostResourceReturnData;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.newfivefour.android.manchester.R;
import com.squareup.otto.Subscribe;

import dagger.ObjectGraph;

public class LongClickDeleteThreadActivator extends View implements Activator<DeletePostResourceReturnData> {

    private static final String TAG = LongClickDeleteThreadActivator.class.getSimpleName();
	private GenericUiObserver mCallback;
    @Inject ScreenOpener screenOpener;
	private ServiceFetcher<Void, DeletePostResourceReturnData> mService;

    public LongClickDeleteThreadActivator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ObjectGraph.create(
                new ScreenOpenerMapper((FragmentActivity) getContext()),
                new CommonMapper((Activity) getContext())
        ).inject(this);
        
        String url = ShamefulStatics.getBasePath() + getContext().getString(R.string.url_del);
		Activity act = (Activity) getContext();
		mService 
        	= new ServiceBuilder<Void, DeletePostResourceReturnData>()
        		.url(url)
        		.method(Request.Method.DELETE)
        		.addLazyHeader(new LazyHeadersCallback() {
					@Override
					public void run(Map<String, String> headersMap) {
						headersMap.put("AuthKey", ShamefulStatics.getAuthKey(getContext().getApplicationContext()));
					}
				 })
        		.createJson(act, DeletePostResourceReturnData.class);        
        
        @SuppressWarnings("unchecked")
		UiEventThenServiceThenUiEvent<DeletePostResourceReturnData> deleteThreadFromPostController =
                new UiEventThenServiceThenUiEvent<DeletePostResourceReturnData>(
                        this,
                        mService,
                        null,
                        new PreviousScreenReceiver(screenOpener));
        deleteThreadFromPostController.setup();
        EventBus.getBus().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getBus().unregister(this);
    }

    @Subscribe
    public void onLongPress(ReceivingClickingAutopaginatingListView.LongPressListViewEvent obj) {
        if(obj.ob instanceof PostResource && obj.menuItem.getItemId()==R.id.posts_context_menu_delete_thread) {
        	Log.d(TAG, "Deleting thread.");
            PostResource tr = (PostResource) obj.ob;
            if(obj.index==0) {
                String url = ShamefulStatics.getBasePath() + getContext().getString(R.string.url_del);
                mService.setUrl(url + tr.getId());
                mCallback.onUiEventActivated();
            }
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
