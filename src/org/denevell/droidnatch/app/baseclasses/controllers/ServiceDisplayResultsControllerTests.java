package org.denevell.droidnatch.app.baseclasses.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.TypeAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
@SuppressWarnings({"rawtypes", "unchecked"})
public class ServiceDisplayResultsControllerTests {

    private ServiceCallThenDisplayController controller;
    private ServiceFetcher service = mock(ServiceFetcher.class);
    private ResultsDisplayer displayable = mock(ResultsDisplayer.class);
    private TypeAdapter typeConverter = mock(TypeAdapter.class);

    @Before
    public void setUp() throws Exception {
        controller = new ServiceCallThenDisplayController(
               service, 
               displayable,
               typeConverter);
    }

    @Test
    public void shouldSetServiceCallbacksOnSetup() {
        // Act
        controller.setup();
        
        // Assert
        verify(service).setServiceCallbacks(controller);
    }

    @Test
    public void shouldStartServiceOnRun() {
        // Act
        controller.go();
        
        // Assert
        verify(service).go();
    }

    @Test
    public void shouldStartDisplayableLoading() {
        // Act
        controller.go();
        
        // Assert
        verify(displayable).startLoading();
    }
    
    @Test
    public void shouldCallDisplaybleOnSuccess() throws Exception {
        // Arrange
        Object r = new Object();
        Object value = new Object();
        when(typeConverter.convert(r)).thenReturn(value);

        // Act
        controller.onServiceSuccess(r);
        
        // Assert
        verify(displayable).onSuccess(value);
        verify(displayable).stopLoading();
    }

    @Test
    public void shouldCallDisplaybleOnFail() throws Exception {
        // Arrange
        FailureResult r = new FailureResult();

        // Act
        controller.onServiceFail(r);
        
        // Assert
        verify(displayable).stopLoading();
        verify(displayable).onFail(r);
    }

}
