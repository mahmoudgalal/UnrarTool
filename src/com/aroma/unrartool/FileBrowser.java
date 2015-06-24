


package com.aroma.unrartool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class FileBrowser extends LinearLayout {

	private Button upBtn=null,newFolder=null,okBtn=null;
	private ListView fileList=null;
	private String currentPath=null;
	private TextView listHeader=null;
	private FileListAdapter fileListAdapter=null;
	private ArrayList<FileEntry> fileEntries=null;
	public static int BROWSE_MODE_FILE=0, BROWSE_MODE_FOLDER=1;
	private int browseMode=BROWSE_MODE_FILE;
	private boolean firstTime=false;
	public static final String FILE_PICKED="com.example.poitest_filepicked";
	public FileBrowser(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.file_browser, this);
		fileList=(ListView) findViewById(R.id.filelist);
		upBtn=(Button) findViewById(R.id.path_up);
		newFolder=(Button) findViewById(R.id.new_folder);
		listHeader=(TextView) findViewById(R.id.listheader);
		listHeader.setText("");
		fileEntries=new ArrayList<FileEntry>();
		fileListAdapter=new FileListAdapter(context, fileEntries);
		fileList.setAdapter(fileListAdapter);
		fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int id,
					long arg3) {
				FileEntry fileEntry = fileEntries.get((int) id);
				if (fileEntry.getFile().isDirectory()) {
					LoadingTask task=new LoadingTask();
					task.execute(fileEntry.getFile().getAbsolutePath());					
				} 
				else
				{
					if(fileEntry.getFile().canRead())
					{
						String filename=fileEntry.getFile().getName();
						if(filename.endsWith("rar"))
						{
							MainActivity act=((MainActivity)getContext());
							act.setSelectedFile(fileEntry.getFile().getAbsolutePath());
							act.switchForms();
							/*FileBrowserActivity act=((FileBrowserActivity)getContext());
							Intent i=new Intent();
							i.putExtra(FILE_PICKED, fileEntry.getFile().getAbsolutePath());
							act.setResult(Activity.RESULT_OK,i);
							act.finish();*/
						}
					}
					
				}
			}
		});
		fileList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		upBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				File file=new File(currentPath);
				if(file.getParent()!=null)	
				{
					LoadingTask task=new LoadingTask();
					task.execute(file.getParent());
					//browseTo(file.getParent());	
				}			
				
			}
		});
		
		newFolder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
						NewFolderDialog folderDialog=new NewFolderDialog(getContext());
						folderDialog.show();				
			}
		});
	}

	public FileBrowser(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.file_browser, this);
		fileList=(ListView) findViewById(R.id.filelist);
		upBtn=(Button) findViewById(R.id.path_up);
		newFolder=(Button) findViewById(R.id.new_folder);
		okBtn = (Button) findViewById(R.id.okBtn);
		
		if(browseMode == BROWSE_MODE_FOLDER)
		{			
			okBtn.setVisibility(VISIBLE);
		}
		else
		{
			okBtn.setVisibility(GONE);
		}
		listHeader=(TextView) findViewById(R.id.listheader);
		listHeader.setText("");
		fileEntries=new ArrayList<FileEntry>();
		fileListAdapter=new FileListAdapter(context, fileEntries);
		fileList.setAdapter(fileListAdapter);
		fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int id,
					long arg3) {
				FileEntry fileEntry = fileEntries.get((int) id);
				if (fileEntry.getFile().isDirectory()) {
					LoadingTask task=new LoadingTask();
					task.execute(fileEntry.getFile().getAbsolutePath());					
				} 
				else
				{
					if(browseMode == BROWSE_MODE_FOLDER)
					{			
						
					}
					else
					if(fileEntry.getFile().canRead())
					{
						String filename=fileEntry.getFile().getName();
						if(filename.endsWith("rar"))
						{
							MainActivity act=((MainActivity)getContext());
							act.setSelectedFile(fileEntry.getFile().getAbsolutePath());
							act.switchForms();							
						}
					}
					
				}
			}
		});
		upBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				File file=new File(currentPath);
				if(file.getParent()!=null)	
				{
					LoadingTask task=new LoadingTask();
					task.execute(file.getParent());
					//browseTo(file.getParent());	
				}			
				
			}
		});
		newFolder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
						NewFolderDialog folderDialog=new NewFolderDialog(getContext());
						folderDialog.show();				
			}
		});
		okBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				File currentFile=new File(currentPath);
				if(!currentFile.isDirectory())
					return;
				if(currentFile.canWrite())
				{					
					MainActivity act=((MainActivity)getContext());
					act.setSelectedExtractionPath(currentPath);
					act.switchForms();	
				}
				
			}
		});
	}
	
	public void initializeList()
	{
		LoadingTask task=new LoadingTask();
		task.execute(Environment.getExternalStorageDirectory().getAbsolutePath());
		
	}
	public void setBrowsingMode(int mode)
	{
		browseMode=mode;
		if(browseMode == BROWSE_MODE_FOLDER)
		{			
			okBtn.setVisibility(VISIBLE);
		}
		else
		{
			okBtn.setVisibility(GONE);
		}
	}
	
	public ArrayList<FileEntry> browseTo(String location)
	{
		currentPath = location;
		File mCurrentDir=new File(location);
		ArrayList<FileEntry> fl=new ArrayList<FileEntry>();			
		
		if (mCurrentDir.getParentFile() != null)
		{
			FileEntry fentry=new FileEntry();
			fentry.setFile(mCurrentDir.getParentFile());
			fl.add(fentry);
		}
		File files[]= mCurrentDir.listFiles();
		if(files != null)
		for (File file :files) 
		{
			if (file.isDirectory()) {
				FileEntry fentry=new FileEntry();
				fentry.isDirectory=true;
				fentry.setFile(file);
				fl.add(fentry);
			} 
			else 
			{
				FileEntry fentry=new FileEntry();
				fentry.isDirectory=false;
				fentry.setFile(file);
				fl.add(fentry);
			}
		}
		return fl;
	}
	private class FileListAdapter extends ArrayAdapter<FileEntry>
	{

		public FileListAdapter(Context context,List<FileEntry> objects) {
			super(context, 0, objects);			
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			FileListItem view=(FileListItem) convertView;
			if(view == null)
			{
				view = new FileListItem(getContext());				
			}
			FileEntry fe=getItem(position);
			view.initialize(fe.fileName, fe.isDirectory?FileListItem.FILE_TYPE_FOLDER:FileListItem.FILE_TYPE_FILE);
			return view;//super.getView(position, convertView, parent);
		}
		
	}
	private class FileListItem extends LinearLayout
	{
		private TextView fileNmae=null;
		final static int FILE_TYPE_FOLDER=1;
		final static int FILE_TYPE_FILE=2;
		public FileListItem(Context context) {
			super(context);
			LayoutInflater.from(context).inflate(R.layout.list_item, this);
			fileNmae=(TextView) findViewById(R.id.file_name);
			
		}
		public void initialize(String name,int type)
		{
			fileNmae.setText(name);
			if(type == FILE_TYPE_FOLDER)
			   fileNmae.setCompoundDrawablesWithIntrinsicBounds(R.drawable.openfoldericon, 0, 0, 0);
			else
				fileNmae.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		}		
	}
	
	class FileEntry
	{
		String absolutePath=null;
		String fileName=null;
		private File file=null;
		boolean isDirectory=false;
		public void setFile(File file)
		{
			this.file=file;
			if(file != null)
			{
				absolutePath=file.getAbsolutePath();
				fileName=file.getName();
			}
		}
		public File getFile()
		{
			return file;
		}		
		
	}
	class LoadingTask extends AsyncTask<String , Void, Void>
	{
		private ArrayList<FileEntry> fe=new ArrayList<FileBrowser.FileEntry>();
		private ProgressDialog pd=null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			fileEntries.clear();
			fileListAdapter.clear();
			fileListAdapter.notifyDataSetChanged();
			if(firstTime)				
			   pd=ProgressDialog.show(getContext(), "",getResources().getString(R.string.loading), true, false);
			firstTime=true;
		}
		@Override
		protected Void doInBackground(String... params) {
			fileEntries= browseTo(params[0]);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			File mCurrentDir=new File(currentPath);
			if(listHeader != null)
			listHeader.setText(mCurrentDir.getName().compareTo("") == 0 ? mCurrentDir
					.getPath() : mCurrentDir.getName());
			if(fileList != null)
			{
				fileListAdapter=new FileListAdapter(getContext(), fileEntries);			
				fileList.setAdapter(fileListAdapter);
			}
			if(mCurrentDir.canWrite())
			{
				newFolder.setEnabled(true);
				//if(browseMode==BROWSE_MODE_FOLDER)
					okBtn.setEnabled(true);
			}
			else
			{
				newFolder.setEnabled(false);
				//if(browseMode==BROWSE_MODE_FOLDER)
					okBtn.setEnabled(false);
			}
				
			if(pd != null)
				pd.dismiss();			
		}
		
	}
	
	class NewFolderDialog extends Dialog
	{
		Button okBtn=null,cancelBtn=null;
		EditText folderName=null;

		public NewFolderDialog(Context context) {
			super(context);
			setCancelable(false);
			setTitle(R.string.newfolderdialogtitle);
			setContentView(R.layout.newfolder_dialog);
			okBtn=(Button) findViewById(R.id.folderok);
			cancelBtn=(Button) findViewById(R.id.foldercancel);
			folderName=(EditText) findViewById(R.id.foldername);
			cancelBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();					
				}
			});
			okBtn.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String newFolderName=folderName.getText().toString();
					if(newFolderName.length()>0)
					{
						File mCurrentDir=new File(currentPath+File.separator+newFolderName);
						System.out.printf("Creating Directory:%s \n",mCurrentDir.getName());
						if(!mCurrentDir.exists())
						{
							if(!mCurrentDir.mkdirs())
								Toast.makeText(getContext(), R.string.dircreationerr
										, Toast.LENGTH_LONG).show();
							else
							{
								FileEntry fentry=new FileEntry();
								fentry.isDirectory=true;
								fentry.setFile(mCurrentDir);
								fileListAdapter.add(fentry);
								fileListAdapter.notifyDataSetChanged();
							}
							dismiss();
						}
						else
						{
							Toast.makeText(getContext(),  R.string.direxists
									, Toast.LENGTH_LONG).show();
						}
							
					}
				}
			});		
			
		}
		
	}

}
