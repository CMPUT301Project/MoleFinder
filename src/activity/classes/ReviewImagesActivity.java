package activity.classes;

import mole.finder.R;

import adapter.classes.MoleFinderArrayAdapter;
import adapter.classes.MoleFinderSpinnerAdapter;
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
 * the corresponding tag. 
 * 
 * @author mbessett
 *
 */

public class ReviewImagesActivity extends FActivity {
	private Spinner spinner;
	private int spinnerPos;
	private ListView list;
	private String tag;	
	
	private final String PREFS = "ReviewImagesSpinnerPos";
	private final String S_KEY = "pos";

	/** Preserve last spinner position.
	 * 
	 */
	@Override
	public void onResume() {
		super.onResume();
		//spinner.setSelection(getSpinnerPos());
		readState(ReviewImagesActivity.this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
        if (!saveState(ReviewImagesActivity.this)) {
            Toast.makeText(this,
                    "Failed to write state!", Toast.LENGTH_LONG).show();
         }
	}
	
	private boolean saveState(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(this.PREFS, MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(this.S_KEY, getSpinnerPos());
        return editor.commit();
	}
	
	private boolean readState(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(this.PREFS, MODE_WORLD_READABLE);
		setSpinnerPos(prefs.getInt(this.S_KEY, 0));
		return (prefs.contains(this.S_KEY));
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
	}

	/** Do not display list of condition entries on creation.
	 * 
	 */
	@Override
	protected void customInit() {
		setTag("");
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
				//Cursor cur = (Cursor) parent.getItemAtPosition(pos);
				//int next = cur.getInt(cur.getColumnIndex(DatabaseManager.KEY_ROWID));
				Intent intent = new Intent(ReviewImagesActivity.this, ImageActivity.class);
				//intent.putExtra("ID", next);
				startActivity(intent);
			}
		};
		return listener;
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
}
