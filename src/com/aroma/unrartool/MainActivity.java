package com.aroma.unrartool;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity {

	Button unrarBtn=null,openArchive=null;
	private TextView openedFile=null,processedFile=null;
	String path=null;
	private ViewFlipper mainViewFlipper=null;
	private MainScreen mainScreen=null;
	private FileBrowser fileBrowser=null;
	private int currentForm=0;
	private Splash splash=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		mainViewFlipper=(ViewFlipper) findViewById(R.id.mainViewFlipper);
		mainScreen=(MainScreen) mainViewFlipper.findViewById(R.id.mainScreen);
		
		fileBrowser=(FileBrowser) mainViewFlipper.findViewById(R.id.fileBrowser);
		
		fileBrowser.initializeList();
		
		splash=new Splash(this);
		addContentView(splash, new LayoutParams(LayoutParams.MATCH_PARENT
				, LayoutParams.MATCH_PARENT));
		
		/*openedFile=(TextView)findViewById(R.id.textView1);
		openedFile.setSelected(true);
		openedFile.setEllipsize(TruncateAt.MARQUEE);
		processedFile=(TextView)findViewById(R.id.textView2);
		unrarBtn=(Button)findViewById(R.id.button1);
		unrarBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(path!= null)
				{
					UnrarTask task=new UnrarTask();
					task.execute(path);
				}
				
			}
		});
		unrarBtn.setEnabled(false);
		openArchive=(Button)findViewById(R.id.openFile);
		openArchive.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent(MainActivity.this, FileBrowserActivity.class);
				startActivityForResult(i, 1111);
				
			}
		});*/
	}
	
	public void switchForms()
	{
		if(AnimationFactory.animationRunning.get())
			return;
		AnimationFactory.flipTransition(mainViewFlipper, AnimationFactory.FlipDirection.LEFT_RIGHT);
		currentForm=mainViewFlipper.getDisplayedChild();
	}
	
	public void setSelectedFile(String path)
	{
		mainScreen.setOpenedArchivePath(path);
	}
	public void setSelectedExtractionPath(String path)
	{
		mainScreen.setSelectedExtractionPath(path);
	}
	public void setFileBrowserMode(int mode)
	{
	   fileBrowser.setBrowsingMode(mode);
	   
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(AnimationFactory.animationRunning.get())
			return;
		if(currentForm==1)
			 switchForms();
		else
		{
			currentForm=-1;
			splash.closeSplash(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					finish();
				}
			});
			//super.onBackPressed();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_main, menu);
	    return true;
	}
	
	/*@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		if(currentForm!=0 || !splash.isSplashEnded())
			return false;
		return super.onPrepareOptionsMenu(menu);
	}*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 switch (item.getItemId()) {
	        case R.id.menu_about:
	        {
	        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        	builder.setMessage(getString(R.string.aboutmsg))//"Unrar Tool v1.0 \n A simple tool to decompress RAR Archives.\nCopyright 2013 \nMahmoud Galal")
	        	       .setCancelable(false)	        	       
	        	       .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	        	           public void onClick(DialogInterface dialog, int id) {
	        	        	   dialog.cancel();
	        	           }
	        	       });
	        	      
	        	AlertDialog alert = builder.create();	
	        	alert.show();
	        }
	           
	          return true;
	        case R.id.menu_share:
	        {
	        	share();
	        }
	            
	         return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1111 && resultCode==RESULT_OK)
    	{
    		path=data.getStringExtra(FileBrowser.FILE_PICKED);
    		openedFile.setText(path);
    		unrarBtn.setEnabled(true);
    	}
	}
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
    	// TODO Auto-generated method stub
    	super.onConfigurationChanged(newConfig);
    }
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		boolean handled = super.onKeyDown(keyCode, event);


	    // Eat the long press event so the keyboard doesn't come up.
	    if (keyCode == KeyEvent.KEYCODE_MENU && event.isLongPress()) {
	    	System.out.println("menu key long press.......");
	        return true;
	    }
		return handled;
	}
	private void share()
	{
		Intent intent=new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

		// Add data to the intent, the receiving app will decide what to do with it.
		intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
		intent.putExtra(Intent.EXTRA_TEXT, "Unrar Tool is WooW!");
		startActivity(Intent.createChooser(intent,getString(R.string.sharehdr)));
	}

}
