package org.denevell.droidnatch.threads.list.di;

import java.util.HashMap;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.ClickableListView;
import org.denevell.droidnatch.app.baseclasses.ListViewResultDisplayer;
import org.denevell.droidnatch.app.interfaces.ContextItemSelectedObserver;
import org.denevell.droidnatch.app.interfaces.OnPressObserver.OnPress;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.posts.list.ListPostsFragment;
import org.denevell.droidnatch.threads.list.ListThreadsArrayAdapter;
import org.denevell.droidnatch.threads.list.ListThreadsContextMenu;
import org.denevell.droidnatch.threads.list.ListThreadsFragment;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import dagger.Module;
import dagger.Provides;

@Module(injects = {ListThreadsFragment.class}, complete=false, library=true)
public class ListThreadsResultsDisplayableMapper {
    
    public static final String PROVIDES_LIST_THREADS_LIST_CLICK = "list_threads_list_click";
    private static final String PROVIDES_LIST_THREADS_LOADING = "listthreads_loading";
    private static final String TAG = ListThreadsResultsDisplayableMapper.class.getSimpleName();
    private Activity mActivity;
    private ContextItemSelectedObserver mContextItemObserver;

    public ListThreadsResultsDisplayableMapper(Activity activity,
            ContextItemSelectedObserver contextItemObserver) {
        mActivity = activity;
        mContextItemObserver = contextItemObserver;
    }

    @Provides @Singleton
    public ResultsDisplayer<List<ThreadResource>> provideResultsDisplayer(
            Context appContext, 
            ArrayAdapter<ThreadResource> arrayAdapter, 
            ClickableListView<ThreadResource> listView, 
            @Named(PROVIDES_LIST_THREADS_LOADING) View listViewLoading) {
        ListViewResultDisplayer<ThreadResource, List<ThreadResource>> displayer = 
                new ListViewResultDisplayer<ThreadResource, List<ThreadResource>>(
                        listView.getListView(), 
                        arrayAdapter, 
                        listViewLoading,
                        appContext);
        return displayer;
    }

    @Provides @Singleton 
    public ClickableListView<ThreadResource> providesListView(
            ContextItemSelectedObserver contextSelectedObserver) {
        ListView listView = (ListView) mActivity.findViewById(R.id.listView1);
        ClickableListView<ThreadResource> ltlv = 
                new ClickableListView<ThreadResource>(
                        listView, 
                        contextSelectedObserver,
                        new ListThreadsContextMenu());
        return ltlv;
    }

    @Provides @Singleton @Named(PROVIDES_LIST_THREADS_LIST_CLICK)
    public OnPress<ThreadResource> providesOnListClickAction(
            final ClickableListView<ThreadResource>onPressObserver, 
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

    @Provides @Singleton @Named(PROVIDES_LIST_THREADS_LOADING)
    public View providesLoadingListView() {
        View v = mActivity.findViewById(R.id.list_threads_loading);
        return v;
    }

    @Provides @Singleton
    public ArrayAdapter<ThreadResource> providesListAdapter(Context appContext) {
        return new ListThreadsArrayAdapter(appContext, R.layout.list_threads_row);
    }

    @Provides
    public ContextItemSelectedObserver providesContextSelectedHolder() {
        return mContextItemObserver;
    }
    
}
