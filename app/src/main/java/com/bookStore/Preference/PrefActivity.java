package com.bookStore.Preference;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.util.TypedValue;
import android.widget.CheckBox;

import com.bookStore.App.DataBase;
import com.bookStore.R;

public class PrefActivity extends ActionBarPreferenceActivity {

	private final static int CLEAR_DIALOG = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);

		Preference clearPref = findPreference("clearDB");
		assert clearPref != null;
		clearPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				showDialog(CLEAR_DIALOG);
				return false;
			}
		});
	}

	protected Dialog onCreateDialog(int id) {
		final AlertDialog.Builder b = new AlertDialog.Builder(this);
		switch (id) {
			case CLEAR_DIALOG:
				final CheckBox checkBox = new CheckBox(this);
				checkBox.setText("Импортировать данные книг из csv файла.");
				TypedValue a = new TypedValue();
				getTheme().resolveAttribute(android.R.attr.textColor, a, true);
				checkBox.setTextColor(getResources().getColor(R.color.ret_color));
				checkBox.setChecked(true);

				b.setTitle("Стереть базу данных.")
						.setView(checkBox)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setMessage("\tДля импорта данных в корне sd карты должен быть файл " +
								" \"books.csv\",состоящий из пяти столбцов: \n" +
								"\n\t1 - название," +
								"\n\t2 - краткое название," +
								"\n\t3 - автор," +
								"\n\t4 - категория (очки)," +
								"\n\t5 - цена," +
								"\n\t6 - количество." +
								"\n\n\tВсе, кроме первого - не обязательны.")
						.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								DataBase.clearDataBase();
								new ImportBooksFromCSV(checkBox.isChecked());
								dialog.cancel();
								PrefActivity.this.finish();
							}
						})
						.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
				break;
		}
		return b.create();
	}
}
