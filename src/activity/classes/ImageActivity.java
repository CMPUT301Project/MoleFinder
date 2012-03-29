package activity.classes;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import mole.finder.R;

// TODO needs button functionality
public class ImageActivity extends FActivity {
	
	private ImageView image;
	private TextView comments;

	@Override
	protected void findViews() {
		image = (ImageView) findViewById(R.id.full_size_image);
		comments = (TextView) findViewById(R.id.textView_comments);	
		
	}

	@Override
	protected void setClickListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateView() {
    	Intent intent = getIntent();
    	Bundle extras = intent.getExtras();
    	int pos = extras.getInt("pos");
    	String imageName = model.getConditions().get(pos).getImageName();
        File imgFile = new File("/sdcard/MoleFinderPics/" + imageName + ".jpg");
        if(imgFile.exists()){
        	Bitmap bitmapImage = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        	image.setImageBitmap(bitmapImage);
        }
	}

	@Override
	protected void customInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int myLayout() {
		return R.layout.image;
	}

	
}
