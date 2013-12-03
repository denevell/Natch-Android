package org.denevell.droidnatch.baseclasses;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;

@RunWith(RobolectricTestRunner.class)
public class ProgressBarIndicatorTests {
    
    private ProgressBarIndicator indicator;
    private Activity activity = mock(Activity.class);

    @Before
    public void setup() {
        indicator = new ProgressBarIndicator(activity);
    }
    
    @Test
    public void start() {
        // Act
        indicator.start();
        
        // Assert
        verify(activity).setProgressBarIndeterminateVisibility(true);
    }

    @Test
    public void stop() {
        // Act
        indicator.stop();
        
        // Assert
        verify(activity).setProgressBarIndeterminateVisibility(false);
    }

}
