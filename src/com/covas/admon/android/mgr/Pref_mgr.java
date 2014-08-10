/*
 * Copyright (c) DoRan Communications Co., Ltd. All rights reserved. 
 * author : 신기웅(moai.kiz@gmail.com)
 * This article may not be published, rewritten or redistributed.
 */

package com.covas.admon.android.mgr;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/** 설명 
 * 
 * @author 신기웅(moai.kiz@gmail.com)
 * @since 2012. 10. 11.
 * @version 1.0.0
 */
public class Pref_mgr {

	
	//Values
	public static final String 		PREF_NAME = "admon";
	
	public static final int 			DEFAULT_VALUE_INT_NULL = -1;
	public static final String 		DEFAULT_VALUE_STRING_NULL = "";
	
	
	public static final String		KEY_USER_ID = "userID";
	public static final String		KEY_USER_PWD = "pwd";
	public static final String		KEY_USER_DEGREE = "degree";
	public static final String		KEY_PUSH_TOKEN = "C2DM_REGID";
	
	
	public static final String 		KEY_USER_EMAIL = "email";
	public static final String 		KEY_USER_BYEAR = "byear";
	public static final String 		KEY_USER_SEX = "sex";
	public static final String 		KEY_USER_LOCATION1 = "location1";
	public static final String		KEY_USER_LOCATION2 = "location2";
	public static final String 		KEY_USER_PHONE = "phone";
	
	
	/*
	public static final String		KEY_USER_PHONE = "phone";
	public static final String		KEY_USER_SEQ = "userSeq";
	public static final String		KEY_USER_NAME = "name";
	public static final String		KEY_USER_GENDER = "gender";
	public static final String		KEY_USER_BIRTH_YEAR = "birth_year";
	public static final String		KEY_USER_SIDO = "sido";
	public static final String		KEY_USER_GUGUN = "gugun";
	*/
	
	
	//Functions
	public static Editor getEditor(Context context) {
		SharedPreferences sharedPreferences = getPref(context);
        Editor editor = sharedPreferences.edit();
        //Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        return editor;
    }
	
	public static SharedPreferences getPref(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences;
    }
	
	public static void setString(Context context, String key, String value) {
        Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }
	public static void setInt(Context context, String key, int value) {
        Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    
    
    public static String getString(Context context, String key) {
    	
        SharedPreferences sharedPreferences = getPref(context);
        
        return sharedPreferences.getString(key, DEFAULT_VALUE_STRING_NULL);
    }
    public static int getInt(Context context, String key) {
        SharedPreferences sharedPreferences = getPref(context);
        return sharedPreferences.getInt(key, DEFAULT_VALUE_INT_NULL);
    }

    public static void clearPreference(Context context) {
        Editor editor = getEditor(context);
        editor.clear();
        editor.commit();
    }
}
