/*
    Assignment 1: Mileage Calculator
    Copyright (C) 2012 Michael Bessette <mbessett@ualberta.ca>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package reference.classes;

import gas.track.LogEntry;
import gas.track.R;
import gas.track.R.drawable;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/* Michael Bessette - 1206154 - mbessett
 *
 * ArrowAdapter.java 
 *
 * The ArrowAdapter class is an extension of the ArrayAdapter,
 * and simply adds an arrow icon, aligned to the right, to each
 * view in a ListView.
 * 
 * Original Code By: Willem van Ketwich
 * 	found at:
 * http://willvk.blogspot.com/2011/07/adding-arrow-image-to-right-of-listview.html
 */

public class ArrowAdapter extends ArrayAdapter<LogEntry> {
    Activity context;
    List<LogEntry> items;
    int layoutId;			// layout file
    int textId;				// TextView to display in
    int imageId;			// icon to display
 
    ArrowAdapter(Activity context, int layoutId, int textId,
    		int imageId, List<LogEntry> items) {
        
    	super(context, layoutId, items);
 
        this.context = context;
        this.items = items;
        this.layoutId = layoutId;
        this.textId = textId;
        this.imageId = imageId;
    }
 
    public View getView(int pos, View convertView, ViewGroup parent)
    {
    	// current row
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(layoutId, null);
        TextView label = (TextView)row.findViewById(textId);
 
        // set text
        label.setText(items.get(pos).getSummary());
         
        // and icon
        ImageView icon = (ImageView)row.findViewById(imageId); 
        icon.setImageResource(R.drawable.arrow_selector);   
 
        return(row);
    }
} 
