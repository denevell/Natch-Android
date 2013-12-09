package org.denevell.droidnatch.addthread.views;

import org.denevell.droidnatch.addthread.entities.AddPostResourceInput;
import org.denevell.droidnatch.app.baseclasses.TextEditableEditText;

import android.widget.EditText;

public class TextEditablePostUpdater extends TextEditableEditText {

    public TextEditablePostUpdater(EditText editText, 
            final AddPostResourceInput resourceInput) {
        super(editText);
        addTextInputCallack(new OnTextSubmitted() {
            @Override
            public void onTextSubmitted(String textSubmitted) {
                resourceInput.setContent("-");
                resourceInput.setSubject(textSubmitted);                    
            }
        });        
    }

}
