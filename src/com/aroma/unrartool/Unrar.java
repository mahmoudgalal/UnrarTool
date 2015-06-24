package com.aroma.unrartool;

import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;

public class Unrar {
	
	static final int ERAR_END_ARCHIVE=  10;
	static final int  ERAR_NO_MEMORY=          11;
	static final int ERAR_BAD_DATA  =        12;
	static final int  ERAR_BAD_ARCHIVE =    13;
	static final int  ERAR_UNKNOWN_FORMAT=     14;
	static final int  ERAR_EOPEN =             15;
	static final int  ERAR_ECREATE =       16;
	static final int  ERAR_ECLOSE =   17;
	static final int  ERAR_EREAD=        18;
	static final int  ERAR_EWRITE  =           19;
	//static final int  ERAR_SMALL_BUF          20
	//static final int  ERAR_UNKNOWN            21
	//static final int  ERAR_MISSING_PASSWORD   22

	private volatile String passWord=null;
	volatile String  archiveComment=null;
	volatile boolean  locked=false;
	volatile boolean  signed=false;
	volatile boolean  recoveryRecord=false;
	volatile boolean  solid=false;
	volatile boolean  commentPresent=false;
	volatile boolean  volume=false;
	volatile boolean  firstVolume= true;
	private AtomicBoolean passSet=new AtomicBoolean(false);
	static {
		System.loadLibrary("unrardyn");
		init();
	}
	static native void init();
	public Unrar(Context con)
	{
		
	}
	public Unrar()
	{
		
	}
	public boolean isPassWordSet()
	{
		return passSet.get();
	}
	public void setPassWord(String pass)
	{
		
		passWord=pass;
		passSet.set(true);
	}
	public String getPassWord()
	{
		if(callListener != null)
		{
			callListener.onPassWordRequired();
			do
			{
				
			}while(!passSet.get());
		}
		return passWord;
	}
	private CallBackListener callListener=null;
	public native int RarGetArchiveItems(String fname);
	public native int RarOpenArchive(String filename,String extpath);
	public native int RarCloseArchive(int handle);
	public native int RarProcessArchive(int handle,String DestPath);
	
	public void setCallBackListener(CallBackListener listener)
	{
		callListener=listener   ;
	}
	/**
	 * Called by native code
	 * @param messageID
	 * @param message
	 */
	private void relayMessage(int messageID,String message)
	{
		if(callListener != null)
		{
			
			callListener.onFileProcessed(messageID,message);	
		}
	}
	public interface CallBackListener
	{
		void onFileProcessed(int msgID,String filename);
		void onPassWordRequired();
		
	}
	

}
