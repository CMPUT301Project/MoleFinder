package activity.classes;

import model.classes.DatabaseManager;
import mole.finder.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/** NewTagActivity creates or overwrites a tag stored in 
 * the database.
 * 
 * @author mbessett
 *
 */

public class NewTagActivity extends Activity { 
	private EditText name;
	private EditText comment;
	private Button save;
	private Button cancel; 
	private Button delete;
	private String initName;
	private String initComment;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtag); 
		
		initName = "";
		initComment = "";

		findViews();
		setEditText();
		setClicks();
	}

	/** Find all the the UI elements and initialise them.
	 * 
	 */
	private void findViews() {
		name = (EditText) findViewById(R.id.editTagName);
		comment = (EditText) findViewById(R.id.editTagComments);
		save = (Button) findViewById(R.id.buttonTagSave);
		cancel = (Button) findViewById(R.id.buttonTagCancel);
		delete = (Button) findViewById(R.id.buttonTagDelete);
	}
	
	/** Set the OnClickListeners for the Buttons 
	 * 
	 */
	private void setClicks() {
		save.setOnClickListener(new View.OnClickListener() {	
			public void onClick(View v) {
				saveTag();				
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {	
			public void onClick(View v) {
				NewTagActivity.this.finish();			
			}
		});
		if (initName.equals("")) {
			delete.setVisibility(View.GONE);
		}
		else {
			delete.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(NewTagActivity.this);
					builder.setMessage("This operation will delete any and all condition " +
							"entries with this tag. Are you sure you wish to continue?")
							.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									//DBManager.deleteAllEntries(initName);
									NewTagActivity.this.finish();
								}
							})
							.setNegativeButton("No", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							})
							.show();			
				}
			});
		}
	}

	/** Save the current tag to the database 
	 * 
	 */
	private void saveTag() {
		String nowName = name.getText().toString();
		String nowComment = comment.getText().toString();
		// disallow empty names
		if (nowName.equals("")) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Tag name cannot be empty.")
			.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			})
			.show();
			return;
		}
		// check repeated data
		else if (nowName.equals(initName)) {
			// nothing has changed, nothing to do
			if (nowComment.equals(initComment)) {
				NewTagActivity.this.finish();
			}
			// rewrite the comment
			else {
				//DBManager.createTagEntry(nowName, nowComment);
				//DBManager.overwriteTag(nowName, nowComment);
			}
		}
		// old name exists, overwrite it
		else if (!initName.equals("")) {
			//DBManager.overwriteTag(nowName, nowComment);
		}
		else {
			// or save the new tag to the database
			//DBManager.createTagEntry(nowName, nowComment);			
		}
		NewTagActivity.this.finish();
	}

	/** If passed an extra labelled "ID", set the 
	 * EditText views to the corresponding database
	 * row values
	 * 
	 */
	private void setEditText() {
		if (getExtra("ID") != null) {        	
			int id = Integer.parseInt(getExtra("ID").toString());
			Cursor tagCur = null;//DBManager.fetchTag(id);

			tagCur.moveToFirst();
			initName = tagCur.getString(tagCur.getColumnIndex(DatabaseManager.KEY_TAG));
			initComment = tagCur.getString(tagCur.getColumnIndex(DatabaseManager.KEY_COMMENTS));
			name.setText(initName);
			comment.setText(initComment);

			tagCur.close();
		}
	}


	/** Retrieve extras passed to the view. User must explicitly
	 * cast before use.
	 * 
	 * @param key The String value denoting the extra
	 * @return The extra, or null if none exists
	 */
	private Object getExtra(String key) {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			return extras.get(key);
		}
		return null;
	}
}
