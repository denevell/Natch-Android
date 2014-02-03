package org.denevell.droidnatch.threads.list.uievents;

import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import org.denevell.droidnatch.app.baseclasses.FailureResult;
import org.denevell.droidnatch.app.interfaces.ActivatingUiObject;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceInput;
import org.denevell.droidnatch.threads.list.entities.AddPostResourceReturnData;

public class AddThreadTextEditUiEvent implements ActivatingUiObject<AddPostResourceReturnData>, OnEditorActionListener {
    
    private EditText mEditText;
    private AddPostResourceInput mResourceInput;
    private GenericUiObserver mCallback;

    public AddThreadTextEditUiEvent(final EditText editText,
            AddPostResourceInput addPostResourceInput) {
        mEditText = editText;
        mResourceInput = addPostResourceInput;
        editText.setOnEditorActionListener(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(event!=null && event.getAction()==KeyEvent.ACTION_DOWN) {
            return true; // Natsty hack to ui automator doesn't call this twice
        }
        mResourceInput.setContent("-");
        mResourceInput.setSubject(v.getText().toString());
        mCallback.onUiEventActivated();
        return true;
    }

    @Override
    public void setOnSubmitObserver(GenericUiObserver observer) {
        mCallback = observer;
    }

    @Override
    public void success(AddPostResourceReturnData result) {
        mEditText.setText("");
    }

    @Override
    public void fail(FailureResult f) {
        if(f!=null && f.getErrorMessage()!=null) {
            mEditText.setError(f.getErrorMessage());
        }
    }

}
