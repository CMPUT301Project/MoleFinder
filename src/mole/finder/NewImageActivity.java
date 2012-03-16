package mole.finder;
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
		DBManager = new DatabaseManager(getBaseContext());
		DBManager.open();
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		imageName = extras.getString("imageName");
		Date = extras.getString("date");
        
		spinnerTag = (Spinner) findViewById(R.id.spinnerTag);
		spinnerTag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

				Cursor tags = (Cursor)(parent.getItemAtPosition(pos));
				Tag = tags.getString(tags.getColumnIndex("vehicle2"));
				fillSpinner();
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
			}
		});
		
		buttonCancel = (Button) findViewById(R.id.buttonCancel);
		buttonCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
    }
    
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
