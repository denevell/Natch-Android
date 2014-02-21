package org.denevell.droidnatch.app.baseclasses;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.denevell.droidnatch.app.interfaces.TypeAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@RunWith(RobolectricTestRunner.class)
@SuppressWarnings({"unchecked"})
public class ListViewResultDisplayerTests  {
    
    private ListView list = mock(ListView.class);
    private ArrayAdapter<Object> adapter = mock(ArrayAdapter.class);
    private ListViewUiEvent<Object, List<Object>, Object> displayer;
    private View loadingView = mock(View.class);
    private Context context = mock(Context.class);
	private TypeAdapter<Object, List<Object>> typeAdapter = mock(TypeAdapter.class);

    @Before
    public void setup() {
        displayer = new ListViewUiEvent<Object, List<Object>, Object>(
        		list, 
        		adapter, 
        		loadingView, 
        		context, 
        		typeAdapter,
        		null,
        		null);
    }
    
    @Test
    public void shouldSetAdapterOnSuccess() {
        //Arrange
        List<Object> success = new ArrayList<Object>();
        
        // Act
        displayer.success(success);
        
        // Assert
        verify(list).setAdapter(adapter);
    }
    
    @Test
    public void shouldClearThenAddToAdapterOnSuccess() {
        //Arrange
        List<Object> success = new ArrayList<Object>();
        List<Object> newObject = new ArrayList<Object>();
        Mockito.when(typeAdapter.convert(success)).thenReturn(newObject);
        
        // Act
        displayer.success(success);
        
        // Assert
        verify(adapter).clear();
        verify(adapter).addAll(success);
    }

    @Test
    public void onFinishedLoadingSetNullEmptyView() {
        //Arrange / Act
        displayer.stop();
        
        // Assert
        verify(loadingView).setVisibility(View.GONE);
    }

    @Test
    public void onLoadingShowLoadingView() {
        //Arrange / Act
        displayer.start();
        
        // Assert
        verify(loadingView).setVisibility(View.VISIBLE);
    }

    @Test
    public void onShowToastOnFail() {
        //Arrange
        FailureResult fail = new FailureResult();
        
        // Act
        displayer.fail(fail);
    }

    @Test
    public void onShowToastErrorMessageIfAvailable() {
        //Arrange
        FailureResult fail = spy(new FailureResult());
        fail.setErrorMessage("hiya");
        
        // Act
        displayer.fail(fail);
        
        // Assert
        verify(fail, times(2)).getErrorMessage();
    }


}