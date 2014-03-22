package org.denevell.droidnatch.app.baseclasses;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.denevell.droidnatch.app.interfaces.Activator;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.Receiver;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
@SuppressWarnings({"unchecked","rawtypes"})
public class UiEventThenServiceThenUiEventTests {

    private UiEventThenServiceThenUiEvent controller;
    private ServiceFetcher service = mock(ServiceFetcher.class);
    private ProgressIndicator displayable = mock(ProgressIndicator.class);
    private Activator uiObservable = mock(Activator.class);
    private Receiver nextUiEvent = mock(Receiver.class);

    @Before
    public void setUp() throws Exception {
        controller = new UiEventThenServiceThenUiEvent(
                uiObservable,
                service,
                displayable, 
                nextUiEvent);
    }

    @Test
    public void shouldSetServiceCallbacksOnSetup() {
        // Act
        controller.setup();
        
        // Assert
        verify(service).setServiceCallbacks(controller);
    }

    @Test
    public void shouldSetUiEventCallbacksOnSetup() {
        // Act
        controller.setup();
        
        // Assert
        verify(uiObservable).setOnSubmitObserver(controller);
    }

    @Test
    public void shouldStartDisplayableLoadingOnUiEvent() {
        // Act
        controller.onUiEventActivated();
        
        // Assert
        verify(displayable).start();
    }

    @Test
    public void shouldStartServiceOnNetworkStart() {
        // Act
        controller.onUiEventActivated();
        
        // Assert
        verify(service).go();
    }
    
    @Test
    public void shouldStopLoadingOnSuccess() throws Exception {
        // Arrange
        Object o = new Object();

        // Act
        controller.onServiceSuccess(o);
        
        // Assert
        verify(displayable).stop();
    }

    @Test
    public void shouldSetUiEventAsSuccessOnSuccess() throws Exception {
        // Arrange
        Object o = new Object();

        // Act
        controller.onServiceSuccess(o);
        
        // Assert
        verify(uiObservable).success(o);
    }

    @Test
    public void shouldStartNextUiEventOnSuccess() throws Exception {
        // Arrange
        Object o = new Object();

        // Act
        controller.onServiceSuccess(o);
        
        // Assert
        verify(nextUiEvent).success(o);
    }

    @Test
    public void shouldStopLoadingOnFail() throws Exception {
        // Arrange
        FailureResult r = new FailureResult();

        // Act
        controller.onServiceFail(r);
        
        // Assert
        verify(displayable).stop();
    }

    @Test
    public void shouldSetUiEventAsFailedOnFail() throws Exception {
        // Arrange
        FailureResult r = new FailureResult();

        // Act
        controller.onServiceFail(r);
        
        // Assert
        verify(uiObservable).fail(r);
    }

}
