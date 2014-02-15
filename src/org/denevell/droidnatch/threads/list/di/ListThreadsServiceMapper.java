package org.denevell.droidnatch.threads.list.di;

import javax.inject.Singleton;

import org.denevell.droidnatch.Urls;
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
import org.denevell.droidnatch.threads.list.uievents.ListThreadsViewStarter;
import org.denevell.natch.android.R;

import android.content.Context;
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
    public VolleyRequest<ListThreadsResource> providesRequest(Context appContext, PaginationObject pagination) {
        String url = Urls.getBasePath() + appContext.getString(R.string.url_threads) + "" + pagination.start + "/" + pagination.range;
        VolleyRequestGET<ListThreadsResource> v = 
                new VolleyRequestGET<ListThreadsResource>();
        v.setUrl(url);
        return v;
    }

    @Provides @Singleton
    public PaginationUpdater prividesPaginationReceivor(final PaginationObject pagination) {
        return new PaginationUpdater(pagination);
    }

    public class PaginationUpdater implements Receiver<ListThreadsResource>{
		private PaginationObject mPagination;

		public PaginationUpdater(PaginationObject pagination) {
			mPagination = pagination;
		}

		@Override
		public void success(ListThreadsResource result) {
			mPagination.totalNumber = result.getNumOfThreads();
		}
		@Override public void fail(FailureResult r) { }
	}


    @Provides @Singleton
    public PaginationObject threadsPaginationObject() {
        return new PaginationObject();
    }

    public class PaginationObject {
        public int start = 0;
        public int range = 2;
		public long totalNumber = 0;
    }
}
