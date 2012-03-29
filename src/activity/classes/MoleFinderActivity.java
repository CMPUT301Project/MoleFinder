package activity.classes;

import mole.finder.*;
import controller.classes.CameraController;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MoleFinderActivity extends FActivity {
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Button buttonCaptureImage;
	private Button buttonCompare;
	private Button buttonReviewImages;
	private Button buttonNewTag;
	private Button buttonEditTag;
	private String imageName;
	private String date;
	private CameraController camera;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch(requestCode){
		case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
			
			if(resultCode== RESULT_OK){

				Intent intentNewImageActivity = new Intent(MoleFinderActivity.this, NewImageActivity.class);
				intentNewImageActivity.putExtra("imageName", imageName);
				intentNewImageActivity.putExtra("date", date);
				startActivity(intentNewImageActivity);
	
			}
		}
	}

	@Override
	protected void findViews() {
		buttonCaptureImage = (Button) findViewById(R.id.buttonCaptureImage);
		buttonCompare = (Button) findViewById(R.id.buttonCompare);
		buttonReviewImages = (Button) findViewById(R.id.buttonReviewImages);
		buttonNewTag = (Button) findViewById(R.id.buttonNewTag);
		buttonEditTag = (Button) findViewById(R.id.buttonEditTag);
	}

	@Override
	protected void setClickListeners() {
		buttonCaptureImage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
		    	Intent intent = camera.takeAPhoto(getBaseContext());
		    	Bundle extras = intent.getExtras();
		    	imageName = extras.getString("imageName");
		    	date = extras.getString("date");
				startActivityForResult(intent,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});
		buttonCompare.setOnClickListener(switchActListener(CompareActivity.class));
		buttonReviewImages.setOnClickListener(switchActListener(ReviewImagesActivity.class));
		buttonNewTag.setOnClickListener(switchActListener(NewTagActivity.class));
		buttonEditTag.setOnClickListener(switchActListener(ReviewTagsActivity.class));
	}
	
	private OnClickListener switchActListener(final Class<?> newAct) {
		return new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MoleFinderActivity.this, newAct);
				startActivity(intent);
			}
		};
	}

	@Override
	protected void updateView() {
		// nothing to update
	}

	@Override
	protected void customInit() {
		camera = new CameraController();
	}

	@Override
	protected int myLayout() {
		return R.layout.main;
	}

}