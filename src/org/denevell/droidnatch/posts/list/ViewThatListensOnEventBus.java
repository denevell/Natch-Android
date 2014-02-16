package org.denevell.droidnatch.posts.list;

import org.denevell.droidnatch.EventBus;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class ViewThatListensOnEventBus extends View {

    public ViewThatListensOnEventBus(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ViewThatListensOnEventBus(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ViewThatListensOnEventBus(Context context) {
		super(context);
	}

	@Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getBus().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getBus().unregister(this);
    }

}
