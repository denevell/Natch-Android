package org.denevell.droidnatch.app.baseclasses;

import java.util.ArrayList;
import java.util.List;

import org.denevell.droidnatch.app.interfaces.TextEditable;

import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class TextEditableEditText implements TextEditable {
    private List<OnTextSubmitted> mCallbacks = new ArrayList<TextEditable.OnTextSubmitted>();
    private EditText mEditText;

    public TextEditableEditText(final EditText editText) {
        mEditText = editText;
        editText.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event!=null && event.getAction()==KeyEvent.ACTION_DOWN) {
                    return true; // Natsty hack to ui automator doesn't call this twice
                }
                for (OnTextSubmitted callback: mCallbacks) {
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

    @Override
    public void setText(String s) {
        if(mEditText!=null) {
            mEditText.setText(s);
        }
    }
}