package org.denevell.droidnatch.listthreads;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.denevell.droidnatch.baseclasses.FailureResult;
import org.denevell.droidnatch.interfaces.FailureResultFactory;
import org.denevell.droidnatch.interfaces.ProgressIndicator;
import org.denevell.droidnatch.interfaces.ResponseConverter;
import org.denevell.droidnatch.interfaces.ServiceCallbacks;
import org.denevell.droidnatch.listthreads.entities.ListThreadsResource;
import org.denevell.droidnatch.listthreads.service.ListThreadsService;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

@SuppressWarnings({"unchecked"})
public class ListThreadsServiceTest {
    
    private ListThreadsService service;
    private ServiceCallbacks<ListThreadsResource> callbacks = mock(ServiceCallbacks.class);
    private ResponseConverter responseConverter = mock(ResponseConverter.class);
    private ListThreadsResource threads = new ListThreadsResource();
    private FailureResultFactory failureResultFactory = mock(FailureResultFactory.class);

    @Before
    public void setup() {
        ProgressIndicator progress = mock(ProgressIndicator.class);
        service = new ListThreadsService(null, null, progress, responseConverter, 
                failureResultFactory);
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

    @Test
    public void onSuccess() throws Exception {
        // Arrange
        JSONObject json = new JSONObject();
        json.put("blar", "blar");
        service.setServiceCallbacks(callbacks);
        when(responseConverter.convert(json.toString(), ListThreadsResource.class))
                .thenReturn(threads);
        
        // Act
        service.onResponse(json);
        
        // Assert
        verify(callbacks).success(threads);
    }

    @Test
    public void onFail() throws Exception {
        // Arrange
        service.setServiceCallbacks(callbacks);
        VolleyError error = new VolleyError();
        FailureResult fail = new FailureResult("", "", -1);
        when(failureResultFactory.newInstance(-1, "", "")).thenReturn(fail);
        
        // Act
        service.onErrorResponse(error);
        
        // Assert
        verify(callbacks).fail(fail);
    }

    @Test
    public void onFailWithStatusCode() throws Exception {
        // Arrange
        service.setServiceCallbacks(callbacks);
        NetworkResponse networkResponse = new NetworkResponse(400, null, null, true);
        VolleyError error = new VolleyError(networkResponse);
        FailureResult fail = new FailureResult("", "", -1);
        when(failureResultFactory.newInstance(400, "", "")).thenReturn(fail);
        
        // Act
        service.onErrorResponse(error);
        
        // Assert
        verify(callbacks).fail(fail);
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
