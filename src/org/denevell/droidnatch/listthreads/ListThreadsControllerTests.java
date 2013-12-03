package org.denevell.droidnatch.listthreads;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.denevell.droidnatch.baseclasses.FailureResult;
import org.denevell.droidnatch.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.interfaces.ServiceFetcher;
import org.denevell.droidnatch.listthreads.entities.ListThreadsResource;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ListThreadsControllerTests {

    private ListThreadsController controller;
    private ServiceFetcher service = mock(ServiceFetcher.class);
    private ResultsDisplayer displayable = mock(ResultsDisplayer.class);

    @Before
    public void setUp() throws Exception {
        controller = new ListThreadsController(
                service, 
                displayable);
    }

    @Test
    public void shouldStartServiceOnRun() {
        // Act
        controller.go();
        
        // Assert
        verify(service).setServiceCallbacks(controller);
        verify(service).go();
    }
    
    @Test
    public void shouldCallDisplaybleOnSuccess() throws Exception {
        // Arrange
        ListThreadsResource r = new ListThreadsResource();

        // Act
        controller.success(r);
        
        // Assert
        verify(displayable).onSuccess(r);
    }

    @Test
    public void shouldCallDisplaybleOnFail() throws Exception {
        // Arrange
        FailureResult r = new FailureResult();

        // Act
        controller.fail(r);
        
        // Assert
        verify(displayable).onFail(r);
    }
    

}
