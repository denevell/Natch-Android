package org.denevell.droidnatch.app.baseclasses;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.denevell.droidnatch.app.baseclasses.ObservableFragment.ContextMenuItemHolder;
import org.denevell.droidnatch.app.interfaces.OnPressObserver.OnPress;
import org.denevell.droidnatch.app.views.ReceivingClickingAutopaginatingListView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListAdapter;

@RunWith(RobolectricTestRunner.class)
@SuppressWarnings("unchecked")
public class ClickableListViewTests {

    private ReceivingClickingAutopaginatingListView<Object, Object, List<Object>> clickableListView;
    private ListAdapter adapter;
	private HideKeyboard hideKeyboard = mock(HideKeyboard.class);

    @SuppressWarnings("rawtypes")
	@Before
    public void setUp() throws Exception {
        AttributeSet attrs = mock(AttributeSet.class);
		clickableListView = Mockito.spy(new ReceivingClickingAutopaginatingListView(
                Robolectric.application, 
                attrs));
        adapter = mock(ListAdapter.class);
        when(clickableListView.getAdapter()).thenReturn(adapter);
    }

    @Test
    public void shouldCallListenersOnClick() {
        // Arrange
        OnPress<Object> callback = mock(OnPress.class);
        Object adapterValue = new Object();
        when(adapter.getItem(0)).thenReturn(adapterValue);

        // Act 
        OnPress<Object> callback1 = mock(OnPress.class);
        clickableListView.addOnPressListener(callback);
        clickableListView.addOnPressListener(callback1);
        clickableListView.onItemClick(null, null, 0, 0);

        // Assert
        verify(callback).onPress(adapterValue);
        verify(callback1).onPress(adapterValue);
    }

    @Test
    public void shouldCallListenersOnLongClick() {
        // Arrange
        AdapterContextMenuInfo menuInfo = mock(AdapterContextMenuInfo.class);
        menuInfo.position = 3;
        MenuItem item = mock(MenuItem.class);
        when(item.getItemId()).thenReturn(3);
        when(item.getTitle()).thenReturn("a");
        when(item.getMenuInfo()).thenReturn(menuInfo);
        Object adapterValue = new Object();
        when(adapter.getItem(3)).thenReturn(adapterValue);

        // Act 

        clickableListView.onContextItemSelected(new ContextMenuItemHolder(item));

        // Assert
        verify(adapter).getItem(3);
        // Should really check the event bus is being called...
    }
    
    @Test
    public void shouldHideKeyboardOnItemClick() {
		// Arrange
        Context context = mock(Context.class);
		AdapterView<?> av = mock(AdapterView.class);
		when(av.getContext()).thenReturn(context);
		clickableListView.setKeyboardHider(hideKeyboard);
		View v = mock(View.class);

    	// Act 
    	clickableListView.onItemClick(av, v, 0, 0);
    	
        // Assert
    	verify(hideKeyboard).hideKeyboard(context, v);
    }

    @Test
    public void shouldntHideKeyboardOnItemClick() {
		// Arrange
        Context context = mock(Context.class);
		AdapterView<?> av = mock(AdapterView.class);
		when(av.getContext()).thenReturn(context);
		clickableListView.setKeyboardHider(null);
		View v = mock(View.class);


    	// Act 
    	clickableListView.onItemClick(av, v, 0, 0);
    	
        // Assert
    	verify(hideKeyboard, Mockito.never()).hideKeyboard(context, v);
    }    
}
