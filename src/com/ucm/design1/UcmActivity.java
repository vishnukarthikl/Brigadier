package com.ucm.design1;

import java.security.Security;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class UcmActivity extends Activity implements OnClickListener,
		OnSharedPreferenceChangeListener {
	/** Called when the activity is first created. */
	Button buttonOk;
	ImageButton imgbuttonSync, imgbuttonBlackList, imgbuttonSettings;
	Button buttonBlockedCalls;
	private static BlackListDataSource datasource;
	public static boolean isAddsyncSuccess = true;
	public static boolean isFetchsyncSuccess = false;
	public static boolean isSynced = false;
	static Editable userName;
	Editable passWord;
	public static String url = null;
	// "http://10.0.2.2/";
	// "http://brigadier.co.in/";
	// "http://122.178.147.169/";
	// "http://vishnukarthik.byethost4.com/";
	// "http://ashokgowtham.byethost8.com/";
	String pHash;
	static MessageEncryptorDecryptor med;
	private static boolean isLogged = false;
	public static ArrayList<Contact> contacts = new ArrayList<Contact>();
	static String responseFromServer;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		settings.registerOnSharedPreferenceChangeListener(this);
		url = settings.getString("url", "brigadier.co.in");
		url = "http://" + url + "/";
		setContentView(R.layout.main);
		Security.addProvider(new BouncyCastleProvider());
		buttonOk = (Button) findViewById(R.id.button1);
		buttonOk.setOnClickListener(this);
		userName = ((EditText) findViewById(R.id.editText1)).getText();
		passWord = ((EditText) findViewById(R.id.editText2)).getText();
		datasource = new BlackListDataSource(this);
		datasource.open();
		datasource.flushTable("blacklist");
		PreferenceScreen.whomToBlock = R.id.radioBlockBlackList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.settingsmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.urlMenu:
			startActivity(new Intent(this, UrlPrefActivity.class));
			break;
		case R.id.disconnectMenu:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View which) {
		switch (which.getId()) {
		case R.id.button1: {
			try {
				JSONObject jsonRequest = new JSONObject();
				jsonRequest.put("UserName", userName);
				pHash = ModifiedHttpClient
						.calculateMD5Hash(passWord.toString());
				jsonRequest.put("PassWord", pHash);
				String request = jsonRequest.toString();

				String response = ModifiedHttpClient.sendRequestAndGetResponse(
						"json", request, url + "alogin.php");
				if (response.contains("java"))
					throw new Exception(response);
				if (response.equals("true")) { // login success
					Toast.makeText(this, "Login Success", Toast.LENGTH_LONG)
							.show();
					setContentView(R.layout.menu);
					imgbuttonSync = (ImageButton) findViewById(R.id.imageButton1);
					imgbuttonSync.setOnClickListener(this);
					imgbuttonBlackList = (ImageButton) findViewById(R.id.imageButton2);
					imgbuttonBlackList.setOnClickListener(this);
					imgbuttonSettings = (ImageButton) findViewById(R.id.imageButton3);
					imgbuttonSettings.setOnClickListener(this);
					buttonBlockedCalls = (Button) findViewById(R.id.buttonBlockedCalls);
					buttonBlockedCalls.setOnClickListener(this);
					isLogged = true;
					med = new MessageEncryptorDecryptor(pHash);
					sync(this);
					if (isFetchsyncSuccess)
						isSynced = true;
					else
						isSynced = false;
				} else {
					Toast.makeText(this, "Login Failure: " + response,
							Toast.LENGTH_LONG).show();
				}
				break;
			} catch (JSONException e) {

				e.printStackTrace();
			} catch (Exception e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

			}
		}
		case R.id.imageButton1: {
			sync(this);
			if (isAddsyncSuccess && isFetchsyncSuccess)
				isSynced = true;
			else
				isSynced = false;
			break;
		}
		case R.id.imageButton2: {
			Intent intent = new Intent(this, BlackListView.class);
			this.startActivity(intent);
			break;
		}
		case R.id.imageButton3: {
			Intent intent = new Intent(this, PreferenceScreen.class);
			this.startActivity(intent);

			break;
		}
		case R.id.buttonBlockedCalls: {
			Intent intent = new Intent(this, BlockedListView.class);
			this.startActivity(intent);
			break;
		}
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		datasource.flushTable("blacklist");
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isLogged) {
			setContentView(R.layout.menu);
			imgbuttonSync = (ImageButton) findViewById(R.id.imageButton1);
			imgbuttonSync.setOnClickListener(this);
			imgbuttonBlackList = (ImageButton) findViewById(R.id.imageButton2);
			imgbuttonBlackList.setOnClickListener(this);
			imgbuttonSettings = (ImageButton) findViewById(R.id.imageButton3);
			imgbuttonSettings.setOnClickListener(this);
			buttonBlockedCalls = (Button) findViewById(R.id.buttonBlockedCalls);
			buttonBlockedCalls.setOnClickListener(this);

		}

	}

	public static void sync(Context context) {

		if (!BlackListView.toDelete.isEmpty()) {
			String response;
			for (int i = 0; i < BlackListView.toDelete.size(); i++) {
				response = ModifiedHttpClient.sendRequestAndGetResponse(
						"delete", BlackListView.toDelete.get(i),
						UcmActivity.url + "adelete.php");
				if (response.contains("success")) {
					BlackListView.toDelete.remove(i);
					i--;
				} else {
					Toast.makeText(context, "Delete unsuccessful",
							Toast.LENGTH_SHORT).show();
				}

			}

		}
		JSONArray toSend = new JSONArray();
		JSONObject toAdd;
		JSONObject toSendEncrypted = null;
		int addCount = 0;
		for (int i = 0; i < BlackListView.contacts.size(); i++) {
			Contact temp = BlackListView.contacts.get(i);
			if (!temp.isSynced && temp.number != null) {
				// add to request
				toAdd = new JSONObject();
				try {
					toAdd.put("name", temp.name);
					toAdd.put("number", temp.number);
					toSend.put(addCount++, toAdd);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}
		String encryptedText = null;
		if (toSend.length() != 0) {
			try {
				toSendEncrypted = new JSONObject();
				toSendEncrypted.put("sender", userName.toString());

				encryptedText = new String(
						MessageEncryptorDecryptor.bytesToHex(med.encrypt(toSend
								.toString())));

				toSendEncrypted.put("crypt", encryptedText);
				encryptedText = toSendEncrypted.toString();
			} catch (Exception e) {

				e.printStackTrace();
			}
			responseFromServer = ModifiedHttpClient.sendRequestAndGetResponse(
					"toAdd", encryptedText, url + "aadd.php");
			if (responseFromServer.contains("success")) {
				isAddsyncSuccess = true;
				Toast.makeText(context, "Add success", Toast.LENGTH_SHORT)
						.show();
			} else {
				isAddsyncSuccess = false;
				Toast.makeText(context, "Add fail", Toast.LENGTH_SHORT).show();
			}
		}

		String decryptedText = null;
		responseFromServer = ModifiedHttpClient.sendRequestAndGetResponse(
				"user", userName.toString(), url + "afetchcontacts.php");

		if (responseFromServer != "") {
			datasource.flushTable("blacklist");

			try {
				decryptedText = new String(med.decrypt(responseFromServer));
				isFetchsyncSuccess = true;
			} catch (Exception e1) {

				Toast.makeText(context, "Problem With Decryption",
						Toast.LENGTH_SHORT).show();
				isFetchsyncSuccess = false;
				e1.printStackTrace();
			}

			String array;

			if (isFetchsyncSuccess) {
				Toast.makeText(context, "Decrypted String: " + decryptedText,
						Toast.LENGTH_LONG).show();
				contacts.clear();
				try {
					JSONArray jsonResponse = new JSONArray(decryptedText);
					for (int i = 0; i < jsonResponse.length(); i++) {
						array = jsonResponse.getString(i);
						JSONArray jsonarray = new JSONArray(array);
						contacts.add(new Contact());
						for (int j = 0; j < jsonarray.length(); j++) {
							contacts.get(i).putValue(j, jsonarray.getString(j));

						}

						contacts.get(i).isSynced = true;

						datasource.createBlackContact(contacts.get(i));
					}
				} catch (JSONException e) {
					Toast.makeText(context, "Error parsing data",
							Toast.LENGTH_LONG).show();
				}

			} else
				Toast.makeText(context, "Error fetching data",
						Toast.LENGTH_LONG).show();

		} else {
			Toast.makeText(context, "Data not received", Toast.LENGTH_LONG)
					.show();
			isFetchsyncSuccess = false;
		}

	}

	public void onSharedPreferenceChanged(SharedPreferences settings, String key) {
		url = settings.getString(key, "brigadier.co.in");
		url = "http://" + url + "/";
	}
}