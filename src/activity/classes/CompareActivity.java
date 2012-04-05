package activity.classes;

import java.io.File;

import model.classes.ConditionEntry;
import mole.finder.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/** Display 2 images on the same view for visual inspection
 * by the user. 
 * 
 * @author mbessett
 *
 */

public class CompareActivity extends FActivity {
	// UI
	private ImageView top;
	private ImageView bottom;
	// names of the images to load from SDCard
	private String topImage;
	private String bottomImage;

	@Override
	protected void findViews() {
		top = (ImageView) findViewById(R.id.imageCompareTop);
		bottom = (ImageView) findViewById(R.id.imageCompareBottom);		
	}

	/* Clicking on the image views allows the user to select which 
	 * image is displayed.
	 * 
	 */
	@Override
	protected void setClickListeners() {
		top.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = createLinkIntent("top");
				startActivityForResult(intent, 1);
			}
		});
		bottom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = createLinkIntent("bottom");
				startActivityForResult(intent, 1);
			}
		});
	}
	
	/* Create an intent that links to ReviewImagesActivity, and sets
	 * the layout extra to either "top" or "bottom" depending on which
	 * picture you are selecting.
	 * 
	 * @param layout Either "top" or "bottom"
	 * @return The created intent. 
	 */
	private Intent createLinkIntent(String layout) {
		Intent intent = new Intent(CompareActivity.this, ReviewImagesActivity.class);
		intent.putExtra("FORWARD", CompareActivity.class);
		intent.putExtra("LAYOUT", layout);
		return intent;
	}

	/* Display the images, if they exist on the SDCard.
	 * 
	 */
	@Override
	protected void updateView() {
        File topImg = new File("/sdcard/MoleFinderPics/" + getTopImage() + ".jpg");
        File bottomImg = new File("/sdcard/MoleFinderPics/" + getBottomImage() + ".jpg");
        if (topImg.exists()){        	
        	Bitmap bitmapImage = BitmapFactory.decodeFile(topImg.getAbsolutePath());
        	top.setImageBitmap(bitmapImage);
        }
        if (bottomImg.exists()) {
        	Bitmap bmpImg = BitmapFactory.decodeFile(bottomImg.getAbsolutePath());
        	bottom.setImageBitmap(bmpImg);
        }        
	}

	/* Images are distinguished in this class by name,
	 * this method sets the correct image name depending
	 * on the LAYOUT extra passed when returning from 
	 * image selection.
	 * 
	 * @param data The intent from onActivityResult
	 */
	private void setImageString(Intent data) {
		String layout = data.getStringExtra("LAYOUT");
		int id = data.getIntExtra("ID", -1);
		ConditionEntry entry = model.getOneEntry(id);
		String name = entry.getImage();

		if (layout.equals("top")) {
			setTopImage(name);
		}
		else if (layout.equals("bottom")) {
			setBottomImage(name);
		}				
	}

	/** Returning from image selection, save the name of the image
	 * to the appropriate variable.
	 * 
	 */
	// called BEFORE onResume
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
		super.onActivityResult(requestCode, resultCode, data); 
		if (resultCode == Activity.RESULT_OK) {
			setImageString(data);
		} 		
	}

	/* Comparing new images, clear the image names. 
	 * 
	 */
	@Override
	protected void customInit() {
		setTopImage("");
		setBottomImage("");
	}

	@Override
	protected int myLayout() {
		return R.layout.compare;
	}

	// getters/setters
	public void setTopImage(String topImage) {
		this.topImage = topImage;
	}

	public String getTopImage() {
		return topImage;
	}

	public void setBottomImage(String bottomImage) {
		this.bottomImage = bottomImage;
	}

	public String getBottomImage() {
		return bottomImage;
	}
}
