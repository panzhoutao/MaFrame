package com.cydroid.coreframe.db.base;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cydroid.coreframe.db.DBLock;
import com.cydroid.coreframe.db.Pager;
import com.cydroid.coreframe.tool.util.LogUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public abstract class BaseHelper <T extends Serializable,PK extends Serializable> extends SQLiteOpenHelper
		implements BaseOperate<T, PK> {


	protected String ID;
	protected final static String DATABASE_NAME = "app_db_order.db";
	private final static int DATABASE_VERSION = 130;
	protected Context context;
	protected String TABLE_NAME;
	public BaseHelper(Context context,String tableName,String key) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		TABLE_NAME=tableName;
		ID=key;
		this.context=context;
		synchronized (DBLock.Lock) {
			onCreate(this.getWritableDatabase());
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql=getCreateSql();
		if(sql!=null){
			db.execSQL(getCreateSql());
		}
	}
	public void clearCash(){
//		final File file = context.getDatabasePath(DATABASE_NAME);
//        file.delete();
//        new BaseHelper(context);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
//		final File file = context.getDatabasePath(DATABASE_NAME);
//        file.delete();
//        new BaseHelper(context);
	}
	protected abstract String getCreateSql();
	protected abstract Long insert(T content, SQLiteDatabase db);
	protected abstract T parseDataCursor(Cursor cursor);

	@Override
	public void clear() {
		synchronized (DBLock.Lock) {
			SQLiteDatabase db = this.getWritableDatabase();
			String dropsql = " DROP TABLE IF EXISTS " + TABLE_NAME;
			db.execSQL(dropsql);
			db.execSQL(getCreateSql());
		}
	}


	@Override
	public Long insert(T entity) {
		// TODO Auto-generated method stub
		synchronized (DBLock.Lock) {
			SQLiteDatabase db = getWritableDatabase();
			return insert(entity,db);
		}
	}

	@Override
	public Long insertAll(List<T> entitys) {
		// TODO Auto-generated method stub
		synchronized (DBLock.Lock) {
			SQLiteDatabase db = getWritableDatabase();
			db.beginTransaction();
			for(T entity:entitys){
				insert(entity,db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		return null;
	}


	@Override
	public Long getNextPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long update(T entity) {
		// TODO Auto-generated method stub
		return insert(entity);
	}

	@Override
	public int updateByProperty(Serializable id, String property,
								Serializable value) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteById(PK id) {
		// TODO Auto-generated method stub
		LogUtil.i("id  " + ID);
		synchronized (DBLock.Lock) {
			SQLiteDatabase db = this.getWritableDatabase();
			return db.delete(TABLE_NAME, ID+"=?"
					, new String[]{String.valueOf(id)});
		}
	}

	@Override
	public int delete(List<PK> ids) {
		// TODO Auto-generated method stub
		synchronized (DBLock.Lock) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.beginTransaction();
			for(PK id:ids){
				db.delete(TABLE_NAME, ID+"=?"
						, new String[]{String.valueOf(id)});
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		return 0;
	}

	@Override
	public int deleteByProperty(String property, Serializable value) {
		// TODO Auto-generated method stub
		synchronized (DBLock.Lock) {
			SQLiteDatabase db = this.getWritableDatabase();
			return db.delete(TABLE_NAME, property+" =?"
					,new String[]{String.valueOf(value)});
		}
	}

	@Override
	public T getById(PK id) {
		// TODO Auto-generated method stub
		synchronized (DBLock.Lock) {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(TABLE_NAME, null, ID + "=?"
					, new String[]{String.valueOf(id)}, null, null, null);
			if (!cursor.moveToFirst()) {
				cursor.close();
				return null;
			}
			T holder = parseDataCursor(cursor);
			cursor.close();
			return holder;
		}
	}

	@Override
	public List<T> getByProperty(String property, Serializable value) {
		// TODO Auto-generated method stub
		synchronized (DBLock.Lock) {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(TABLE_NAME, null, property + " = ?"
					, new String[]{String.valueOf(value)}, null, null, null);
			List<T> holderlist = new ArrayList<T>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				T holder = parseDataCursor(cursor);
				holderlist.add(holder);
			}
			cursor.close();
			return holderlist;
		}
	}
	@Override
	public List<T> getByLikeProperty(String property, Serializable value) {
		// TODO Auto-generated method stub
		synchronized (DBLock.Lock) {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(TABLE_NAME, null, property + " like ?"
					, new String[]{"%"+String.valueOf(value)+"%"}, null, null, null);
			List<T> holderlist = new ArrayList<T>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				T holder = parseDataCursor(cursor);
				holderlist.add(holder);
			}
			cursor.close();
			return holderlist;
		}
	}

	@Override
	public List<T> getByExample(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findAllByPage(Pager mPager,BaseDAO.SortType sortType) {
		// TODO Auto-generated method stub
		synchronized (DBLock.Lock) {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, ID + (sortType== BaseDAO.SortType.asc?" asc":" desc"), mPager.curpage * mPager.pagesize + "," + mPager.pagesize);
			List<T> holders = new ArrayList<T>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				holders.add(parseDataCursor(cursor));
			}
			cursor.close();
			return holders;
		}
	}

	@Override
	public List<T> findAll(String orderby) {
		// TODO Auto-generated method stub
		synchronized (DBLock.Lock) {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, orderby);
			List<T> holderlist = new ArrayList<T>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				T holder = parseDataCursor(cursor);
				holderlist.add(holder);
			}
			cursor.close();
			return holderlist;
		}
	}
	@Override
	public List<T> findAll() {
		synchronized (DBLock.Lock) {
			// TODO Auto-generated method stub
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
			List<T> holderlist = new ArrayList<T>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				T holder = parseDataCursor(cursor);
				holderlist.add(holder);
			}
			cursor.close();
			return holderlist;
		}
	}


}
