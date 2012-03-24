package adapter.classes;

import java.util.List;

import model.classes.DatabaseEntry;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MoleFinderSpinnerAdapter extends ArrayAdapter<DatabaseEntry> {
    Activity context;
    List<DatabaseEntry> items;
 
    public MoleFinderSpinnerAdapter(Activity context, List<DatabaseEntry> items) {
        
    	super(context, android.R.layout.simple_spinner_dropdown_item, items); 
        this.context = context;
        this.items = items;
    }
 
    public View getView(int pos, View convertView, ViewGroup parent)
    {
    	// current row
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
        TextView title = (TextView)row.findViewById(android.R.id.text1);
 
        // set text
        title.setText(items.get(pos).getTitle());
 
        return(row);
    }
}