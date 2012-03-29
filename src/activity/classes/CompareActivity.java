package activity.classes;

import java.io.File;

import model.classes.ConditionEntry;
import mole.finder.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

/** Display 2 images on the same view for visual inspection
 * by the user. 
 * 
 * @author 
 *
 */

public class CompareActivity extends FActivity {
	private ImageView top;
	private ImageView bottom;
	
	private String topImage;
	private String bottomImage;
	
	private static final int REQ_CODE = 666;

	@Override
	protected void findViews() {
		top = (ImageView) findViewById(R.id.imageCompareTop);
		bottom = (ImageView) findViewById(R.id.imageCompareBottom);		
	}

	@Override
	protected void setClickListeners() {
		top.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = createLinkIntent("top");
				startActivityForResult(intent, REQ_CODE);
			}
		});
		bottom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = createLinkIntent("bottom");
				startActivityForResult(intent, REQ_CODE);
			}
		});
	}
	
	private Intent createLinkIntent(String layout) {
		Intent intent = new Intent(CompareActivity.this, ReviewImagesActivity.class);
		intent.putExtra("FORWARD", CompareActivity.class);
		intent.putExtra("LAYOUT", layout);
		return intent;
	}

	@Override
	protected void updateView() {
		setImageString();
		
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
	
	private void setImageString() {
		if (getExtra("LAYOUT") != null && getExtra("ID") != null) {
			int id = Integer.parseInt(getExtra("ID").toString());
			ConditionEntry entry = model.getOneEntry(id);
			String name = entry.getImageName();
			String layout = getExtra("LAYOUT").toString();
			
			if (layout.equals("top")) {
				setTopImage(name);
			}
			else if (layout.equals("bottom")) {
				setBottomImage(name);
			}
		}
	}

	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
		super.onActivityResult(requestCode, resultCode, data); 
		if (resultCode == Activity.RESULT_OK) {
			updateView();
		} 
	}


	@Override
	protected void customInit() {
		//setTopImage("");
		//setBottomImage("");
	}

	@Override
	protected int myLayout() {
		return R.layout.compare;
	}

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
