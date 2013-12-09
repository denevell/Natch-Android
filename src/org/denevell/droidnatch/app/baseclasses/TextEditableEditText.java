package org.denevell.droidnatch.app.baseclasses;

import java.util.ArrayList;
import java.util.List;

import org.denevell.droidnatch.app.interfaces.TextEditable;

import android.content.Context;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class TextEditableEditText implements TextEditable {
    private List<OnTextSubmitted> mCallbacks = new ArrayList<TextEditable.OnTextSubmitted>();

    public TextEditableEditText(final EditText editText) {
        editText.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event!=null && event.getAction()==KeyEvent.ACTION_DOWN) {
                    return true;
                }
                for (OnTextSubmitted callback: mCallbacks) {
                    InputMethodManager imm = 
                            (InputMethodManager)editText.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                      imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    callback.onTextSubmitted(editText.getText().toString());
                }
                return true;
            }
        });
    }

    @Override
    public void addTextInputCallack(final OnTextSubmitted callback) {
        mCallbacks.add(callback);
    }
}