package org.denevell.droidnatch.app.baseclasses;

import android.support.v4.app.FragmentActivity;

import org.denevell.droidnatch.app.interfaces.ScreenOpener;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        complete = false, library=true)
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
