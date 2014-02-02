package org.denevell.droidnatch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Created by user on 26/01/14.
 */
public class UrlsTests {

    @Test
    public void setUrl() {
    	Urls.setBasePath("basepath1");
    	
    	assertEquals("basepath1", Urls.getBasePath());
    }

    @Test
    public void setAuthKey() {
        Urls.setAuthKey("authkey1");

        assertEquals("authkey1", Urls.getAuthKey());
    }
}
