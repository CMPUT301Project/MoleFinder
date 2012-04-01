package activity.classes;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import mole.finder.R;

public class ReviewPatientsActivity extends FActivity {
	private TextView message;
	private Button button;

	@Override
	protected void findViews() {
		message = (TextView) findViewById(R.id.textSeeUpdate);
		button = (Button) findViewById(R.id.buttonSeeUpdate);
	}

	@Override
	protected void setClickListeners() {
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewPage();
			}
		});
	}
	private void viewPage() {
		Uri uri = Uri.parse("https://github.com/CMPUT301Project/MoleFinder");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}

	@Override
	protected void updateView() {

	}

	@Override
	protected void customInit() {
		message.setText("This area is currently under construction.\n" +
				"Please visit\n\n https://github.com/CMPUT301Project/MoleFinder \n\nto check for updates");
		button.setText("Visit Page");
	}

	@Override
	protected int myLayout() {
		return R.layout.patient;
	}

}
