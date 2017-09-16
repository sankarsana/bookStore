package com.bookStore.App;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.PopupMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.bookStore.R;

public abstract class MyActionBarActivity extends ActionBarActivity {

	protected ActionBar actionBar;
	private PopupMenu popupMenu;
	private boolean isHomeButtonFinish = true;

	public boolean isHomeButtonFinish() {
		return isHomeButtonFinish;
	}

	public void setHomeButtonFinish(boolean isHomeButtonFinish) {
		this.isHomeButtonFinish = isHomeButtonFinish;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar = getSupportActionBar();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (isHomeButtonFinish) {
			if (item.getItemId() == android.R.id.home &&
					(actionBar.getDisplayOptions() & ActionBar.DISPLAY_HOME_AS_UP) != 0)
				finish();
		}
		if (item.getItemId() == R.id.overflowItemMenu) {
			if (popupMenuShow())
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private boolean popupMenuShow() {
		if (popupMenu == null) {
			popupMenu = new PopupMenu(this, findViewById(R.id.overflowItemMenu));
			popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem menuItem) {
					onOptionsItemSelected(menuItem);
					return false;
				}
			});
			onCreateOverflowOptionsMenu(popupMenu.getMenu());
		}
		if (popupMenu.getMenu().size() != 0) {
			popupMenu.show();
			return true;
		} else
			return false;
	}

	public void onCreateOverflowOptionsMenu(Menu menu) {
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			popupMenuShow();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
}
