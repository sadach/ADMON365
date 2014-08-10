/*
 * Copyright (c) DoRan Communications Co., Ltd. All rights reserved. 
 * author : 신기웅(moai.kiz@gmail.com)
 * This article may not be published, rewritten or redistributed.
 */

package com.covas.admon.android.util;

import android.util.Log;

/** 설명 
 * 
 * @author 신기웅(moai.kiz@gmail.com)
 * @since 2012. 6. 15.
 * @version 1.0.0
 */
public class Logger {
	
	private static final String TAG = "Gibigs";
	
	/**
	 * 간단하게 하나만 찍어봄
	 * @param param = 값
	 */
	public static void d(Object param){
		Log.d(TAG, param+"");
	}
	
	/**
	 * @param caller = 호출한 곳
	 * @param param = 값
	 */
	public static void d(Object caller, Object param){
		Log.d(TAG, caller.getClass().getSimpleName()+"  |  "+param);
	}
	
	/**
	 * @param caller = 호출한 곳
	 * @param param1 = 값
	 * @param param2 = 값
	 */
	public static void d(Object caller, Object param1, Object param2){
		Log.d(TAG, caller.getClass().getSimpleName()+"  |  "+param1+",  "+param2);
	}
	
	
	
	
	
	/**
	 * 간단하게 하나만 찍어봄
	 * @param param = 값
	 */
	public static void e(Object param){
		Log.e(TAG, param+"");
	}
	
	/**
	 * @param caller = 호출한 곳
	 * @param param = 값
	 */
	public static void e(Object caller, Object param){
		Log.e(TAG, caller.getClass().getSimpleName()+"  |  "+param);
	}
	
	/**
	 * @param caller = 호출한 곳
	 * @param param1 = 값
	 * @param param2 = 값
	 */
	public static void e(Object caller, Object param1, Object param2){
		Log.e(TAG, caller.getClass().getSimpleName()+"  |  "+param1+",  "+param2);
	}
	
	
	
	/**
	 * 간단하게 하나만 찍어봄
	 * @param param = 값
	 */
	public static void i(Object param){
		Log.i(TAG, param+"");
	}
	
	/**
	 * @param caller = 호출한 곳
	 * @param param = 값
	 */
	public static void i(Object caller, Object param){
		Log.i(TAG, caller.getClass().getSimpleName()+"  |  "+param);
	}
	
	/**
	 * @param caller = 호출한 곳
	 * @param param1 = 값
	 * @param param2 = 값
	 */
	public static void i(Object caller, Object param1, Object param2){
		Log.i(TAG, caller.getClass().getSimpleName()+"  |  "+param1+",  "+param2);
	}
}
