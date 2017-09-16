package com.bookStore.Calculation;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bookStore.R;
import com.development.NumberKeyboard;


public class CalcInputAct extends ListActivity {

	private BookItemsAdapter bookItemsAdapter = BookItemsAdapter.getInstance();
	private BookItemProgress bookItemProgress;
	private EditText inputCount;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calc_input);

		inputCount = (EditText) findViewById(R.id.calcInputEditText);
		((NumberKeyboard) findViewById(R.id.calcInputKeyboard)).setEditText(inputCount);
		bookItemProgress = BookItemProgress.initialise(this, bookItemsAdapter.getProgress());
		setListAdapter(bookItemProgress);
		getListView().setItemChecked(bookItemProgress.getCount() - 1, true);

		((TextView) findViewById(R.id.calcInputProgress))
				.setText(bookItemsAdapter.getProgress());

		findViewById(R.id.calcInputBthOk).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (inputCount.length() != 0) {
					bookItemProgress.setProgressEntry(
							getListView().getCheckedItemPosition(), inputCount.getText().toString());
					bookItemProgress.saveData();
					finish();
				}
			}
		});
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (position == bookItemProgress.getCount() - 1)
			inputCount.setText("");
		else
			inputCount.setText(bookItemProgress.getItem(position));
		l.invalidateViews();
	}
}