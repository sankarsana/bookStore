package com.bookStore.Import;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.bookStore.App.DataBase;
import com.bookStore.R;
import com.bookStore.ui.BaseListActivityOld;
import com.bookStore.ui.SelectBookActivity;
import com.development.NumberKeyboard;

public class ImportActivity extends BaseListActivityOld implements OnClickListener {

	private Button btnBook;
	private Button btnEnter;
	private EditText count;
	private ImportDetailAdapter importDetail;
	private long bookId = -1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.import_act);
		importDetail = new ImportDetailAdapter(this);
		setListAdapter(importDetail);
		registerForContextMenu(getListView());

		btnBook = (Button) findViewById(R.id.addBookBtnBook);
		btnEnter = (Button) findViewById(R.id.addBookBtnEnter);
		count = (EditText) findViewById(R.id.addBookCount);
		((NumberKeyboard) findViewById(R.id.addKeyboard)).setEditText(count);

		importDetail.setDate(getIntent().getLongExtra("dateId", -1));
		setToolbarTitle(importDetail.getDateStringExtend());

		btnBook.setOnClickListener(this);
		btnEnter.setOnClickListener(this);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.addbooks, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
			case R.id.addBooks_itemDelete:
				importDetail.delete_importDetail(info.position);
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.addBookBtnBook:
				Intent intent = new Intent(this, SelectBookActivity.class);
				intent.putExtra(SelectBookActivity.Companion.getNEW_BOOK(), true);
				startActivityForResult(intent, 1);
				break;

			case R.id.addBookBtnEnter:
				if (count.getText().length() == 0 || bookId == -1) {
					count.setError("");
					break;
				}
				int countOfBooks = Integer.parseInt(count.getText().toString());
				if (countOfBooks == 0) {
					count.setError("Введите количество!");
					break;
				}
				DataBase.log_d("bookId = " + bookId);
				importDetail.addNewEntryAndUpdateBooksCount(bookId, countOfBooks);
				btnBook.setText(R.string.addBookBtnBookText);
				bookId = -1;
				btnBook.setTextColor(Color.GRAY);
				count.setText(null);
				break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_CANCELED)
			return;
		Bundle extras = data.getExtras();
		bookId = extras.getLong("bookId");
		btnBook.setTextColor(getResources().getColor(R.color.textLight));
		btnBook.setText(extras.getString("bookName"));
		btnEnter.setClickable(true);
	}

	@Override
	public void finish() {
		importDetail.delete_CurrentDateIfItIsEmpty();
		super.finish();
	}
}
