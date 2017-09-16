package com.bookStore.App;

import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bookStore.R;

public abstract class MyFragment extends Fragment {

	private PopupMenu popupMenu;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.overflowItemMenu) {
			popupMenuShow();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void popupMenuShow() {
		if (popupMenu == null) {
			popupMenu = new PopupMenu(getActivity(), getActivity().findViewById(R.id.overflowItemMenu));
			popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem menuItem) {
					onOptionsItemSelected(menuItem);
					return false;
				}
			});
			onCreateOverflowOptionsMenu(popupMenu.getMenu(), getActivity().getMenuInflater());
		}
		popupMenu.show();
	}

	public void onCreateOverflowOptionsMenu(Menu menu, MenuInflater inflater) {
	}
}
