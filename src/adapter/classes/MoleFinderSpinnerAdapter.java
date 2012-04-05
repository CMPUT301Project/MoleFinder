package adapter.classes;

import java.util.List;

import model.classes.DatabaseEntry;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * The MoleFinderSpinnerAdapter is used to fill the spinners with tag entries.
 * 
 * @author mbessett
 */

public class MoleFinderSpinnerAdapter extends ArrayAdapter<DatabaseEntry> {
    Activity context;
    List<DatabaseEntry> items;
 
    /** Set the context and list of items to display.
     * 
     * @param context The calling Activity
     * @param items The list of DatabaseEntry objects to display
     */
    public MoleFinderSpinnerAdapter(Activity context, List<DatabaseEntry> items) {
        
    	super(context, android.R.layout.simple_spinner_dropdown_item, items); 
        this.context = context;
        this.items = items;
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
        View row = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
        TextView title = (TextView)row.findViewById(android.R.id.text1);
 
        // set text
        if (!items.isEmpty()) {
        	DatabaseEntry item = items.get(pos);
        	title.setText(item.getTitle());
        }

        return(row);
    }
}
