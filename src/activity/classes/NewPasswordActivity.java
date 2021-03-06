package activity.classes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import model.classes.ConditionUser;
import mole.finder.R;

/**
 * This activity is only called on the initial creation of the database to allow
 * the user to set the password they will use to log into the application in the
 * future.
 * 
 * @author jletourn
 */

public class NewPasswordActivity extends FActivity{
	// UI
	private EditText passwd;
	private CheckBox patient;
	private CheckBox doctor;
	private Button login;
	private TextView enterPass;
	
	@Override
	protected void findViews() {
		passwd = (EditText) findViewById(R.id.password);
		patient = (CheckBox) findViewById(R.id.checkBox_patient);
		doctor = (CheckBox) findViewById(R.id.checkBox_doctor);
		login = (Button) findViewById(R.id.Loginbutton);
		enterPass = (TextView) findViewById(R.id.EnterPassword);
	}

	@Override
	protected void setClickListeners() {
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				submitUser();
			}
		});
		
	}

	@Override
	protected void updateView() {
		doctor.setChecked(false);
		doctor.setClickable(false);
		patient.setChecked(true);
		patient.setClickable(false);
	}

	@Override
	protected void customInit() {
		enterPass.setText("Please enter a Password to hide your information");
		enterPass.setTextColor(Color.RED);
	}

	@Override
	protected int myLayout() {
		return R.layout.login;
	}
	
	/**
	 * submitUser is used to check conditions such as if the password field is not
	 * empty before saving the users password to the database.
	 * @param 
	 * @return void
	 */
	public void submitUser(){
		String password = passwd.getText().toString();
		if(password.equals("")){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Password field must be filled.");
			builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			builder.show();
			return;
		}
		else{
			ConditionUser user = new ConditionUser(0, password, "patient");
			model.saveUser(user);
			finish();
		}
		
	}

}
