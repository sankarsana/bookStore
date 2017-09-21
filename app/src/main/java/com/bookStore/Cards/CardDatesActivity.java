package com.bookStore.Cards;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bookStore.App.BaseActivity;
import com.bookStore.App.Utilities;
import com.bookStore.R;

import java.util.Calendar;

public class CardDatesActivity extends BaseActivity {

	public static final int DATE_DIALOG_ID = 100;
	public static final int MODE_GET = R.id.editorMode_get;
	public static final int MODE_GET_AND_DISTR = R.id.editorMode_getAndDistrib;
	public static final int MODE_DISTR = R.id.editorMode_distr;
	public static final int MODE_RET = R.id.editorMode_ret;
	public static final String CURRENT_DATE = "CurrentDate";

	private ViewPager viewPager;
	private CardDates cardDates;
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
					long newDate = Utilities.getDate_DB_long(i, i2 + 1, i3);
					if (newDate <= Utilities.getCurrentDate_DB_long()) {
						int pos = cardDates.insertNewDate(newDate);
						viewPager.setAdapter(cardDates);
						viewPager.setCurrentItem(pos);
					} else {
						Toast.makeText(CardDatesActivity.this,
								"Дата должна показывать только прошлое или настояще!", Toast.LENGTH_SHORT)
								.show();
					}
				}
			};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cards);
		setToolbarTitle(getIntent().getStringExtra("personName"));

		viewPager = (ViewPager) findViewById(R.id.card_viewPager);
		cardDates = new CardDates(getSupportFragmentManager());
		viewPager.setAdapter(cardDates);
		viewPager.setCurrentItem(cardDates.getCount() - 1);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Calendar c = Calendar.getInstance();
		return new DatePickerDialog(this, mDateSetListener,
				c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		String currentDate = getIntent().getStringExtra(CURRENT_DATE);
		((DatePickerDialog) dialog).updateDate(
				Utilities.getYear(currentDate),
				Utilities.getMonth(currentDate) - 1,
				Utilities.getDay(currentDate));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		viewPager.destroyDrawingCache();
		cardDates.deleteDateTodayIfItEmpty();
	}
}
