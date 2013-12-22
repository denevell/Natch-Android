package org.denevell.droidnatch.post.delete_thread;

import org.denevell.droidnatch.app.interfaces.ContextItemSelectedObserver;

import dagger.Module;

@Module(complete = false, library = true)
public class DeleteThreadFromPostMapper {
    
    private ContextItemSelectedObserver mActivity;

    public DeleteThreadFromPostMapper(ContextItemSelectedObserver activity) {
        mActivity = activity;
    }

}
