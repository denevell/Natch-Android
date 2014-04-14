package org.denevell.droidnatch.posts.list.uievents;

import java.util.List;

import javax.inject.Inject;

import org.denevell.droidnatch.Application;
import org.denevell.droidnatch.EventBus;
import org.denevell.droidnatch.PaginationMapper;
import org.denevell.droidnatch.PaginationMapper.ListPostsPaginationObject;
import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.ObservableFragment;
import org.denevell.droidnatch.app.baseclasses.UiEventThenServiceThenUiEvent;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.views.ReceivingClickingAutopaginatingListView;
import org.denevell.droidnatch.app.visited_db.VisitedPostsTable;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.posts.list.ListPostsMapper;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.newfivefour.android.manchester.R;
import com.squareup.otto.Subscribe;

import dagger.ObjectGraph;

public class ListPostsViewStarter extends View {

    protected static final String TAG = ListPostsViewStarter.class.getSimpleName();
	private UiEventThenServiceThenUiEvent<ThreadResource> controller;
    private final class ScrollToBottomReceiver implements Receiver<ThreadResource> {
		@Override public void success(ThreadResource result) {
		    if(mScrollToBottom && listViewReceivingUiObject!=null) {
		        int count = listViewReceivingUiObject.getCount();
		        if(count>0) listViewReceivingUiObject.smoothScrollToPosition(count);
		    }
		}
		@Override public void fail(FailureResult r) {}
	}

	public static class CallControllerListPosts {
		private boolean scrollToBottom = false;
		public CallControllerListPosts(boolean scrollToBottom) {
			this.scrollToBottom = scrollToBottom;
		}
	}

    @Inject ServiceFetcher<Void, ThreadResource> listPostsService;
    @Inject ReceivingClickingAutopaginatingListView<ThreadResource, PostResource, List<PostResource>> listViewReceivingUiObject;
    @Inject ListPostsPaginationObject mPaginationObject;
	private boolean mScrollToBottom;

    public ListPostsViewStarter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("unchecked")
	public void setup(Bundle bundle) {
        String threadId = bundle.getString(ListPostsFragment.BUNDLE_KEY_THREAD_ID);
        ObjectGraph.create(
                new CommonMapper((Activity) getContext()),
                PaginationMapper.getInstance(),
                new ListPostsMapper((Activity) getContext(), threadId)
        ).inject(this);
        controller =
                new UiEventThenServiceThenUiEvent<ThreadResource>(
                        listPostsService,
                        listViewReceivingUiObject, 
                        new ScrollToBottomReceiver(), 
                        new Receiver<ThreadResource>() {
							@Override public void success(ThreadResource result) {
								try {
									List<PostResource> posts = result.getPosts();
									VisitedPostsTable visitedPostsTable = Application.getVisitedPostsDatabase(getContext());
									for (PostResource postResource : posts) {
										long modificationDateOfPost = visitedPostsTable.isPostIdInTable(postResource.getId());
										if(modificationDateOfPost==-1) {
											visitedPostsTable.insert(postResource.getId(), postResource.getModification());
										} else if(modificationDateOfPost!=postResource.getModification()) {
											visitedPostsTable.update(postResource.getId(), postResource.getModification());
										}
									}
								} catch (Exception e) {
									Log.e(TAG, "Couldn't add `this as a seen thread", e);
								}
							}
							@Override public void fail(FailureResult r) { }
						});
        controller.setup();
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
    public void listItYeah(CallControllerListPosts events) {
    	mScrollToBottom = events.scrollToBottom;
        if(controller!=null) {
            controller.onUiEventActivated();
        }
    }
    
	@Subscribe
	public void onOptionMenu(ObservableFragment.OptionMenuItemHolder menu) {
		if(R.id.posts_option_menu_refresh!= menu.item.getItemId()) return;
        if(controller!=null)  {
            controller.onUiEventActivated();
        }
	}    
}