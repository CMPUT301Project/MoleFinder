package controller.classes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

/**
 * This class sets up the file path and name
 * the image that is being stored.
 * @author jletourn
 *
 */

public class CameraController {
	
	private Uri imageUri;	
	private String imageName;
	private String date;
    
	/**
	 * Sets the file path and environment to take and store an image
	 * @return
	 */
    public Intent takeAPhoto(Context context){
    
    	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	
    	String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MoleFinderPics";
    	
    	File folderF = new File(folder);
    	
    	if(!folderF.exists()){
    		folderF.mkdir();
    	}
    	
    	setImageName();

    	String imageFilePath = folder + "/" + imageName + ".jpg"; //can overwrite itself
    	
    	File imageFile = new File(imageFilePath);
    	
    	imageUri = Uri.fromFile(imageFile);
    	
    	intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
    	intent.putExtra("date", date);
    	intent.putExtra("imageName", imageName);;
    
    	return intent;
    }
    
    /**
     * Sets a unique name for the image to be stored.
     */
    public void setImageName(){
    	String name;
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    	SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat2.setTimeZone(TimeZone.getTimeZone("GMT"));
		date = dateFormat2.format(new Date()).toString();
		name = dateFormat.format(new Date()).toString();
    	imageName = name;
    }
    
}