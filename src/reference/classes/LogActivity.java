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

import gas.track.AddEntryActivity;
import gas.track.LogEntry;
import gas.track.LogEntryDataSource;
import gas.track.R;
import gas.track.ViewLogActivity;
import gas.track.R.id;
import gas.track.R.layout;

import java.util.List;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.content.Intent;

/* Michael Bessette - 1206154 - mbessett
 *
 * LogActivity.java
 *
 * This Activity displays the list of all entries currently
 * in the log. It uses a LogEntryDataSource to keep an updated
 * list of current LogEntrys and calls the AddEntryActivity 
 * when the addButton is pressed. LogEntrys are only displayed
 * as a summary. Clicking on an item in the view (as denoted by
 * the pretty little arrow) brings up the ViewLogActivity from
 * which you can view the entry in its entirety.
 */

public class LogActivity extends ListActivity {
	
	private LogEntryDataSource datasource;
	private List<LogEntry> values;
	private Button addButton;
	
	// life cycle overrides
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// init db
		datasource = new LogEntryDataSource(this);
		datasource.open();
		
		// load layout
		setContentView(R.layout.loglayout);
		
		// init Button
		addButton = (Button) findViewById(R.id.addlogbutton);
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addEntry();
			}
		});
	
		// keep list up to date
		updateList();
	}	
	
	@Override
	protected void onResume() {
		datasource.open();
		updateList();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}
	
	// clicking list item brings up full entry
	//  entryNumber (or entryID in db) req'd by next Activity
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {			
		Intent i = new Intent(this, ViewLogActivity.class);
		
		// only send the entry number to the next Activity, where it will
		//  query the database for its own LogEntry object
		String entryNumber = getListAdapter().getItem(position).toString(); 
		i.putExtra("entryNumber", entryNumber);
		
		startActivity(i);
	}
	
	// acquire all entries from database and use them as list-fodder
	private void updateList() {
		values = datasource.getAllEntries();
		setListAdapter(new ArrowAdapter(this, R.layout.rowlayout, 
				R.id.entrysummary, R.id.arrowicon, values));
	}
	
	// Button click calls new Activity
	private void addEntry() {
		Intent i = new Intent(this, AddEntryActivity.class);
		startActivity(i);
	}		
}
