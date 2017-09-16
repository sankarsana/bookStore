package com.bookStore.Preference;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bookStore.R;

public abstract class ActionBarPreferenceActivity extends PreferenceActivity {

	private TextView actionBarTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.preference_action_bar);

		actionBarTitle = (TextView) findViewById(R.id.action_bar_view_text);
		ImageButton btnBack;
		btnBack = (ImageButton) findViewById(R.id.action_bar_view_btn);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}

	protected String getActionBarTitle() {
		return actionBarTitle.getText().toString();
	}

	protected void setActionBarTitle(String actionBarText) {
		this.actionBarTitle.setText(actionBarText);
	}
}
