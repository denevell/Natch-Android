package org.denevell.natch.android;

import javax.inject.Inject;

import org.denevell.natch.android.service.LoginService;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import dagger.ObjectGraph;

public class MainActivity extends FragmentActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @Inject public LoginService mLoginService;
    @Inject public Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
            setContentView(R.layout.activity_main);
            LoginMapper loginMapper = new LoginMapper(this);
            ObjectGraph.create(loginMapper).inject(this);

            mLoginButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLoginService.go();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse activity", e);
            return;
        }

    }

}
