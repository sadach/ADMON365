package com.covas.admon.android.mgr;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.covas.admon.android.R;
import com.covas.admon.android.util.Utils;

/**
 * 기웅 정의 웹서비스 정보를 담는다
 * 
 * @author MyHome
 * 
 */
public class WAS_Mgr {
	
	//public static final String BASE_URL = "http://175.45.163.1:7575/";
	public static final String BASE_URL = "http://hmgmkt.cn/";
	
	public static final String BASE_WEB_SERVICE = BASE_URL+"hmgAppSvc.asmx/";
	
	// -> web service
	public static final String STORE = "http://covassoft.cafe24.com/";
	
	public static final String CHECK_UID = BASE_WEB_SERVICE+"checkUID";
	
	public static final String CHECK_PHONE = BASE_WEB_SERVICE+"checkPhone";
	
	public static final String REGISTER_MEMBER = BASE_WEB_SERVICE+"registerMember";
	
	public static final String UPDATE_MY_INFO = BASE_WEB_SERVICE+"updateMyInfo";
	
	public static final String LOGIN = BASE_WEB_SERVICE+"login";
	
	public static final String UPDATE_PUSH_TOKEN = BASE_WEB_SERVICE+"updatePushToken";
	
	public static final String GET_ITEM_CATEGORY_LIST = BASE_WEB_SERVICE+"getItemCategoryList";
	
	public static final String GET_ITEM_LIST_BY_CATEGOTY = BASE_WEB_SERVICE+"getItemListByCategory";
	
	public static final String GET_MY_INFO = BASE_WEB_SERVICE+"getMyInfo";
	// <- web service
	
	
	
	
	// -> Webview Page
	public static final String WEBPAGE_PRODUCT_DETAIL = BASE_URL+"view_detail.aspx";
	
	public static final String WEBPAGE_BUY_HISTORY = BASE_URL+"buy_history.aspx";
	
	public static final String WEBPAGE_POINT_HISTORY = BASE_URL+"point_history.aspx";
	
	public static final String WEBPAGE_NOTICE = BASE_URL+"notice.aspx";
	// <- Webview Page
	
	
	
	// -> web service result
	public static final String RESULT_KEY_RESULT = "Result";
	public static final String RESULT_KEY_MSG = "Msg";
	public static final String RESULT_KEY_DATA = "data";
	
	public static final String RESULT_MSG_SUCCESS = "1";
	public static final String RESULT_MSG_FAIL = "0";
	// <- web service result
	
	
	// -> web service param
	public static final String PARAM_UID = "uid";
	public static final String PARAM_UID_WEBPAGE = "userid";
	public static final String PARAM_EMAIL = "email";
	public static final String PARAM_ID = "id";
	public static final String PARAM_BYEAR = "byear";
	public static final String PARAM_SEX = "sex";
	public static final String PARAM_LOCATION1 = "location1";
	public static final String PARAM_LOCATION2 = "location2";
	public static final String PARAM_PWD = "passwd";
	public static final String PARAM_PHONE = "phone";
	public static final String PARAM_DEGREE = "degree";
	
	public static final String PARAM_USER_JSON = "jsonString";
	
	public static final String PARAM_PUSH_TOKEN = "token";
	
	public static final String PARAM_INVITER = "inviter";
	// <- web service param
	
	
	
	
	public static final String FAIL = "fail";
	public static final String NETWORK_ERROR = "network_error";
	
	/*public static final String		LOADING_MSG 			= "waiting..";
	public static final String		LOADING_ERROR_Title 	= "네트워크 오류";
	//public static final String		LOADING_ERROR_MSG 		= "잠시후에 다시 시도해주세요.";
*/	
	
	
	
	
	
	
	public static Boolean isFail(JSONObject rs){
		String result = "";
		try {
			result = rs.getString(RESULT_KEY_RESULT);
		} catch (JSONException e) {
			e.printStackTrace();
			return true;
		}
		return result.equalsIgnoreCase( RESULT_MSG_FAIL );
	}
	
	
	public static void showResultErrorAlert(Activity activity, String title, String msg){
		Utils.showSimpleAlert(activity, title, msg, activity.getString(R.string.txt_confirm));
	}
}
