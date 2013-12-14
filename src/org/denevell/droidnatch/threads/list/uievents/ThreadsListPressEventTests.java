package org.denevell.droidnatch.threads.list.uievents;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.HashMap;

import org.denevell.droidnatch.app.interfaces.OnPressObserver;
import org.denevell.droidnatch.app.interfaces.ScreenOpener;
import org.denevell.droidnatch.posts.list.views.ListPostsFragment;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@SuppressWarnings("unchecked")
@RunWith(RobolectricTestRunner.class)
public class ThreadsListPressEventTests {
    private OnPressObserver<ThreadResource> onPressObserver = mock(OnPressObserver.class);
    private ScreenOpener screenOpener = mock(ScreenOpener.class);
    private HashMap<String, String> passedVars = spy(new HashMap<String, String>());
    private ThreadsListPressEvent threadsListPressEvent = 
            new ThreadsListPressEvent(screenOpener, onPressObserver, passedVars);

    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void shouldSetOnPressCallbacks() throws Exception {
        // Arrange
        ThreadResource obj = new ThreadResource();
        obj.setId("a");
        
        // Act
        threadsListPressEvent.onPress(obj);
        
        // Assert
        verify(passedVars).put(ListPostsFragment.BUNDLE_KEY_THREAD_ID, "a");
        verify(screenOpener).openScreen(ListPostsFragment.class, passedVars);
    }
    
    @Test
    public void shouldSetOnPressObserverOnRun() throws Exception {
        // Arrange
        // Act
        threadsListPressEvent.run();
        
        // Assert
        verify(onPressObserver).addOnPressListener(threadsListPressEvent);
    }

}
