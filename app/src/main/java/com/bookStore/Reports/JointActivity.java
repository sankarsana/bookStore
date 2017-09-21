package com.bookStore.Reports;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.bookStore.App.BaseActivity;
import com.bookStore.App.DataBase;
import com.bookStore.App.Utilities;
import com.bookStore.Persons.Persons;
import com.bookStore.R;
import com.bookStore.store.Writers;

import java.util.Calendar;

public class JointActivity extends BaseActivity implements
		AdapterView.OnItemSelectedListener {

	private Spinner datesSpinner;
	private Button personView;
	private Button writerView;
	private TextView count;
	private TextView scores;
	private TextView mb;
	private TextView big;
	private TextView med;
	private TextView small;
	private TextView mag;
	private PopupMenu personMenu;
	private long personId = -1;
	private long writerId = -1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_joint);

//		toolbar.setDisplayHomeAsUpEnabled(true);

		datesSpinner = (Spinner) findViewById(R.id.report_period);
		personView = (Button) findViewById(R.id.report_person);
		writerView = (Button) findViewById(R.id.report_writer);
		count = (TextView) findViewById(R.id.report_countValue);
		scores = (TextView) findViewById(R.id.report_scoreValue);
		mb = (TextView) findViewById(R.id.report_mbValue);
		big = (TextView) findViewById(R.id.report_bigValue);
		med = (TextView) findViewById(R.id.report_medValue);
		small = (TextView) findViewById(R.id.report_smallValue);
		mag = (TextView) findViewById(R.id.report_magValue);

		Persons persons = new Persons();

		personView.setText("Все распространители");
		personMenu = new PopupMenu(this, personView);
		personMenu.getMenu().add(0, -1, 0, "Все распространители");
		persons.addPersonsInPopupMenu(personMenu.getMenu());
		personView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				personMenu.show();
			}
		});
		personMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				personView.setText(menuItem.getTitle());
				personId = menuItem.getItemId();
				refresh();
				return true;
			}
		});


		writerView.setText("Все авторы");
		final PopupMenu writerMenu = new PopupMenu(this, writerView);
		writerMenu.getMenu().add(0, -1, 0, "Все авторы");
		Writers.getInstance().addWritersInPopupMenu(writerMenu.getMenu());
		writerView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				writerMenu.show();
			}
		});
		writerMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				writerView.setText(menuItem.getTitle());
				writerId = menuItem.getItemId();
				refresh();
				return true;
			}
		});

		datesSpinner.setOnItemSelectedListener(this);
		refresh();
	}

	private void refresh() {
		String[] dates = new String[2];
		switch (datesSpinner.getSelectedItemPosition()) {
			case 0:
				dates[0] = Utilities.getCurrentDate_DB_string().substring(0, 6) + "01";
				dates[1] = Utilities.getCurrentDate_DB_string().substring(0, 6) + "31";
				query(dates);
				break;
			case 1:
				Calendar c = Calendar.getInstance();
				c.add(Calendar.MONTH, -1);
				dates[0] = Utilities.getDate_DB_string(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, 0);
				dates[1] = Utilities.getDate_DB_string(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, 31);
				query(dates);
				break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.joint_act, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.joint_action_send:
				startActivity(Intent.createChooser(getSendIntent(), "Отправка письма..."));
				break;

			case R.id.joint_action_settings:
				startActivity(new Intent("com.bookStore.preferences.ACTION_PREFERENCE_REPORT"));
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private Intent getSendIntent() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(JointActivity.this);
		String subject = "\t" + sp.getString("subjectStart", "") + "\n" +
				"\n Всего " + count.getText().toString() +
				"\n Очки \t" + scores.getText().toString() +
				"\n MB \t " + mb.getText().toString() +
				"\n Big \t " + big.getText().toString() +
				"\n Med \t " + med.getText().toString() +
				"\n Small \t " + small.getText().toString() +
				"\n Mag \t " + mag.getText().toString() +
				"\n\n\t" + sp.getString("signature", "");
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{sp.getString("address", "")});
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, sp.getString("theme", ""));
		emailIntent.putExtra(Intent.EXTRA_TEXT, subject);
		return emailIntent;
	}

	private void query(String[] dates) {
		String query = "SELECT " +
				"SUM(CardEntries.distr) AS distr, " +
				"SUM(Categories.value * CardEntries.distr) AS scores, " +
				"SUM (CASE WHEN books.categoryId = 5 THEN CardEntries.distr END) AS mb, " +
				"SUM (CASE WHEN books.categoryId = 4 THEN CardEntries.distr END) AS big, " +
				"SUM (CASE WHEN books.categoryId = 3 THEN CardEntries.distr END) AS med, " +
				"SUM (CASE WHEN books.categoryId = 2 THEN CardEntries.distr END) AS small, " +
				"SUM (CASE WHEN books.categoryId = 1 THEN CardEntries.distr END) AS mag " +
				"FROM CardEntries " +
				"INNER JOIN books ON CardEntries.bookId = books._id " +
				"INNER JOIN Categories ON books.categoryId = Categories._id " +
				"INNER JOIN CardDate ON CardEntries.dateId = CardDate._id " +
				"WHERE CardDate.dateFld >= ? AND CardDate.dateFld <= ?";
		if (personId != -1)
			query = query.concat(" AND CardDate.personId = '" + personId + "'");
		if (writerId != -1)
			query = query.concat(" AND books.writerId = '" + writerId + "'");

		Cursor c = DataBase.get().rawQuery(query, dates);
		c.moveToFirst();
		count.setText(getTextWithoutZero(c.getString(0)));
		scores.setText(getTextWithoutZero(c.getString(1)));
		mb.setText(getTextWithoutZero(c.getString(2)));
		big.setText(getTextWithoutZero(c.getString(3)));
		med.setText(getTextWithoutZero(c.getString(4)));
		small.setText(getTextWithoutZero(c.getString(5)));
		mag.setText(getTextWithoutZero(c.getString(6)));
		c.close();
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
		refresh();
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {

	}

	private String getTextWithoutZero(String str) {
		if (str != null)
			return str.equals("0") ? "" : str;
		else
			return str;
	}
}