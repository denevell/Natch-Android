/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.denevell.droidnatch.app.baseclasses.networking;

import java.io.UnsupportedEncodingException;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

public class StringWithJsonBodyRequest extends StringRequest {

	private String mRequestBody;
    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE =
        String.format("application/json; charset=%s", PROTOCOL_CHARSET);

	public StringWithJsonBodyRequest(int method, String url, Listener<String> listener, ErrorListener errorListener) {
		super(method, url, listener, errorListener);
	}

    public StringWithJsonBodyRequest(String url, Listener<String> listener, ErrorListener errorListener) {
        super(Method.GET, url, listener, errorListener);
    }
    
    public void setBody(String body) {
    	mRequestBody = body;
	}
    
    @Override
    public byte[] getBody() throws AuthFailureError {
    	try {
			return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }
	
}
