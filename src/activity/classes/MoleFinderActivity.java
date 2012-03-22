package activity.classes;

import controller.classes.CameraController;
import mole.finder.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MoleFinderActivity extends Activity {
	// button objects
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Button buttonCaptureImage;
	private Button buttonCompare;
	private Button buttonReviewImages;
	private Button buttonNewTag;
	private Button buttonEditTag;
	private String imageName;
	private String Date;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		

		// setup buttons
		// capture images
		buttonCaptureImage = (Button) findViewById(R.id.buttonCaptureImage);
		buttonCaptureImage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				CameraController Camera = new CameraController();
		    	Intent intent = Camera.takeAPhoto();
		    	Bundle extras = intent.getExtras();
		    	imageName = extras.getString("imageName");
		    	Date = extras.getString("date");
				startActivityForResult(intent,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});
		// compare images
		buttonCompare = (Button) findViewById(R.id.buttonCompare);
		buttonCompare.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MoleFinderActivity.this, CompareActivity.class);
				startActivity(intent);
			}
		});
		// review images
		buttonReviewImages = (Button) findViewById(R.id.buttonReviewImages);
		buttonReviewImages.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MoleFinderActivity.this, ReviewImagesActivity.class);
				startActivity(intent);
			}
		});		
		// new tag
		buttonNewTag = (Button) findViewById(R.id.buttonNewTag);
		buttonNewTag.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MoleFinderActivity.this, NewTagActivity.class);
				startActivity(intent);
			}
		});			
		// edit tag
		buttonEditTag = (Button) findViewById(R.id.buttonEditTag);
		buttonEditTag.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MoleFinderActivity.this, ReviewTagsActivity.class);
				startActivity(intent);
			}
		});			
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch(requestCode){
		case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
			
			if(resultCode== RESULT_OK){

				Intent intentNewImageActivity = new Intent(MoleFinderActivity.this, NewImageActivity.class);
				intentNewImageActivity.putExtra("imageName", imageName);
				intentNewImageActivity.putExtra("date", Date);
				startActivity(intentNewImageActivity);
	
			}
		}
	}
}