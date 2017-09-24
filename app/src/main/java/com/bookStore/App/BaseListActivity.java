package com.bookStore.App;

import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.bookStore.R;

public abstract class BaseListActivity<T extends ListAdapter> extends BaseActivity {

	private ListView listView;

	@Override
	protected void onStart() {
		super.onStart();
		ListAdapter adapter = getListAdapter();
		if (adapter instanceof CursorSearchAdapter)
			((CursorSearchAdapter) adapter).refresh();
	}

	protected ListView getListView() {
		if (listView == null) {
			listView = (ListView) findViewById(android.R.id.list);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					onListItemClick(getListView(), view, i, l);
				}
			});
		}
		return listView;
	}

	protected T getListAdapter() {
		T adapter = (T) getListView().getAdapter();
		if (adapter instanceof HeaderViewListAdapter) {
			return (T) ((HeaderViewListAdapter) adapter).getWrappedAdapter();
		} else {
			return adapter;
		}
	}

	protected void setListAdapter(T adapter) {
		getListView().setAdapter(adapter);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return initializeMySearchView(menu);
	}

	protected boolean initializeMySearchView(Menu menu) {
		MenuItem searchItem = menu.findItem(R.id.my_action_search);
		if (searchItem == null) return false;
		MySearchView searchView = (MySearchView) MenuItemCompat.getActionView(searchItem);
		searchView.setHint(searchItem.getTitleCondensed());
		searchView.setDataOfSearch((IDataOfSearch) getListView().getAdapter());
		return true;
	}
}
