package org.denevell.droidnatch.posts.list.uievents;

import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.GenericUiObject;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceInput;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;

public class AddPostTextEditGenericUiEvent extends GenericUiObject<PostResource> implements OnEditorActionListener {
    
    private EditText mEditText;
    private AddPostResourceInput mResourceInput;

    public AddPostTextEditGenericUiEvent(final EditText editText,
            AddPostResourceInput addPostResourceInput) {
        mEditText = editText;
        mResourceInput = addPostResourceInput;
        editText.setOnEditorActionListener(this);
        setOnSuccess(new GenericUiSuccess<AddPostResourceReturnData>() {
            @Override
            public void onGenericUiSuccess(AddPostResourceReturnData object) {
                mEditText.setText("");
            }
        });
        setOnFail(new GenericUiFailure() {
            @Override
            public void onGenericUiFailure(FailureResult f) {
                if(f!=null && f.getErrorMessage()!=null) {
                    mEditText.setError(f.getErrorMessage());
                }
            }
        });        
    }
    
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(event!=null && event.getAction()==KeyEvent.ACTION_DOWN) {
            return true; // Natsty hack to ui automator doesn't call this twice
        }
        mResourceInput.setSubject("-");
        mResourceInput.setContent(v.getText().toString());                  
        submit(null);
        return true;
    }

}
