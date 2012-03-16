package mole.finder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;


public class CameraController {
	
	private Uri imageUri;	
	private String imageName;
	private String Date;
	private int counter=0;
    
    protected Intent takeAPhoto(){
    
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
    	intent.putExtra("date", Date);
    	intent.putExtra("imageName", imageName);
    	return intent;
    }
    
    public void setImageName(){
    	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date = dateFormat.format(new Date()).toString();
		counter++;
    	imageName = Date.concat("-" +  String.valueOf(counter));
    }
    
}