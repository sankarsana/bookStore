package com.bookStore.Calculation;

import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.bookStore.App.ActionBarListActivity;
import com.bookStore.R;


public class CalcActivity extends ActionBarListActivity {

	private static final String SETTINGS = "settings";
	private static final String isDuring = "isDuring";
	private BookItemsAdapter bookItemsAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calc_act);

		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(R.drawable.ic_calculate);

		bookItemsAdapter = BookItemsAdapter.initialize(this);
		SharedPreferences sp = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
		if (!sp.getBoolean("hasVisited", false)) {
			SharedPreferences.Editor e = sp.edit();
			e.putBoolean(isDuring, true);
			e.putBoolean("hasVisited", true);
			e.commit();
		}

//        if (!sp.getBoolean(isDuring, false))
		bookItemsAdapter.clearData();

		setListAdapter(bookItemsAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.calc_act, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.calc_beginAgain:
				bookItemsAdapter.moveCurrentToPreviousAndClearCurrent();
				break;
			case R.id.calc_returnPrevious:
				bookItemsAdapter.movePreviousToCurrentAndClearPrevious();
				break;
			case R.id.calc_apply:
				new AlertDialog.Builder(this)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setMessage("Применить изменения?")
						.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								bookItemsAdapter.updateCountBooks();
								finish();
							}
						})
						.setNegativeButton(android.R.string.cancel, null)
						.show();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this, CalcInputAct.class);
		bookItemsAdapter.setCurrentPosition(position);
		startActivityForResult(intent, 0);
	}
}