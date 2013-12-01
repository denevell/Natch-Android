package org.denevell.droidnatch.listthreads.views;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.denevell.droidnatch.baseclasses.FailureResult;
import org.denevell.droidnatch.interfaces.PopupDisplayer;
import org.denevell.droidnatch.listthreads.entities.ListThreadsResource;
import org.denevell.droidnatch.listthreads.entities.ThreadResource;
import org.junit.Before;
import org.junit.Test;

import android.widget.ArrayAdapter;
import android.widget.ListView;

@SuppressWarnings("unchecked")
public class ListThreadsResultDisplayerTests  {
    
    private ListView list = mock(ListView.class);
    private ArrayAdapter<ThreadResource> adapter = mock(ArrayAdapter.class);
    private ListThreadsResultDisplayer displayer;
    private PopupDisplayer popupError = mock(PopupDisplayer.class);

    @Before
    public void setup() {
        displayer = new ListThreadsResultDisplayer(list, adapter, popupError);
    }
    
    @Test
    public void onSuccess() {
        //Arrange
        ListThreadsResource success = new ListThreadsResource();
        
        // Act
        displayer.onSuccess(success);
        
        // Assert
        verify(list).setAdapter(adapter);
    }

    @Test
    public void onFail() {
        //Arrange
        FailureResult fail = new FailureResult();
        
        // Act
        displayer.onFail(fail);
    }


}