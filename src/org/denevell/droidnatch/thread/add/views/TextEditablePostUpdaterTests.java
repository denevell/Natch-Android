package org.denevell.droidnatch.thread.add.views;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import org.denevell.droidnatch.thread.add.entities.AddPostResourceInput;
import org.junit.Test;

import android.widget.EditText;

public class TextEditablePostUpdaterTests {

    private EditText editText = mock(EditText.class);
    private AddPostResourceInput resourceInput = mock(AddPostResourceInput.class);

    @Test
    public void shouldSetTextEditableCallbackOnSelf() {
        // Act
        final ArrayList<Object> wasCalled = new ArrayList<Object>();
        new TextEditablePostUpdater(editText, resourceInput) {
            @Override
            public void addTextInputCallack(OnTextSubmitted callback) {
                wasCalled.add(new Object());
                super.addTextInputCallack(callback);
            } 
        };
        
        // Assert
        assertTrue("addTextInputCalled called in constructor", wasCalled.size()==1);
    }
    
    @Test
    public void shouldSetResourceInputOnTextSubmit() throws Exception {
        // Arrange
        TextEditablePostUpdater textEditable = spy(new TextEditablePostUpdater(editText, resourceInput));
        
        // Act
        textEditable.onTextSubmitted("hiya");
        
        // Assert
        verify(resourceInput).setSubject("hiya");
    }

}
