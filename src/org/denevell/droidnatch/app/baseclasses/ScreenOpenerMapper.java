package org.denevell.droidnatch.app.baseclasses;

import javax.inject.Singleton;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;

import android.app.Activity;
import dagger.Module;
import dagger.Provides;

@Module(injects = {MainPageActivity.class}, complete = false, library=true)
public class ScreenOpenerMapper {
    
    private Activity mActivity;

    public ScreenOpenerMapper(Activity mainPageActivity) {
        mActivity = mainPageActivity;
    }

    @Provides @Singleton
    public ScreenOpener providesScreenOpener() {
        return new FragmentScreenOpener(mActivity);
    }

}
