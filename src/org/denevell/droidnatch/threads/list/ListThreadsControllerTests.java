package org.denevell.droidnatch.threads.list;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.OnPressObserver;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.posts.list.views.ListPostsFragment;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ListThreadsControllerTests {

    private ListThreadsController controller;
    private ServiceFetcher service = mock(ServiceFetcher.class);
    private ResultsDisplayer displayable = mock(ResultsDisplayer.class);
    OnPressObserver<ThreadResource> onPressObserver = mock(OnPressObserver.class);
    ScreenOpener screenCreator = mock(ScreenOpener.class);

    @Before
    public void setUp() throws Exception {
        controller = new ListThreadsController(
                service, 
                displayable,
                onPressObserver,
                screenCreator);
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
    public void shouldStartDisplayableLoading() {
        // Act
        controller.go();
        
        // Assert
        verify(displayable).startLoading();
    }
    
    @Test
    public void shouldCallDisplaybleOnSuccess() throws Exception {
        // Arrange
        ListThreadsResource r = new ListThreadsResource();

        // Act
        controller.onServiceSuccess(r);
        
        // Assert
        verify(displayable).onSuccess(r);
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
    
    @Test
    public void shouldListenForThreadResourceClicks() throws Exception {
        // Arrange
        // Act
        controller.go();
        
        // Assert
        verify(onPressObserver).addOnPressListener(controller);
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
