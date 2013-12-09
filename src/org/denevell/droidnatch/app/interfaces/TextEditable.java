package org.denevell.droidnatch.app.interfaces;

public interface TextEditable {
    
    public interface OnTextSubmitted {
        void onTextSubmitted(String textSubmitted);
    }
    
    public void setText(String s);
    public void addTextInputCallack(OnTextSubmitted callback);
}
