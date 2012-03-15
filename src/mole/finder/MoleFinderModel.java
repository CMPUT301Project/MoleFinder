package mole.finder;

import java.util.ArrayList;
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
	private List<ConditionEntry> conditions;
	private List<ConditionTag> tags;
	
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
		conditions = new ArrayList<ConditionEntry>();
		tags = new ArrayList<ConditionTag>();
		getTags(); // fill on construction
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
		Cursor cur = DBManager.fetchAllImages(tag);
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			ConditionEntry entry = cursorToCondition(cur);
			conditions.add(entry);
			cur.moveToNext();
		}
		cur.close();
	}
	
	/** Converts the first element in the cursor to a ConditionEntry.
	 * 
	 * @param cur List of ConditionEntry data returned from database
	 * @return The converted ConditionEntry object
	 */
	public ConditionEntry cursorToCondition(Cursor cur) {
		// (id, tag, image, comment, date)
		return new ConditionEntry(cur.getInt(0), cur.getString(1), 
				cur.getBlob(4), cur.getString(3), cur.getString(2));
	}
	
	/** Fills the tag list with all of the tags in the database.
	 * 
	 */
	public void fetchTags() {
		clearTags();
		Cursor cur = DBManager.fetchTags();
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			ConditionTag tag = cursorToTag(cur);
			tags.add(tag);
			cur.moveToNext();
		}
		cur.close();
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
	public List<ConditionEntry> getConditions () {
		return conditions;
	}
	
	/** Get the current list of ConditionTag objects.
	 * 
	 * @return The current list of ConditionTag objects
	 */
	public List<ConditionTag> getTags() {
		return tags;
	}
}
