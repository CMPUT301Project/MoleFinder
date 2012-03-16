package mole.finder;

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


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final CameraController Camera = new CameraController();

		// setup buttons
		// capture images
		buttonCaptureImage = (Button) findViewById(R.id.buttonCaptureImage);
		buttonCaptureImage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
		    	Intent i = Camera.takeAPhoto();
				startActivityForResult(i,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});
		// compare images
		buttonCompare = (Button) findViewById(R.id.buttonCompare);
		buttonCompare.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(MoleFinderActivity.this, CompareActivity.class);
				startActivity(i);
			}
		});
		// review images
		buttonReviewImages = (Button) findViewById(R.id.buttonReviewImages);
		buttonReviewImages.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(MoleFinderActivity.this, ReviewImagesActivity.class);
				startActivity(i);
			}
		});		
		// new tag
		buttonNewTag = (Button) findViewById(R.id.buttonNewTag);
		buttonNewTag.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(MoleFinderActivity.this, NewTagActivity.class);
				startActivity(i);
			}
		});			
		// edit tag
		buttonEditTag = (Button) findViewById(R.id.buttonEditTag);
		buttonEditTag.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(MoleFinderActivity.this, EditTagActivity.class);
				startActivity(i);
			}
		});			
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
			
			if(resultCode== RESULT_OK){
				
				//ImageButton button = (ImageButton) findViewById(R.id.TakeAPhoto);
				
				//button.setImageDrawable(Drawable.createFromPath(imageUri.getPath()));
				
			}
		}
	}
}