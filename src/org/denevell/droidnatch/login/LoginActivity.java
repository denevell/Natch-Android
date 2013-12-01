package org.denevell.droidnatch.login;

import java.util.List;

import javax.inject.Inject;

import org.denevell.droidnatch.interfaces.Controller;
import org.denevell.natch.android.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import dagger.ObjectGraph;

public class LoginActivity extends FragmentActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    @Inject List<Controller> mControllers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
            setContentView(R.layout.activity_main);
            ObjectGraph.create(new LoginMapper(this)).inject(this);
            
            for (Controller c: mControllers) {
               c.go(); 
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse activity", e);
            return;
        }
        
    }

}
