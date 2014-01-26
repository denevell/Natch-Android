package org.denevell.droidnatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest="../Natch-Android/AndroidManifest.xml")
public class ApplicationTests {

	@Test
	public void baseUrlNotFromStringsXml() throws Exception {
		// Arrange
		Urls.setBasePath("something");
	    Robolectric.buildActivity(MainPageActivity.class).create().get();		
				
		// Assert
		assertEquals("something", Urls.getBasePath());
	}
	
	@Test
	public void baseUrlFromStringsXmlIfNothingSet() throws Exception {
		// Arrange
	    Robolectric.buildActivity(MainPageActivity.class).create().get();		
				
		// Assert
		assertTrue(Urls.getBasePath().length()>0);
	}
}
