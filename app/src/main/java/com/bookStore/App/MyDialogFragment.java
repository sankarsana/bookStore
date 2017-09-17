package com.bookStore.App;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.bookStore.R;
import com.bookStore.store.Writer;

import java.util.HashMap;

public class MyDialogFragment extends DialogFragment {

	protected HashMap<String, String> data = new HashMap<String, String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NORMAL, R.style.DialogLight);
	}

	public void addData(String key, String value) {
		data.put(key, value);
	}

	public interface IMyDialogFragment {
		public boolean executeOk(HashMap<String, String> data);
	}

	public interface IDialogItemClick {
		public void onDialogItemClick(Writer writer);
	}
}
