package org.denevell.droidnatch.threads.list;

import static org.mockito.Mockito.mock;

import org.denevell.droidnatch.app.baseclasses.ServiceDisplayResultsController;
import org.denevell.droidnatch.app.interfaces.OnPressObserver;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ListThreadsControllerTests {

    private ServiceDisplayResultsController controller;
    private ServiceFetcher service = mock(ServiceFetcher.class);
    private ResultsDisplayer displayable = mock(ResultsDisplayer.class);
    OnPressObserver<ThreadResource> onPressObserver = mock(OnPressObserver.class);
    ScreenOpener screenCreator = mock(ScreenOpener.class);

    @Before
    public void setUp() throws Exception {
        //controller = new ListThreadsController(
         //       service, 
          //      displayable,
           //     onPressObserver,
            //    screenCreator);
    }

    @Test
    public void shouldListenForThreadResourceClicks() throws Exception {
        // Arrange
        // Act
        //controller.go();
        
        // Assert
        //verify(onPressObserver).addOnPressListener(controller);
    }
    
    @Test
    public void shouldCallNewFragmentOnThreadClick() throws Exception {
        // Arrange
        ThreadResource obj = new ThreadResource();
        
        // Act
        //controller.onPress(obj);
        
        // Assert
        //verify(screenCreator).openScreen(ListPostsFragment.class, null);
    }    

}
