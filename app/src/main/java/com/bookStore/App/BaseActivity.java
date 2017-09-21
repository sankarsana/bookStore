package com.bookStore.App;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bookStore.R;

public abstract class BaseActivity extends AppCompatActivity {

	@Override
	public void setContentView(@LayoutRes int layoutResID) {
		super.setContentView(layoutResID);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null && layoutResID != R.layout.activity_store) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
			toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onBackPressed();
				}
			});
		}
	}

	protected void setToolbarTitle(String title) {
		if (getSupportActionBar() != null)
			getSupportActionBar().setTitle(title);
	}

	protected void setToolbarSubtitle(String title) {
		if (getSupportActionBar() != null)
			getSupportActionBar().setSubtitle(title);
	}
}
