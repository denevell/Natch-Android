package org.denevell.droidnatch.posts.list;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.posts.entities.ListPostsResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@SuppressWarnings("unchecked")
@RunWith(RobolectricTestRunner.class)
public class ListPostsControllerTests {

    private ListPostsController controller;
    private ServiceFetcher<ListPostsResource> service = mock(ServiceFetcher.class);

    @Before
    public void setUp() throws Exception {
        controller = new ListPostsController(service);
    }

    @Test
    public void shouldSetServiceCallback() throws Exception {
        // Arrange
        // Act
        controller.go();
        
        // Assert
        verify(service).setServiceCallbacks(controller);
    }
    
    @Test
    public void shouldCallServiceOnGoMethod() throws Exception {
        // Arrange
        // Act
        controller.go();
        
        // Assert
        verify(service).go();
    }
        
}
