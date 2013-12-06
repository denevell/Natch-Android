package org.denevell.droidnatch.listthreads.views;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.listthreads.entities.ListThreadsResource;
import org.denevell.droidnatch.listthreads.entities.ThreadResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@RunWith(RobolectricTestRunner.class)
@SuppressWarnings("unchecked")
public class ListThreadsResultDisplayerTests  {
    
    private ListView list = mock(ListView.class);
    private ArrayAdapter<ThreadResource> adapter = mock(ArrayAdapter.class);
    private ListThreadsResultDisplayer displayer;
    private View loadingView = mock(View.class);
    private Context context = mock(Context.class);

    @Before
    public void setup() {
        displayer = new ListThreadsResultDisplayer(list, adapter, loadingView, context);
    }
    
    @Test
    public void shouldSetAdapterOnSuccess() {
        //Arrange
        ListThreadsResource success = new ListThreadsResource();
        
        // Act
        displayer.onSuccess(success);
        
        // Assert
        verify(list).setAdapter(adapter);
    }
    
    @Test
    public void shouldClearThenAddToAdapterOnSuccess() {
        //Arrange
        ListThreadsResource success = new ListThreadsResource();
        
        // Act
        displayer.onSuccess(success);
        
        // Assert
        verify(adapter).clear();
        verify(adapter).addAll(success.getThreads());
    }

    @Test
    public void onFinishedLoadingSetNullEmptyView() {
        //Arrange / Act
        displayer.stopLoading();
        
        // Assert
        verify(loadingView).setVisibility(View.GONE);
    }

    @Test
    public void onLoadingShowLoadingView() {
        //Arrange / Act
        displayer.startLoading();
        
        // Assert
        verify(loadingView).setVisibility(View.VISIBLE);
    }

    @Test
    public void onShowToastOnFail() {
        //Arrange
        FailureResult fail = new FailureResult();
        
        // Act
        displayer.onFail(fail);
    }

    @Test
    public void onShowToastErrorMessageIfAvailable() {
        //Arrange
        FailureResult fail = spy(new FailureResult());
        fail.setErrorMessage("hiya");
        
        // Act
        displayer.onFail(fail);
        
        // Assert
        verify(fail, times(2)).getErrorMessage();
    }


}