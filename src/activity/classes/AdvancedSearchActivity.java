package activity.classes;

import java.util.List;

import adapter.classes.MoleFinderSpinnerAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import model.classes.DatabaseEntry;
import mole.finder.R;

public class AdvancedSearchActivity extends FActivity {
	// UI
	private Spinner spinner;
	private TextView spinnerLabel;
	private TextView timeLabel;
	private TextView resultsLabel;
	private TextView orderLabel;
	private RadioGroup timeGroup;
	private RadioGroup orderGroup;
	private RadioButton timeOneWeek;
	private RadioButton timeTwoWeek;
	private RadioButton timeOneMonth;
	private RadioButton timeThreeMonth;
	private RadioButton timeAll;
	private RadioButton orderFirst;
	private RadioButton orderLast;
	private EditText nResults;
	private Button searchButton;
	
	// values
	private int spinnerPos;
	private int searchInterval;
	
	@Override
	public void onPause() {
		setSpinnerPos(spinner.getSelectedItemPosition());
		super.onPause();
	}

	@Override
	protected void findViews() {
		spinner = (Spinner) findViewById(R.id.spinner_adv_tag);
		
		spinnerLabel = (TextView) findViewById(R.id.spinnerLabel);
		timeLabel = (TextView) findViewById(R.id.radio_label);
		orderLabel = (TextView) findViewById(R.id.orderLabel);
		
		timeGroup = (RadioGroup) findViewById(R.id.radioGroupTime);
		orderGroup = (RadioGroup) findViewById(R.id.radioGroupOrder);
		
		timeOneWeek = (RadioButton) findViewById(R.id.radioOneWeek);
		timeTwoWeek = (RadioButton) findViewById(R.id.radioTwoWeek);
		timeOneMonth = (RadioButton) findViewById(R.id.radioOneMonth);
		timeThreeMonth = (RadioButton) findViewById(R.id.radioThreeMonth);
		timeAll = (RadioButton) findViewById(R.id.radioAll);
		orderFirst = (RadioButton) findViewById(R.id.radioRecentFirst);
		orderLast = (RadioButton) findViewById(R.id.radioRecentLast);
		
		searchButton = (Button) findViewById(R.id.buttonSearch);
		nResults = (EditText) findViewById(R.id.editResults);
		resultsLabel = (TextView) findViewById(R.id.resultsLabel);
	}

	@Override
	protected void setClickListeners() {
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (timeGroup.getCheckedRadioButtonId()) {
				case R.id.radioOneWeek: setSearchInterval(7); break;
				case R.id.radioTwoWeek: setSearchInterval(14); break;
				case R.id.radioOneMonth: setSearchInterval(30); break;
				case R.id.radioThreeMonth: setSearchInterval(90); break;
				case R.id.radioAll: setSearchInterval(0); break;
				}
			}
		});		
	}

	@Override
	protected void updateView() {
		model.fetchTags();
		List<DatabaseEntry> tagList = model.getTags();
		int count = tagList.size();
		spinner.setAdapter(new MoleFinderSpinnerAdapter(this, tagList));
		if (getSpinnerPos() < count) {
			spinner.setSelection(getSpinnerPos());
		}
	}

	@Override
	protected void customInit() {
		spinnerLabel.setText("Select a tag");
		timeLabel.setText("Select a time period");
		resultsLabel.setText("Display how many results?");
		resultsLabel.setHint("Leaving this field blank will not limit the number of results.");
		orderLabel.setText("Select results ordering");
		
		timeOneWeek.setText("One Week");
		timeTwoWeek.setText("Two Weeks");
		timeOneMonth.setText("One Month");
		timeThreeMonth.setText("Three Months");
		timeAll.setText("All");
		orderFirst.setText("Most Recent First");
		orderLast.setText("Most Recent Last");
		
		searchButton.setText("Search");
		
		setSearchInterval(0);
	}

	@Override
	protected int myLayout() {
		return R.layout.advancedsearch;
	}

	public int getSpinnerPos() {
		return spinnerPos;
	}

	public void setSpinnerPos(int spinnerPos) {
		this.spinnerPos = spinnerPos;
	}

	public int getSearchInterval() {
		return searchInterval;
	}

	public void setSearchInterval(int daysToAdd) {
		this.searchInterval = daysToAdd;
	}

}
