package org.denevell.droidnatch.settings;

import org.denevell.droidnatch.app.push.GcmServerRegister;
import org.denevell.droidnatch.app.utils.AndroidUtils;
import org.denevell.natch.android.R;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity{
    
    @SuppressWarnings("unused")
	private static final String TAG = SettingsActivity.class.getSimpleName();

    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	addPreferencesFromResource(R.xml.preferences);
    	// Google play status
    	boolean playServicesEnabled = AndroidUtils.checkPlayServices(this);
    	EditTextPreference editText = (EditTextPreference) findPreference(getString(R.string.settings_google_play_services_info));
    	if(!playServicesEnabled) {
    		editText.setSummary("Could not initialise Google Play Services");
    	} else {
    		editText.setSummary("Google Play Services found");
    	}
    	// Gcm status
    	EditTextPreference gcmStatus = (EditTextPreference) findPreference(getString(R.string.settings_gcm_registered));
    	gcmStatus.setSummary(GcmServerRegister.sRegSuccessful);
    }
}
