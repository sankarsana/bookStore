package com.bookStore.Persons;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bookStore.App.MyDialogFragment;
import com.bookStore.R;

public class UpdateDialog extends MyDialogFragment {

	private TextView title;
	private EditText name;
	private EditText percent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.person_dialog, container);
		title = (TextView) view.findViewById(R.id.personDialogTitle);
		name = (EditText) view.findViewById(R.id.personDialogName);
		percent = (EditText) view.findViewById(R.id.personDialogPercent);

		view.findViewById(R.id.personDialogOk).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (name.getText().length() == 0) {
					name.setError("Не может быть пустым");
					return;
				}
				data.put("name", name.getText().toString());
				data.put("percent", percent.getText().toString());
				if (((IMyDialogFragment) getActivity()).executeOk(data))
					dismiss();
			}
		});

		view.findViewById(R.id.personDialogCancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		name.requestFocus();
		name.setText(data.get("name"));

		String dataTitle = data.get("title");
		if (dataTitle != null)
			title.setText(dataTitle);

		String dataPercent = data.get("percent");
		if (dataPercent != null)
			percent.setText(dataPercent);
		else
			percent.setVisibility(View.GONE);
	}
}
