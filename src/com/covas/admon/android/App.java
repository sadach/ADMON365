package com.covas.admon.android;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Toast;

import com.covas.admon.android.begin.BeginMain;
import com.covas.admon.android.begin.Intro;
import com.covas.admon.android.dao.User;
import com.covas.admon.android.mgr.Pref_mgr;
import com.covas.admon.android.mgr.WAS_Mgr;
import com.covas.admon.android.product.Product_Mgr;
import com.covas.admon.android.util.AES256Cipher;
import com.covas.admon.android.util.AsyncHttpRequest;
import com.covas.admon.android.util.Image_Mgr;
import com.covas.admon.android.util.Logger;
import com.covas.admon.android.util.StringUtils;
import com.covas.admon.android.util.Utils;
import com.covas.admon.android.wechat.WeChatMgr;
import com.google.android.gcm.GCMRegistrar;

public class App extends Application {
	
	public String isAppInitialized = "";
	
	public float displayWidth = 0;
	public float displayHeight = 0;

	private final float STANDARD_WIDTH = 800;
	private final float STANDARD_HEIGHT = 1280;

	public int dpi = 0;

	private User user = new User();
	
	public static final String FLAG_NOT_SHOW_INTRO = "flag_show_intro";
	
	public 	Product_Mgr 	productMgr;
	private  Image_Mgr	 	imgMgr;
	
	
	private WeChatMgr	wechatMgr;
	
	@Override
	public void onCreate() {
		super.onCreate();

		initDisplayInfo();
		
		productMgr = Product_Mgr.getInstance();
		imgMgr = new Image_Mgr(this);
		
		//wechatMgr = WeChatMgr.getInstance( this );
	}
	
	/**
	 * 어플 메인 액티비티에서 반드시 호출해 주어야 하는 함수<br>
	 * 어플이 정상적으로 실행됐는지 판별하는 변수를 초기화해줌<br>
	 * <br>
	 * 타스크 킬러등의 메모리관리 프로그램에 의해 비정상적으로 강제 종료 됐을 경우<br>
	 * 마지막으로 활성화됐던 액티비티 부터 시작되게 되는데<br>
	 * 이 때는 어플의 어떤 내용도 없는 상태이기 때문에<br>
	 * 의미없이 실행 된 마지막 상태없이 실행 된 액티비티를 종료시켜 주기 위한 체크 변수<br>
	 * BaseActivity 에서 onCreate시 이 값을 체크하여 처리
	 */
	public void initialize(){
		isAppInitialized = "ok";
		//isLogin = false;
	}
	/**
	 * 어플이 구동될 수 있도록 기본 정보가 초기화 되었는지 체크하는 변수<br>
	 * <br>
	 * ex) 타스크 킬러등의 메모리 관리 프로그램에 의해 <br>
	 * 	    비정상적으로 강제 종료됐었는지 체크
	 */
	public boolean isInitialized(){
		return ! StringUtils.isEmpty(isAppInitialized);
	}
	
	public boolean isLogin(){
		String userId = Pref_mgr.getString(this, Pref_mgr.KEY_USER_ID);
		
		return ! StringUtils.isEmpty( userId );
	}
	
	public void logout(Activity activity) {

		Editor editor =Pref_mgr.getEditor(activity);
		editor.clear();
		editor.commit();

		GCMIntentService.unregister(this);
		
		/**
		 * WAS 서버에 푸시토큰 등록 해제 요청
		 */
		GCMIntentService.registerGcmKeyToServer( this, "" );
		
		Intent intent = new Intent(activity, BeginMain.class);
		intent.putExtra(FLAG_NOT_SHOW_INTRO, true);
		activity.startActivity(intent);
		
		activity.overridePendingTransition(R.anim.push_up_in, R.anim.hold);
		activity.finish();
	}
	
	
	public void restoreUser() {

		user.id = Pref_mgr.getString(this, Pref_mgr.KEY_USER_ID);
		user.degree = Pref_mgr.getString(this, Pref_mgr.KEY_USER_DEGREE);
		
		user.byear = Pref_mgr.getString(this, Pref_mgr.KEY_USER_BYEAR);
		user.email = Pref_mgr.getString(this, Pref_mgr.KEY_USER_EMAIL);
		user.location1 = Pref_mgr.getString(this, Pref_mgr.KEY_USER_LOCATION1);
		user.location2 = Pref_mgr.getString(this, Pref_mgr.KEY_USER_LOCATION2);
		user.phone = Pref_mgr.getString(this, Pref_mgr.KEY_USER_PHONE);
		user.sex = Pref_mgr.getString(this, Pref_mgr.KEY_USER_SEX);
		
		String encodedpwd = Pref_mgr.getString(this, Pref_mgr.KEY_USER_PWD);;
		try {
			user.passwd = AES256Cipher.AES_Decode(encodedpwd, AES256Cipher.key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Logger.d(user.toString());
	}

	public void saveUser(User user) {
		this.user.copy( user );

		Editor editor = Pref_mgr.getEditor(this);
		editor.putString(Pref_mgr.KEY_USER_ID, user.id);
		editor.putString(Pref_mgr.KEY_USER_DEGREE, user.degree);
		
		editor.putString(Pref_mgr.KEY_USER_BYEAR, user.byear);
		editor.putString(Pref_mgr.KEY_USER_EMAIL, user.email);
		editor.putString(Pref_mgr.KEY_USER_LOCATION1, user.location1);
		editor.putString(Pref_mgr.KEY_USER_LOCATION2, user.location2);
		editor.putString(Pref_mgr.KEY_USER_PHONE, user.phone);
		editor.putString(Pref_mgr.KEY_USER_SEX, user.sex);
		
		String encodedpwd = user.passwd;
		try {
			encodedpwd = AES256Cipher.AES_Encode(user.passwd, AES256Cipher.key);
		} catch (Exception e) {
			e.printStackTrace();
			encodedpwd = user.passwd;
		}
		editor.putString(Pref_mgr.KEY_USER_PWD, encodedpwd);
		editor.commit();
	}

	public User getUser() {
		return user;
	}
	
	public void updateUserInfo(final Activity activity, final User user){
		
		AsyncHttpRequest ar = new AsyncHttpRequest(activity);
		
		JSONObject userJson = null;
		try {
			userJson = user.getUserJson();
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		ar.addParam(WAS_Mgr.PARAM_USER_JSON, userJson.toString());
		
		ar.setOnRequestComplete(new AsyncHttpRequest.OnRequestComplete() {
			
			@Override
			public void onRequestComplete(String result, String task, List<NameValuePair> params) {
				if( checkError(activity, result) ) return;
				
				try {
					JSONObject rs = new JSONObject(result);
					
					if( WAS_Mgr.isFail(rs) ) {
						//TODO 가입 실패한 경우  실패 이유 서버에서 받아서 보여주게 처리
						
						String errorMsg = rs.getString(WAS_Mgr.RESULT_KEY_MSG);
						WAS_Mgr.showResultErrorAlert(activity, "", errorMsg);
						return;
					}
					
					
					saveUser( user );
					
					AlertDialog.Builder ad = new Builder(activity);
					//ad.setTitle(title);
					ad.setMessage( getString(R.string.modify_msg_modify_complete) );
					ad.setNegativeButton( getString(R.string.txt_confirm) , new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							activity.finish();
						}
					});
					ad.show();
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		ar.execute(WAS_Mgr.UPDATE_MY_INFO);
	}
	
	
	

	public void showJoinPage(Activity activity) {
		activity = getTopParent(activity);

		Intent intent = new Intent(activity, BeginMain.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.hold, R.anim.hold);
		activity.finish();
	}

	public void showIntro(Activity activity) {
		activity = getTopParent(activity);

		Intent intent = new Intent(activity, Intro.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.hold, R.anim.hold);
	}
	
	public void showLogoutDialog(Activity activity){
		
		String titleStr = getString(R.string.logout_alert_msg);
		String okStr = getString(R.string.logout_alert_ok);
		String cancelStr = getString(R.string.logout_alert_cancel);
		
		
		final Activity _activity = getTopParent(activity);
		AlertDialog.Builder ad = new AlertDialog.Builder(_activity);
		ad.setIcon(R.drawable.ic_launcher);
		ad.setTitle(titleStr);
		ad.setNegativeButton(cancelStr, null);
		ad.setPositiveButton(okStr, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				logout(_activity);
			}
		});
		ad.show();
	}
	
	public void startMain(Activity activity){
		activity = getTopParent(activity);
		Intent intent = new Intent(activity, MainAct.class);
		intent.putExtra(App.FLAG_NOT_SHOW_INTRO, true);
		
		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.hold, R.anim.push_down_out);
		activity.finish();
	}
	
	
	public Image_Mgr getImgMgr(){
		return imgMgr;
	}
	
	
	
	

	/**
	 * 뷰의 백그라운드 이미지의 원래 사이즈를 구한 뒤 <br>
	 * 현재 디스플레이 사이즈에 맞게 리사이즈하여 리턴해줌
	 * 
	 * @param btn
	 * @param lp
	 * @return
	 */
	public View initViewWithResize(View view) {
		view.setLayoutParams(setLayout(view));
		return view;
	}

	/**
	 * drawable 리소스의 원래 사이즈를 구해 디스플레이 사이즈에 맞게 계산하여 LayoutParams 리턴
	 * 
	 * @param drawable
	 * @param lp
	 * @return
	 */
	private LayoutParams setLayout(View view) {

		Drawable drawable = view.getBackground();
		LayoutParams lp = view.getLayoutParams();

		float w = displayWidth / STANDARD_WIDTH;
		float h = displayHeight / STANDARD_HEIGHT;

		lp.width = (int) (((float) drawable.getIntrinsicWidth()) * w);
		lp.height = (int) (((float) drawable.getIntrinsicHeight()) * h);

		return lp;
	}

	/**
	 * 디스플레이 pixel 및 dpi를 구함
	 */
	protected void initDisplayInfo() {

		if (dpi != 0) return;

		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);

		displayWidth = metrics.widthPixels;
		displayHeight = metrics.heightPixels;

		dpi = metrics.densityDpi;

	}

	public Activity getTopParent(Activity activity) {
		return Utils.getTopParent(activity);
	}

	public void onExit() {
		try{
			GCMRegistrar.onDestroy(this);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void onTerminate() {
		onExit();
		super.onTerminate();
	}

	public boolean checkError(Activity activity, String result) {
		if (result.equals(WAS_Mgr.NETWORK_ERROR)) {

			if( activity != null )showErrorAlert(activity);
			return true;
		}
		return false;
	}

	public void showErrorAlert(Activity activity) {
		showSimpleAlert(activity, getString(R.string.network_error_title),
				getString(R.string.network_error_msg), getString(R.string.txt_confirm));
	}

	public void showErrorAlertAndFinish(final Activity activity) {
		AlertDialog.Builder ad = new Builder(activity);
		ad.setTitle( getString(R.string.network_error_title) );
		ad.setMessage( getString(R.string.network_error_msg) );
		ad.setNegativeButton(getString(R.string.txt_confirm), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.finish();
			}
		});

		ad.show();
	}
	

	public AlertDialog.Builder showSimpleAlert(Activity activity, String title,
			String msg, String btnText) {
		return Utils.showSimpleAlert(activity, title, msg, btnText);
	}

	public void exitAlert(Activity activity) {

		final Activity rootAct = getTopParent(activity);

		AlertDialog.Builder exitAlert = new Builder(rootAct);
		exitAlert.setIcon(R.drawable.ic_launcher);
		exitAlert.setTitle( getString(R.string.msg_ask_exit) );
		exitAlert.setPositiveButton( getString(R.string.txt_exit) ,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						exit(rootAct);
					}
				});
		exitAlert.setNegativeButton( getString(R.string.txt_cancel) , null);
		exitAlert.show();
	}

	public void exit(Activity activity) {

		onExit();

		activity.finish();
	}

	
	
	
	

	public String getAppVersion() {
		PackageInfo pi = getPackageInfo();
		if (pi == null) return "0.0";
		return pi.versionName;
	}

	public int getAppVersionCode() {
		PackageInfo pi = getPackageInfo();
		if (pi == null) return -1;
		return pi.versionCode;
	}

	public PackageInfo getPackageInfo() {
		return Utils.getPackageInfo(this);
	}
}
