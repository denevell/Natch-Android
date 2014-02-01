package org.denevell.droidnatch.threads.list.uievents;

import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.baseclasses.GenericUiObject;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceInput;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;

public class AddThreadTextEditUiEvent extends GenericUiObject implements OnEditorActionListener {
    
    private EditText mEditText;
    private AddPostResourceInput mResourceInput;

    public AddThreadTextEditUiEvent(final EditText editText,
            AddPostResourceInput addPostResourceInput) {
        mEditText = editText;
        mResourceInput = addPostResourceInput;
        editText.setOnEditorActionListener(this);
        setOnSuccess(new GenericUiSuccess<AddPostResourceReturnData>() {
            @Override
            public void onGenericUiSuccess(AddPostResourceReturnData tr) {
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
        submit(null);
        return true;
    }

}
