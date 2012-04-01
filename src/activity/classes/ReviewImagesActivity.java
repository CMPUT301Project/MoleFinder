package activity.classes;

import controller.classes.CameraController;
import model.classes.DatabaseEntry;
import mole.finder.R;

import adapter.classes.MoleFinderArrayAdapter;
import adapter.classes.MoleFinderSpinnerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

/** The ReviewImagesActivity displays a list of available tags
 * in the spinner at the top, and once selected displays a list
 * of conditions that are currently stored in the database with
 * the corresponding tag. Be sure to include the "FORWARD" extra
 * when calling this view or you will not be able to link to the 
 * next page and you may get exceptions. 
 * 
 * @author mbessett
 */

public class ReviewImagesActivity extends FActivity {
	// UI items
	private Spinner spinner;
	private ListView list;
	private Button addButton;
	private Button compareButton;

	// internal variables
	private int spinnerPos;	
	private String tag;
	private String layout;
	private Class<?> forwardView;	// this is the screen to link to
									// use the key "FORWARD"
	private String imageName;
	private String date;
	private CameraController camera;

	// fixed values
	private final int CAPTURE_CODE = 111;
	private final int COMPARE_CODE = 222;

	/** Find Spinner and ListView.    
	 * Get the position and value of the spinner from the file, or a default value if the
	 * key-value pair does not exist.
	 */
	@Override
	protected void findViews() {
		spinner = (Spinner) findViewById(R.id.spinner1);
		list = (ListView) findViewById(R.id.listView1);	
		addButton = (Button) findViewById(R.id.addNewButton);
		addButton.setText("Capture New Image");
		compareButton = (Button) findViewById(R.id.compareButton);
		compareButton.setText("Compare Two Images");
	}

	/** Allow list and spinner items to be clicked.
	 * 
	 */
	@Override
	protected void setClickListeners() {
		spinner.setOnItemSelectedListener(setupSpinListener());
		list.setOnItemClickListener(setupListListener());
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = camera.takeAPhoto(getBaseContext());
		    	Bundle extras = intent.getExtras();
		    	imageName = extras.getString("imageName");
		    	date = extras.getString("date");
				startActivityForResult(intent,CAPTURE_CODE);
			}
		});
		compareButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ReviewImagesActivity.this, CompareActivity.class);
				startActivity(intent);
			}
		});
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
		camera = new CameraController();
		setTag("");

		Class<?> next = (Class<?>) getExtra("FORWARD");
		setForwardView(next);

		if (getExtra("LAYOUT") != null) {
			setLayout(getExtra("LAYOUT").toString());
			addButton.setVisibility(View.GONE);
			compareButton.setVisibility(View.GONE);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch(requestCode){
		case CAPTURE_CODE:
			
			if(resultCode== RESULT_OK){

				Intent intentNewImageActivity = new Intent(ReviewImagesActivity.this, NewImageActivity.class);
				intentNewImageActivity.putExtra("imageName", imageName);
				intentNewImageActivity.putExtra("date", date);
				startActivity(intentNewImageActivity);
	
			} break;
		case COMPARE_CODE: 
			break;
		}
	}
	
	@Override
	public void onPause() {
		setSpinnerPos(spinner.getSelectedItemPosition());
		super.onPause();
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

	/**
	 * nextView is used to set the layout to the next view and start the next activity.
	 * @param id of the item selected in the ListView
	 */
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
