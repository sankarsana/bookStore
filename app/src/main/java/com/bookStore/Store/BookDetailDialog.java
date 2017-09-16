package com.bookStore.Store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bookStore.App.MyDialog;
import com.bookStore.R;

public class BookDetailDialog extends MyDialog implements OnClickListener {
	private ImageButton btnOk;
	private ImageButton btnUpdate;
	private TextView bookName;
	private TextView shortName;
	private TextView writer;
	private TextView category;
	private TextView cost;
	private Book book;

	public BookDetailDialog(Context context) {
		super(context);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookdetail);

		btnOk = (ImageButton) findViewById(R.id.newBookBtnOk);
		btnOk.setOnClickListener(this);
		btnUpdate = (ImageButton) findViewById(R.id.newBookBtnUpdate);
		btnUpdate.setOnClickListener(this);

		bookName = (TextView) findViewById(R.id.bookDetailName);
		shortName = (TextView) findViewById(R.id.bookDetailShortName);
		writer = (TextView) findViewById(R.id.bookDetailWriter);
		category = (TextView) findViewById(R.id.bookDetailCategory);
		cost = (TextView) findViewById(R.id.bookDetailCost);
		setCanceledOnTouchOutside(true);
	}

	@Override
	public void onClick(View v) {
		this.hide();
		if (v.getId() == btnUpdate.getId()) {
			Intent intent = new Intent(getContext(), UpdateBookAct.class);
			intent.putExtra("bookId", book.id);
			getContext().startActivity(intent);
		}
	}

	public void show(long bookId) {
		super.show();
		book = new Book(bookId);
		bookName.setText(book.name);
		shortName.setText(book.shortName);
		writer.setText(book.writer.getName());
		category.setText(book.category.getName());
		cost.setText(book.getCostSting());
	}
}
