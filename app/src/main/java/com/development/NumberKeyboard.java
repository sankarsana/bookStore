package com.development;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bookStore.R;

public class NumberKeyboard extends LinearLayout implements OnClickListener {
	private EditText editText = null;

	public NumberKeyboard(Context context, AttributeSet attrs) {
		super(context, attrs);

		if (!isInEditMode()) {

			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			@SuppressWarnings("unused")
			View view = layoutInflater.inflate(R.layout.number_keyboard, this);

			(findViewById(R.id.ik0)).setOnClickListener(this);
			(findViewById(R.id.ik1)).setOnClickListener(this);
			(findViewById(R.id.ik2)).setOnClickListener(this);
			(findViewById(R.id.ik3)).setOnClickListener(this);
			(findViewById(R.id.ik4)).setOnClickListener(this);
			(findViewById(R.id.ikb)).setOnClickListener(this);
			(findViewById(R.id.ik5)).setOnClickListener(this);
			(findViewById(R.id.ik6)).setOnClickListener(this);
			(findViewById(R.id.ik7)).setOnClickListener(this);
			(findViewById(R.id.ik8)).setOnClickListener(this);
			(findViewById(R.id.ik9)).setOnClickListener(this);
			(findViewById(R.id.ikb)).setOnClickListener(this);
		}
	}

	public void setEditText(EditText editText) {
		this.editText = editText;
	}

	@Override
	public void onClick(View v) {
		if (editText != null) {
			editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,
					Integer.parseInt(v.getTag().toString())));
		}
	}

}
