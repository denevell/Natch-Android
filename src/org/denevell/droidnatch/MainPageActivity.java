package org.denevell.droidnatch;

import javax.inject.Inject;
import javax.inject.Named;

import org.denevell.droidnatch.addthread.AddThreadMapper;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.baseclasses.CommonMapper;
import org.denevell.droidnatch.listthreads.ListThreadsMapper;
import org.denevell.natch.android.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import dagger.ObjectGraph;

public class MainPageActivity extends FragmentActivity {

    private static final String TAG = MainPageActivity.class.getSimpleName();
    @Inject @Named("listthreads") Controller mController;
    @Inject @Named("addthread") Controller mControllerAddThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
            setContentView(R.layout.activity_main);
            ObjectGraph.create(
                    new ListThreadsMapper(this),
                    new AddThreadMapper(this),
                    new CommonMapper(this))
                    .inject(this);

            mController.go();
            mControllerAddThread.go();
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse activity", e);
            return;
        }
        
    }

}
