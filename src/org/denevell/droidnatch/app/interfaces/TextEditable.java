package org.denevell.droidnatch.app.interfaces;

public interface TextEditable {
    
    public interface OnTextSubmitted {
        void onTextSubmitted(String textSubmitted);
    }
    
    void addTextInputCallack(OnTextSubmitted callback);
}
