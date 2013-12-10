package org.denevell.droidnatch;

import javax.inject.Inject;
import javax.inject.Named;

import org.denevell.droidnatch.app.baseclasses.CommonMapper;
import org.denevell.droidnatch.app.interfaces.ContextItemSelected;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.thread.add.AddThreadMapper;
import org.denevell.droidnatch.thread.delete.DeleteThreadController;
import org.denevell.droidnatch.thread.delete.DeleteThreadMapper;
import org.denevell.droidnatch.threads.list.ListThreadsMapper;
import org.denevell.natch.android.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import dagger.ObjectGraph;

public class MainPageActivity extends FragmentActivity {

    private static final String TAG = MainPageActivity.class.getSimpleName();
    @Inject @Named("listthreads") Controller mController;
    @Inject @Named("addthread") Controller mControllerAddThread;
    @Inject @Named("deletethread") DeleteThreadController mControllerDeleteThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
            setContentView(R.layout.activity_main);
            ObjectGraph.create(
                    new DeleteThreadMapper(this),
                    new ListThreadsMapper(this),
                    new AddThreadMapper(this),
                    new CommonMapper(this))
                    .inject(this);
            mController.go();
            mControllerAddThread.go();
            mControllerDeleteThread.go();
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse activity", e);
            return;
        }
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        try {
            ContextItemSelected listThreadsContext = (ContextItemSelected) mControllerDeleteThread;
            listThreadsContext.onContextItemSelected(item);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return super.onContextItemSelected(item);
    }
    

}
