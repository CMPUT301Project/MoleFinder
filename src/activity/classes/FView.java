package activity.classes;

import model.classes.DatabaseManager;
import android.app.Activity;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;


/** The FView abstract class provides the basic functionality
 * and interface for a generic Activity for the MoleFinder
 * application.
 * 
 * @author mbessett
 *
 */
public abstract class FView extends Activity {
	protected DatabaseManager DBManager;
	
	/** Create a DatabaseManager, link UI elements to their IDs,
	 * allow for clickable UI items, and update the information.
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setViewContent();
		
		DBManager = new DatabaseManager(getBaseContext());
		findViews();
		customInit(); 
		setClickListeners();
		updateView();
	}
	
	/** Keep the data on the page consistent with the database tables.
	 * 
	 */
	@Override
	public void onResume() {
		super.onResume();
		updateView();
	}
	
	/** Link references to gui items.
	 * 
	 */
	protected abstract void findViews();
	
	/** Apply on click listeners to the views that require them.
	 * 
	 */
	protected abstract void setClickListeners();
	
	/** Perform operations necessary to 
	 * 
	 */
	protected abstract void updateView();
	
	/** 
	 * 
	 */
	protected abstract void customInit();
	
	
	/** Retrieve extras passed to the view. User must explicitly
	 * cast before use.
	 * 
	 * @param key The String value denoting the extra
	 * @return The extra, or null if none exists
	 */
	protected Object getExtra(String key) {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			return extras.get(key);
		}
		return null;
	}
}
