package mole.finder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MoleFinderActivity extends Activity {
	// button objects
	private Button buttonCaptureImage,
	buttonCompare, buttonReviewImages, buttonNewTag,
	buttonEditTag;


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
				Intent i = new Intent(MoleFinderActivity.this, CaptureImageActivity.class);
				startActivity(i);
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
}