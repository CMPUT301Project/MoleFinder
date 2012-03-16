package mole.finder.test;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import mole.finder.DatabaseManager;
import android.test.AndroidTestCase;

/**
 * This is a test class for DatabaseManager, it tests the basic functions
 * of the DatabaseManager class including initialization, and adding and
 * deleting entries in the tags and images tables.
 * @author jletourn
 *
 */

public class DatabaseManagerTester extends AndroidTestCase {
	
	private DatabaseManager DBManager;

	/**
	 * tests that the DatabaseManager object will initialize itself
	 */
	public void testDatabaseManagerConstructor() {
		DBManager = new DatabaseManager(null);
		assertNotNull("BDManager not initialised",DBManager);
	}

	/**
	 * tests that the DatabaseManager can store and fetch images from the database.
	 * this test fails because for some reason the expected and actual images are just a little different
	 */
	public void testCreateAndFetchImageEntry() {
		Context context = getContext();
		DBManager = new DatabaseManager(context);
		DBManager.open();
		String tag = "tag";
		String comments = "comments";
		String date = "01-01-12";
		String image = date.concat(tag);
		DBManager.createTagEntry(tag,comments);
		DBManager.createImageEntry(tag, date, comments, image);
		Cursor images = DBManager.fetchAllImages(tag);
		images.moveToFirst();
		assertNotNull("images Cursor is empty",images);
		String storedImage = images.getString(images.getColumnIndex(DatabaseManager.KEY_IMAGE));
		assertNotNull("The image was not stored in the Database", storedImage);
		assertEquals("The expected and actual images are different",image, storedImage);
	}
	
	/**
	 * tests that the DatabaseManager will store and fetch tags, and that the stored and initial tags are the same
	 */
	public void testCreateAndFetchTags() {
		DBManager = new DatabaseManager(getContext());
		DBManager.open();
		String comments = "comments";
		String tag = "tag";
		DBManager.createTagEntry(tag, comments);
		Cursor tags = DBManager.fetchAllTags();
		assertNotNull("tags Cursor is empty",tags);
		tags.moveToFirst();
		String trueTag = tags.getString(tags.getColumnIndex(DatabaseManager.KEY_TAG));
		String trueComments = tags.getString(tags.getColumnIndex(DatabaseManager.KEY_COMMENTS));
		assertEquals("the expected tag and actual tag are different",tag,trueTag);
		assertEquals("the expected comment and actual comment are different",comments,trueComments);
	}

	/**
	 * tests that the DatabaseManager can delete a specific image
	 */
	public void testDeleteImageEntry() {
		Context context = getContext();
		DBManager = new DatabaseManager(context);
		DBManager.open();
		String tag = "tag";
		String comments = "comments";
		String date = "01-01-12";
		String image = date.concat(tag);
		DBManager.createTagEntry(tag,comments);
		DBManager.createImageEntry(tag, date, comments, image);
		DBManager.deleteAllEntries(tag);
		Cursor images = DBManager.fetchAllImages(tag);
		assertFalse("image was not deleted",images.moveToLast());
	}

	/**
	 * tests that the DatabaseManager can delete all data in the tables
	 */
	public void testDeleteAllEntries() {
		Context context = getContext();
		DBManager = new DatabaseManager(context);
		DBManager.open();
		String tag = "tag";
		String comments = "comments";
		String date = "01-01-12";
		String image = date.concat(tag);
		DBManager.createTagEntry(tag,comments);
		DBManager.createImageEntry(tag, date, comments, image);
		DBManager.deleteAllEntries(tag);
		Cursor images = DBManager.fetchAllImages(tag);
		Cursor tags = DBManager.fetchAllTags();
		assertFalse("image was not deleted",images.moveToLast());
		assertFalse("tag was not deleted", tags.moveToLast());
	}

}
