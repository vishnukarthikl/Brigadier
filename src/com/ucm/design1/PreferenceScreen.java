package com.ucm.design1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class PreferenceScreen extends Activity {

	public static int whomToBlock;
	RadioButton rbBlack, rbAll, rbNone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		rbBlack = (RadioButton) findViewById(R.id.radioBlockBlackList);
		rbAll = (RadioButton) findViewById(R.id.radioBlockAll);
		rbNone = (RadioButton) findViewById(R.id.RadioBlockNone);
		rbBlack.setOnClickListener(myOptionOnClickListener);
		rbAll.setOnClickListener(myOptionOnClickListener);
		rbNone.setOnClickListener(myOptionOnClickListener);
		switch (whomToBlock) {
		case R.id.radioBlockBlackList:
			rbBlack.setChecked(true);
			break;
		case R.id.radioBlockAll:
			rbAll.setChecked(true);
			break;
		case R.id.RadioBlockNone:
			rbNone.setChecked(true);
			break;

		default:
			rbBlack.setChecked(true);
			whomToBlock = R.id.radioBlockBlackList;
			break;
		}

	}

	RadioButton.OnClickListener myOptionOnClickListener = new RadioButton.OnClickListener() {

		public void onClick(View v) {

			whomToBlock = v.getId();
		}

	};
}
