package activity.classes;

import model.classes.DatabaseEntry;
import mole.finder.R;

import adapter.classes.MoleFinderArrayAdapter;
import adapter.classes.MoleFinderSpinnerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.content.SharedPreferences;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

/** The ReviewImagesActivity displays a list of available tags
 * in the spinner at the top, and once selected displays a list
 * of conditions that are currently stored in the database with
 * the corresponding tag. Be sure to include the "FORWARD" extra
 * when calling this view or you will not be able to link to the 
 * next page and you may get exceptions. 
 * 
 * @author mbessett
 *
 */

public class ReviewImagesActivity extends FActivity {
	// UI items
	private Spinner spinner;
	private ListView list;

	// internal variables
	private int spinnerPos;	
	private String tag;
	private String layout;
	private Class<?> forwardView;	// this is the screen to link to
	// use the key "FORWARD"

	// fixed values
	private final String PREFS = "ReviewImagesSpinnerPos";
	private final String S_KEY = "pos";

	@Override
	public void onPause() {
		setSpinnerPos(spinner.getSelectedItemPosition());
		super.onPause();
	}

	/** Find Spinner and ListView.    
	 * Get the position and value of the spinner from the file, or a default value if the
	 * key-value pair does not exist.
	 */
	@Override
	protected void findViews() {
		spinner = (Spinner) findViewById(R.id.spinner1);
		list = (ListView) findViewById(R.id.listView1);	
	}

	/** Allow list and spinner items to be clicked.
	 * 
	 */
	@Override
	protected void setClickListeners() {
		spinner.setOnItemSelectedListener(setupSpinListener());
		list.setOnItemClickListener(setupListListener());
	}

	/** Keep the list of tags in the Spinner updated.
	 * 
	 */
	@Override
	protected void updateView() {
		model.fetchTags();
		spinner.setAdapter(new MoleFinderSpinnerAdapter(this, model.getTags()));
		spinner.setSelection(getSpinnerPos());
	}

	/** Do not display list of condition entries on creation.
	 * Also setup which class to return to after selection.
	 * 
	 */
	@Override
	protected void customInit() {
		setTag("");

		Class<?> next = (Class<?>) getExtra("FORWARD");
		setForwardView(next);

		if (getExtra("LAYOUT") != null) {
			setLayout(getExtra("LAYOUT").toString());
		}
		else {
			setLayout("");
		}
	}

	@Override
	protected int myLayout() {
		return R.layout.review;
	}

	/** Setup the OnItemSelectedListener for the Tag Spinner, and
	 * remember the Spinner position.
	 * 
	 */
	private OnItemSelectedListener setupSpinListener() {
		// allow for clicks
		OnItemSelectedListener listener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int pos, long row) {
				// grab item
				Object tag = parent.getItemAtPosition(pos);				
				// set attributes
				setTag(tag.toString());
				setSpinnerPos(pos);
				// only need to update the list when new tag selected
				updateList();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		};
		return listener;
	}

	/** This uses the activity's tag attribute to create a list
	 * of Condition entries with the same tag
	 *
	 */
	private void updateList() {
		if (getTag() != "") {
			model.fetchConditions(getTag());
			list.setAdapter(new MoleFinderArrayAdapter(this, R.layout.list_view_layout,
					R.id.imageView, R.id.date_text, R.id.tag_text, model.getConditions()));
		}
	}

	/** Setup the OnItemSelectedListener for the Condition ListView
	 * 
	 */
	// TODO When image is clicked on display the image larger
	private OnItemClickListener setupListListener() {
		// allow for clicks
		OnItemClickListener listener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long row) {
				// get entry from list
				DatabaseEntry entry = (DatabaseEntry) parent.getItemAtPosition(pos);
				int id = entry.getId();
				// send to next page
				nextView(id);
			}
		};
		return listener;
	}

	private void nextView(int id) {
		Intent intent = new Intent(ReviewImagesActivity.this, getForwardView());
		intent.putExtra("ID", id);
		String layout = getLayout();
		if (layout.equals("top") || layout.equals("bottom")) {
			intent.putExtra("LAYOUT", layout);
			setResult(Activity.RESULT_OK, intent);
			this.finish();
		}
		else {
			startActivity(intent);
		}
	}

	// getters/setters
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getSpinnerPos() {
		return spinnerPos;
	}

	public void setSpinnerPos(int spinnerPos) {
		this.spinnerPos = spinnerPos;
	}

	public void setForwardView(Class<?> forwardView) {
		this.forwardView = forwardView;
	}

	public Class<?> getForwardView() {
		return forwardView;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public String getLayout() {
		return layout;
	}
}
