package org.denevell.droidnatch.app.baseclasses;

import android.support.v4.app.FragmentActivity;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.posts.list.uievents.LongClickDeleteThreadActivator;
import org.denevell.droidnatch.threads.list.uievents.AddThreadEditTextActivator;
import org.denevell.droidnatch.threads.list.uievents.ListThreadsViewStarter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = {MainPageActivity.class, AddThreadEditTextActivator.class, ListThreadsViewStarter.class, LongClickDeleteThreadActivator.class}, complete = false, library=true)
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
