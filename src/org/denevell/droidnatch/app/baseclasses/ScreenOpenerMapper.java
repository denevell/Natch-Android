package org.denevell.droidnatch.app.baseclasses;

import javax.inject.Singleton;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;

import android.support.v4.app.FragmentActivity;
import dagger.Module;
import dagger.Provides;

@Module(injects = {MainPageActivity.class}, complete = false, library=true)
public class ScreenOpenerMapper {
    
    private FragmentActivity mActivity;

    public ScreenOpenerMapper(FragmentActivity mainPageActivity) {
        mActivity = mainPageActivity;
    }

    @Provides @Singleton
    public ScreenOpener providesScreenOpener() {
        return new FragmentScreenOpener(mActivity);
    }

}
