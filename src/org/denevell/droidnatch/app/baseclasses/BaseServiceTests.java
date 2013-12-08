package org.denevell.droidnatch.app.baseclasses;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.junit.Before;
import org.junit.Test;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

@SuppressWarnings({"unchecked"})
public class BaseServiceTests {
    
    private ServiceCallbacks<Object> callbacks = mock(ServiceCallbacks.class);
    //private ResponseConverter responseConverter = mock(ResponseConverter.class);
    private FailureResultFactory failureResultFactory = mock(FailureResultFactory.class);
    private ProgressIndicator progress = mock(ProgressIndicator.class);
    private BaseService<Object> service = new BaseService<Object>(null, null, progress, failureResultFactory, null) {
    };

    @Before
    public void setup() {
    }

    @Test
    public void onFail() throws Exception {
        // Arrange
        service.setServiceCallbacks(callbacks);
        VolleyError error = new VolleyError();
        FailureResult fail = new FailureResult("", "", -1);
        when(failureResultFactory.newInstance(-1, error.toString(), "")).thenReturn(fail);
        
        // Act
        service.onErrorResponse(error);
        
        // Assert
        verify(callbacks).onServiceFail(fail);
    }

    @Test
    public void onFailWithStatusCode() throws Exception {
        // Arrange
        service.setServiceCallbacks(callbacks);
        NetworkResponse networkResponse = new NetworkResponse(400, null, null, true);
        VolleyError error = new VolleyError(networkResponse);
        FailureResult fail = new FailureResult("", "", -1);
        when(failureResultFactory.newInstance(400, error.toString(), "")).thenReturn(fail);
        
        // Act
        service.onErrorResponse(error);
        
        // Assert
        verify(callbacks).onServiceFail(fail);
    }

    @Test
    public void whenNullCallbackOnFail() throws Exception {
        // Arrange
        VolleyError error = new VolleyError();
        service.setServiceCallbacks(null);
        
        // Act
        service.onErrorResponse(error);
        
        // Assert
        verifyNoMoreInteractions(callbacks);
    }

}