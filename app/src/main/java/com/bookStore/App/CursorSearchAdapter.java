package com.bookStore.App;

public abstract class CursorSearchAdapter extends CursorAdapter implements IDataOfSearch {

	private String lastSearch;

	public abstract String getQuery(String search);

	@Override
	public void refresh() {
		if (lastSearch != null)
			refresh(lastSearch);
		else
			super.refresh();
	}

	public void refresh(String search) {
		lastSearch = search;
		if (search.length() == 0)
			super.refresh();
		else {
			cursor = DataBase.get().rawQuery(getQuery(search), null);
			notifyDataSetChanged();
		}
	}
}
