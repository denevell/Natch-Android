package org.denevell.droidnatch.thread.add.uievents;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.GenericUiObject;
import org.denevell.droidnatch.thread.add.entities.AddPostResourceInput;

import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class AddThreadTextEditUiEvent extends GenericUiObject implements OnEditorActionListener {
    
    private EditText mEditText;
    private AddPostResourceInput mResourceInput;

    public AddThreadTextEditUiEvent(final EditText editText,
            AddPostResourceInput addPostResourceInput) {
        mEditText = editText;
        mResourceInput = addPostResourceInput;
        editText.setOnEditorActionListener(this);
        setOnSuccess(new GenericUiSuccess() {
            @Override
            public void onGenericUiSuccess() {
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
        mResourceInput.setContent("-");
        mResourceInput.setSubject(v.getText().toString());                  
        submit();
        return true;
    }

}
