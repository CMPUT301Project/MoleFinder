package activity.classes;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import model.classes.ConditionEntry;
import mole.finder.R;

/**
 * This is the ImageActivity class this activity allows the user to view
 * his/her images in an ImageView. There is also edit and delete functionality.
 * 
 * @author jletourn
 */

public class ImageActivity extends FActivity {
	// UI
	private ImageView image;
	private TextView comments;
	private Button editButton;
	private Button deleteButton;
	// internal values
	private int id;
	
	@Override
	protected void findViews() {
		image = (ImageView) findViewById(R.id.full_size_image);
		comments = (TextView) findViewById(R.id.textView_comments);	
		editButton = (Button) findViewById(R.id.buttonImageEdit);
		deleteButton = (Button) findViewById(R.id.buttonImageDelete);	
	}

	@Override
	protected void setClickListeners() {
		
		editButton.setOnClickListener(new View.OnClickListener() {	
			public void onClick(View v) {
				Intent intent = new Intent(ImageActivity.this, NewImageActivity.class);
				intent.putExtra("ID", id);
				startActivity(intent);
			}
		});
		
		deleteButton.setOnClickListener(new View.OnClickListener() {	
			public void onClick(View v) {
				ConditionEntry entry = model.getOneEntry(id);
	        	File imgFile = new File("/sdcard/MoleFinderPics/" + entry.getImage() + ".jpg");
	        	imgFile.delete();
				model.deleteImage(entry);
				finish();
			}
		});
		
	}

	@Override
	protected void updateView() {
    	Intent intent = getIntent();
    	Bundle extras = intent.getExtras();
    	id = extras.getInt("ID");
    	ConditionEntry entry = model.getOneEntry(id);
    	String imageName = entry.getImage();
    	String commentString = entry.getComment();
        File imgFile = new File("/sdcard/MoleFinderPics/" + imageName + ".jpg");
        if(imgFile.exists()){
        	Bitmap bitmapImage = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        	image.setImageBitmap(bitmapImage);
        	comments.setText(commentString);
        }
	}

	@Override
	protected void customInit() {
	}

	@Override
	protected int myLayout() {
		return R.layout.image;
	}
}
