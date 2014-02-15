package org.denevell.droidnatch;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(library=true, complete=false)
public class AppWideMapper {
	
	private static AppWideMapper sStaticInstance;
	private static ListThreadsPaginationObject sPaginationObject;
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
    	if(sPaginationObject==null) {
    		sPaginationObject = new ListThreadsPaginationObject();
    	}
		return sPaginationObject;
    }

    public static class ListThreadsPaginationObject {
        public int start = 0;
        public int range = 5;
		public long totalNumber = 0;
    }

}
