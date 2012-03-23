package activity.classes;


import model.classes.DatabaseManager;
import mole.finder.R;
import adapter.classes.MoleFinderArrayAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;

/** The ReviewTagsActivity class displays a clickable list
 * of tags currently in the database. Selecting one directs
 * the user to the Activity where they may edit or delete it.
 * 
 * @author mbessett
 *
 */

public class ReviewTagsActivity extends FActivity {
	private Spinner spinner;
	private ListView list;

	/** Link the Spinner and ListView to their respective ids.
	 * 
	 */
	@Override
	protected void findViews() {
		list = (ListView) findViewById(R.id.listView1);
		spinner = (Spinner) findViewById(R.id.spinner1);		
	}

	/** Make the ListView clickable.
	 * 
	 */
	@Override
	protected void setClickListeners() {
		list.setOnItemClickListener(setupListListener());		
	}

	/** Keep the list of tags consistent with the database.
	 * 
	 */
	@Override
	protected void updateView() {
		model.fetchTags();
		list.setAdapter(new MoleFinderArrayAdapter(this, R.layout.list_view_layout,
				R.id.date_text, R.id.tag_text, model.getTags()));
	}

	/** Hide the spinner. 
	 * 
	 */
	@Override
	protected void customInit() {
		spinner.setVisibility(View.GONE);
	}

	@Override
	protected int myLayout() {
		return R.layout.review;
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
				Intent intent = new Intent(ReviewTagsActivity.this, NewTagActivity.class);
				intent.putExtra("ID", next);
				startActivity(intent);
			}
		};
		return listener;
	}
}
