/*
 * Copyright (c) DoRan Communications Co., Ltd. All rights reserved. 
 * author : 신기웅(moai.kiz@gmail.com)
 * This article may not be published, rewritten or redistributed.
 */

package com.covas.admon.android.util;


import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;


/** 설명 
 * 
 * @author 신기웅(moai.kiz@gmail.com)
 * @since 2012. 6. 12.
 * @version 1.0.0
 */

public class BtnUtils {
	
	//private static Animation scaleDownAni;
	//private static Animation scaleUpAni;
	
	private static final float 	SCALE_DOWN_VALUE 	= 0.96f;
	private static final long 	SCALE_ANI_DURATION 	= 40;
	
	public static View initBtnWithTouchEffect(OnClickListener onClickListener, View view, boolean shader){
		if(shader){ view.setOnTouchListener(touchedEffectHandlerWithShader);}
		else{	    view.setOnTouchListener(touchedEffectHandler);}
		view.setOnClickListener(onClickListener);
		return view;
	}
	public static View initBtnWithTouchEffect(View view, boolean shader){
		if(shader){ view.setOnTouchListener(touchedEffectHandlerWithShader);}
		else{	    view.setOnTouchListener(touchedEffectHandler);}
		return view;
	}
	
	public static View initWithTouchedShader(OnClickListener onClickListener, View view){
		view.setOnTouchListener(touchedShaderHandler);
		view.setOnClickListener(onClickListener);
		return view;
	}
	public static View initWithTouchedShader(View view){
		view.setOnTouchListener(touchedShaderHandler);
		return view;
	}

	private static void downEffect(View v){
		
		/*ColorMatrix cm = new ColorMatrix(
                new float[] {
                        -1, 0, 0, 0, 255,
                        0, -1, 0, 0, 255,
                        0, 0, -1, 0, 255,
                        0, 0, 0, 1, 0 }
        );
		
        v.getBackground().mutate().setColorFilter(new ColorMatrixColorFilter(cm));*/
		
		Drawable drwb;
		if(v instanceof ImageView){
			drwb = ((ImageView)v).getDrawable();
		}else {
			drwb = v.getBackground();
		}
		
		if(drwb == null) return;
		drwb.mutate().setColorFilter(0xffaaaaaa, PorterDuff.Mode.MULTIPLY);
    	
		v.invalidate();
    }
	private static void upEffect(View v){
    	//if(v.getBackground() == null) return;
    	
		Drawable drwb;
		if(v instanceof ImageView){
			drwb = ((ImageView)v).getDrawable();
			
		}else {
			drwb = v.getBackground();
		}
		
		if(drwb == null) return;
    	
		drwb.clearColorFilter();
		v.invalidate();
    }
	
    /*private static void downEffect2(View v){
		if(v.getBackground() == null || v.getTag() == null) return;
		int[] resID = (int[]) v.getTag();
    	v.setBackgroundResource(resID[1]);
		//v.invalidate();
    }
	private static void upEffect2(View v){
    	if(v.getBackground() == null || v.getTag() == null) return;
    	int[] resID = (int[]) v.getTag();
    	v.setBackgroundResource(resID[0]);
		//v.invalidate();
    }*/
    
    private static boolean isPointerOutside(View v, MotionEvent event){
    	float x = event.getX();
    	float y = event.getY();
    	if(x < 0 || x > v.getWidth() || y < 0 || y > v.getHeight()) return true;
		return false;
    }
	
    private static OnTouchListener touchedEffectHandler = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				scaleDownAni(v);
			}else if(event.getAction() == MotionEvent.ACTION_UP || isPointerOutside(v, event) == true){
				scaleUpAni(v);
			}
			return false;
		}
	};
    
	private static OnTouchListener touchedEffectHandlerWithShader = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				BtnUtils.downEffect(v);
				scaleDownAni(v);
			}else if(event.getAction() == MotionEvent.ACTION_UP || isPointerOutside(v, event) == true){
				BtnUtils.upEffect(v);
				scaleUpAni(v);
			}
			return false;
		}
	};
	
	private static OnTouchListener touchedShaderHandler = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				BtnUtils.downEffect(v);
			}else if(event.getAction() == MotionEvent.ACTION_UP || isPointerOutside(v, event) == true){
				BtnUtils.upEffect(v);
			}
			return false;
		}
	};
	
	/*private static OnTouchListener touchHandler2 = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				BtnUtils.downEffect2(v);
				scaleDownAni(v);
				
			}else if(event.getAction() == MotionEvent.ACTION_UP || isPointerOutside(v, event) == true){
				BtnUtils.upEffect2(v);
				scaleUpAni(v);
			}
			
			return false;
		}
	};*/
	
	private static void scaleDownAni(View v){
		/*if(scaleDownAni == null){
			scaleDownAni = new ScaleAnimation(1.0f, SCALE_DOWN_VALUE, 1.0f, SCALE_DOWN_VALUE, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			scaleDownAni.setDuration(SCALE_ANI_DURATION);
			scaleDownAni.setFillAfter(true);
		}*/
		
		ScaleAnimation scaleDownAni = new ScaleAnimation(1.0f, SCALE_DOWN_VALUE, 1.0f, SCALE_DOWN_VALUE, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleDownAni.setDuration(SCALE_ANI_DURATION);
		scaleDownAni.setFillAfter(true);
		
		//v.startAnimation(scaleDownAni);
		//v.setAnimation(scaleDownAni);
		//scaleDownAni.startNow();
		v.startAnimation(scaleDownAni);
		
		/*v.animate().scaleX(SCALE_DOWN_VALUE);
		v.animate().scaleY(SCALE_DOWN_VALUE);
		v.animate().setDuration(SCALE_ANI_DURATION);
		v.animate().start();*/
		
		//v.setScaleX(SCALE_DOWN_VALUE);
		//v.setScaleY(SCALE_DOWN_VALUE);
	}
	
	private static void scaleUpAni(View v){
		/*if(scaleUpAni == null){
			scaleUpAni = new ScaleAnimation(SCALE_DOWN_VALUE, 1.0f, SCALE_DOWN_VALUE, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			scaleUpAni.setDuration(SCALE_ANI_DURATION);
			scaleUpAni.setFillAfter(true);
		}
		
		//v.startAnimation(scaleUpAni);
		v.setAnimation(scaleUpAni);
		scaleUpAni.startNow();*/
		
		//v.getAnimation().cancel();
		//v.getAnimation().reset();
		/*v.clearAnimation();
		v.clearFocus();*/
		
		/*v.clearAnimation();
		v.setAnimation(null);*/
		if( v.getAnimation() != null ){
			v.getAnimation().cancel();
			v.getAnimation().reset();
		}
		
		v.clearAnimation();
		v.setAnimation(null);
		
		/*v.animate().scaleX(1.0f);
		v.animate().scaleY(1.0f);
		v.animate().setDuration(SCALE_ANI_DURATION);
		v.animate().start();*/
		
		//v.setScaleX(1.0f);
		//v.setScaleY(1.0f);
	}
}
