package org.denevell.droidnatch.settings;

import org.denevell.natch.android.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity{
    
    @SuppressWarnings("unused")
	private static final String TAG = SettingsActivity.class.getSimpleName();

    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	addPreferencesFromResource(R.xml.preferences);
    }
}
