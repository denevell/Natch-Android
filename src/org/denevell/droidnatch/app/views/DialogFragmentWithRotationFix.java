package org.denevell.droidnatch.app.views;

import android.support.v4.app.DialogFragment;

public class DialogFragmentWithRotationFix extends DialogFragment {

        @Override
        public void onDestroyView() {
            if (getDialog() != null && getRetainInstance())
                getDialog().setDismissMessage(null);
            super.onDestroyView();
        }

}
