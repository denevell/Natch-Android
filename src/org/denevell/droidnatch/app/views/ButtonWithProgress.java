package org.denevell.droidnatch.app.views;

import org.denevell.natch.android.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class ButtonWithProgress extends RelativeLayout {

	private Button mButton;
	private ProgressBar mProgress;

	@SuppressWarnings("deprecation")
	public ButtonWithProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(org.denevell.natch.android.R.layout.button_with_progress, this, true);
		mButton = (Button) findViewById(org.denevell.natch.android.R.id.button_with_progress_button);
		mProgress = (ProgressBar) findViewById(org.denevell.natch.android.R.id.button_with_progress_progressbar);
		mProgress.setVisibility(View.GONE);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ButtonWithProgress, 0, 0);
		try {
			String buttonText = a.getString(R.styleable.ButtonWithProgress_button_text);
			Drawable buttonBackground = a.getDrawable(R.styleable.ButtonWithProgress_button_background);
			mButton.setText(buttonText);
			mButton.setBackgroundDrawable(buttonBackground);
		} finally {
			a.recycle();
		}
	}

	public ButtonWithProgress(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
	}
	
	public void loadingStart() {
		mButton.setEnabled(false);
		mProgress.setVisibility(View.VISIBLE);
		AlphaAnimation fadeIn = new AlphaAnimation(0,1);
		fadeIn.setDuration(300);
		fadeIn.setFillAfter(true);
		mProgress.startAnimation(fadeIn);
	}
	
	public void loadingStop() {
		mButton.setEnabled(true);
		AlphaAnimation fadeIn = new AlphaAnimation(1,0);
		fadeIn.setDuration(300);
		fadeIn.setFillAfter(true);
		mProgress.startAnimation(fadeIn);
		mProgress.setVisibility(View.GONE);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		mButton.setEnabled(enabled);
		super.setEnabled(enabled);
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		mButton.setOnClickListener(l);
	}

	public Button getButton() {
		return mButton;
	}
	
	public ProgressBar getProgress() {
		return mProgress;
	}

}
