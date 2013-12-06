package org.denevell.droidnatch.app.interfaces;

public interface TextEditable {
    
    public interface OnTextInputted {
        void onTextSubmitted(String textSubmitted);
    }
    
    void setTextInputCallack(OnTextInputted callback);
}
