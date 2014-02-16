package org.denevell.droidnatch.app.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class DialogueFragmentWithView extends DialogFragmentWithRotationFix {
	public View mView;

	public static DialogueFragmentWithView getInstance(View view) {
		DialogueFragmentWithView instance = new DialogueFragmentWithView();
		instance.mView = view;
		return instance;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setRetainInstance(true);
		if (getDialog() != null) {
			return getDialog();
		} else {
			return new AlertDialog.Builder(getActivity()).setView(mView)
					.create();
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		View view = mView;
		if (view != null && view.getParent() != null
				&& view.getParent() instanceof ViewGroup) {
			ViewParent parent = view.getParent();
			((ViewGroup) parent).removeView(view);
		}
	}

}
