package org.denevell.droidnatch.login.views;

import org.denevell.droidnatch.interfaces.Clickable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class LoginButton extends Button implements Clickable {
    
    private Runnable mOnClick;

    public LoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });
    }

    @Override
    public void setOnClick(Runnable r) {
        mOnClick = r;
    }

    @Override
    public void click() {
        if(mOnClick!=null) {
            mOnClick.run();
        }
    }
    
}
