package activity.classes;

import mole.finder.*;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/** This is the main activity for MoleFinder. It uses a TabHost
 * to switch between the 3 main action categories available in 
 * the application - Images,Tags,Patients.
 * 
 * @author mbessett
 *
 */

public class MoleFinderActivity extends TabActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.tablayout);

        Resources res = getResources(); // to get tab icons
        TabHost tabHost = getTabHost();
        TabSpec spec;
        Intent intent;

        // Initialise a TabSpec for each tab and add it to the TabHost
        intent = new Intent().setClass(this, ReviewImagesActivity.class);  
        intent.putExtra("FORWARD", ImageActivity.class);
        spec = tabHost.newTabSpec("images");
        spec.setIndicator("Images", res.getDrawable(R.drawable.tab_images));
        spec.setContent(intent);
        
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ReviewTagsActivity.class);
        spec = tabHost.newTabSpec("tags");
        spec.setIndicator("Tags", res.getDrawable(R.drawable.tab_tags));
        spec.setContent(intent);
        
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, ReviewPatientsActivity.class);
        spec = tabHost.newTabSpec("patients");
        spec.setIndicator("Patients", res.getDrawable(R.drawable.tab_patients));
        spec.setContent(intent);
        
        tabHost.addTab(spec);

        // default to tags page
        tabHost.setCurrentTab(1);
    }

}
