package com.ucm.design1;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.android.internal.telephony.ITelephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class PhoneCallReceiver extends BroadcastReceiver {

	Context context = null;
	private static final String TAG = "Phonecall";
	private ITelephony telephonyService;
	public static List<Contact> Blocked=new ArrayList<Contact>();

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle b = intent.getExtras();
		String incommingCallNumber = b
				.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
		Log.v(TAG, "Receiving from " + incommingCallNumber);
		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		switch (PreferenceScreen.whomToBlock) {
		case R.id.radioBlockBlackList: {
			// block only black list

			if (checkToBlock(incommingCallNumber)) {
				try {
					Class c = Class.forName(telephony.getClass().getName());
					Method m = c.getDeclaredMethod("getITelephony");
					m.setAccessible(true);
					telephonyService = (ITelephony) m.invoke(telephony);
					telephonyService.silenceRinger();
					telephonyService.endCall();
					
					
				} catch (Exception e) {
					Toast.makeText(context, "Cannot block", Toast.LENGTH_LONG).show();
				}
			}

			break;

		}
		case R.id.radioBlockAll: {
			// block all
			try {
				Class c = Class.forName(telephony.getClass().getName());
				Method m = c.getDeclaredMethod("getITelephony");
				m.setAccessible(true);
				telephonyService = (ITelephony) m.invoke(telephony);
				telephonyService.silenceRinger();
				telephonyService.endCall();
				Blocked.add(new Contact("unknown",incommingCallNumber));
			} catch (Exception e) {
				Toast.makeText(context, "Cannot block", Toast.LENGTH_LONG).show();
			}
			break;
		}
		case R.id.RadioBlockNone: { // do nothing

		}
		}

	}

	private boolean checkToBlock(String incommingCallNumber) {
		for (int i = 0; i < BlackListView.contacts.size(); i++) {
			if (incommingCallNumber
					.contains(BlackListView.contacts.get(i).number))
			{
				Blocked.add(new Contact(BlackListView.contacts.get(i)));
				return true;
			}
		}
		return false;
	}

}
