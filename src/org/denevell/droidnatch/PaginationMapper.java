package org.denevell.droidnatch;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(library=true, complete=false)
public class PaginationMapper {
	
    public static class PaginationObject {
        public int start = 0;
        public int range = 10;
        public int defaultRange = 10;
        public int paginationMaximum = 10;
		public void paginate() {
			range=range+paginationMaximum;
		}
    }

	public class ListPostsPaginationObject extends PaginationObject {}
	public class ListThreadsPaginationObject extends PaginationObject {}

	private static PaginationMapper sStaticInstance;
	private static ListPostsPaginationObject sPostsPaginationObject;
	private static ListThreadsPaginationObject sThreadsPaginationObject;
	private PaginationMapper() {}

	public static PaginationMapper getInstance() {
		if(sStaticInstance==null) {
			sStaticInstance = new PaginationMapper();
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
