package activity.classes;

import model.classes.DatabaseManager;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

/** An extension 
 * 
 * @author mbessett
 *
 */
public abstract class SpinnerView extends FView {
	protected Spinner spinner;
	
	/** Load all the tags currently in the database and display them
	 * in the Spinner for the user to select.
	 * 
	 */
	protected void fillSpinner() {
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
}
