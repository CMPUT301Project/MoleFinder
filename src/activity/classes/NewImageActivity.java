package activity.classes;
import model.classes.DatabaseManager;
import mole.finder.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

/**
 * This is the NewImageActivity class that is called when a new image is saved.
 * The user is asked to input a tag name and optional comments to be stored with the image.
 *  //To-Do - Stop user from pressing save when tag is null.
 * @author jletourn
 *
 */

public class NewImageActivity extends Activity{
	
	private Button buttonSave;
	private Button buttonCancel;
	private Button buttonNewTag;
	private Spinner spinnerTag;
	private EditText edittextComments;
	private String Tag;
	private String imageName;
	private String Date;
	private String Comments;
	private DatabaseManager DBManager;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newphoto); 
        
        //create and open the Database
		DBManager = new DatabaseManager(getBaseContext());
		DBManager.open();
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		//get the saved image name and date
		imageName = extras.getString("imageName");
		Date = extras.getString("date");
        
		spinnerTag = (Spinner) findViewById(R.id.spinnerTag);
		spinnerTag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

				Cursor tags = (Cursor)(parent.getItemAtPosition(pos));
				Tag = tags.getString(tags.getColumnIndex(DatabaseManager.KEY_TAG));
				fillSpinner(); //populates spinner
			}
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		buttonNewTag = (Button) findViewById(R.id.buttonNewTag);
		buttonNewTag.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				Intent i = new Intent(NewImageActivity.this, NewTagActivity.class);
				startActivity(i);
				fillSpinner();
			}
		});
		
		edittextComments = (EditText) findViewById(R.id.editTextComments);
		Comments = edittextComments.getText().toString();
		
		buttonSave = (Button) findViewById(R.id.buttonSave);
		buttonSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				DBManager.createImageEntry(Tag, Date, Comments, imageName);
				DBManager.close();
				finish();
			}
		});
		
		buttonCancel = (Button) findViewById(R.id.buttonCancel);
		buttonCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DBManager.close();
				finish();
			}
		});
		
    }
    

    /**
     * Uses a cursor to populate the tag spinner
     */
    private void fillSpinner() {
    	// get tags
    	Cursor tagCursor = DBManager.fetchAllTags();
    	startManagingCursor(tagCursor);

    	// bind values
    	String[] from = new String[] { DatabaseManager.KEY_TAG };
    	int[] to = new int[] { android.R.id.text1 };

    	// set adapter
    	SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this,
    	    android.R.layout.simple_spinner_item, tagCursor, from, to);
    	mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spinnerTag.setAdapter(mAdapter);
    	}
    
}
