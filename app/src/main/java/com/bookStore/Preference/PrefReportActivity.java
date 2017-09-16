package com.bookStore.Preference;

import android.os.Bundle;

import com.bookStore.R;

public class PrefReportActivity extends ActionBarPreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference_report);
	}
}
