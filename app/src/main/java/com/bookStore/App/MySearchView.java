package com.bookStore.App;

import android.content.Context;
import android.support.v7.view.CollapsibleActionView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bookStore.R;

public class MySearchView extends LinearLayout implements CollapsibleActionView {

	private ImageView mCloseButton;
	private ImageView mSearchHint;
	private EditText_immStateRemember queryEditText;
	private IDataOfSearch dataOfSearch;

	public MySearchView(Context context) {
		this(context, null);
	}

	public MySearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.my_search_view, this, true);

		queryEditText = (EditText_immStateRemember) findViewById(R.id.search_src_text);
		mCloseButton = (ImageView) findViewById(R.id.search_close_btn);
		mSearchHint = (ImageView) findViewById(R.id.search_hint);

		mCloseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				queryEditText.setText("");
			}
		});

		queryEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				updateView();
				if (dataOfSearch != null) {
					dataOfSearch.refresh(s.toString().toLowerCase());
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	public void setDataOfSearch(IDataOfSearch dataOfSearch) {
		this.dataOfSearch = dataOfSearch;
	}

	@Override
	public void onActionViewExpanded() {
		updateView();
		queryEditText.requestFocus();
		App.setImeVisibility(getContext(), true, queryEditText);
	}

	@Override
	public void onActionViewCollapsed() {
		updateView();
		App.setImeVisibility(getContext(), false, queryEditText);
		clearFocus();
		queryEditText.clearFocus();
		queryEditText.setText("");
	}

	public void setHint(CharSequence hint) {
		queryEditText.setHint(hint);
	}

	private void updateView() {
		final boolean hasText = !TextUtils.isEmpty(queryEditText.getText());
		mCloseButton.setVisibility(hasText ? VISIBLE : GONE);
		mSearchHint.setVisibility(hasText ? GONE : VISIBLE);
	}

	public static class EditText_immStateRemember extends EditText {

		public EditText_immStateRemember(Context context) {
			super(context);
		}

		public EditText_immStateRemember(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void onWindowFocusChanged(boolean hasWindowFocus) {
			super.onWindowFocusChanged(hasWindowFocus);
			InputMethodManager imm = (InputMethodManager)
					getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			if (hasWindowFocus)
				imm.showSoftInput(this, 0);
		}
	}
}

