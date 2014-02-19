package org.denevell.droidnatch.posts.list.uievents;

import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.app.views.ClickableListView;
import org.denevell.droidnatch.app.views.DialogueFragmentWithView;
import org.denevell.droidnatch.app.views.DialogueFragmentWithView.InitialiseView;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.natch.android.R;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.squareup.otto.Subscribe;

public class LongClickEditPostActivator extends View {

    @SuppressWarnings("unused")
    private static final String TAG = LongClickEditPostActivator.class.getSimpleName();
    private final FragmentActivity mActivity;

    public LongClickEditPostActivator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = (FragmentActivity)getContext();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getBus().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getBus().unregister(this);
    }

    @Subscribe
    public void onLongPress(final ClickableListView.LongPressListViewEvent obj) {
        if(obj.index!=0 && obj.ob instanceof PostResource && obj.title.equals("Edit post")) {

			InitialiseView initView = new InitialiseView() {
				@Override
				public void intialise(View v, final DialogFragment df) {
					if (v instanceof EditPostViewActivator) {
						EditPostViewActivator view = (EditPostViewActivator) v;
						view.setPost((PostResource) obj.ob);
						view.setSuccessCallback(new Runnable() {
							@Override
							public void run() {
								df.getDialog().dismiss();
							}
						});
					}
				}
			};

            final DialogueFragmentWithView df = 
            		DialogueFragmentWithView.getInstance(
            				R.layout.edit_post_dialogue_layout, 
            				initView);
            mActivity.getSupportFragmentManager().beginTransaction().add(df, "editthread_dialogue").commit();
        }
    }

}