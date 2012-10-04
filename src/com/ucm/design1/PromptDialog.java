package com.ucm.design1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public abstract class PromptDialog extends AlertDialog implements OnClickListener {

	private final EditText number;
	private final EditText name;

	 public PromptDialog(Context context, String string, CharSequence message) {
	  super(context);
	  setTitle(string);
	  setMessage(message);

	  number = new EditText(context);
	  setView(number);
	  name = new EditText(context);
	  setView(name);
	  
	  setButton("Ok", this);
	  setButton2("Cancel", this);
	  
	 }

	 /**
	  * will be called when "cancel" pressed.
	  * closes the dialog.
	  * can be overridden.
	  * @param dialog
	  */
	 public void onCancelClicked(DialogInterface dialog) {
	  dialog.dismiss();
	 }

	 public void onClick(DialogInterface dialog, int which) {
	  if (which == DialogInterface.BUTTON_POSITIVE) {
	   if (onOkClicked(name.getText().toString())) {
		   Toast.makeText(getContext(), name.getText().toString(), Toast.LENGTH_LONG).show();
		   dialog.dismiss();
	   }
	  } else {
	   onCancelClicked(dialog);
	  }
	 }

	 /**
	  * called when "ok" pressed.
	  * @param input
	  * @return true, if the dialog should be closed. false, if not.
	  */
	 abstract public boolean onOkClicked(String input);


}
