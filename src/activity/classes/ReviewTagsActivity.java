package activity.classes;


import model.classes.DatabaseEntry;
import mole.finder.R;
import adapter.classes.MoleFinderArrayAdapter;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;

/** The ReviewTagsActivity class displays a clickable list
 * of tags currently in the database. Selecting one directs
 * the user to the Activity where they may edit or delete it.
 * 
 * @author mbessett
 */

public class ReviewTagsActivity extends FActivity {
	// UI
	private Spinner spinner;
	private ListView list;
	private Button addButton;
	private Button compareButton;
	private Button searchButton;

	/* Link the Spinner and ListView to their respective ids.
	 * 
	 */
	@Override
	protected void findViews() {
		list = (ListView) findViewById(R.id.listView1);
		spinner = (Spinner) findViewById(R.id.spinner1);
		addButton = (Button) findViewById(R.id.addNewButton);		
		compareButton = (Button) findViewById(R.id.compareButton);
		searchButton = (Button) findViewById(R.id.advancedButton);
	}

	/* Make the ListView and Button clickable.
	 * 
	 */
	@Override
	protected void setClickListeners() {
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ReviewTagsActivity.this, NewTagActivity.class);
				startActivity(intent);
			}
		});
		list.setOnItemClickListener(setupListListener());		
	}

	/* Keep the list of tags consistent with the database.
	 * 
	 */
	@Override
	protected void updateView() {
		model.fetchTags();
		list.setAdapter(new MoleFinderArrayAdapter(this, R.layout.tag_row_layout,
				R.id.tag_text, R.id.comments_text, model.getTags()));
	}

	/* Hide the spinner and align the list to the top. 
	 * 
	 */
	@Override
	protected void customInit() {
		addButton.setText("Create New Tag");
		compareButton.setVisibility(View.GONE);
		searchButton.setVisibility(View.GONE);
		spinner.setVisibility(View.GONE);
		
		LayoutParams params = (LayoutParams) list.getLayoutParams();
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		list.setLayoutParams(params); //causes layout update

	}

	@Override
	protected int myLayout() {
		return R.layout.review;
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
				DatabaseEntry entry = (DatabaseEntry) parent.getItemAtPosition(pos);
				Intent intent = new Intent(ReviewTagsActivity.this, NewTagActivity.class);
				intent.putExtra("ID", entry.getId());
				startActivity(intent);
			}
		};
		return listener;
	}
}
