package com.ucm.design1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class BlackListView extends Activity implements OnClickListener {

	final int PICK_CONTACT = 0;
	Button buttonAdd;
	Button buttonAddFromContacts;
	Button buttonDelete;
	public static List<Contact> contacts = new ArrayList<Contact>();
	ArrayAdapter<Contact> adapter;
	Editable toAddNumber = null;
	Editable toAddName = null;
	private BlackListDataSource datasource;
	static List<String> toDelete= new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blacklist);
		ListView lv = (ListView) findViewById(R.id.contactlist);
		if(UcmActivity.isSynced)
		{
			datasource = new BlackListDataSource(this);
			datasource.open();
			contacts = datasource.getAllBlackContacts();
			UcmActivity.isSynced=false;
			
		}
		adapter = new InteractiveContactArrayAdapter(this, contacts);
		lv.setAdapter(adapter);
		buttonAdd = (Button) findViewById(R.id.buttonAdd);
		buttonAdd.setOnClickListener(this);
		buttonDelete = (Button) findViewById(R.id.buttonDelete);
		buttonDelete.setOnClickListener(this);
		buttonAddFromContacts = (Button) findViewById(R.id.buttonAddFromContacts);
		buttonAddFromContacts.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode) {
		case PICK_CONTACT: {
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = intent.getData();
				Cursor cur = managedQuery(contactData, null, null, null, null);
				ContentResolver contect_resolver = getContentResolver();

				if (cur.moveToFirst()) {
					String id = cur
							.getString(cur
									.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
					String name = "";
					String number = "";

					Cursor phoneCur = contect_resolver.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);

					if (phoneCur.moveToFirst()) {
						name = phoneCur
								.getString(phoneCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
						number = phoneCur
								.getString(phoneCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					}

					Toast.makeText(this,
							"Name :" + name + "\nPhone No: " + number,
							Toast.LENGTH_LONG).show();
					Contact tempContact=new Contact();
					tempContact.putValue(1, name);
					tempContact.putValue(2, number);
					contacts.add(tempContact);
					adapter.notifyDataSetChanged();

				}
				contect_resolver = null;
				cur = null;
			}
			break;
		}

		default:
			break;
		}

	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.buttonAdd: {

			final AlertDialog alertDialog = new AlertDialog.Builder(this)
					.create();
			final View dialog_layout = getLayoutInflater().inflate(
					R.layout.dialog, null);

			alertDialog.setView(dialog_layout);
			alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					EditText textaddnumber = (EditText) dialog_layout
							.findViewById(R.id.textaddnumber);
					EditText textaddname = (EditText) dialog_layout
							.findViewById(R.id.textaddname);
					
					Contact tempContact=new Contact();
					tempContact.putValue(1, textaddname.getText().toString());
					tempContact.putValue(2, textaddnumber.getText().toString());
					contacts.add(tempContact);
					
					adapter.notifyDataSetChanged();
				}
			});
			alertDialog.show();
			break;
		}
		case R.id.buttonAddFromContacts: {
			Intent intent = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			// intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
			startActivityForResult(intent, PICK_CONTACT);
			break;
		}

		case R.id.buttonDelete: {
			for (int i = 0; i < contacts.size(); i++) {
				if (contacts.get(i).isSelected()) {
					if(contacts.get(i).isSynced==true)
					{
						toDelete.add(contacts.get(i).id);
						
					}
					contacts.remove(i);
					i--;
				}
			}
			adapter.notifyDataSetChanged();
			break;
		}

		default:
			break;
		}

	}
}
