package org.denevell.droidnatch.threads.list.uievents;

import java.util.Arrays;
import java.util.Map;

import javax.inject.Inject;

import org.denevell.droidnatch.AppWideMapper;
import org.denevell.droidnatch.SeenThreadsSaver;
import org.denevell.droidnatch.Urls;
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
import org.denevell.droidnatch.threads.list.entities.AddPostResourceInput;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;

import dagger.ObjectGraph;

public class AddThreadViewActivator extends LinearLayout implements
        Activator<AddPostResourceReturnData>, 
        View.OnClickListener,
        Finishable {

    private ServiceFetcher<AddPostResourceInput, AddPostResourceReturnData> mAddPostService;
    @Inject ScreenOpener mScreenOpener;
    private GenericUiObserver mCallback;
    private Button mButton;
    private TextView mSubject;
    private TextView mContent;
    private TextView mTags;
    private Runnable mSuccessCallback;

    public AddThreadViewActivator(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.add_thread_layout, this, true);
        mButton = (Button) findViewById(R.id.add_thread_button);
        mSubject = (TextView) findViewById(R.id.add_thread_subject_edittext);
        mContent = (TextView) findViewById(R.id.add_thread_content_edittext);
        mTags = (TextView) findViewById(R.id.add_thread_tags_edittext);
    }

    private void inject() {
        ObjectGraph.create(
                new ScreenOpenerMapper((FragmentActivity) getContext()),
                AppWideMapper.getInstance(),
                new CommonMapper((Activity) getContext())
        ).inject(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mButton.setOnClickListener(this);
        inject();
        
        Activity act = (Activity) getContext();
        
        String url = Urls.getBasePath()+getContext().getString(R.string.url_addthread);
        mAddPostService = new ServiceBuilder<AddPostResourceInput, AddPostResourceReturnData>()
        		.url(url)
        		.method(Request.Method.PUT)
        		.entity(new AddPostResourceInput())
        		.addLazyHeader(new LazyHeadersCallback() {
					@Override public void run(Map<String, String> headersMap) {
						headersMap.put("AuthKey", Urls.getAuthKey());
					}
				})
        		.create(act, AddPostResourceReturnData.class);
        
        @SuppressWarnings("unchecked")
		Controller addThreadController =
                new UiEventThenServiceThenUiEvent<AddPostResourceReturnData>(
                        this,
                        mAddPostService,
                        null,
                        new OpenNewThreadReceiver(mScreenOpener));
        addThreadController.setup();
    }
    
    @Override
    public void setOnSubmitObserver(GenericUiObserver observer) {
        mCallback = observer;
    }

    @Override
    public void success(AddPostResourceReturnData result) {
        mSubject.setText("");
        mSubject.setError(null);
        mContent.setText("");
        mContent.setError(null);
        mTags.setText("");
        mTags.setError(null);
        if(mSuccessCallback!=null) mSuccessCallback.run();
        // TODO: Refactor this away
        try {
        	String id = result.getThread().getId();
        	SeenThreadsSaver.addThreadId(getContext().getApplicationContext(), id);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @Override
    public void setFinishedCallback(Runnable runnable) {
        mSuccessCallback = runnable;
    }

    @Override
    public void fail(FailureResult f) {
        if(f!=null && f.getErrorMessage()!=null) {
            mSubject.setError(f.getErrorMessage());
        }
    }

    @Override
    public void onClick(View view) {
    	mAddPostService.getRequest().getBody().setContent(mContent.getText().toString());
        mAddPostService.getRequest().getBody().setSubject(mSubject.getText().toString());
        String[] tags = mTags.getText().toString().split(",");
        mAddPostService.getRequest().getBody().setTags(Arrays.asList(tags));
        mCallback.onUiEventActivated();
    }

}
