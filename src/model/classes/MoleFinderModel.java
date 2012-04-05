package model.classes;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

/** MoleFinderModel represents the current data in the system.
 * It uses the DatabaseManager to create lists of objects 
 * to be displayed elsewhere.
 * 
 * @author mbessett
 */

public class MoleFinderModel {
	// consistent data across all views => singleton
	private static MoleFinderModel instance = null;
	// db access
	private DatabaseManager DBManager;
	// active entries - easily view in a list
	private List<DatabaseEntry> conditions;
	private List<DatabaseEntry> tags;
	private List<DatabaseEntry> users;
	
	
	/** Returns the current instance of the MoleFinderModel,
	 * or if none exists, creates one.
	 * 
	 * @param context Database creation requires context
	 * @return The current MoleFinderModel instance
	 */
	public static MoleFinderModel getInstance(Context context) {
		if (instance == null) {
			instance = new MoleFinderModel(context);
		}
		return instance;
	}
	
	/* Private constructor. Use the static method getInstance.
	 * 
	 * @param context Database creation requires context
	 */
	private MoleFinderModel(Context context) {
		DBManager = new DatabaseManager(context);
		conditions = new ArrayList<DatabaseEntry>();
		tags = new ArrayList<DatabaseEntry>();
		users = new ArrayList<DatabaseEntry>();
		getTags(); // fill on construction
		
		//createTestEntries();
		
	}
	
	/* Fake entries to test the date range feature.
	 * Working as expected.
	 */
	private void createTestEntries() {
		String tag = "Face";
		String img = ""; 
		String com = "Comment";
		int i = 0;
		ConditionTag t = new ConditionTag(1, tag);
		saveTag(t);
		
		ConditionEntry entry = new ConditionEntry(++i, tag, img, com, "2012-01-12");
		saveImage(entry);
		entry = new ConditionEntry(++i, tag, img, com, "2012-01-13");
		saveImage(entry);
		entry = new ConditionEntry(++i, tag, img, com, "2012-02-13");
		saveImage(entry);
		entry = new ConditionEntry(++i, tag, img, com, "2012-02-15");
		saveImage(entry);
		entry = new ConditionEntry(++i, tag, img, com, "2012-03-01");
		saveImage(entry);
		entry = new ConditionEntry(++i, tag, img, com, "2012-03-05");
		saveImage(entry);
		entry = new ConditionEntry(++i, tag, img, com, "2012-03-13");
		saveImage(entry);
	}
	
	/** Explicitly empty the current list of conditions.
	 * 
	 */
	public void clearConditions() {
		conditions.clear();
	}
	/** Explicitly empty the current list of tags;
	 * 
	 */
	public void clearTags() {
		tags.clear();
	}
	
	/** Fills the condition list with entries that match tag.
	 * 
	 * @param tag The grouping of conditions entries
	 */
	public void fetchConditions(String tag) {		
		clearConditions();
		DBManager.open();
		Cursor cur = DBManager.fetchAllImages(tag);
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			DatabaseEntry entry = cursorToCondition(cur);
			conditions.add(entry);
			cur.moveToNext();
		}
		cur.close();
		DBManager.close();
	}
	
	/** Fill the condition list with entries that match user input
	 * advanced search criteria. 
	 * 
	 * @param tag The grouping of entries to search in.
	 * @param interval The time in days from the current date to search.
	 * @param displaying The number of results to display.
	 * @param recentFirst Fetch in chronological order?
	 */
	public void fetchSpecificConditions(String tag, int interval, 
			int displaying, boolean mostRecentFirst) {
		clearConditions();
		DBManager.open();
		Cursor cur = DBManager.fetchAdvancedConditions(tag, interval, mostRecentFirst);
		int count;
		for (cur.moveToFirst(), count = 0; !cur.isAfterLast() && count < displaying;
		cur.moveToNext(), count++) {				
			DatabaseEntry entry = cursorToCondition(cur);
			conditions.add(entry);				
		}
		cur.close();
		DBManager.close();
	}

	/** Converts the first element in the cursor to a ConditionEntry.
	 * 
	 * @param cur List of ConditionEntry data returned from database
	 * @return The converted ConditionEntry object
	 */
	public ConditionEntry cursorToCondition(Cursor cur) {
		// (id, tag, image, comment, date)
		return new ConditionEntry(cur.getInt(0), cur.getString(1), 
				cur.getString(4), cur.getString(3), cur.getString(2));
	}
	
	/** Converts the first element in the cursor to a ConditionUser.
	 * 
	 * @param cur List of ConditionUser data returned from database
	 * @return The converted ConditionUser object
	 */
	public ConditionUser cursorToConditionUser(Cursor cur) {
		// (id, tag, image, comment, date)
		return new ConditionUser(cur.getInt(0), cur.getString(0), cur.getString(1));
	}
	
	/** Fills the tag list with all of the tags in the database.
	 * 
	 */
	public void fetchTags() {
		clearTags();
		DBManager.open();
		Cursor cur = DBManager.fetchAllTags();
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			DatabaseEntry tag = cursorToTag(cur);
			tags.add(tag);
			cur.moveToNext();
		}
		cur.close();
		DBManager.close();
	}
	
	/** Acquire one ConditionTag object from the database.
	 * 
	 * @param id RowId in the database tag table.
	 * @return A ConditionTag object corresponding to that
	 * row in the table.
	 */
	public ConditionTag getOneTag(long id) {
		DBManager.open();
		
		Cursor cursor = DBManager.fetchTag(id);
		cursor.moveToFirst();
		ConditionTag tag = cursorToTag(cursor);
		
		cursor.close();
		DBManager.close();
		return tag;
	}
	
	/** Acquire one ConditionEntry object from the database.
	 * 
	 * @param id RowId in the database image table. 
	 * @return A ConditionEntry object corresponding to that
	 * row in the table.
	 */
	public ConditionEntry getOneEntry(long id) {
		DBManager.open();
		
		Cursor cursor = DBManager.fetchImage(id);
		cursor.moveToFirst();
		ConditionEntry entry = cursorToCondition(cursor);
		
		cursor.close();
		DBManager.close();
		return entry;		
	}
	
	/** Acquire one ConditionUser object from the database.
	 *  
	 * @return A ConditionUser object corresponding to that
	 * row in the table.
	 */
	public ConditionUser getUser() {
		DBManager.open();
		
		Cursor cursor = DBManager.fetchPassword();
		cursor.moveToFirst();
		ConditionUser entry = cursorToConditionUser(cursor);
		
		cursor.close();
		DBManager.close();
		return entry;		
	}
	
	/** Delete the tag and all images associated with it. 
	 * 
	 * @param tag The tag to remove. 
	 */
	public void deleteTag(ConditionTag tag) {
		DBManager.open();
		DBManager.deleteAllEntries(tag.getName());		
		DBManager.close();
	}
	
	/** Delete the image. 
	 * 
	 * @param entry The entry to remove. 
	 */
	public void deleteImage(ConditionEntry entry){
		DBManager.open();
		DBManager.deleteImageEntry(entry.getId());		
		DBManager.close();
	}
	
	/** The comment of this tag has changed, overwrite it.
	 * 
	 * @param tag The ConditionTag to overwrite.
	 */
	public void overwriteTag(ConditionTag tag) {
		DBManager.open();
		DBManager.editTag(tag.getId(), tag.getName(), tag.getComment());
		DBManager.close();
	}
	
	/** An attribute of this object has changed, overwrite it. 
	 * 
	 * @param entry The ConditionEntry object to change. 
	 */
	public int overwriteImage(ConditionEntry entry) {
		DBManager.open();
		if(DBManager.editImage(entry.getId(), entry.getTag(), entry.getComment())){
			DBManager.close();
			return 1;
		}
		DBManager.close();
		return -1;
	}
	
	/** The name of this tag has changed, overwrite it and update all 
	 * applicable ConditionEntry's that use this tag. 
	 * 
	 * @param tag The ConditionTag to update. 
	 */
	public void overwriteCascade(String oldTagName, ConditionTag tag) {
		DBManager.open();
		Cursor cursor = DBManager.fetchAllImages(oldTagName);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ConditionEntry entry = cursorToCondition(cursor);
			DBManager.editImage(entry.getId(), tag.getName(), entry.getComment());
			cursor.moveToNext();
		}
		cursor.close();
		DBManager.editTag(tag.getId(), tag.getName(), tag.getComment());
		DBManager.close();
	}
	
	/** Store a new ConditionTag in the database.
	 * 
	 * @param tag The tag to save to the database.
	 */
	public void saveTag(ConditionTag tag) {
		DBManager.open();
		DBManager.createTagEntry(tag.getName(), tag.getComment());
		DBManager.close();
	}
	
	/** Store a new ConditionEntry in the database.
	 * 
	 * @param ConditionEntry object.
	 */
	public void saveImage(ConditionEntry entry) {
		DBManager.open();
		DBManager.createImageEntry(entry.getTag(), entry.getDate(), entry.getComment(), entry.getImage());
		DBManager.close();
	}
	
	/** Store a new ConditionUser in the database.
	 * 
	 * @param ConditionUser object.
	 */
	public void saveUser(ConditionUser user) {
		DBManager.open();
		DBManager.createUser(user.getPassword(), user.getRole());
		DBManager.close();
	}
	
	/**
	 * Check is there is a user in the database, if not return true
	 * @return true is this is a new user.
	 */
	public boolean isNewUser(){
		DBManager.open();
		Cursor cursor = DBManager.fetchPassword();
		if(!cursor.moveToFirst()){
			DBManager.close();
			return true;
		}
		DBManager.close();
		return false;
	}
	
	/** Converts the first element in the cursor to a ConditionTag.
	 * 
	 * @param cur List of ConditionTag data returned from database 
	 * @return The converted ConditionEntry object
	 */
	public ConditionTag cursorToTag(Cursor cur) {
		return new ConditionTag(cur.getInt(0), cur.getString(1), cur.getString(2));
	}
	
	/** Append one ConditionEntry to the end of the list.
	 * 
	 * @param entry The ConditionEntry to append
	 */
	public void addCondition(ConditionEntry entry) {
		conditions.add(entry);
	}
	
	/** Append one ConditionTag to the end of the list.
	 * 
	 * @param tag The ConditionTag to append
	 */
	public void addTag(ConditionTag tag) {
		tags.add(tag);
	}
	
	/** Get the current list of ConditionEntry objects.
	 * 
	 * @return The current list of ConditionEntry objects
	 */
	public List<DatabaseEntry> getConditions () {
		return conditions;
	}
	
	/** Get the current list of ConditionTag objects.
	 * 
	 * @return The current list of ConditionTag objects
	 */
	public List<DatabaseEntry> getTags() {
		return tags;
	}
	
	// start DB with some test objects
	public void initialiseTags() {			
		DBManager.open();
		DBManager.createTagEntry("Face","");
		DBManager.createTagEntry("Foot", "");
		DBManager.createTagEntry("Hand","");
		DBManager.createTagEntry("Leg", "");
		DBManager.createTagEntry("Arm","");
		DBManager.close();
	}
}
