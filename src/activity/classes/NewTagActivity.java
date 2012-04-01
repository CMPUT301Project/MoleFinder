package activity.classes;

import model.classes.ConditionTag;
import mole.finder.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/** This is the NewTagActivity class, it creates or overwrites a tag stored in 
 * the database.
 * 
 * @author mbessett
 */

public class NewTagActivity extends FActivity { 
	private EditText name;
	private EditText comment;
	private Button save;
	private Button cancel; 
	private Button delete;

	private ConditionTag initTag;

	/** Save the current tag to the database 
	 * 
	 */
	private void saveTag() {
		ConditionTag nowTag = new ConditionTag(initTag);
		String curName = name.getText().toString();
		nowTag.setName(curName);
		nowTag.setComment(comment.getText().toString());
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
		int status = initTag.compareTo(nowTag);
		switch (status) {
		case ConditionTag.IDENTICAL:
			break;
		case ConditionTag.DUMMY_ID:
			model.saveTag(nowTag); break;
		case ConditionTag.DIFF_NAME:
			model.overwriteCascade(initTag.getName(), nowTag); break;
		case ConditionTag.DIFF_COMMENT:
			model.overwriteTag(nowTag); break;
		}
		if (getParent() == null) {
			setResult(Activity.RESULT_OK);
		} else {
			getParent().setResult(Activity.RESULT_OK);
		}
		finish();
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
		if (initTag.getId() == ConditionTag.DUMMY_ID) {
			delete.setVisibility(View.GONE);
		}
		else {
			// NEED CASCADING DELETE HERE AS WELL!!!
			delete.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(NewTagActivity.this);
					builder.setMessage("This operation will delete any and all condition " +
							"entries with this tag. Are you sure you wish to continue?");
					builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							model.deleteTag(initTag);
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
			long id = Long.parseLong(getExtra("ID").toString());
			initTag = model.getOneTag(id);
			name.setText(initTag.getName());
			comment.setText(initTag.getComment());
		}
	}

	@Override
	protected void customInit() {
		initTag = ConditionTag.createDummyTag();
	}

	@Override
	protected int myLayout() {
		return R.layout.newtag;
	}
}
