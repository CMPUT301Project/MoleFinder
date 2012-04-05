package adapter.classes;

import java.io.File;
import java.util.List;

import model.classes.ConditionEntry;
import model.classes.DatabaseEntry;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This is the MoleFinderArrayAdapter, it is used to put the images and text fields
 * into the layout used to view images.
 * 
 * @author mbessett
 *
 */

public class MoleFinderArrayAdapter extends ArrayAdapter<DatabaseEntry> {
    Activity context;
    List<DatabaseEntry> items;
    int layoutId;			// layout file
    int imageId;			// ImageView to display thumbnail in
    int titleTextId;		// TextView to display title in
    int commentTextid;		// TextView to display comment in
 
    /** Explicit constructor, allows caller to set all internal variables. 
     * 
     * @param context The calling Activity
     * @param layoutId The XML layout file ID
     * @param imageId The ImageView to display the thumb nail
     * @param titleTextId The TextView to display the title
     * @param commentTextId The TextView to display the comments
     * @param items The list of DatabaseEntry objects to display
     */
    public MoleFinderArrayAdapter(Activity context, int layoutId, int imageId, int titleTextId, int commentTextId,
    		List<DatabaseEntry> items) {
        
    	super(context, layoutId, items); 
        this.context = context;
        this.items = items;
        this.layoutId = layoutId;
        this.imageId = imageId;
        this.titleTextId = titleTextId;
        this.commentTextid = commentTextId;
    }
    
    /** Alternate constructor, no image to display.
     * 
     * @param context The calling Activity
     * @param layoutId The XML layout file
     * @param titleTextId The TextView to display the title
     * @param commentTextId The TextView to display the comments
     * @param items The list of DatabaseEntry objects to display
     */
    public MoleFinderArrayAdapter(Activity context, int layoutId, int titleTextId, int commentTextId,
    		List<DatabaseEntry> items) {
        
    	super(context, layoutId, items); 
        this.context = context;
        this.items = items;
        this.layoutId = layoutId;
        this.titleTextId = titleTextId;
        this.commentTextid = commentTextId;
    }
 
    /**
     * getView is used to join the images with the image views and
     * to set the text of the text views
     * @param	pos position of the item in the ListView
     * 			View is not used
     * 			ViewGroup is not used
     */
    public View getView(int pos, View convertView, ViewGroup parent)
    {
    	// current row
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(layoutId, null);
        ImageView thumbnail = (ImageView)row.findViewById(imageId);
        TextView title = (TextView)row.findViewById(titleTextId);
        TextView comment = (TextView) row.findViewById(commentTextid);
 
        // only ConditionEntry objects have thumbnails to display
        if (items.get(pos) instanceof ConditionEntry) {
        	ConditionEntry entry = (ConditionEntry) items.get(pos);
        	File imgFile = new File("/sdcard/MoleFinderPics/" + entry.getImage() + ".jpg");
        	if(imgFile.exists()){

        		Bitmap image = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        		Bitmap scaledImage = Bitmap.createScaledBitmap(image, 100, 100, false);
        		thumbnail.setImageBitmap(scaledImage);
        	}
        }
        // set text
        title.setText(items.get(pos).getTitle());
        comment.setText(items.get(pos).getDescription());
 
        return(row);
    }
} 
