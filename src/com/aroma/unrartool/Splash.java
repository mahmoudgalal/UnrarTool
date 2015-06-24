package com.aroma.unrartool;

import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

public class Splash extends FrameLayout {

	Animation upAnim=null,downAnim=null;
	AnimationListener upAnimListener=null,downAnimListener=null;
	Runnable closeRunnable=null;
	private AtomicBoolean downAnimRunning=null,upAnimRunning=null;
	private AtomicBoolean splashEnded=new AtomicBoolean(false);
	public Splash(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.splash, this);
		setClickable(true);
		downAnimRunning=new AtomicBoolean(false);
		upAnimRunning=new AtomicBoolean(false);
		upAnim=AnimationUtils.loadAnimation(context, R.anim.move_up);
		downAnim= AnimationUtils.loadAnimation(context, R.anim.move_down);
		upAnimListener=new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				splashEnded.set(false);
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				/*ViewGroup parent=(ViewGroup) getParent();
				if(parent != null)
				{
					System.out.println("Removing The Splash....");
					parent.removeView(Splash.this);
				}*/
				
				
				setVisibility(GONE);
				setClickable(false);
				upAnimRunning.set(false);
				splashEnded.set(true);
			}
		};
		upAnim.setAnimationListener(upAnimListener);
		
		downAnimListener=new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				downAnimRunning.set(true);
				setClickable(true);
				setVisibility(VISIBLE);
				splashEnded.set(false);
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				
				if(closeRunnable!=null)
				{
					postDelayed(closeRunnable, 1100);
					//splashEnded.set(true);
				}
				
			}
		};
		downAnim.setAnimationListener(downAnimListener);
	}
	
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		upAnimRunning.set(true);
		postDelayed(new Runnable() {
			
			@Override
			public void run() {
				startAnimation(upAnim);
				
			}
		}, 3000);
	}
	
	public boolean isSplashEnded()
	{
		return splashEnded.get();
	}
	public void closeSplash(Runnable close )
	{
		if(downAnimRunning.get() || upAnimRunning.get())
			return;
		closeRunnable=close;		
		downAnimRunning.set(true);
		startAnimation(downAnim);
	}

}
