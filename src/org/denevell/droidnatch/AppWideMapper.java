package org.denevell.droidnatch;

import javax.inject.Singleton;

import org.denevell.droidnatch.app.baseclasses.ClickableListView.ListThreadsPaginationObject;

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

}
