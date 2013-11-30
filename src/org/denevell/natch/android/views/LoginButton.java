package org.denevell.natch.android.views;

import javax.inject.Inject;

import org.denevell.natch.android.LoginMapper;
import org.denevell.natch.android.service.LoginService;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import dagger.ObjectGraph;

public class LoginButton extends Button implements OnClickListener {
    
    @Inject LoginService mLoginService;

    public LoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        ObjectGraph.create(new LoginMapper(((Activity)getContext()))).inject(this);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mLoginService.go();
    }
    
}
