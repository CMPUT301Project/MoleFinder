package activity.classes;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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
        File imgFile = new File("/sdcard/MoleFinderPics/" + model.getConditions().get(1).getImageName() + ".jpg");
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
