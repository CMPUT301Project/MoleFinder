package activity.classes;
import java.io.File;

import model.classes.ConditionEntry;
import model.classes.ConditionTag;
import mole.finder.R;
import adapter.classes.MoleFinderSpinnerAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * This activity is used to save images and call the camera activities.
 * 
 * @author jletourn
 *
 */

public class NewImageActivity extends FActivity{
	// UI
	private Button buttonSave;
	private Button buttonCancel;
	private Button buttonNewTag;
	private Spinner spinnerTag;
	private EditText edittextComments;
	// internal values
	private String imageName;
	private String date;
	private ConditionEntry initImage;
	private Object tags;
	private Boolean newTag;

	@Override
	protected void findViews() {
		edittextComments = (EditText) findViewById(R.id.editTextComments);
		spinnerTag = (Spinner) findViewById(R.id.spinnerTag);
		buttonSave = (Button) findViewById(R.id.buttonSave);
		buttonCancel = (Button) findViewById(R.id.buttonCancel);
		buttonNewTag = (Button) findViewById(R.id.buttonNewTag);
	}

	@Override
	protected void updateView() {
		model.fetchTags();
		spinnerTag.setAdapter(new MoleFinderSpinnerAdapter(this, model.getTags()));		
		if (getExtra("ID") != null) {
			long id = Long.parseLong(getExtra("ID").toString());
			initImage = model.getOneEntry(id);
			edittextComments.setText(initImage.getComment());
		}
	}

	@Override
	protected void setClickListeners() {

		buttonCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (getExtra("ID") == null) {
					File imgFile = new File("/sdcard/MoleFinderPics/" + imageName + ".jpg");
					imgFile.delete();
				}
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
				newTag=true;
				Intent intent = new Intent(buttonNewTag.getContext(), NewTagActivity.class);
				startActivity(intent);
			}
		});

		spinnerTag.setOnItemSelectedListener(setupSpinListener());


	}

	@Override
	protected void customInit() {
		initImage = ConditionEntry.createDummyEntry();
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		//get the saved image name and date
		imageName = extras.getString("imageName");
		date = extras.getString("date");
		newTag=false;
	}

	@Override
	protected int myLayout() {
		return R.layout.newphoto;
	}
	
	/** Set the spinner to select a newly created tag if
	 * it exists, else default to the first tag in the list. 
	 * 
	 */
	@Override
	public void onResume(){
		super.onResume();
		if(newTag){
			spinnerTag.setSelection(spinnerTag.getCount()-1);
		}
		else
			spinnerTag.setSelection(0);
	}

	/* Save the current image to the database 
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
	
	/* Setup the OnItemSelectedListener for the Tag Spinner
	 */
	private OnItemSelectedListener setupSpinListener() {
		OnItemSelectedListener listener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int pos, long row) {
				tags = parent.getItemAtPosition(pos);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		};
		return listener;
	}
}
