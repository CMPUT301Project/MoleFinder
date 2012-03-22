package controller.classes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

/**
 * This is the CameraController class that will set up the file path and name
 * the image that is being stored.
 * @author jletourn
 *
 */

public class CameraController {
	
	private Uri imageUri;	
	private String imageName;
	private String date;
	private int counter=0;
    
	/**
	 * Sets the file path and environment to take and store an image
	 * @return
	 */
    public Intent takeAPhoto(){
    
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
    	intent.putExtra("imageName", imageName);
    	return intent;
    }
    
    /**
     * Sets a unique name for the image to be stored.
     */
    public void setImageName(){
    	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		date = dateFormat.format(new Date()).toString();
		counter++;
    	imageName = date.concat("-" +  String.valueOf(counter));
    }
    
}