package org.denevell.droidnatch.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

public class ButtonHideKeyboard extends Button {
	
	private static String TAG = ButtonHideKeyboard.class.getSimpleName();

	public ButtonHideKeyboard(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
	}
	
	public ButtonHideKeyboard(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	 	if(event.getAction()==MotionEvent.ACTION_UP) {
				try {
					Log.i(TAG, "Attempting to hide keyboard");
					InputMethodManager imm = (InputMethodManager) getContext()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(getWindowToken(), 0);
				} catch (Exception e) {
					e.printStackTrace();
				}
	 		
	 	}
		return super.onTouchEvent(event);
	}

}
