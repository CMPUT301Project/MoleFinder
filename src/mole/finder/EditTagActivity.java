package mole.finder;

import android.app.Activity;
import android.os.Bundle;

public class EditTagActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // until we have an activity set aside for listing tags 
        // this button links to the ReviewImagesActivity
        setContentView(R.layout.review); 
    }
}
