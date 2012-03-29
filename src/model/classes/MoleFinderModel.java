package model.classes;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

/** MoleFinderModel represents the current data in the system.
 * It uses the DatabaseManager to create lists of objects 
 * to be displayed elsewhere.
 * 
 * @author mbessett
 *
 */

public class MoleFinderModel {
	// consistent data across all views => singleton
	private static MoleFinderModel instance = null;
	// db access
	private DatabaseManager DBManager;
	// active entries - easily view in a list
	private List<DatabaseEntry> conditions;
	private List<DatabaseEntry> tags;
	
	
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
	
	/** Private constructor. Use the static method getInstance.
	 * 
	 * @param context Database creation requires context
	 */
	private MoleFinderModel(Context context) {
		DBManager = new DatabaseManager(context);
		conditions = new ArrayList<DatabaseEntry>();
		tags = new ArrayList<DatabaseEntry>();
		getTags(); // fill on construction
		//temp(); // test database items
	}
	
	/** Empty the current list of conditions.
	 * 
	 */
	public void clearConditions() {
		conditions.clear();
	}
	/** Empty the current list of tags;
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
	
	/** Delete the tag and all images associated with it. 
	 * 
	 * @param tag The tag to remove. 
	 */
	public void deleteTag(ConditionTag tag) {
		DBManager.open();
		DBManager.deleteAllEntries(tag.getName());		
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
	public void overwriteImage(ConditionEntry entry) {
		DBManager.open();
		DBManager.editImage(entry.getId(), entry.getTag(), entry.getComment());
		DBManager.close();
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
	
	/** Store a new ConditionTag in the database.
	 * 
	 * @param tag The tag to save to the database.
	 */
	public void saveImage(ConditionEntry entry) {
		DBManager.open();
		DBManager.createImageEntry(entry.getTag(), entry.getDate(), entry.getComment(), entry.getImage());
		DBManager.close();
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
	private void temp() {			
		DBManager.open();
		DBManager.deleteAllEntries("Face");
		DBManager.deleteAllEntries("Foot");
		DBManager.createTagEntry("Face", "This is your face.");
		String today = DateFormat.getDateInstance().format(new Date());
		String img = "img";
		DBManager.createImageEntry("Face", today, "Look at that thing on your face", img);
		DBManager.createTagEntry("Foot", "This is your foot. You only get one.");
		DBManager.createImageEntry("Foot", today, "You only have one foot?", img);
		DBManager.close();
	}
}
