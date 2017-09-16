package com.bookStore.App;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class App extends Application {

	private static App app;
	private static Resources res;

	public static Context getContext() {
		return app;
	}

	public static Resources getRes() {
		return res;
	}

	public static void setImeVisibility(Context context, final boolean visible, View view) {
		InputMethodManager imm = (InputMethodManager)
				context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (visible)
			imm.showSoftInput(view, 0);
		else
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		DataBase.initialize(this);
		res = getResources();
	}
}