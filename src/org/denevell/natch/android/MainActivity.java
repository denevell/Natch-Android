package org.denevell.natch.android;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import dagger.ObjectGraph;

public class MainActivity extends FragmentActivity {

    @Inject public LoginService mLoginService;
    @Inject public Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        ObjectGraph objectGraph = ObjectGraph.create(new ObjectMapper(this));
        objectGraph.inject(this);

        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginService.go();
            }
        });
    }

}
