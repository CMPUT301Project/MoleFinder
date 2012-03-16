package mole.finder;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

/** The ReviewImagesActivity displays a list of available tags
 * in the spinner at the top, and once selected displays a list
 * of conditions that are currently stored in the database with
 * the corresponding tag. 
 * 
 * @author mbessett
 *
 */

public class ReviewImagesActivity extends Activity {
	private DatabaseManager DBManager;
	private Spinner spinner;
	private int spinnerPos;
	private ListView list;
	private String tag;	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// init view
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review); 

		// init database
		DBManager = new DatabaseManager(getBaseContext());
		DBManager.open();

		// populate spinner on load
		spinner = (Spinner) findViewById(R.id.spinner1);
		list = (ListView) findViewById(R.id.listView1);		
		
		// random db objects
		randomEntries();

		// all tags into spinner
		fillSpinner();
		// listen for clicks
		spinner.setOnItemSelectedListener(setupSpinListener());
		list.setOnItemClickListener(setupListListener());
		
		DBManager.close();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//randomEntries();
		DBManager.open();
		fillSpinner();
		spinner.setSelection(spinnerPos);
		DBManager.close();
	}
	
	private void randomEntries() {
		// temporary test objects		
		DBManager.deleteAllEntries("Face");
		DBManager.deleteAllEntries("Foot");
		DBManager.createTagEntry("Face", "This is your face.");
		String today = DateFormat.getDateInstance().format(new Date());
		String img = "img";
		DBManager.createImageEntry("Face", today, "Look at that thing on your face", img);
		DBManager.createTagEntry("Foot", "This is your foot. You only get one.");
		DBManager.createImageEntry("Foot", today, "You only have one foot?", img);
	}

	/** This uses the activity's tag attribute to create a list
	 * of Condition entries with the same tag
	 *
	 */
	private void updateList() {
		// no list for no tag
		if (getTag() != null) {
			// Get all of the rows from the database and create the item list
			DBManager.open();
			Cursor imageCursor = DBManager.fetchAllImages(getTag());			
			startManagingCursor(imageCursor);

			// Create an array to specify the fields we want to display in the list
			String[] from = new String[] { DatabaseManager.KEY_DATE, DatabaseManager.KEY_TAG };

			// and an array of the fields we want to bind those fields to
			int[] to = new int[] { R.id.date_text, R.id.tag_text };

			// Now create a simple cursor adapter and set it to display
			SimpleCursorAdapter imageEntry = new SimpleCursorAdapter(this, 
					R.layout.list_view_layout, imageCursor, from, to);
			list.setAdapter(imageEntry);
			DBManager.close();
		}
	}

	/** Load all the tags currently in the database and display them
	 * in the Spinner for the user to select.
	 * 
	 */
	private void fillSpinner() {
		// get the tags from the database
		Cursor tagCursor = DBManager.fetchAllTags();
		startManagingCursor(tagCursor);

		// bind values
		String[] from = new String[] { DatabaseManager.KEY_TAG };
		int[] to = new int[] { android.R.id.text1 };

		// set adapter
		SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this, 
				android.R.layout.simple_spinner_item, tagCursor, from, to);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(mAdapter); 
	}

	/** Setup the OnItemSelectedListener for the Tag Spinner
	 * 
	 */
	private OnItemSelectedListener setupSpinListener() {
		// allow for clicks
		OnItemSelectedListener listener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int pos, long row) {
				// grab item
				Cursor cur = (Cursor) parent.getItemAtPosition(pos);
				spinnerPos = pos;
				// set attributes
				setTag(cur.getString(cur.getColumnIndex(DatabaseManager.KEY_TAG)));
		        // only need to update the list when new tag selected
		        updateList();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// do nothing				
			}
		};
		return listener;
	}
	
	/** Setup the OnItemSelectedListener for the Condition ListView
	 * 
	 */
	private OnItemClickListener setupListListener() {
		// allow for clicks
		OnItemClickListener listener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long row) {
				Cursor cur = (Cursor) parent.getItemAtPosition(pos);
				int next = cur.getInt(cur.getColumnIndex(DatabaseManager.KEY_ROWID));
				Intent i = new Intent(ReviewImagesActivity.this, SaveImageActivity.class);
				i.putExtra("ID", next);
				startActivity(i);
			}
		};
		return listener;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
