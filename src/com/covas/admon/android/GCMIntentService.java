package com.covas.admon.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.covas.admon.android.mgr.Pref_mgr;
import com.covas.admon.android.mgr.WAS_Mgr;
import com.covas.admon.android.util.AsyncHttpRequest;
import com.covas.admon.android.util.Logger;
import com.covas.admon.android.util.StringUtils;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;


public class GCMIntentService extends GCMBaseIntentService {

	private static final String 	SENDER_ID = "659252227511";
	
    private static final String TAG = "GCMIntentService";

    private static int mLastId = 0;
    
    private static List<Integer> mActiveIdList = new ArrayList<Integer>();
    
    public static String reg_id = null;
    
    /**
     * 푸시토큰 업데이트 주기 (일수)
     */
    private static final int UPDATE_CYCLE_DAY = 1;
    
    public GCMIntentService() {
        super(SENDER_ID);
    }
    
    public static void init(Context context){
    	
    	Log.d("gcm", "gcm init()!!!");
    	//JPushInterface.onKillProcess(context);
        JPushInterface.init(context);

        
        Log.d("gcm" ,"현재 JPushInterface.getConnectionState : " + JPushInterface.getConnectionState(context));
        Log.d("gcm" , "현재 JPushInterface.getRegistrationID : " + JPushInterface.getRegistrationID(context));

    	try{
			GCMRegistrar.checkDevice(context);
	        GCMRegistrar.checkManifest(context);
	        
	        if( GCMRegistrar.isRegistered( context ) == false ){
	        	Log.d("gcm", "푸쉬토큰 요청");
	        	GCMRegistrar.register(context, SENDER_ID);
	        }else{
	        	if( GCMRegistrar.isRegisteredOnServer( context ) == false ){
		        	
		        	registerGcmKeyToServer( context, GCMRegistrar.getRegistrationId( context ) );
		        }
	        }
	        
	        
    	}catch (Exception e) {
    		Log.d("gcm", "gcm register failed");
		}
	}
    
    public static void unregister(Context context){
    	try{
    		GCMRegistrar.setRegisteredOnServer( context, false );
    		GCMRegistrar.unregister(context);
    	}catch (Exception e) {
    		Log.d("gcm", "gcm register failed");
		}
    }

    @Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
    
    @Override
    protected void onRegistered(final Context context, final String registrationId) {
        Log.i(TAG, "GCM Registered: regId = " + registrationId);
        
		registerGcmKeyToServer( context, registrationId );
        
		
    }
    
    public static void registerGcmKeyToServer( final Context context, final String registrationId ){
    	
    	final App app = (App) context.getApplicationContext();
    	
    	if( StringUtils.isEmpty( app.getUser().id ) ){
    		Logger.i( GCMIntentService.class, "func = registerGcmKeyToServer", "유저 정보 없음" );
    		return;
    	}
        
		AsyncHttpRequest ar = new AsyncHttpRequest(app, "", false);
		
		ar.addParam(WAS_Mgr.PARAM_PUSH_TOKEN, registrationId);
		ar.addParam(WAS_Mgr.PARAM_UID, app.getUser().id);
		
		ar.setOnRequestComplete( new AsyncHttpRequest.OnRequestComplete() {
			
			@Override
			public void onRequestComplete( String result, String task, List<NameValuePair> params )
			{
				if( app.checkError( null, result ) || result.contains( "success" ) == false ){
					Log.d("gcm", "푸쉬토큰 서버 등록 실패");
					GCMRegistrar.setRegisteredOnServer( context, false );
					return;
				}
				
				long lifespan = UPDATE_CYCLE_DAY * 24 * 60 * 60 * 1000;
				
				GCMRegistrar.setRegisterOnServerLifespan( context, lifespan );
				GCMRegistrar.setRegisteredOnServer( context, true );
				
			}
		} );
		
		ar.execute(WAS_Mgr.UPDATE_PUSH_TOKEN);
    }
    

    @Override
    protected void onMessage(Context context, Intent intent) {
		
    	if(intent.hasExtra("msg") == false) return;
    	
		String msg = intent.getExtras().getString("msg");
		
		notifyNewMsg(context, msg);
    }
    
    

    private void notifyNewMsg( Context ctx, String msg ){
		
    	// Notification 생성
        Notification n = new Notification();
        // 아이콘의 설정
        n.icon = R.drawable.ic_launcher;
        // tickerText의 설정
        n.tickerText = msg;
        // 발생 날짜 설정
        n.when = new Date().getTime();
        // 발생 수량 설정
        n.number = 1;
        //통지 방식
        n.defaults |= Notification.DEFAULT_SOUND;
        
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        
        String title = ctx.getString(R.string.app_name);
        
        // 확장된 상태 표시줄 표시 설정
        n.setLatestEventInfo(
    		ctx,
    		title,
            msg,
            pendingIntent(ctx));
        // Notification 발생
        NotificationManager mManager = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        mManager.notify(createNotificationId(),n);
	}
    
    private PendingIntent pendingIntent(Context ctx) {
        // 이동할 화면의 Activity
        //Intent i = new Intent(ctx, MainAct.class); 
        Intent i = this.getPackageManager().getLaunchIntentForPackage(ctx.getPackageName());
        i.addFlags( Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY );
        PendingIntent pi = PendingIntent.getActivity(ctx, 0, i, 0);
        return pi;
    }
    
    // 발생하는 Notification의 ID를 생성
    private int createNotificationId() {
        //int id = ++mLastId;
        //mActiveIdList.add(id);
        return mLastId;
    }
	
	
	
	
	@Override
    protected void onDeletedMessages(Context context, int total) {
    	
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }

}
