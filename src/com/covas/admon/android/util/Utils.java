package com.covas.admon.android.util;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.widget.Toast;

/**
 * 기웅 정의
 * 간단한 공통 모듈 모아놓은 클래스
 * @author MyHome
 *
 */
public class Utils {

	public static void showAlertNetworkError(Activity activity){
		Toast.makeText(activity, "서버와의 통신에 오류가 발생하였습니다.\n3G 또는 Wi-fi 연결 확인후 \n다시 시도해 주세요", Toast.LENGTH_SHORT).show();
	}
	
	
	public static boolean isActivity(Context context){
		
		if(context instanceof Activity){
			return true;
		}
		return false;
	}
	
	public static Activity getTopParent(Activity activity){
		if(activity.isChild()){
			activity = getTopParent(activity.getParent());
		}
		return activity;
	}
	
	public static void forceExit(Activity activity){
		if(getTopParent(activity) != null){
			getTopParent(activity).moveTaskToBack(true);
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	public static AlertDialog.Builder showSimpleAlert(Activity activity, String title, String msg, String btnText){
		activity = getTopParent(activity);
		AlertDialog.Builder ad = new Builder(activity);
		if(StringUtils.isEmpty(title)==false) ad.setTitle(title);
		if(StringUtils.isEmpty(msg)==false) ad.setMessage(msg);
		if(StringUtils.isEmpty(btnText)==false) ad.setNegativeButton(btnText, null);
		ad.show();
		
		return ad;
	}
	
	public static void toast(Activity activity, String msg){
		activity = getTopParent(activity);
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static PackageInfo getPackageInfo(Context context){
    	PackageInfo pkgInfo = null;
		try {
			pkgInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
    	return pkgInfo;
    }
	
	public static String getAndroid_OS_Version(){
    	return Build.VERSION.RELEASE.toString();
    }
	
	public static void setAniForStartActivity(){
		
	}
	
	public static String getBirthYear(String age){
		
		int _age = Integer.parseInt(age);
		int _year =  Calendar.getInstance().get(Calendar.YEAR);
		
		int birthYear = _year - _age + 1;
		
		return String.valueOf(birthYear);
	}
	
	public static int getAge(String birthYear){
		
		int _birthYear = Integer.parseInt(birthYear);
		int _year =  Calendar.getInstance().get(Calendar.YEAR);
		
		int age = _year - _birthYear + 1;
		
		return age;
	}
	
	public static String getAppVersion(Context context)
	{
    	String appVersion = "";
    	PackageInfo pinfo;
    	
		try {
			pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			appVersion = pinfo.versionName;
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		return appVersion;
	}
	
	
}
