package org.denevell.droidnatch.addthread;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.TextEditable;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings({"rawtypes", "unchecked"})
public class AddThreadControllerTests {

    private AddThreadController controller;
    private ServiceFetcher service = mock(ServiceFetcher.class);
    private ResultsDisplayer displayable = mock(ResultsDisplayer.class);
    private TextEditable textEditable = mock(TextEditable.class);
    private Controller listThreadsController = mock(Controller.class);

    @Before
    public void setUp() throws Exception {
        controller = new AddThreadController(
                textEditable, 
                service,
                displayable,
                listThreadsController);
    }

    @Test
    public void shouldSetItselfAsTextEditableCallback() {
        // Act
        controller.go();
        
        // Assert
        verify(textEditable).addTextInputCallack(controller);
    }

    @Test
    public void shouldSetItselfAsServiceCallback() {
        // Act
        controller.go();
        
        // Assert
        verify(service).setServiceCallbacks(controller);
    }

    @Test
    public void shouldCallServiceOnTextEditableCallback() {
        // Arrange
        controller.go();
        
        // Act
        controller.onTextSubmitted("sometext");
        
        // Assert
        verify(service).go();
    }

    @Test
    public void shouldCallDisplayableLoadingOnTextEditableCallback() {
        // Arrange
        controller.go();
        
        // Act
        controller.onTextSubmitted("sometext");
        
        // Assert
        verify(displayable).startLoading();
    }
    
    @Test
    public void shouldCallDisplayableStopLoadingOnServiceSuccess() {
        // Arrange
        controller.go();
        
        // Act
        controller.onServiceSuccess(null);
        
        // Assert
        verify(displayable).stopLoading();
    } 
    
    @Test
    public void shouldCallDisplayableStopLoadingOnServiceFail() {
        // Arrange
        controller.go();
        
        // Act
        controller.onServiceFail(null);
        
        // Assert
        verify(displayable).stopLoading();
    }    
    

    @Test
    public void shouldCallListThreadsControllerOnSucess() throws Exception {
        // Arrange
        // Act
        controller.onServiceSuccess(null);
        
        // Assert
        verify(listThreadsController).go();
    }

    @Test
    public void shouldClearTextInputOnSucess() throws Exception {
        // Arrange
        // Act
        controller.onServiceSuccess(null);
        
        // Assert
        verify(textEditable).setText("");
    }
        
}
