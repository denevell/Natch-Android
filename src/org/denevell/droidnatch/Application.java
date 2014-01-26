package org.denevell.droidnatch;

import org.denevell.natch.android.R;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setBasePathIfEmpty();
    }

    /**
     * Set the base path of the services if they're not empty.
     * If they are NOT empty, it means some testing framework has set it
     * and so we should probably leave it alone.
     */
    private void setBasePathIfEmpty() {
        if(Urls.getBasePath()==null || Urls.getBasePath().isEmpty()) {
            Urls.setBasePath(getString(R.string.url_baseursl));
        }
    }
}
