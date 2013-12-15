package org.denevell.droidnatch.post.add.uievents;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.GenericUiObject;
import org.denevell.droidnatch.app.interfaces.GenericUiObservable.GenericUiFailure;
import org.denevell.droidnatch.app.interfaces.GenericUiObservable.GenericUiSuccess;
import org.denevell.droidnatch.thread.add.entities.AddPostResourceInput;

import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class AddPostTextEditGenericUiEvent implements OnEditorActionListener {
    
    private EditText mEditText;
    private GenericUiObject mGenericUiEvent;
    private AddPostResourceInput mResourceInput;

    public AddPostTextEditGenericUiEvent(final EditText editText,
            AddPostResourceInput addPostResourceInput) {
        mEditText = editText;
        mResourceInput = addPostResourceInput;
        mGenericUiEvent = new GenericUiObject();
        editText.setOnEditorActionListener(this);
        mGenericUiEvent.setOnSuccess(new GenericUiSuccess() {
            @Override
            public void onGenericUiSuccess() {
                mEditText.setText("");
            }
        });
        mGenericUiEvent.setOnFail(new GenericUiFailure() {
            @Override
            public void onGenericUiFailure(FailureResult f) {
                if(f!=null && f.getErrorMessage()!=null) {
                    mEditText.setError(f.getErrorMessage());
                }
            }
        });        
    }
    
    public GenericUiObject getGenericUiEvent() {
        return mGenericUiEvent;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(event!=null && event.getAction()==KeyEvent.ACTION_DOWN) {
            return true; // Natsty hack to ui automator doesn't call this twice
        }
        mResourceInput.setSubject("-");
        mResourceInput.setContent(v.getText().toString());                  
        mGenericUiEvent.submit();
        return true;
    }

}
