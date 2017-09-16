package com.development;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;

public class InputView extends AppCompatEditText {

	public InputView(Context context) {
		super(context);
		setInputType(InputType.TYPE_NULL);
	}

	public InputView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setInputType(InputType.TYPE_NULL);
	}

	public InputView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setInputType(InputType.TYPE_NULL);
	}
}
