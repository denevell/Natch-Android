package org.denevell.droidnatch.threads.list.di;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.denevell.droidnatch.app.baseclasses.ClickableListView;
import org.denevell.droidnatch.app.baseclasses.HideKeyboard;
import org.denevell.droidnatch.app.baseclasses.ListViewUiEvent;
import org.denevell.droidnatch.app.interfaces.OnPressObserver.OnPress;
import org.denevell.droidnatch.app.interfaces.ReceivingUiObject;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.TypeAdapter;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.threads.list.ListThreadsFragment;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.droidnatch.threads.list.views.AddThreadEditText;
import org.denevell.droidnatch.threads.list.views.ListThreadsView;
import org.denevell.natch.android.R;

import java.util.HashMap;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = {ListThreadsFragment.class, AddThreadEditText.class, ListThreadsView.class}, complete=false, library=true)
public class ListThreadsUiEventMapper {
    
    public static final String PROVIDES_LIST_THREADS_LIST_CLICK = "list_threads_list_click";
    private static final String TAG = ListThreadsUiEventMapper.class.getSimpleName();
    private Activity mActivity;

    public ListThreadsUiEventMapper(Activity activity) {
        mActivity = activity;
    }

    @Provides @Singleton
    public ReceivingUiObject<ListThreadsResource> providesReceivingUiObject(
            Context appContext, 
            ClickableListView<ThreadResource> listView,
            // We're taking in the OnPress simply so it's constructed.
            OnPress<ThreadResource> listClickListener) {
        View loadingListView = mActivity.findViewById(R.id.list_threads_loading);
        ListThreadsArrayAdapter listAdapter = new ListThreadsArrayAdapter(appContext, R.layout.list_threads_row);
        ListViewUiEvent<ThreadResource, List<ThreadResource>, ListThreadsResource> displayer =
                new ListViewUiEvent<ThreadResource, List<ThreadResource>, ListThreadsResource>(
                        listView.getListView(), 
                        listAdapter, 
                        null,
                        appContext,
                        new TypeAdapter<ListThreadsResource, List<ThreadResource>>() {
                            @Override
                            public List<ThreadResource> convert(ListThreadsResource ob) {
                                return ob.getThreads();
                            }
                        });
        return displayer;
    }

    @Provides @Singleton 
    public ClickableListView<ThreadResource> providesListView() {
        ListView listView = (ListView) mActivity.findViewById(R.id.list_threads_listview);
        ClickableListView<ThreadResource> ltlv = 
                new ClickableListView<ThreadResource>(
                        listView, 
                        new HideKeyboard(),
                        new ListThreadsContextMenu());
        return ltlv;
    }

    @Provides @Singleton 
    public OnPress<ThreadResource> providesOnListClickAction(
            final ClickableListView<ThreadResource> onPressObserver,
            final ScreenOpener screenOpener) {
        OnPress<ThreadResource> onPress = new OnPress<ThreadResource>() {
                    @Override
                    public void onPress(ThreadResource obj) {
                        Log.v(TAG, "Opening new list posts fragment");
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put(ListPostsFragment.BUNDLE_KEY_THREAD_ID, obj.getId());
                        hm.put(ListPostsFragment.BUNDLE_KEY_THREAD_NAME, obj.getSubject());
                        screenOpener.openScreen(ListPostsFragment.class, hm);
                    }
                };
        onPressObserver.addOnPressListener(onPress);
        return onPress;
    }

}