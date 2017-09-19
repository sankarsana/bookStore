package com.bookStore.store;

import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.bookStore.App.MyActionBarActivity;
import com.bookStore.App.MyDialogFragment;
import com.bookStore.R;

import java.util.HashMap;

@SuppressWarnings("ConstantConditions")
public class UpdateBookAct extends MyActionBarActivity implements OnClickListener,
		MyDialogFragment.IDialogItemClick, MyDialogFragment.IMyDialogFragment {

	public static boolean isChanged = false;
	private EditText bookName;
	private EditText shortName;
	private Button btnWriter;
	private Button btnCategory;
	private PopupMenu categoryMenu;
	private EditText cost;
	private Book book;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookupdate);

		bookName = (EditText) findViewById(R.id.bookNewName);
		shortName = (EditText) findViewById(R.id.bookNewShortName);
		btnWriter = (Button) findViewById(R.id.bookNewWriter);
		btnCategory = (Button) findViewById(R.id.bookNewCategory);
		cost = (EditText) findViewById(R.id.bookNewCost);

		categoryMenu = new PopupMenu(this, btnCategory);
		for (Category category : Categories.getInstance()) {
			categoryMenu.getMenu().add(0, (int) category.getId(), 0, category.getName());
		}
		categoryMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				btnCategory.setText(menuItem.getTitle());
				book.setCategory(menuItem.getItemId());
				return true;
			}
		});

//		toolbar.setIcon(R.drawable.ic_action_edit);

		bookName.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				shortName.setText(Book.createShortName(bookName.getText().toString()));
			}
		});

		btnWriter.setOnClickListener(this);
		btnCategory.setOnClickListener(this);
		findViewById(R.id.bookUpdate_Ok).setOnClickListener(this);
		findViewById(R.id.bookUpdate_Cancel).setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();

		long bookId = getIntent().getLongExtra("bookId", -1);
		if (bookId != -1) {
			book = new Book(bookId);
			bookName.setText(book.name);
			shortName.setText(book.shortName);
			btnWriter.setText(book.writer.getName());
			btnCategory.setText(book.category.getName());
			cost.setText(book.cost == 0 ? "" : book.getCostSting());
			toolbar.setTitle(book.name);
		} else {
			book = new Book();
			toolbar.setTitle("Новая книга");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.bookUpdate_Ok:
				book.name = bookName.getText().toString();
				if (book.name.length() == 0) {
					bookName.setError("Введите название");
					return;
				}
				book.shortName = shortName.getText().toString();
				book.cost = Integer.parseInt(cost.getText().toString());
				if (book.id == -1) {
					book.insertToBase();
					getIntent().putExtra("bookId", book.id);
					getIntent().putExtra("bookName", book.name);
				} else
					book.updateInBase();
				isChanged = true;
				setResult(RESULT_OK, getIntent());
				finish();
				break;

			case R.id.bookUpdate_Cancel:
				finish();
				break;

			case R.id.bookNewWriter:
				WriterChoiceFragment fr = new WriterChoiceFragment(this);
				fr.show(getSupportFragmentManager(), "joi");
				break;

			case R.id.bookNewCategory:
				categoryMenu.show();
				break;
		}
	}

	@Override
	public void onDialogItemClick(Writer writer) {
		btnWriter.setText(writer.getName());
		book.writer.setId(writer.getId());
	}

	/**
	 * * Add new Writer
	 */
	@Override
	public boolean executeOk(HashMap<String, String> data) {
		Writer newWriter = new Writer();
		newWriter.setName(data.get("name"));
		Writers.getInstance().insert(newWriter);
		btnWriter.setText(newWriter.getName());
		book.writer.setId(newWriter.getId());
		return true;
	}
}
