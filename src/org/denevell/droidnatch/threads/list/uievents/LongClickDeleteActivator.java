package org.denevell.droidnatch.threads.list.uievents;

import java.util.Map;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.baseclasses.networking.ServiceBuilder;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestImpl.LazyHeadersCallback;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.ReceivingClickingAutopaginatingListView;
import org.denevell.droidnatch.threads.list.entities.DeletePostResourceReturnData;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.android.volley.Request;
import com.squareup.otto.Subscribe;

import dagger.ObjectGraph;

public class LongClickDeleteActivator extends View implements Activator<DeletePostResourceReturnData> {
    
    private GenericUiObserver mCallback;
	private ServiceFetcher<Void, DeletePostResourceReturnData> mService;

    public LongClickDeleteActivator(Context context, AttributeSet attrs) {
        super(context, attrs);
        ObjectGraph.create(
                new CommonMapper((Activity) context)
        ).inject(this);
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        
        Activity act = (Activity) getContext();
        
        String url = ShamefulStatics.getBasePath() + getContext().getString(R.string.url_del);
		mService 
        	= new ServiceBuilder<Void, DeletePostResourceReturnData>()
        		.url(url)
        		.method(Request.Method.DELETE)
        		.addLazyHeader(new LazyHeadersCallback() {
					@Override
					public void run(Map<String, String> headersMap) {
						headersMap.put("AuthKey", ShamefulStatics.getAuthKey(getContext()));
					}
				 })
        		.create(act, DeletePostResourceReturnData.class);
        
        @SuppressWarnings("unchecked")
		Controller deleteThreadController =
            new UiEventThenServiceThenUiEvent<DeletePostResourceReturnData>(
                this,
                mService,
                null,
                new Receiver<DeletePostResourceReturnData>() {
                    @Override
                    public void success(DeletePostResourceReturnData v) {
                        EventBus.getBus().post(new ListThreadsViewStarter.CallControllerListThreads());
                    }
                    @Override
                    public void fail(FailureResult r) { }
                });
        deleteThreadController.setup();
        EventBus.getBus().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getBus().unregister(this);
    }

    @Subscribe
    public void onLongPress(ReceivingClickingAutopaginatingListView.LongPressListViewEvent obj) {
        if(obj.ob instanceof ThreadResource && obj.menuItem.getItemId()==R.id.posts_context_menu_delete_thread) {
            ThreadResource pr = (ThreadResource) obj.ob;
            String url = ShamefulStatics.getBasePath() + getContext().getString(R.string.url_del);
            mService.getRequest().setUrl(url+pr.getRootPostId());
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
