package activity.classes;

import model.classes.DatabaseManager;
import mole.finder.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/** NewTagActivity creates or overwrites a tag stored in 
 * the database.
 * 
 * @author mbessett
 *
 */

public class NewTagActivity extends FActivity { 
	private EditText name;
	private EditText comment;
	private Button save;
	private Button cancel; 
	private Button delete;
	
	private String initName;
	private String initComment;
	
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

	@Override
	protected void findViews() {
		name = (EditText) findViewById(R.id.editTagName);
		comment = (EditText) findViewById(R.id.editTagComments);
		save = (Button) findViewById(R.id.buttonTagSave);
		cancel = (Button) findViewById(R.id.buttonTagCancel);
		delete = (Button) findViewById(R.id.buttonTagDelete);
	}

	@Override
	protected void setClickListeners() {
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
							"entries with this tag. Are you sure you wish to continue?");
					builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									//DBManager.deleteAllEntries(initName);
									NewTagActivity.this.finish();
								}
							});
					builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
					builder.show();			
				}
			});
		}
	}

	/** If editing a tag, display it's current values.
	 * 
	 */
	@Override
	protected void updateView() {
		if (getExtra("ID") != null) {        	
			//int id = Integer.parseInt(getExtra("ID").toString());
			Cursor tagCur = null;//DBManager.fetchTag(id);

			tagCur.moveToFirst();
			initName = tagCur.getString(tagCur.getColumnIndex(DatabaseManager.KEY_TAG));
			initComment = tagCur.getString(tagCur.getColumnIndex(DatabaseManager.KEY_COMMENTS));
			name.setText(initName);
			comment.setText(initComment);

			tagCur.close();
		}
	}
	
	@Override
	protected void customInit() {
		initName = "";
		initComment = "";
	}

	@Override
	protected int myLayout() {
		return R.layout.newtag;
	}
}
