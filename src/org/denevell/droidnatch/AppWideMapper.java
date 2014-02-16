package org.denevell.droidnatch;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(library=true, complete=false)
public class AppWideMapper {
	
    public static class PaginationObject {
        public int start = 0;
        public int range = 5;
        public int defaultRange = 5;
        public int paginationMaximum = 5;
		public void paginate() {
			range=range+paginationMaximum;
		}
    }

	public class ListPostsPaginationObject extends PaginationObject {}
	public class ListThreadsPaginationObject extends PaginationObject {}

	private static AppWideMapper sStaticInstance;
	private static ListPostsPaginationObject sPostsPaginationObject;
	private static ListThreadsPaginationObject sThreadsPaginationObject;
	private AppWideMapper() {}

	public static AppWideMapper getInstance() {
		if(sStaticInstance==null) {
			sStaticInstance = new AppWideMapper();
			return sStaticInstance;
		} else {
			return sStaticInstance;
		}
	}

    @Provides @Singleton
    public ListThreadsPaginationObject threadsPaginationObject() {
    	if(sThreadsPaginationObject==null) {
    		sThreadsPaginationObject = new ListThreadsPaginationObject();
    	}
		return sThreadsPaginationObject;
    }

    @Provides @Singleton
    public ListPostsPaginationObject postsPaginationObject() {
    	if(sPostsPaginationObject==null) {
    		sPostsPaginationObject = new ListPostsPaginationObject();
    	}
		return sPostsPaginationObject;
    }

}
