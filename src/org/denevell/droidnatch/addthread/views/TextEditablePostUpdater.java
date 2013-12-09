package org.denevell.droidnatch.addthread.views;


import org.denevell.droidnatch.addthread.entities.AddPostResourceInput;
import org.denevell.droidnatch.app.baseclasses.TextEditableEditText;
import org.denevell.droidnatch.app.interfaces.TextEditable.OnTextSubmitted;

import android.widget.EditText;

public class TextEditablePostUpdater extends TextEditableEditText implements OnTextSubmitted {

    private AddPostResourceInput mResourceInput;

    public TextEditablePostUpdater(EditText editText, 
            final AddPostResourceInput resourceInput) {
        super(editText);
        mResourceInput = resourceInput;
        addTextInputCallack(this);
    }

    @Override
    public void onTextSubmitted(String textSubmitted) {
        mResourceInput.setContent("-");
        mResourceInput.setSubject(textSubmitted);                    
    }

}
