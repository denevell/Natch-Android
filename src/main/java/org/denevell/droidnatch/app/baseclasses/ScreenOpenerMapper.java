package org.denevell.droidnatch.app.baseclasses;

import org.denevell.droidnatch.app.interfaces.ScreenOpener;

import android.support.v4.app.FragmentActivity;
import dagger.Module;
import dagger.Provides;

@Module(
        complete = false, library=true)
public class ScreenOpenerMapper {
    
    private FragmentActivity mActivity;

    public ScreenOpenerMapper(FragmentActivity mainPageActivity) {
        mActivity = mainPageActivity;
    }

    @Provides 
    public ScreenOpener providesScreenOpener() {
        return new FragmentScreenOpener(mActivity);
    }

}
