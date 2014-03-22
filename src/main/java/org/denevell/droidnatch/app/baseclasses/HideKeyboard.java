package org.denevell.droidnatch.app.baseclasses;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class HideKeyboard {

    public static String TAG = HideKeyboard.class.getSimpleName();

    public void hideKeyboard(Context context, View view) {
        if(context==null || view==null || view.getWindowToken()==null) {
            Log.d(TAG, "We passed a null context or view to hideKeyboard.");
        }
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
