package com.bookStore.Cards;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
import android.view.*;
import android.widget.*;

import com.bookStore.App.App;
import com.bookStore.App.MyFragment;
import com.bookStore.Persons.Person;
import com.bookStore.Persons.PersonsActivity;
import com.bookStore.R;

public class CardDateFragment extends MyFragment implements View.OnClickListener {

	private static CardDate currentCardDate;
	private TextView tvDate;
	private TextView tvSum;
	private TextView tvPercent;
	private TextView tvMark;
	private LinearLayout panel;
	private TextView confirmation;
	private CardDate cardDate;
	private CardEntriesAdapter cardEntriesAdapter;
	private PopupMenu confirmationPopupMenu;

	public CardDateFragment(CardDate cardDate) {
		super();
		this.cardDate = cardDate;
	}

	public static CardDate getCardDate() {
		return currentCardDate;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_card, container, false);

		tvPercent = (TextView) rootView.findViewById(R.id.personCard_percent);
		tvDate = (TextView) rootView.findViewById(R.id.personCard_cardDate);
		tvSum = (TextView) rootView.findViewById(R.id.personCard_cardSum);
		tvMark = (TextView) rootView.findViewById(R.id.personCard_mark);
		panel = (LinearLayout) rootView.findViewById(R.id.personCard_panel);
		confirmation = (TextView) rootView.findViewById(R.id.personCard_confirmation);

		setHasOptionsMenu(true);

		if (PersonsActivity.getPerson().id == Person.STORE_ID) {
			cardEntriesAdapter = new CardEntriesAdapter_store(cardDate.id);
			rootView.findViewById(R.id.personCard_hiddenLinear).setVisibility(View.GONE);
		} else
			cardEntriesAdapter = new CardEntriesAdapter(PersonsActivity.getPerson().id, cardDate.id);

		ListView lv = (ListView) rootView.findViewById(android.R.id.list);
		lv.setAdapter(cardEntriesAdapter);
		registerForContextMenu(lv);
		confirmationPopupMenu = new PopupMenu(getActivity(), confirmation);
		confirmationPopupMenu.getMenu().add("");
		confirmationPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				cardDate.setConductOpposite();
				update();
				return false;
			}
		});

		tvDate.setOnClickListener(this);
		tvPercent.setOnClickListener(this);
		tvMark.setOnClickListener(this);
		confirmation.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		update();
	}

	public void update() {
		cardEntriesAdapter.refresh();
		cardDate.refresh();
		Resources res = App.getRes();
		tvDate.setText(cardDate.getDateSimple_String());
		tvSum.setText(cardDate.sum + res.getString(R.string.rDot));
		tvPercent.setText(cardDate.getPercent() + res.getString(R.string.percent));
		if (cardDate.getConduct()) {
			panel.setBackgroundColor(res.getColor(R.color.panelNormal));
			confirmation.setText(R.string.conduct_YES);
			confirmationPopupMenu.getMenu().getItem(0).setTitle(R.string.conduct_NO);
		} else {
			panel.setBackgroundColor(res.getColor(R.color.panelSingleOut));
			confirmation.setText(R.string.conduct_NO);
			confirmationPopupMenu.getMenu().getItem(0).setTitle(R.string.conduct_YES);
		}
		tvMark.setText(cardDate.mark);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.card_date_fragment, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onCreateOverflowOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.card_date_fragment_over, menu);
		super.onCreateOverflowOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = getActivity().getIntent();
		currentCardDate = cardDate;
		switch (item.getItemId()) {
			case R.id.overflowItemMenu:
				if (PersonsActivity.getPerson().id == Person.STORE_ID) {
					intent.setClass(getActivity(), EditorActivity.class);
					intent.putExtra("mode", CardDatesActivity.MODE_DISTR);
					intent.putExtra("modeTitle", getResources().getString(R.string.toDistrib));
					startActivityForResult(intent, 0);
					return true;
				}
				break;
			case CardDatesActivity.MODE_GET:
			case CardDatesActivity.MODE_GET_AND_DISTR:
			case CardDatesActivity.MODE_DISTR:
			case CardDatesActivity.MODE_RET:
				intent.setClass(getActivity(), EditorActivity.class);
				intent.putExtra("mode", item.getItemId());
				intent.putExtra("modeTitle", item.getTitle());
				startActivityForResult(intent, 0);
				break;
			case R.id.card_delete:
				cardDate.clearInTransaction();
				update();
				break;
			case R.id.editorAddMissing:
				intent.setClass(getActivity(), GetMissingBookActivity.class);
				startActivityForResult(intent, 1);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.personCard_cardDate:
				getActivity().getIntent().putExtra(
						CardDatesActivity.CURRENT_DATE, cardDate.dateString);
				getActivity().showDialog(CardDatesActivity.DATE_DIALOG_ID);
				break;
			case R.id.personCard_percent:
				final EditText et = new EditText(getActivity());
				et.setText(Integer.toString(cardDate.getPercent()));
				et.setSelectAllOnFocus(true);
				et.setInputType(InputType.TYPE_CLASS_NUMBER);
				new AlertDialog.Builder(getActivity())
						.setTitle("Проценты")
						.setView(et)
						.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								cardDate.setPercent(Integer.parseInt(et.getText().toString()));
								dialogInterface.cancel();
								update();
							}
						})
						.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								dialogInterface.cancel();
							}
						})
						.show();
				break;
			case R.id.personCard_mark:
				final EditText etMark = new EditText(getActivity());
				etMark.setText(cardDate.mark);
				etMark.setSelection(etMark.getText().length());
				new AlertDialog.Builder(getActivity())
						.setTitle("Заметка")
						.setView(etMark)
						.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								cardDate.setMark(etMark.getText().toString());
								dialogInterface.cancel();
								update();
							}
						})
						.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								dialogInterface.cancel();
							}
						})
						.show();
				break;
			case R.id.personCard_confirmation:
				confirmationPopupMenu.show();
				break;
		}
	}
}
