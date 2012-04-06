package mole.finder.test;

import java.util.List;

import android.test.AndroidTestCase;
import model.classes.ConditionEntry;
import model.classes.ConditionTag;
import model.classes.ConditionUser;
import model.classes.DatabaseEntry;
import model.classes.MoleFinderModel;

public class MoleFinderModelTest extends AndroidTestCase {
	
	private MoleFinderModel model;
	
	/**
	 * tests that the DatabaseManager object will initialize itself
	 */
	public void testDatabaseManagerConstructor() {
		model = MoleFinderModel.getInstance(getContext());
		assertNotNull("BDManager not initialised",model);
	}
	
	/** Store a new ConditionTag in the database.
	 * 
	 * @param tag The tag to save to the database.
	 */
	public void testsaveAndFetchTagTest() {

		model = MoleFinderModel.getInstance(getContext());
		ConditionTag tag = new ConditionTag(1,"tag","comment");
		model.saveTag(tag);
		model.fetchTags();
		List<DatabaseEntry> list = model.getTags();
		DatabaseEntry tagEntry = list.get(0);
		assertNotNull(tag);
		assertEquals("The tag is not equal", "tag", tagEntry.getTitle());
	}
	
	/** Store a new ConditionEntry in the database.
	 * 
	 * @param ConditionEntry object.
	 */
	public void testAsaveAndFetchImageTest() {

		model = MoleFinderModel.getInstance(getContext());
		ConditionEntry entry = new ConditionEntry(1,"tag","image", "comment", "date");
		model.saveImage(entry);
		ConditionEntry imageEntry = model.getOneEntry(1);
		assertNotNull(imageEntry);
		assertEquals("The image is not equal", "image", imageEntry.getImage());
	}
	
	/** Store a new ConditionUser in the database.
	 * 
	 * @param ConditionUser object.
	 */
	public void testsaveUserTest() {

		model = MoleFinderModel.getInstance(getContext());
		ConditionUser user = new ConditionUser(1,"password","role", "username");
		model.saveUser(user);
		ConditionUser userEntry = model.getUser();
		assertNotNull(userEntry);
		assertEquals("The username is not equal", "password", userEntry.getPassword());
	}
	
	public void testDeleteTag(){
		model = MoleFinderModel.getInstance(getContext());
		model.clearTags();
		ConditionTag tag = new ConditionTag(1,"tag","comment");
		model.saveTag(tag);
		model.fetchTags();
		List<DatabaseEntry> list = model.getTags();
		int sizeBefore = list.size();
		assertNotNull(sizeBefore);
		model.deleteTag(tag);
		model.fetchTags();
		List<DatabaseEntry> tagList = model.getTags();
		int sizeAfter= tagList.size();
		assertNotNull(sizeAfter);
		assertEquals("The tag was not deleted", sizeBefore-1,sizeAfter);
		
	}
	
	public void testDeleteEntry(){
		model = MoleFinderModel.getInstance(getContext());
		model.clearConditions();
		ConditionEntry entry = new ConditionEntry(1,"tag","image", "comment", "date");
		model.saveImage(entry);
		model.fetchConditions("tag");
		List<DatabaseEntry> imageEntry = model.getConditions();
		int sizeBefore = imageEntry.size();
		assertNotNull(sizeBefore);
		model.deleteImage(entry);
		model.fetchConditions("tag");
		List<DatabaseEntry> imageEntries = model.getConditions();
		int sizeAfter = imageEntries.size();
		assertNotNull(sizeAfter);
		assertEquals("the image was not deleted", sizeBefore-1, sizeAfter);
	}
	
	

}
