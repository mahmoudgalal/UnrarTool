package com.aroma.unrartool;



import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.text.TextUtils.TruncateAt;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainScreen extends RelativeLayout {

	
	private Button unrarBtn=null,openArchive=null;
	private TextView openedFile=null,processedFile=null
			,extractionPathText=null,archiveData = null;
	private String path=null,extractionPath=null;
	private Animation scaleUp=null;
	
	public MainScreen(Context context) {
		super(context);
		setBackgroundColor(Color.argb(255, 255, 0, 0));
		setPadding(5, 5, 5, 5);
		LayoutInflater.from(context).inflate(R.layout.activity_main, this);
		openedFile=(TextView)findViewById(R.id.textView1);
		openedFile.setSelected(true);
		openedFile.setEllipsize(TruncateAt.MARQUEE);
		processedFile=(TextView)findViewById(R.id.textView2);
		extractionPathText=(TextView)findViewById(R.id.extractionpath);
		archiveData = (TextView)findViewById(R.id.archivedata);
		archiveData.setMovementMethod(new ScrollingMovementMethod());
		archiveData.setVisibility(INVISIBLE);
		//processedFile.setVisibility(INVISIBLE);
		unrarBtn=(Button)findViewById(R.id.okBtn);
		unrarBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(path!= null)
				{
//					UnrarTask task=new UnrarTask();
//					task.execute(path);
					ExtrPathDialog extrDialog=new ExtrPathDialog(getContext());
					extrDialog.show();
				}
				
			}
		});
		unrarBtn.setEnabled(false);
		openArchive=(Button)findViewById(R.id.openFile);
		openArchive.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity act=((MainActivity)getContext());	
				act.setFileBrowserMode(FileBrowser.BROWSE_MODE_FILE);
				act.switchForms();
				
			}
		});
		scaleUp=AnimationUtils.loadAnimation(context, R.anim.scale_up);
		// TODO Auto-generated constructor stub
	}

	public MainScreen(Context context, AttributeSet attrs) {
		super(context, attrs);	
		setBackgroundColor(Color.argb(255, 255, 0, 0));
		setPadding(5, 5, 5, 5);
		LayoutInflater.from(context).inflate(R.layout.activity_main, this);
		openedFile=(TextView)findViewById(R.id.textView1);
		openedFile.setSelected(true);
		openedFile.setEllipsize(TruncateAt.MARQUEE);
		processedFile=(TextView)findViewById(R.id.textView2);
		archiveData = (TextView)findViewById(R.id.archivedata);
		archiveData.setMovementMethod(new ScrollingMovementMethod());
		archiveData.setVisibility(INVISIBLE);
		//processedFile.setVisibility(INVISIBLE);
		extractionPathText=(TextView)findViewById(R.id.extractionpath);
		extractionPathText.setSelected(true);
		extractionPathText.setEllipsize(TruncateAt.MARQUEE);
		unrarBtn=(Button)findViewById(R.id.okBtn);
		unrarBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(path!= null)
				{
//					UnrarTask task=new UnrarTask();
//					task.execute(path);
					ExtrPathDialog extrDialog=new ExtrPathDialog(getContext());
					extrDialog.show();
				}
				
			}
		});
		unrarBtn.setEnabled(false);
		openArchive=(Button)findViewById(R.id.openFile);
		openArchive.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				MainActivity act=((MainActivity)getContext());
				act.setFileBrowserMode(FileBrowser.BROWSE_MODE_FILE);
				act.switchForms();
			}
		});
		scaleUp=AnimationUtils.loadAnimation(context, R.anim.scale_up);
	}

	public MainScreen(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setBackgroundColor(Color.argb(255, 255, 0, 0));
		setPadding(3, 3, 3, 3);
		LayoutInflater.from(context).inflate(R.layout.activity_main, this);
		openedFile=(TextView)findViewById(R.id.textView1);
		openedFile.setSelected(true);
		openedFile.setEllipsize(TruncateAt.MARQUEE);
		processedFile=(TextView)findViewById(R.id.textView2);
		archiveData = (TextView)findViewById(R.id.archivedata);
		archiveData.setVisibility(INVISIBLE);
		archiveData.setMovementMethod(new ScrollingMovementMethod());
		//processedFile.setVisibility(INVISIBLE);
		extractionPathText=(TextView)findViewById(R.id.extractionpath);
		extractionPathText.setSelected(true);
		extractionPathText.setEllipsize(TruncateAt.MARQUEE);
		unrarBtn=(Button)findViewById(R.id.okBtn);
		unrarBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(path!= null)
				{
					
//					UnrarTask task=new UnrarTask();
//					task.execute(path);
					ExtrPathDialog extrDialog=new ExtrPathDialog(getContext());
					extrDialog.show();
				}
				
			}
		});
		unrarBtn.setEnabled(false);
		openArchive=(Button)findViewById(R.id.openFile);
		openArchive.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				MainActivity act=((MainActivity)getContext());	
				act.setFileBrowserMode(FileBrowser.BROWSE_MODE_FILE);
				act.switchForms();
			}
		});
		scaleUp=AnimationUtils.loadAnimation(context, R.anim.scale_up);
	}
	 
	public void setOpenedArchivePath(String path)
	 {
		 this.path=path;
		 openedFile.setText(path);
 		 unrarBtn.setEnabled(true);
 		 archiveData.setText("");
	 }
	public String getOpenedArchivePath()
	 {
		 return path;
	 }
	public void setSelectedExtractionPath(String path)
	 {
		extractionPath=path;
		 extractionPathText.setText(path);
		 
	 }
	
	
	class UnrarTask extends AsyncTask<String, String, Void>
	{
		ProgressDialog pd=null;
		 Unrar ur=null;
		 int numOfItems=1,progressUpdate=0;
		 int unrarResult=0;
		 PowerManager.WakeLock wl=null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if(!archiveData.isShown())
			{
				
				archiveData.startAnimation(scaleUp);
				archiveData.setVisibility(VISIBLE);
				
			}
			PowerManager pm = (PowerManager) MainScreen.this.getContext().
					getSystemService(Context.POWER_SERVICE); 
			wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
			wl.acquire();   
			
			/*if(!processedFile.isShown())
			{
				processedFile.startAnimation(scaleUp);
				processedFile.setVisibility(VISIBLE);
			}*/
			pd=ProgressDialog.show(MainScreen.this.getContext(), ""
					,getContext().getString(R.string.wait),true,false);
		}
		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);	
			if(values != null && values.length == 1)
			  processedFile.setText(values[0]);	
			else
			{
				if(values == null)
				{
					String yes=getContext().getString(R.string.yes);
					String no=getContext().getString(R.string.no);
					String archiveDataTxt="";
					archiveDataTxt+=getResources().getString(R.string.arcCmt)+":"/*"Archive Comment:"*/+ no/*"N/A"*/+"\n";
					archiveDataTxt+=getResources().getString(R.string.arcSld)+":"/*"Archive Solid:"*/+ (ur.solid?yes:no)+"\n";
					archiveDataTxt+=getResources().getString(R.string.arcSnd)+":"/*"Archive Signed:"*/+ (ur.signed?yes:no)+"\n";
					archiveDataTxt+=getResources().getString(R.string.arcRrp)+":"/*"Recovery Record Present:"*/+ (ur.recoveryRecord?yes:no)+"\n";
					archiveDataTxt+=getResources().getString(R.string.arcVol)+":"/*"Is Volume:"*/+ (ur.volume?yes:no)+"\n";
					archiveDataTxt+=getResources().getString(R.string.arcLoc)+":"/*"Archive Locked:"*/+ (ur.locked?yes:no);
					archiveData.setText(archiveDataTxt);					
				}
				else
					archiveData.setText(getResources().getString(R.string.arcCmt)+": "/*"Archive Comment: "*/+ values[1]);
			}
			
		}

		@Override
		protected Void doInBackground(String... params) {
			 ur=new Unrar();
			ur.setCallBackListener(new Unrar.CallBackListener() {
				String message="";
				@Override
				public void onFileProcessed(int msgID,String filename) {
					if(msgID == 0)
					{
						progressUpdate++ ;
						message=getResources().getString(R.string.processing)+":"+ filename +
								(numOfItems>0?
								String.format(" %.2f %s", (100*((float)(progressUpdate)/numOfItems))
										,"%"):"");
								
					    UnrarTask.this.publishProgress(message);
					}
					else
					{
						switch(msgID)
						{ 
							case Unrar.ERAR_BAD_DATA :
								message=String.format("Error:unable to process %s File CRC error!",filename);								
								break;
							case Unrar.ERAR_BAD_ARCHIVE:								
								message="Error:Volume is not valid RAR archive !";
								break;	
							case Unrar.ERAR_UNKNOWN_FORMAT:	
								message="Error:Unknown archive format !";
								break;
							case Unrar.ERAR_EOPEN:
								message="Error:Volume open error !";
								break;
							case Unrar.ERAR_ECREATE:
								message=String.format("Error:File create error! in: %s",filename);								
								break;
							case Unrar.ERAR_ECLOSE:
								message=String.format("Error:File close error ! in: %s",filename);								
								break;
							case Unrar.ERAR_EREAD:
								message=String.format("Error:Read error ! in: %s",filename);								
								break;
							case Unrar.ERAR_EWRITE:
								message=String.format("Error:Write error ! in: %s",filename);								
								break;
						}
						UnrarTask.this.publishProgress(message);
					}
				}

				@Override
				public void onPassWordRequired() {
					
					MainActivity mact=(MainActivity) MainScreen.this.getContext();
					mact.runOnUiThread(new Runnable() {
						 
						@Override
						public void run() {
							System.out.println("Asking for the password...");
							PassWordDialog pd=new PassWordDialog(getContext(), ur);
							pd.show();						
							
						}
					});
				}
			});
		     numOfItems = ur.RarGetArchiveItems(params[0]);
			System.out.println("Archive Num of items is:"+numOfItems);
			if(ur.archiveComment !=  null )
			  publishProgress(new String[] {"",ur.archiveComment});
			if(!ur.commentPresent)
				publishProgress((String[])null);
			unrarResult=ur.RarOpenArchive(params[0],params[1]);
			return null ;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(pd != null)
				pd.dismiss();
			if(unrarResult == 0)
			    processedFile.setText(getResources().getString(R.string.unrardone));//Unrar done !");
			else
			{
				switch(unrarResult)
				{
					case Unrar.ERAR_BAD_DATA :
						processedFile.setText(R.string.filecrcerr);//"Error:File CRC error !");
						break;
					case Unrar.ERAR_BAD_ARCHIVE:	
						processedFile.setText(R.string.filenotvaliderr);//"Error:File is not valid RAR archive !");
						break;	
					case Unrar.ERAR_UNKNOWN_FORMAT:	
						processedFile.setText(R.string.archiveformaterr);//"Error:Unknown archive format !");
						break;
					case Unrar.ERAR_EOPEN:
						processedFile.setText(R.string.archiveopenerr);//"Error:Archive open error !");
						break;
					case Unrar.ERAR_ECREATE:
						processedFile.setText(R.string.filecreaterr);//"Error:File create error !");
						break;
					case Unrar.ERAR_ECLOSE:	
						processedFile.setText(R.string.filecloserr);//"Error:File close error !");
						break;
					case Unrar.ERAR_EREAD:
						processedFile.setText(R.string.readerr);//"Error:Read error !");
						break;
					case Unrar.ERAR_EWRITE:
						processedFile.setText(R.string.writerr);//"Error:Write error !");
						break;
				}
			}
			if(ur.volume && !ur.firstVolume)
				processedFile.setText(processedFile.getText()+"\n"+
			        getResources().getString(R.string.notfirstvol));//"Not The First Volume!");
			if(wl!=null && wl.isHeld() )
			{
				System.out.println("Releasing WakeLock...");
				wl.release(); 
			}
		}
		
		
	}
	
	class PassWordDialog extends Dialog
	{
		Button passCancel=null,passOk=null;
		EditText passtext=null;
		Unrar unrarInst=null;
		public PassWordDialog(Context context,Unrar unrar) {
			super(context);
			setCancelable(false);
			setTitle(R.string.setpassword);
			setContentView(R.layout.pass_dialog);
			passCancel=(Button) findViewById(R.id.passcancel);
			passOk=(Button) findViewById(R.id.passok);
			passtext=(EditText) findViewById(R.id.passtext);
			unrarInst=unrar;
			
			passOk.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String pass=passtext.getText().toString();
					if(pass != null && pass.length()>0)
					{
						InputMethodManager act=(InputMethodManager) MainScreen.this.getContext()
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						if(act!= null)							
							act.hideSoftInputFromWindow(passtext.getWindowToken()
									,0);						
						unrarInst.setPassWord(pass);
						dismiss();
					}
				}
			});
			
			passCancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {					
					
						unrarInst.setPassWord(null);
						dismiss();					
				}
			});
			//this.
			setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					System.out.println("Dialog Dismissed....");
					if(!unrarInst.isPassWordSet())
					{
					   unrarInst.setPassWord(null);
					}
				}
			});
			
		}
		@Override
		public boolean onKeyLongPress(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			System.out.println("onKeyLongPress() called....");
			return true;//super.onKeyLongPress(keyCode, event);
		}
		
	}
	
	
	class ExtrPathDialog extends Dialog
	{
		Button extractBtn=null,browseBtn=null;
		TextView extrPathName=null;
		public ExtrPathDialog(Context context) {
			super(context);
			setCancelable(false);
			setTitle(getResources().getString(R.string.extractto)+"...");
			setContentView(R.layout.extr_dialog);
			extractBtn=(Button) findViewById(R.id.pathok);
			browseBtn=(Button) findViewById(R.id.browse);
			extrPathName = (TextView) findViewById(R.id.pathname);
			extrPathName.setSelected(true);
			extrPathName.setEllipsize(TruncateAt.MARQUEE);
			browseBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MainActivity act=((MainActivity)MainScreen.this.getContext());	
					act.setFileBrowserMode(FileBrowser.BROWSE_MODE_FOLDER);
					act.switchForms();
					dismiss();
				}
			});
			extractBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					UnrarTask task=new UnrarTask();
					task.execute(new String[]{path,extractionPath});
					dismiss();
				}
			});
			setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					System.out.println("Dialog Canceled....");
					
				}
			});
			setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					System.out.println("Dialog Dismissed....");
					
				}
			});
			
		}
		@Override
		public void show() {
			// TODO Auto-generated method stub
			if(extractionPath==null)
			{
				extractBtn.setEnabled(false);
				extrPathName.setText(getResources().getString(R.string.extractto)+": ");
			}
			else{
				extractBtn.setEnabled(true);
				extrPathName.setText(getResources().getString(R.string.extractto)+": "+extractionPath);
			}
			super.show();
			
		}
		
	}

}
