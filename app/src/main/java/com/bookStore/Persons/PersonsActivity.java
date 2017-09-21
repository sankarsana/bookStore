package com.bookStore.Persons;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bookStore.App.BaseListActivity;
import com.bookStore.App.MyDialogFragment;
import com.bookStore.Cards.CardDatesActivity;
import com.bookStore.R;

import java.util.HashMap;

public class PersonsActivity extends BaseListActivity implements MyDialogFragment.IMyDialogFragment {

	private static Person person;
	private Persons persons;
	private UpdateDialog updateDialog;

	public static Person getPerson() {
		return person;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_persons);
		persons = new Persons();
		setListAdapter(persons);
		registerForContextMenu(getListView());
		updateDialog = new UpdateDialog();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_persons, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.persons_cm, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_persons_add) {
			person = new Person();
			showDialog();
		}
		return super.onOptionsItemSelected(item);
	}

	private void showDialog() {
		updateDialog.addData("name", person.name);
		updateDialog.addData("percent", person.percent + "");
		updateDialog.show(getSupportFragmentManager(), "personDialog");
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this, CardDatesActivity.class);
		person = Person.getPerson(id);
		intent.putExtra("personName", person.name);
		startActivity(intent);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		try {
			person = Person.getPerson(((AdapterView.AdapterContextMenuInfo) item
					.getMenuInfo()).id);
		} catch (Exception ignored) {
		}
		switch (item.getItemId()) {
			case R.id.persons_cm_delete:
				if (person.id == Person.STORE_ID)
					Toast.makeText(this, "Невозможно удалить!", Toast.LENGTH_SHORT).show();
				else {
					persons.delete(person.id);
					persons.refresh();
				}
				break;
			case R.id.persons_cm_update:
				showDialog();
				break;
		}
		return true;
	}

	@Override
	public boolean executeOk(HashMap<String, String> data) {
		person.name = data.get("name");
		String percent = data.get("percent");
		if (percent.length() == 0)
			person.percent = 100;
		else
			person.percent = Integer.parseInt(percent);
		if (person.id == -1) {
			persons.insert(person);
			Intent intent = new Intent(this, CardDatesActivity.class);
			startActivity(intent);
		} else {
			persons.update(person);
		}
		return true;
	}
}
