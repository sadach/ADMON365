package com.covas.admon.android.wechat;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.covas.admon.android.R;
import com.covas.admon.android.dao.User;
import com.covas.admon.android.mgr.WAS_Mgr;
import com.covas.admon.android.product.Product;
import com.covas.admon.android.util.Logger;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

/** 설명 
 * 
 * @author 신기웅(moai.kiz@gmail.com)
 * @since 2013. 11. 26.
 * @version 1.0.0
 */
public class WeChatMgr
{
	private static final String App_ID = "wxec50fe5beb5079d9";
	
	
	private static Bitmap appIconBitmap;
	
	private static IWXAPI api;
	
	private static Bitmap getThumbIcon( Context context ){
		
		if( appIconBitmap == null ){
			appIconBitmap = BitmapFactory.decodeResource( context.getResources(), R.drawable.wechat_thumb );
		}
		
		return appIconBitmap;
	}
	
	
	public static IWXAPI getIWXAPI( Context context ){
		
		if( api != null ) return api;
		
		api = WXAPIFactory.createWXAPI( context, App_ID, true ); 
		
		boolean regResult =  api.registerApp( App_ID );
		
		Logger.d( WeChatMgr.class, "init", "regResult = "+regResult );
		
		return api;
	}
	
	public static void sendMsg( Context context, String title, String desc, String url ){
		
		IWXAPI api = getIWXAPI( context );
		
		if( api.isWXAppInstalled() == false ){
			showNeedInstallAlert( context );
			return;
		}
		
		
		
		
		WXWebpageObject webpage = new WXWebpageObject();
		
		if( url != "" && url != null ){
			
			webpage.webpageUrl = url;
			
			
		}
		WXMediaMessage msg = new WXMediaMessage(webpage);
		
		msg.title = title;
		msg.description = desc;
		
		
		msg.setThumbImage( getThumbIcon( context ) );
		
		
		//Construct a req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;
		//Using API to send data to WeChat
		boolean requestResult =  api.sendReq(req);
		
		Logger.d( WeChatMgr.class, "sendMsg", "requestResult = "+requestResult );
		
		
		//api.unregisterApp();
		//if( requestResult == false && api.isWXAppInstalled() == false ) showAppStore( context );
		
		 
	}
	
	private static void showNeedInstallAlert( final Context context ){
		
		String msgStr = context.getString(R.string.wechat_alert_msg_need_install);
		String okStr = context.getString(R.string.wechat_install);
		String cancelStr = context.getString(R.string.wechat_install_cancel);
		
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setMessage(msgStr);
		ad.setNegativeButton(cancelStr, null);
		ad.setPositiveButton(okStr, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showAppStore(context);
			}
		});
		
		ad.show();
	}
	
	private static void showAppStore( final Context context ){
		
		Uri uri = Uri.parse("market://details?id=com.tencent.mm");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
		context.startActivity(intent);
	}
	
	private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
	
	public static void sendViralMsg( Context context, Product product, User user ){
		
		String title = product.itemName;
		String desc = product.defaultAdText;
		
		String encodedUserID = user.id;
		
		try
		{
			encodedUserID = URLEncoder.encode( user.id, "UTF-8" );
			Logger.d( "encodedUserID = "+encodedUserID );
		}
		catch ( UnsupportedEncodingException e )
		{
			encodedUserID = user.id;
			e.printStackTrace();
		}
		
		
		
		
		
		String url = product.landingURL + "&"+WAS_Mgr.PARAM_INVITER+"="+encodedUserID;
		WeChatMgr.sendMsg( context, title, desc, url );
	}
	
	
	
	
	
	
	/*
	private IWXAPI api;
	
	private Bitmap appIconBitmap;
	
	private boolean isRegistered = false;
	
	private static WeChatMgr _this;
	
	public synchronized static WeChatMgr getInstance( Context context ){
		if( _this == null ){
			_this = new WeChatMgr( context );
		}
		return _this;
	}
	
	private WeChatMgr( Context context ){
		init( context );
	}
	
	private void init( Context context ){
		api = WXAPIFactory.createWXAPI( context, App_ID, false ); 
		
		boolean regResult =  api.registerApp( App_ID );
		
		Logger.d( this, "init", "regResult = "+regResult );
		
		isRegistered = regResult;
		
		if( appIconBitmap == null ) initThumbIcon( context );
	}
	
	private void initThumbIcon( Context context ){
		Options opts = new Options();
		opts.inDensity = DisplayMetrics.DENSITY_XHIGH;
		appIconBitmap = BitmapFactory.decodeResource( context.getResources(), R.drawable.ic_launcher, opts );
	}
	
	public IWXAPI getIWXAPI(){
		return api;
	}
	
	public void sendMsg( Context context, long productID, String userid ){
		
		init( context );
		
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = "http://www.naver.com";
		
		WXMediaMessage msg = new WXMediaMessage(webpage);
		
		msg.title = "제목";
		msg.description = "설명";
		
		//msg.setThumbImage( appIconBitmap );
		
		
		//Construct a req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;
		//Using API to send data to WeChat
		boolean requestResult =  api.sendReq(req);
		
		Logger.d( this, "sendMsg", "requestResult = "+requestResult );
		
		if( requestResult == false && api.isWXAppInstalled() == false ) showAppStore( context );
	}
	
	private void showAppStore( Context context ){
		//com.tencent.mm
		Uri uri = Uri.parse("market://details?id=com.tencent.mm");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
		context.startActivity(intent);
	}
	
	private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	*/
}
