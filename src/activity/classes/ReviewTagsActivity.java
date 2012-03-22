package activity.classes;

import model.classes.DatabaseManager;
import mole.finder.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/** The ReviewTagsActivity class displays a clickable list
 * of tags currently in the database. Selecting one directs
 * the user to the Activity where they may edit or delete it.
 * 
 * @author mbessett
 *
 */

public class ReviewTagsActivity extends Activity {
	private DatabaseManager DBManager;
	private ListView list;
	private Spinner spinner;

	/** Called when the view is created.
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review); 

		// open the database
		DBManager = new DatabaseManager(getBaseContext());
		

		// ui
		list = (ListView) findViewById(R.id.listView1);
		spinner = (Spinner) findViewById(R.id.spinner1);
		spinner.setVisibility(View.GONE); 
		
		// fill list
		DBManager.open();
		updateList();
		DBManager.close();
		
		// listen for clicks
		list.setOnItemClickListener(setupListListener());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		DBManager.open();
		updateList();
		DBManager.close();
	}

	/** This uses the activity's tag attribute to create a list
	 * of Condition entries with the same tag
	 *
	 */
	private void updateList() {
		// Get all of the rows from the database and create the item list
		Cursor tagCursor = DBManager.fetchAllTags();
		startManagingCursor(tagCursor);

		// Create an array to specify the fields we want to display in the list
		String[] from = new String[] { DatabaseManager.KEY_TAG, DatabaseManager.KEY_COMMENTS };

		// and an array of the fields we want to bind those fields to
		int[] to = new int[] { R.id.date_text, R.id.tag_text };

		// Now create a simple cursor adapter and set it to display
		SimpleCursorAdapter tagEntry = new SimpleCursorAdapter(this, 
				R.layout.list_view_layout, tagCursor, from, to);
		list.setAdapter(tagEntry);
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
				Intent i = new Intent(ReviewTagsActivity.this, NewTagActivity.class);
				i.putExtra("ID", next);
				startActivity(i);
			}
		};
		return listener;
	}
}
