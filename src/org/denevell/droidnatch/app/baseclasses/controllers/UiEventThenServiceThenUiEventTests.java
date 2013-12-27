package org.denevell.droidnatch.app.baseclasses.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.GenericUiObservable;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
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
    private ResultsDisplayer displayable = mock(ResultsDisplayer.class);
    private GenericUiObservable uiObservable = mock(GenericUiObservable.class);
    private GenericUiObservable nextController = mock(GenericUiObservable.class);

    @Before
    public void setUp() throws Exception {
        controller = new UiEventThenServiceThenUiEvent(
                uiObservable,
                service,
                displayable, 
                nextController);
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
        verify(uiObservable).setObserver(controller);
    }

    @Test
    public void shouldStartDisplayableLoadingOnUiEvent() {
        // Act
        controller.onGenericUiEvent();
        
        // Assert
        verify(displayable).startLoading();
    }

    @Test
    public void shouldStartServiceOnNetworkStart() {
        // Act
        controller.onGenericUiEvent();
        
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
        verify(displayable).stopLoading();
    }

    @Test
    public void shouldSetUiEventAsSuccessOnSuccess() throws Exception {
        // Arrange
        Object o = new Object();

        // Act
        controller.onServiceSuccess(o);
        
        // Assert
        verify(uiObservable).success();
    }

    @Test
    public void shouldStartNextUiEventOnSuccess() throws Exception {
        // Arrange
        Object o = new Object();

        // Act
        controller.onServiceSuccess(o);
        
        // Assert
        verify(nextController).submit();
    }

    @Test
    public void shouldStopLoadingOnFail() throws Exception {
        // Arrange
        FailureResult r = new FailureResult();

        // Act
        controller.onServiceFail(r);
        
        // Assert
        verify(displayable).stopLoading();
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
