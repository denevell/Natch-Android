package org.denevell.droidnatch.threads.list.di;

import javax.inject.Named;
import javax.inject.Singleton;

import org.denevell.droidnatch.AppWideMapper.ListThreadsPaginationObject;
import org.denevell.droidnatch.Urls;
import org.denevell.droidnatch.app.baseclasses.ClickableListView;
import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.networking.BaseService;
import org.denevell.droidnatch.app.baseclasses.networking.VolleyRequestGET;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.posts.list.uievents.ListPostsViewStarter;
import org.denevell.droidnatch.threads.list.ListThreadsFragment;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.droidnatch.threads.list.uievents.ListThreadsViewStarter;
import org.denevell.natch.android.R;

import android.content.Context;
import android.widget.Button;
import android.widget.ListView;
import dagger.Module;
import dagger.Provides;

@Module(injects = {ListThreadsFragment.class, ListThreadsViewStarter.class, ListPostsViewStarter.class}, complete=false, library=true)
public class ListThreadsServiceMapper {
    
	@SuppressWarnings("unused")
    private static final String TAG = ListThreadsServiceMapper.class.getSimpleName();

    public ListThreadsServiceMapper() {
    }
    
    @Provides 
    public ServiceFetcher<ListThreadsResource> provideService(
            ObjectToStringConverter responseConverter, 
            FailureResultFactory failureFactory, 
            Context appContext,
            ProgressIndicator progress,
            VolleyRequest<ListThreadsResource> request) {
        return new BaseService<ListThreadsResource>(
                request,
                progress,
                responseConverter,
                failureFactory,
                ListThreadsResource.class);
    }

    @Provides @Singleton
    public VolleyRequest<ListThreadsResource> providesRequest(Context appContext, ListThreadsPaginationObject pagination) {
        String url = Urls.getBasePath() + appContext.getString(R.string.url_threads) + "" + pagination.start + "/" + pagination.range;
        VolleyRequestGET<ListThreadsResource> v = 
                new VolleyRequestGET<ListThreadsResource>();
        v.setUrl(url);
        return v;
    }

    @Provides @Singleton
    public PaginationUpdater providesPaginationReceivor(
    		final ListThreadsPaginationObject pagination,
    		@Named(ListThreadsUiEventMapper.PROVIDES_LIST_THREADS_PAGINATION_BUTTON) final Button listThreadsPaginationButton,
    		ClickableListView<ThreadResource> listView) {
        return new PaginationUpdater(pagination, listThreadsPaginationButton, listView);
    }

    public class PaginationUpdater implements Receiver<ListThreadsResource>{
		private ListThreadsPaginationObject mPagination;
		private Button mListViewPaginationFooter;
		private ListView mListView;

		public PaginationUpdater(ListThreadsPaginationObject pagination, 
				Button listThreadsPaginationButton,
				ListView listView) {
			mPagination = pagination;
			mListView = listView;
			mListViewPaginationFooter = listThreadsPaginationButton;
		}

		@Override
		public void success(ListThreadsResource result) {
			mPagination.totalNumber = result.getNumOfThreads();
			if(mPagination.start+mPagination.range>=mPagination.totalNumber) {
				mListView.removeFooterView(mListViewPaginationFooter);
			}
		}
		@Override public void fail(FailureResult r) { }
	}

}
