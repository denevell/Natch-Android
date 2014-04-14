package org.denevell.droidnatch.app.visited_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VisitedPostsTable {

	private static final String VISITED_POSTS = "visitedPosts";
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private Context mContext;

	public VisitedPostsTable(Context context)  {
		dbHelper = new MySQLiteHelper(context, VISITED_POSTS);
		setContext(context);
	}

	public SQLiteDatabase open() throws SQLException {
		database = dbHelper.getWritableDatabase();
		return database;
	}

	public void close() {
		dbHelper.close();
	}

	public void insert(long postId, long modDate) {
		ContentValues values = new ContentValues();
		values.put("post_id", postId);
		values.put("mod_date", modDate);
		long insertId = database.insert(VISITED_POSTS, null, values);
		if(insertId==-1) {
			throw new IllegalArgumentException("Couldn't insert your value into the db");
		}
	}

	public void update(long id, long modification) {
		ContentValues values = new ContentValues();
		values.put("mod_date", modification);
		values.put("post_id", id);
		database.update(VISITED_POSTS, values, "post_id=?", new String[]{String.valueOf(id)});
	}

	public int delete(String id) {
		return database.delete(VISITED_POSTS,"_id = " + id, null);
	}

/*	@SuppressWarnings("unchecked")
	public List getAll() {
		List all = new ArrayList<Object>();
		
		Cursor cursor = database.query(VISITED_POSTS,
				null, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			T a = null;
			try {
				a = (T) mClazz.newInstance(); //mTableMetaData.newInstance();
				a.fromCursor(cursor);
				all.add(a);
			} catch (Exception e) {
				e.printStackTrace();
			}
			cursor.moveToNext();
		}
		
		cursor.close();
		return all;
	}
*/	
	/**
	 * @return the modification date of the post, or -1 if doesn't exist
	 */
	public long isPostIdInTable(long postId) {
		Cursor cursor = database.query(VISITED_POSTS,
				null, "post_id="+postId, null, null, null, null);
		if(cursor!=null && cursor.getCount()>0) {
			cursor.moveToFirst();
			int colIndex = cursor.getColumnIndex("mod_date");
			return cursor.getLong(colIndex);
		} else {
			return -1;
		}
	}

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context mContext) {
		this.mContext = mContext;
	}

	public class MySQLiteHelper extends SQLiteOpenHelper {

		public MySQLiteHelper(Context context, String dbName) {
			super(context, dbName, null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase database) {
			try {
				database.execSQL(getTableCreationString());
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if(newVersion==1) {
				db.execSQL("DROP TABLE IF EXISTS " + VISITED_POSTS);
				onCreate(db);
			}
		}
		
		public String getTableCreationString() {
			String cmd = "create table " + VISITED_POSTS + "(" +
					"_id integer primary key autoincrement";
				cmd += ", post_id long unique";
				cmd += ", mod_date long";
			cmd += ");";
			return cmd;
		}		

	}


}