package org.denevell.droidnatch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Created by user on 26/01/14.
 */
public class UrlsTests {

    @Test
    public void setUrl() {
    	ShamefulStatics.setBasePath("basepath1");
    	
    	assertEquals("basepath1", ShamefulStatics.getBasePath());
    }

    @Test
    public void setAuthKey() {
    	ShamefulStatics.setAuthKey("authkey1", null);

        assertEquals("authkey1", ShamefulStatics.getAuthKey(null));
    }
}
