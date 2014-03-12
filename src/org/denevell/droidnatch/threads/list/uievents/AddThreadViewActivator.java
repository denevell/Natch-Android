package org.denevell.droidnatch.threads.list.uievents;

import java.util.Arrays;
import java.util.Map;

import javax.inject.Inject;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.PaginationMapper;
import org.denevell.droidnatch.ShamefulStatics;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.ScreenOpenerMapper;
import org.denevell.droidnatch.app.baseclasses.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.baseclasses.networking.ServiceBuilder;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestImpl.LazyHeadersCallback;
import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.Finishable;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.ButtonWithProgress;
import org.denevell.droidnatch.posts.list.ListPostsMapper;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceInput;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;
import org.denevell.droidnatch.threads.list.uievents.LoginViewActivator.LoginUpdatedEvent;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.squareup.otto.Subscribe;

import dagger.ObjectGraph;

public class AddThreadViewActivator extends LinearLayout implements
        Activator<AddPostResourceReturnData>, 
        View.OnClickListener,
        Finishable {

    private static final String TAG = AddThreadViewActivator.class.getSimpleName();
	private ServiceFetcher<AddPostResourceInput, AddPostResourceReturnData> mAddPostService;
    @Inject ScreenOpener mScreenOpener;
    private GenericUiObserver mCallback;
    private ButtonWithProgress mButton;
    private TextView mSubject;
    private TextView mContent;
    private Runnable mSuccessCallback;
    
    public AddThreadViewActivator(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.add_thread_layout, this, true);
        mButton = (ButtonWithProgress) findViewById(R.id.add_thread_button);
        mSubject = (TextView) findViewById(R.id.add_thread_subject_edittext);
        mContent = (TextView) findViewById(R.id.add_thread_content_edittext);
    }

    private void inject() {
        ObjectGraph.create(
                new ScreenOpenerMapper((FragmentActivity) getContext()),
                PaginationMapper.getInstance(),
                new CommonMapper((Activity) getContext())
        ).inject(this);
    }
    
    @Override
    protected void onDetachedFromWindow() {
    	super.onDetachedFromWindow();
    	EventBus.getBus().unregister(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    	EventBus.getBus().register(this);
        updatedAddButtonOnLogin();
        inject();
        
        String url = ShamefulStatics.getBasePath()+getContext().getString(R.string.url_addthread);
        mAddPostService = new ServiceBuilder<AddPostResourceInput, AddPostResourceReturnData>()
        		.url(url)
        		.method(Request.Method.PUT)
        		.entity(new AddPostResourceInput())
        		.addLazyHeader(new LazyHeadersCallback() {
					@Override public void run(Map<String, String> headersMap) {
						headersMap.put("AuthKey", ShamefulStatics.getAuthKey(getContext().getApplicationContext()));
					}
				})
        		.create(null, AddPostResourceReturnData.class);
        
        @SuppressWarnings("unchecked")
		Controller addThreadController =
                new UiEventThenServiceThenUiEvent<AddPostResourceReturnData>(
                        this,
                        mAddPostService,
                        null,
                        new OpenNewThreadReceiver(mScreenOpener));
        addThreadController.setup();
    }

	private void updatedAddButtonOnLogin() {
		try {
			if (ShamefulStatics.emptyUsername(getContext().getApplicationContext())) {
				// mButton.setText("Please login or register");
				//mButton.setEnabled(false);
			} else {
				//mButton.setEnabled(true);
				//mButton.setOnClickListener(this);
			}
			mButton.setEnabled(true);
			mButton.setOnClickListener(this);
			mButton.requestLayout();
			mButton.refreshDrawableState();
		} catch (Exception e) {
			Log.d(TAG, "Problem updating the add button", e);
		}
	}
    
    @Subscribe
    public void onLoginStatusChanged(LoginUpdatedEvent e) {
    	updatedAddButtonOnLogin();
    }
    
    @Override
    public void setOnSubmitObserver(GenericUiObserver observer) {
        mCallback = observer;
    }

    @Override
    public void success(AddPostResourceReturnData result) {
        if(mButton!=null) mButton.loadingStop();
        mSubject.setText("");
        mSubject.setError(null);
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
        if(f!=null && f.getStatusCode()==403 || f.getStatusCode()==401) {
    		try {
    			FragmentActivity act = (FragmentActivity) getContext();
    			act.startActionMode(new ListPostsMapper.NotLoggedInActionMenuImplementation());
			} catch (Exception e) {
				Log.e(TAG, "Couldn't open action menu for login / reg");
				if(mSubject!=null) mSubject.setError("Please login first");
			} 
        } else if(f!=null && f.getErrorMessage()!=null) {
            mSubject.setError(f.getErrorMessage());
        } else {
            mSubject.setError("Unknown error");
        }
    }

    @Override
    public void onClick(View view) {
    	if(mSubject!=null && mSubject.getText()!=null && 
    			mSubject.getText().toString().trim().length()==0) return;
    	mAddPostService.getRequest().getBody().setContent(mContent.getText().toString());
        mAddPostService.getRequest().getBody().setSubject(mSubject.getText().toString());
        String[] tags = new String[] {""};//mTags.getText().toString().split(",");
        mAddPostService.getRequest().getBody().setTags(Arrays.asList(tags));
        if(mButton!=null) mButton.loadingStart();
        mCallback.onUiEventActivated();
    }

}
