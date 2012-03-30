package activity.classes;
import model.classes.ConditionEntry;
import model.classes.ConditionTag;
import model.classes.DatabaseManager;
import mole.finder.R;
import adapter.classes.MoleFinderArrayAdapter;
import adapter.classes.MoleFinderSpinnerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * This is the NewImageActivity class that is called when a new image is saved.
 * The user is asked to input a tag name and optional comments to be stored with the image.
 *  //To-Do - Stop user from pressing save when tag is null.
 * @author jletourn
 *
 */

public class NewImageActivity extends FActivity{
	
	private Button buttonSave;
	private Button buttonCancel;
	private Button buttonNewTag;
	private Spinner spinnerTag;
	private EditText edittextComments;
	private String tag;
	private String imageName;
	private String date;
	private String comments;
	private int spinnerPos;
	private ConditionEntry initImage;
	private Object tags;
    
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
				tags = parent.getItemAtPosition(pos);
				// set attributes
				setTag(tags.toString());
				setSpinnerPos(pos);
		        // only need to update the list when new tag selected
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		};
		return listener;
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


	/** Find Spinner and ListView.    
	 * Get the position and value of the spinner from the file, or a default value if the
	 * key-value pair does not exist.
	 */
	@Override
	protected void findViews() {
		edittextComments = (EditText) findViewById(R.id.editTextComments);
		spinnerTag = (Spinner) findViewById(R.id.spinnerTag);
		buttonSave = (Button) findViewById(R.id.buttonSave);
		buttonCancel = (Button) findViewById(R.id.buttonCancel);
		buttonNewTag = (Button) findViewById(R.id.buttonNewTag);
	}

	/** Keep the list of tags in the Spinner updated.
	 * 
	 */
	@Override
	protected void updateView() {
		model.fetchTags();
		spinnerTag.setAdapter(new MoleFinderSpinnerAdapter(this, model.getTags()));		
		if (getExtra("ID") != null) {
			long id = Long.parseLong(getExtra("ID").toString());
			initImage = model.getOneEntry(id);
			tag = initImage.getTag();
			edittextComments.setText(initImage.getComment());
		}
	}

	@Override
	protected void setClickListeners() {

		buttonCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		buttonSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				saveImage();
				finish();
			}
		});
		
		buttonNewTag.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				Intent i = new Intent(NewImageActivity.this, NewTagActivity.class);
				startActivity(i);
			}
		});
		
		spinnerTag.setOnItemSelectedListener(setupSpinListener());
		
		
	}

	/** Do not display list of condition entries on creation.
	 * 
	 */
	@Override
	protected void customInit() {
		initImage = ConditionEntry.createDummyEntry();
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		//get the saved image name and date
		imageName = extras.getString("imageName");
		date = extras.getString("date");
	}


	@Override
	protected int myLayout() {
		return R.layout.newphoto;
	}
	
	/** Save the current tag to the database 
	 * 
	 */
	private void saveImage() {
		ConditionEntry nowImage = new ConditionEntry(initImage);
		String curName = tags.toString();
		nowImage.setImage(imageName);
		nowImage.setTag(curName);
		nowImage.setDate(date);
		nowImage.setComment(edittextComments.getText().toString());
		// disallow empty names
		if (curName.equals("")) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Tag name cannot be empty.");
			builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			builder.show();
			return;
		}
		// check for repeat data
		int status = initImage.compareTo(nowImage);
		switch (status) {
		case ConditionTag.IDENTICAL:
			break;
		case ConditionEntry.DUMMY_ID:
			model.saveImage(nowImage); break;
		case ConditionEntry.DIFF_COMMENT:
			model.overwriteImage(nowImage); break;
		}
		finish();
	}
    
}
