package org.denevell.droidnatch.post.delete;

import org.denevell.droidnatch.app.baseclasses.controllers.UiEventThenServiceCallController.NextControllerHaltable;

public class HaltOnDeleteThread implements NextControllerHaltable {

    private boolean shouldHalt;

    @Override
    public boolean shouldHalt() {
        return shouldHalt;
    }

    public void setShouldHalt(boolean shouldHalt) {
        this.shouldHalt = shouldHalt;
    }

}
