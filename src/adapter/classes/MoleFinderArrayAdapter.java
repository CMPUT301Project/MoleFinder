package adapter.classes;

import java.util.List;

import model.classes.DatabaseEntry;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MoleFinderArrayAdapter extends ArrayAdapter<DatabaseEntry> {
    Activity context;
    List<DatabaseEntry> items;
    int layoutId;			// layout file
    int titleTextId;		// TextView to display title in
    int commentTextid;		// TextView to display comment in
 
    public MoleFinderArrayAdapter(Activity context, int layoutId, int titleTextId, int commentTextId,
    		List<DatabaseEntry> items) {
        
    	super(context, layoutId, items); 
        this.context = context;
        this.items = items;
        this.layoutId = layoutId;
        this.titleTextId = titleTextId;
        this.commentTextid = commentTextId;
    }
 
    public View getView(int pos, View convertView, ViewGroup parent)
    {
    	// current row
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(layoutId, null);
        TextView title = (TextView)row.findViewById(titleTextId);
        TextView comment = (TextView) row.findViewById(commentTextid);
 
        // set text
        title.setText(items.get(pos).getTitle());
        comment.setText(items.get(pos).getDescription());
 
        return(row);
    }
} 
