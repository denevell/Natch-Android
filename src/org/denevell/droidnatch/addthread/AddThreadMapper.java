package org.denevell.droidnatch.addthread;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.denevell.droidnatch.MainPageActivity;
import org.denevell.droidnatch.app.interfaces.Controller;
import org.denevell.droidnatch.app.interfaces.TextEditable;

import android.app.Activity;
import dagger.Module;
import dagger.Provides;

@Module(injects = {MainPageActivity.class}, 
        complete = false)
public class AddThreadMapper {
    
    @SuppressWarnings("unused")
    private Activity mActivity;

    public AddThreadMapper(Activity activity) {
        mActivity = activity;
    }
    
    // controllers
    
    @SuppressWarnings("serial")
    @Provides @Named("addthread")
    public List<Controller> provideControllers() {
        return new ArrayList<Controller>() {{
            add(providesLoginController());
        }};
    }

    public Controller providesLoginController() {
        AddThreadController controller = 
                new AddThreadController(providesTextInput(), 
                        null, 
                        null,
                        null);
        return controller;
    }

    private TextEditable providesTextInput() {
        return null;
    }

}
