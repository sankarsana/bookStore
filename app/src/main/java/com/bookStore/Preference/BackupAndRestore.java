package com.bookStore.Preference;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.bookStore.App.ActionBarListActivity;
import com.bookStore.App.DataBase;
import com.bookStore.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class BackupAndRestore extends ActionBarListActivity {

	private String[] backupList = new String[0];

	public static boolean backup() {
		try {
			File backupDB = new File(FileHelper.getDataBaseBackupFolder(),
					FileHelper.getDataBaseBackupName());
			File currentDB = new File(DataBase.DB_PATH);
			FileChannel src = new FileInputStream(currentDB).getChannel();
			FileChannel dst = new FileOutputStream(backupDB).getChannel();
			dst.transferFrom(src, 0, src.size());
			src.close();
			dst.close();
			return true;
		} catch (Exception ignored) {
		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backap_restore_act);

//		toolbar.setDisplayHomeAsUpEnabled(true);
//		toolbar.setIcon(R.drawable.ic_action_save);
		updateBackupList();
		setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, backupList));
		refreshList();
	}

	private void updateBackupList() {
		backupList = FileHelper.getDataBaseBackupFolder().list();
		if (backupList == null)
			backupList = new String[0];
	}

	private void refreshList() {
		updateBackupList();
		((BaseAdapter) getListAdapter()).notifyDataSetChanged();
	}

	@Override
	protected void onListItemClick(ListView l, View v, final int position, long id) {
		super.onListItemClick(l, v, position, id);
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Восстановление базы")
				.setMessage("\t Восстановить базу из \n" + backupList[position] +
						" ? \n\tТекущая база будет полностью уничтоженна.")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						restore(backupList[position]);
						dialogInterface.cancel();
						BackupAndRestore.this.finish();
					}
				})
				.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.cancel();
					}
				})
				.show();
	}

	public void restore(String fileName) {
		try {
			File sd = Environment.getExternalStorageDirectory();
			if (sd.canWrite()) {
				File backupDB = new File(FileHelper.getDataBaseBackupFolder(), fileName);
				File currentDB = new File(DataBase.DB_PATH);
				FileChannel src = new FileInputStream(backupDB).getChannel();
				FileChannel dst = new FileOutputStream(currentDB).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
			}
		} catch (Exception ignored) {
		}
	}
}
