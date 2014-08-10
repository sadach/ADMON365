package com.covas.admon.android.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * 네트워크 통신이 가능한 상태인지 체크하는 클래스<br/>
 * 3g, 4g, wifi 체크
 * 
 * @author 신기웅(tlsrldnd0418@naver.com)
 * @since 2012. 4. 2.
 * @version 1.0.0
 */
public class NetworkStateChecker {

	public static boolean isConnected(Context context){
		
		boolean flag = false;
		
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if (cm.getActiveNetworkInfo() != null) {
			
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			
			/*switch (activeNetwork.getType()) {
				case ConnectivityManager.TYPE_WIMAX: // 4g 망 체크
				flag = true;
				break;
			case ConnectivityManager.TYPE_WIFI: // wifi망 체크
				flag = true;
				break;
			case ConnectivityManager.TYPE_MOBILE: // 3g 망 체크
				flag = true;
				break;
			}*/
			
			if(activeNetwork.isConnected()){
				flag = true;
			}
			
		}/*else{
			Toast.makeText(context,"네트워크가 연결되어 있지 않습니다.", 1).show();
		}*/
		return flag;
	}
	
	public static boolean checkAndResult(Context context){
		
		boolean flag = false;
		
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if (cm.getActiveNetworkInfo() != null) {
			
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
				switch (activeNetwork.getType()) {
				case ConnectivityManager.TYPE_WIMAX: // 4g 망 체크
				flag = true;
				break;
			case ConnectivityManager.TYPE_WIFI: // wifi망 체크
				flag = true;
				break;
			case ConnectivityManager.TYPE_MOBILE: // 3g 망 체크
				flag = true;
				break;
			}
		}else{
			Toast.makeText(context,"네트워크가 연결되어 있지 않습니다.", 1).show();
		}
		return flag;
	}
}
