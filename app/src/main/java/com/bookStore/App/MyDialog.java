package com.bookStore.App;

import android.app.Dialog;
import android.content.Context;

import com.bookStore.R;

public class MyDialog extends Dialog {

	public MyDialog(Context context) {
		super(context, R.style.DialogLight);
		this.setTitle("Title");
	}
}
