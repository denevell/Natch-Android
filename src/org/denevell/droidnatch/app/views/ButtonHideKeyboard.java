package org.denevell.droidnatch.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

public class ButtonHideKeyboard extends Button {

    public ButtonHideKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    public void onEditorAction(int actionCode) {
        if(actionCode == EditorInfo.IME_ACTION_DONE) {
            try {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindowToken(), 0);
            } catch (Exception e) {}
        }
        super.onEditorAction(actionCode);
    }

}
