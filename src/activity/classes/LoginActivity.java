package activity.classes;

import model.classes.ConditionUser;
import mole.finder.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends FActivity {
	
	private EditText passwd;
	private CheckBox patient;
	private CheckBox doctor;
	private Button login;
	private ConditionUser user;
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
				Intent intent = new Intent(LoginActivity.this, MoleFinderActivity.class);
				String password = passwd.getText().toString();
				if(passwordCheck(password)){
					startActivity(intent);
					finish();
				}
				else{
					enterPass.setText("Wrong Password!");
					enterPass.setTextColor(Color.RED);
				}
			}
		});
		
		patient.setChecked(true);
		patient.setClickable(false);
		doctor.setClickable(false);
		
	}

	@Override
	protected void updateView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void customInit() {
		
		if(model.isNewUser()){
			Intent intent = new Intent(LoginActivity.this, NewPasswordActivity.class);
			startActivity(intent);
		}
	}

	@Override
	protected int myLayout() {
		return R.layout.login;
	}
	
	private boolean passwordCheck(String password){
		user = model.getUser();
		String truePassword = user.getPassword();
		return(password.equals(truePassword));

	}
	

}
