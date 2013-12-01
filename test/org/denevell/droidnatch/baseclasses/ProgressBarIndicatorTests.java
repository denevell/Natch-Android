package org.denevell.droidnatch.baseclasses;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import android.app.Activity;

public class ProgressBarIndicatorTests {
    
    private ProgressBarIndicator indicator;
    private Activity activity = mock(Activity.class);

    @Before
    public void setup() {
        indicator = new ProgressBarIndicator(activity);
    }
    
    @Test
    public void start() {
        indicator.start();
    }

}
