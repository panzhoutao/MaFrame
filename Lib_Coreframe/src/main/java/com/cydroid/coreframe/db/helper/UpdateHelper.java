package com.cydroid.coreframe.db.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cydroid.coreframe.db.base.BaseHelper;

import java.io.File;
import java.io.Serializable;

public class UpdateHelper extends BaseHelper<Serializable,Integer>{

	public UpdateHelper(Context context) {
		super(context, null,null);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getCreateSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Long insert(Serializable content, SQLiteDatabase db) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Serializable parseDataCursor(Cursor cursor) {
		// TODO Auto-generated method stub
		return null;
	}
	public void clearCash(){
		final File file = context.getDatabasePath(DATABASE_NAME);
        file.delete();
        new UpdateHelper(context);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		final File file = context.getDatabasePath(DATABASE_NAME);
        file.delete();
        new UpdateHelper(context);
	}
}
