package activity.classes;

import model.classes.MoleFinderModel;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


/** The FView abstract class provides the basic functionality
 * and interface for a generic Activity for the MoleFinder
 * application.
 * 
 * @author mbessett
 *
 */
public abstract class FActivity extends Activity {
	protected MoleFinderModel model;
	
	/** Create a DatabaseManager, link UI elements to their IDs,
	 * allow custom initialization for the derived classes,
	 * allow for clickable UI items, and update the information.
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(myLayout());
		
		model = MoleFinderModel.getInstance(getBaseContext());
		findViews();
		customInit();
		updateView();
		setClickListeners();
		
	}
	
	/** Keep the data on the page consistent with the database tables.
	 * 
	 */
	@Override
	public void onResume() {
		super.onResume();
		updateView();
	}

	/** Link references to UI elements.
	 * 
	 */
	protected abstract void findViews();
	
	/** Apply on click listeners to the views that require them.
	 * 
	 */
	protected abstract void setClickListeners();
	
	/** Perform operations necessary to keep the information in
	 * the activity consistent with the internal storage.
	 * 
	 */
	protected abstract void updateView();
	
	/** Some classes may need initialization in a particular
	 * order. This method provides the ability to do that.
	 * 
	 */
	protected abstract void customInit();
	
	/** Get the layout id for this object's XML layout file.
	 * 
	 */
	protected abstract int myLayout();
	
	
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
