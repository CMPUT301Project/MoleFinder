package model.classes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * This is the database controller for the MoleFinder application,
 * the database contains three tables. One table is for storing the references to images,
 * one for the tags of those images, and one for users. This class gives the 
 * database functionality such as deleting, inserting and fetching database entries.
 * @author jletourn
 *
 */

public class DatabaseManager{

	/* declaration of global variables used in this class*/
	public static final String KEY_TAG = "tag";	
	public static final String KEY_DATE = "date";
	public static final String KEY_COMMENTS = "comments";
	public static final String KEY_IMAGE = "image";
	public static final String KEY_ROWID = "_id";
	
	public static final String KEY_USERNAME = "user";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_ROLE = "role";

	private static final String TAG = "DbController";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_NAME = "ImageLog";
	private static final String DATABASE_IMAGE_TABLE = "ImageTable";
	private static final String DATABASE_TAG_TABLE = "TagTable";
	private static final String DATABASE_LOGIN_TABLE = "LoginTable";
	private static final int DATABASE_VERSION = 2;

	/*
	 * Database creation sql statement
	 */
	private static final String DATABASE_CREATE_IMAGE =
		"create table " + DATABASE_IMAGE_TABLE + " (_id integer primary key autoincrement, "
		+ "tag text not null, date text not null, comments text, image text not null);";

	private static final String DATABASE_CREATE_TAG =
		"create table " + DATABASE_TAG_TABLE + " (_id integer primary key autoincrement, "
		+ "tag text not null, comments text);";
	
	private static final String DATABASE_CREATE_LOGIN = 
			"create table " + DATABASE_LOGIN_TABLE + " (_id integer primary key autoincrement, "
			+ "password text not null, role text not null, user text);";

	private final Context mCtx;

	/*
	 * Helper class that initializes the DatabaseManager object
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		/** Create the database tables.
		 * 
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE_IMAGE);	//runs the create table statement
			db.execSQL(DATABASE_CREATE_TAG);
			db.execSQL(DATABASE_CREATE_LOGIN);
			}

		/** Update the table rows.
		 * 
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_IMAGE_TABLE);
			onCreate(db);
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx the Context within which to work
	 */
	public DatabaseManager(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Open the database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException if the database could be neither opened or created
	 */
	public DatabaseManager open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Closes the database.
	 */
	public void close() {
		mDbHelper.close();
	}


	/**
	 * Create a new Image Entry using the information the user provided. If the entry is
	 * successfully created return the new rowId for that entry, otherwise return
	 * a -1 to indicate failure.
	 * 
	 * @param String tag, String date, String comments, String image
	 * @return rowId or -1 if failed
	 */
	public long createImageEntry(String tag, String date, String comments, String image) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TAG, tag);		
		initialValues.put(KEY_DATE, date);
		initialValues.put(KEY_COMMENTS, comments);
		initialValues.put(KEY_IMAGE, image);
		return mDb.insert(DATABASE_IMAGE_TABLE, null, initialValues);
	}
	
	/**
	 * Create a new user in the database.
	 * 
	 * @param String password
	 * @return rowId or -1 if failed
	 */
	public long createUser(String password, String role) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_PASSWORD, password);
		initialValues.put(KEY_ROLE, role);
		return mDb.insert(DATABASE_LOGIN_TABLE, null, initialValues);
	}

	/**
	 * Create a new Tag Entry using the information the user provided. If the entry is
	 * successfully created return the new rowId for that note, otherwise return
	 * a -1 to indicate failure.
	 * 
	 * @param String tag, String comments
	 * @return rowId or -1 if failed
	 */
	public long createTagEntry(String tag, String comments) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TAG, tag);		
		initialValues.put(KEY_COMMENTS, comments);
		return mDb.insert(DATABASE_TAG_TABLE, null, initialValues);
	}


	/**
	 * Delete the image entry with the given rowId
	 * 
	 * @param rowId id of note to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteImageEntry(long rowId) {
		return (mDb.delete(DATABASE_IMAGE_TABLE, KEY_ROWID + "=" + rowId, null) > 0);
	}
	
	/** Delete the tag with the given rowId
	 * 
	 * @param rowId Id of tag to delete
	 * @return true if deleted, else false
	 */
	public boolean deleteTag(long rowId) {
		return (mDb.delete(DATABASE_TAG_TABLE, KEY_ROWID + " = " + rowId, null) > 0);
	}


	/**
	 * Delete all entries with the specified tag in the database
	 * 
	 * @param tag used to categorize the images
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteAllEntries(String tag) {
		boolean images = mDb.delete(DATABASE_IMAGE_TABLE, KEY_TAG + " = " + "'" + tag + "'", null) > 0;
		boolean tags = mDb.delete(DATABASE_TAG_TABLE, KEY_TAG + " = " + "'" + tag + "'", null) > 0;
		
		return images && tags;
	}

	/**
	 * Updates the values of a row in the Tag table
	 * 
	 * @param long rowId, String tag, String comments
	 * @return rowId or -1 if failed
	 */
	public boolean editTag(long rowId, String tag, String comments){
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_TAG, tag);
		updateValues.put(KEY_COMMENTS, comments);
		return (mDb.update(DATABASE_TAG_TABLE, updateValues,
				KEY_ROWID + "=" + rowId, null) > 0);
	}

	/**
	 * Updates the values of a row in the Image table
	 * 
	 * @param long rowId, String tag, String comments
	 * @return rowId or -1 if failed
	 */
	public boolean editImage(long rowId, String tag, String comments){
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_TAG, tag);
		updateValues.put(KEY_COMMENTS, comments);
		return (mDb.update(DATABASE_IMAGE_TABLE, updateValues, 
				KEY_ROWID + "=" + rowId, null) > 0);
	}

	/**
	 * Return a Cursor over the specified tag in the database
	 * 
	 * @return Cursor of the specified tag
	 */
	public Cursor fetchTag(String tag){
		return mDb.query(DATABASE_TAG_TABLE, new String[] {KEY_TAG, KEY_COMMENTS},
				KEY_TAG + "=" + tag, null, null, null, null);
	}
	
	/**
	 * Return a Cursor over the specified user in the database
	 * 
	 * @return Cursor of the specified user
	 */
	public Cursor fetchPassword(){
		return mDb.query(DATABASE_LOGIN_TABLE, new String[] {KEY_PASSWORD, KEY_ROLE},null, null, null, null, null);
	}

	/**
	 * Return a Cursor over the specified image in the database based on 
	 * 
	 * @return Cursor of the specified image
	 */
	public Cursor fetchImage(String imageName){
		return mDb.query(DATABASE_IMAGE_TABLE, new String[] {KEY_TAG, KEY_DATE,
				KEY_COMMENTS, KEY_IMAGE}, KEY_IMAGE + " = " + "'" + imageName + "'", 
				null, null, null, null);
	}

	/**
	 * Return a Cursor over the specified tag in the database based on id.
	 * 
	 * @return Cursor of the specified tag
	 */
	public Cursor fetchTag(long id) {
		return mDb.query(DATABASE_TAG_TABLE, new String[] { KEY_ROWID, KEY_TAG, KEY_COMMENTS}, 
				KEY_ROWID + " = " + id, null, null, null, null);
	}
	
	/**
	 * Return a Cursor over the specified image in the database based on id.
	 * 
	 * @return Cursor of the specified image
	 */
	public Cursor fetchImage(long id) {
		return mDb.query(DATABASE_IMAGE_TABLE, new String[] { KEY_ROWID, KEY_TAG, KEY_DATE, KEY_COMMENTS, KEY_IMAGE }, 
				KEY_ROWID + " = " + id, null, null, null, null);
	}
	
	/** Return a Cursor over all the images in the database. 
	 * 
	 * @return A Cursor over all images.
	 */
	public Cursor fetchAllImages() {
		String[] columns = new String[] { KEY_ROWID, KEY_TAG, KEY_DATE, KEY_COMMENTS, KEY_IMAGE };
		String order = KEY_DATE + " ASC";
		return mDb.query(DATABASE_IMAGE_TABLE, columns, null, null, null, null, order);
	}
	
	/**
	 * Return a Cursor over the list of all Images in the database
	 * 
	 * @param tag used to categorize the images
	 * @return Cursor over all images with matching tag.
	 */
	public Cursor fetchAllImages(String tag) {
		String[] columns = new String[] {KEY_ROWID, KEY_TAG, KEY_DATE, KEY_COMMENTS, KEY_IMAGE};
		String where = KEY_TAG + " = " + "'" + tag + "'";
		String order = KEY_DATE + " ASC";
		return mDb.query(DATABASE_IMAGE_TABLE, columns, where, null, null, null, order);
	}

	/**
	 * Return a Cursor over the list of all tags in the database
	 * 
	 * @return Cursor over all tags
	 */
	public Cursor fetchAllTags(){
		return mDb.query(DATABASE_TAG_TABLE, new String[] {KEY_ROWID, KEY_TAG, KEY_COMMENTS}, 
				null, null, null, null, null);
	}
	
	/** Return a cursor over the list of tags that are within the time interval and 
	 * match the tag. 
	 *  
	 * @param tag The group of images to search
	 * @param interval The time (in days) away from the current date to search
	 * @return A Cursor over the matching tags 
	 */
	public Cursor fetchAdvancedConditions(String tag, int interval, boolean mostRecentFirst) {	
		// setup query clauses
		String where = KEY_TAG + " = " + "'" + tag + "'";
		String[] args = null;
		String suffix;
		if (mostRecentFirst) {
			suffix = " DESC";
		}
		else {
			suffix = " ASC";
		}
		
		if (interval > 0) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, -1*interval);
			Date today = new Date();
			Date startDate = cal.getTime();
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
			String from = format.format(startDate);
			String to = format.format(today);
						
			where += " AND " + KEY_DATE + " BETWEEN ? AND ?"; 
			args = new String[] { from, to };
		}

		String[] columns = new String[] { KEY_ROWID, KEY_TAG, KEY_DATE, KEY_COMMENTS, KEY_IMAGE };
		String order = KEY_DATE + suffix;
		// run query
		return mDb.query(DATABASE_IMAGE_TABLE, columns, where, args, null, null, order);
	}

}