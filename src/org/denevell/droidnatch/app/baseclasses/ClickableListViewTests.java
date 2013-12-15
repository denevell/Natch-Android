package org.denevell.droidnatch.app.baseclasses;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.denevell.droidnatch.app.interfaces.ContextItemSelectedObserver;
import org.denevell.droidnatch.app.interfaces.OnLongPressObserver.OnLongPress;
import org.denevell.droidnatch.app.interfaces.OnPressObserver.OnPress;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.view.MenuItem;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListAdapter;
import android.widget.ListView;

@RunWith(RobolectricTestRunner.class)
@SuppressWarnings("unchecked")
public class ClickableListViewTests {

    private ListView listView = mock(ListView.class);
    private ContextItemSelectedObserver contextSelectedObservable = mock(ContextItemSelectedObserver.class);
    private OnCreateContextMenuListener onCreateContextMenu = mock(OnCreateContextMenuListener.class);
    private ClickableListView<Object> clickableListView;
    private ListAdapter adapter;

    @Before
    public void setUp() throws Exception {
        adapter = mock(ListAdapter.class);
        when(listView.getAdapter()).thenReturn(adapter);
        clickableListView = new ClickableListView<Object>(
                listView, 
                contextSelectedObservable, 
                onCreateContextMenu);
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
        MenuItem item = mock(MenuItem.class);
        when(item.getItemId()).thenReturn(1);
        when(item.getTitle()).thenReturn("a");
        when(item.getMenuInfo()).thenReturn(menuInfo);
        Object adapterValue = new Object();
        when(adapter.getItem(0)).thenReturn(adapterValue);
        OnLongPress<Object> callback = mock(OnLongPress.class);
        OnLongPress<Object> callback1 = mock(OnLongPress.class);

        // Act 
        clickableListView.addOnLongClickListener(callback);
        clickableListView.addOnLongClickListener(callback1);
        clickableListView.onContextItemSelected(item);

        // Assert
        verify(callback).onLongPress(adapterValue, 1, "a", 0);
        verify(callback1).onLongPress(adapterValue, 1, "a", 0);
    }


}
