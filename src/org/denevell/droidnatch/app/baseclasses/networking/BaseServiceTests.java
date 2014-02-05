package org.denevell.droidnatch.app.baseclasses.networking;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.FailureResultFactory;
import org.denevell.droidnatch.app.interfaces.ProgressIndicator;
import org.denevell.droidnatch.app.interfaces.ObjectToStringConverter;
import org.denevell.droidnatch.app.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.app.interfaces.VolleyRequest;
import org.denevell.droidnatch.threads.list.entities.ListThreadsResource;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

@SuppressWarnings({"unchecked"})
@RunWith(RobolectricTestRunner.class)
public class BaseServiceTests {
    
    private ServiceCallbacks<Object> callbacks = mock(ServiceCallbacks.class);
    private FailureResultFactory failureResultFactory = mock(FailureResultFactory.class);
    private ProgressIndicator progress = mock(ProgressIndicator.class);
    private ObjectToStringConverter responseConverter = mock(ObjectToStringConverter.class);
    private VolleyRequest<Object> volleyRequest = mock(VolleyRequest.class);
    private BaseService<Object> service = new BaseService<Object>(
            volleyRequest, 
            progress, 
            responseConverter, 
            failureResultFactory, 
            Object.class) {
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
    

    @Test
    public void onSuccess() throws Exception {
        // Arrange
        JSONObject json = new JSONObject();
        json.put("blar", "blar");
        service.setServiceCallbacks(callbacks);
        ListThreadsResource threads = mock(ListThreadsResource.class);
        when(responseConverter.convert(json.toString(), Object.class))
                .thenReturn(threads);
        
        // Act
        service.onResponse(json);
        
        // Assert
        verify(callbacks).onServiceSuccess(threads);
    }

    @Test
    public void whenNullCallbackOnSuccess() throws Exception {
        // Arrange
        JSONObject json = new JSONObject();
        service.setServiceCallbacks(null);
        
        // Act
        service.onResponse(json);
        
        // Assert
        verifyNoMoreInteractions(callbacks);
    }    

}