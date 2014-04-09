package org.denevell.droidnatch.app.views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class DialogueFragmentWithView extends DialogFragmentWithRotationFix {
	
	public static interface InitialiseView {
		public void intialise(View v, DialogFragment df);
	}
	
	public int mLayout;
	public InitialiseView mInitView;

	public static DialogueFragmentWithView getInstance(int view, InitialiseView initView) {
		DialogueFragmentWithView instance = new DialogueFragmentWithView();
		instance.mLayout = view;
		instance.mInitView = initView;
		return instance;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setRetainInstance(true);
		Dialog onCreateDialog = super.onCreateDialog(savedInstanceState);
		onCreateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		return onCreateDialog;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(mLayout==0) {
			// If we've got a 0 zero, which we may if the app was destroyed (since onRetainInstance 
			// only saves on configuration changes), then destroy the dialogue.

			// The reason we're doing this is because we can't save the mInitView, or more 
			// preciously the Object therein because it's not a Parcelable.
			
			// The reason they are not Parcelable's is because they often come from MenuItems
			return new View(getActivity());
		}
		View view = LayoutInflater.from(getActivity()).inflate(mLayout, container, false);
		if(mInitView!=null) mInitView.intialise(view, this);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(mLayout==0) {
			// See above comment about a zero layout
			dismissAllowingStateLoss();
		}
	}
	
}
