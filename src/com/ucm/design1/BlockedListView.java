package com.ucm.design1;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BlockedListView extends Activity {

	ArrayAdapter<Contact> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blockedlist);
		ListView lv = (ListView) findViewById(R.id.blocklist);
		adapter = new ContactArrayAdapter(this, PhoneCallReceiver.Blocked);
		lv.setAdapter(adapter);
	}
	
	

}
