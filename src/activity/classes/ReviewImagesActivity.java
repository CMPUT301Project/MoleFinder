package activity.classes;

import java.util.List;

import controller.classes.CameraController;
import model.classes.DatabaseEntry;
import mole.finder.R;

import adapter.classes.MoleFinderArrayAdapter;
import adapter.classes.MoleFinderSpinnerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

/** The ReviewImagesActivity displays a list of available tags
 * in the spinner at the top, and once selected displays a list
 * of conditions that are currently stored in the database with
 * the corresponding tag. 
 * 
 * @author mbessett
 */

public class ReviewImagesActivity extends FActivity {
	// UI items
	private Spinner spinner;
	private ListView list;
	private Button addButton;
	private Button compareButton;
	private Button searchButton;

	// internal variables
	private boolean advancedResults;
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
	private final int SEARCH_CODE = 333;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch(requestCode){
		case CAPTURE_CODE:
			
			if(resultCode == RESULT_OK){

				Intent intentNewImageActivity = new Intent(ReviewImagesActivity.this, NewImageActivity.class);
				intentNewImageActivity.putExtra("imageName", imageName);
				intentNewImageActivity.putExtra("date", date);
				startActivity(intentNewImageActivity);
	
			} break;
		case COMPARE_CODE: 
			break;

		case SEARCH_CODE:
			if (resultCode == RESULT_OK) {
				displayAdvancedSearchResults(intent);
			}
			break;			
		}
	}

	private void displayAdvancedSearchResults(Intent data) {
		setAdvancedResults(true); // skip normal ui update					
		//String tag = data.getStringExtra("TAG");		
		int interval = data.getIntExtra("INTERVAL", 0);
		int displaying = data.getIntExtra("RESULTS", 999);		
		//int pos = data.getIntExtra("POS", 0);
		//setSpinnerPos(pos);
		boolean recentFirst = data.getBooleanExtra("ORDER", true);

		model.fetchSpecificConditions(getTag(), interval, displaying, recentFirst);
		list.setAdapter(new MoleFinderArrayAdapter(this, R.layout.list_view_layout,
				R.id.imageView, R.id.date_text, R.id.tag_text, model.getConditions()));				
	}
	
	/** Save the current spinner position.
	 * 
	 */
	@Override
	public void onPause() {
		setSpinnerPos(spinner.getSelectedItemPosition());
		super.onPause();
	}

	/* Find Spinner, ListView, and Buttons
	 */
	@Override
	protected void findViews() {
		spinner = (Spinner) findViewById(R.id.spinner1);
		
		addButton = (Button) findViewById(R.id.addNewButton);		
		compareButton = (Button) findViewById(R.id.compareButton);
		searchButton = (Button) findViewById(R.id.advancedButton);
		list = (ListView) findViewById(R.id.listView1);	
	}

	/* Allow list and spinner items to be clicked.
	 * 
	 */
	@Override
	protected void setClickListeners() {
		spinner.setOnTouchListener(setupSpinTouch());
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
		compareButton.setOnClickListener(switchActListener(CompareActivity.class));
		searchButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				Intent intent = new Intent(ReviewImagesActivity.this, AdvancedSearchActivity.class);
				intent.putExtra("IN_TAG", getTag());
				setAdvancedResults(true);
				startActivityForResult(intent, SEARCH_CODE);
			}
		});
	}

	private OnClickListener switchActListener(final Class<?> newAct) {
		return new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ReviewImagesActivity.this, newAct);
				startActivity(intent);
			}
		};
	}


	/* Keep the list of tags in the Spinner updated.
	 * Only update the list if not displaying advanced
	 * search results.
	 */
	@Override
	protected void updateView() {
		model.fetchTags();
		List<DatabaseEntry> tagList = model.getTags();
		int currentSize = tagList.size();
		
		if (getSpinnerPos() < currentSize) {
			spinner.setSelection(getSpinnerPos());
		}
		
		if (!isAdvancedResults()) {
			spinner.setAdapter(new MoleFinderSpinnerAdapter(this, tagList));
			updateList();
		}
	}

	/* Hide appropriate UI items, set the next activity,
	 * and initialise state variables.
	 * 
	 */
	@Override
	protected void customInit() {
		camera = new CameraController();
		setTag("");		
		searchButton.setText("Hone List");
		setAdvancedResults(false);

		Class<?> next = (Class<?>) getExtra("FORWARD");
		setForwardView(next);

		if (getExtra("LAYOUT") != null) {
			setLayout(getExtra("LAYOUT").toString());
			addButton.setVisibility(View.GONE);
			compareButton.setVisibility(View.GONE);
		}
		else {
			setLayout("");	
			addButton.setText("Capture New");
			compareButton.setText("Compare");			
		}
	}

	@Override
	protected int myLayout() {
		return R.layout.review;
	}

	/* Setup the OnItemSelectedListener for the Tag Spinner, and
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
				updateList();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		};
		return listener;
	}
	
	/* If the spinner is touched, no longer displaying advanced search results.
	 * Reset the flag.
	 * 
	 * @return An OnTouchListener for the Spinner.
	 */
	private OnTouchListener setupSpinTouch() {
		OnTouchListener listener = new OnTouchListener() {			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				setAdvancedResults(false);
				return false;
			}
		};
		
		return listener;
	}

	/* This uses the activity's tag attribute to create a list
	 * of Condition entries with the same tag
	 *
	 */
	private void updateList() {
		if (getTag() != "") {
			model.fetchConditions(getTag());
			List<DatabaseEntry> conditions = model.getConditions();
			list.setAdapter(new MoleFinderArrayAdapter(this, R.layout.list_view_layout,
					R.id.imageView, R.id.date_text, R.id.tag_text, conditions));
		}
	}

	/* Setup the OnItemSelectedListener for the Condition ListView
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

	/*
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

	public void setAdvancedResults(boolean advancedResults) {
		this.advancedResults = advancedResults;
	}

	public boolean isAdvancedResults() {
		return advancedResults;
	}
}
