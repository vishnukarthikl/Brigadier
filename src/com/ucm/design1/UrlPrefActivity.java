package com.ucm.design1;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class UrlPrefActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.urlmenu);
	}
	
}
