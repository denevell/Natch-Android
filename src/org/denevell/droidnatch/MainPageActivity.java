package org.denevell.droidnatch;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.denevell.droidnatch.addthread.AddThreadMapper;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.listthreads.ListThreadsMapper;
import org.denevell.natch.android.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import dagger.ObjectGraph;

public class MainPageActivity extends FragmentActivity {

    private static final String TAG = MainPageActivity.class.getSimpleName();
    @Inject List<Controller> mControllers;
    @Inject @Named("addthread") List<Controller> mControllersAddThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
            setContentView(R.layout.activity_main);
            ObjectGraph.create(new ListThreadsMapper(this),
                    new AddThreadMapper(this)).inject(this);
            for (Controller c: mControllers) {
               c.go(); 
            }
            
            for (Controller c: mControllersAddThread) {
               c.go(); 
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse activity", e);
            return;
        }
        
    }

}
