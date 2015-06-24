package com.aroma.unrartool;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class FileBrowserActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FileBrowser fb=new FileBrowser(this);
        setContentView(fb);
        fb.initializeList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.activity_file_browser, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub    		
		setResult(Activity.RESULT_CANCELED);		
    	super.onBackPressed();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	// TODO Auto-generated method stub
    	super.onConfigurationChanged(newConfig);
    }

    
}
