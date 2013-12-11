package org.denevell.droidnatch.thread.delete;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.ContextItemSelectedHolder;
import org.denevell.droidnatch.app.interfaces.ResultsDisplayer;
import org.denevell.droidnatch.app.interfaces.ServiceFetcher;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.thread.delete.entities.DeletePostResourceReturnData;
import org.denevell.droidnatch.threads.list.ListThreadsController;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.content.Context;
import android.view.MenuItem;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListAdapter;
import android.widget.ListView;

@SuppressWarnings({"rawtypes", "unchecked"})
@RunWith(RobolectricTestRunner.class)
public class DeleteThreadControllerTests {

    private DeleteThreadController controller;
    private ListThreadsController listThreadsController;
    private ServiceFetcher service = mock(ServiceFetcher.class);
    private ResultsDisplayer displayable = mock(ResultsDisplayer.class);
    private ContextItemSelectedHolder contextSelectedListener = mock(ContextItemSelectedHolder.class);
    private ListView listView = mock(ListView.class);

    @Before
    public void setUp() throws Exception {
        controller = spy(new DeleteThreadController(
                mock(Context.class), 
                mock(VolleyRequest.class),
                listView,
                service,
                listThreadsController,
                displayable,
                contextSelectedListener 
                ));
    }

    @Test
    public void shouldSetServiceCallbacksOnRun() {
        // Act
        controller.go();
        
        // Assert
        verify(service).setServiceCallbacks(controller);
    }

    @Test
    public void shouldStartDisplayableLoadingOnNetworkStart() {
        // Act
        controller.startNetworkCall();
        
        // Assert
        verify(displayable).startLoading();
    }

    @Test
    public void shouldStartServiceOnNetworkStart() {
        // Act
        controller.startNetworkCall();
        
        // Assert
        verify(service).go();
    }
    
    @Test
    public void shouldStopLoadingOnSuccess() throws Exception {
        // Arrange
        DeletePostResourceReturnData r = new DeletePostResourceReturnData();

        // Act
        controller.onServiceSuccess(r);
        
        // Assert
        verify(displayable).stopLoading();
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
    public void shouldStartNetworkCallOnContextItemSelected() throws Exception {
        // Arrange
        MenuItem menu = mock(MenuItem.class);
        ListAdapter adapter = mock(ListAdapter.class);
        ThreadResource threadResource = new ThreadResource();
        when(adapter.getItem(0)).thenReturn(threadResource);
        when(listView.getAdapter()).thenReturn(adapter);
        when(menu.getMenuInfo()).thenReturn(mock(AdapterContextMenuInfo.class));

        // Act
        controller.onContextItemSelected(menu);
        
        // Assert
        verify(controller).startNetworkCall();
    }

}
