package org.denevell.droidnatch.posts.list;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.denevell.droidnatch.app.interfaces.OnPressObserver;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.posts.list.views.ListPostsFragment;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class ListPostsControllerTests {

    OnPressObserver<ThreadResource> onPressObserver = mock(OnPressObserver.class);
    ScreenOpener screenCreator = mock(ScreenOpener.class);
    private ListPostsController controller;
    
    @Before
    public void setUp() throws Exception {
       controller = new ListPostsController(onPressObserver, screenCreator); 
    }
    
    @Test
    public void shouldListenForThreadResourceClicks() throws Exception {
        // Arrange
        // Act
        controller.go();
        
        // Assert
        verify(onPressObserver).addOnLongClickListener(controller);
    }
    
    @Test
    public void shouldCallNewFragmentOnThreadClick() throws Exception {
        // Arrange
        ThreadResource obj = new ThreadResource();
        
        // Act
        controller.onPress(obj);
        
        // Assert
        verify(screenCreator).openScreen(ListPostsFragment.class);
    }

}
