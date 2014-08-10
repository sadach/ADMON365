package com.covas.admon.android.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;

import com.covas.admon.android.R;
import com.covas.admon.android.mgr.WAS_Mgr;

/**
 * HTTP 통신을 비동기적으로 처리해주는 클래스 
 * 
 * @author 신기웅(tlsrldnd0418@naver.com)
 * @since 2012. 4. 2.
 * @version 1.0.0
 */
public class AsyncHttpRequest extends AsyncTask<String, Integer, String> implements OnDismissListener{

	private OnRequestComplete _OnRequestComplete;
	private ProgressDialog _progressDialog;
	private Context context;
	private String requestURL = "";
	private String task="";
	private List<NameValuePair> params;
	
	private final String ERROR = WAS_Mgr.NETWORK_ERROR;
	private final String CANCEL = "cancel";
	
	private String loadingMsg = "waiting...";
	
	//private String _result="";
	
	private boolean isCancel = false;
	
	private boolean dialogShow = true;
	
	public interface OnRequestComplete{
		
		void onRequestComplete(String result, String task, List<NameValuePair> params);
	}
	public void setOnRequestComplete(OnRequestComplete callback){
		_OnRequestComplete = callback;
	}
	
	public AsyncHttpRequest(Context context){
		this(context, "default", true);
	}
	
	public AsyncHttpRequest(Context context, String task){
		this(context, task, true);
	}
	
	public AsyncHttpRequest(Context context, String task, boolean dialogShow){
		this.context = context;
		this.task = task;
		this.dialogShow = dialogShow;
		this.loadingMsg = context.getString(R.string.msg_loading);
	}
	
	
	
	public void addParam(String key, String value){
		
		if(params == null) params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(key, value));
	}
	
	@Override
	protected void onPreExecute() {
		if(dialogShow && isCancel == false){
			
			_progressDialog = new ProgressDialog(context);
			//_progressDialog.setInverseBackgroundForced(true);
			_progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			_progressDialog.setOnDismissListener(this);
			_progressDialog.setMessage( loadingMsg );	
			_progressDialog.setCancelable(false);
			_progressDialog.show();
		}
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... url) {
		requestURL = url[0];
		Logger.d("- HTTP Request - \n"
					+"requestURL = "+requestURL+"\n"
					+"params = "+params);
		String result = ERROR;
		
		try {
			if(params == null){
				result = HTTPUtils.doGet(url[0]);
			}else{
				result = HTTPUtils.doPost(url[0], params);
			}
			
		} catch (Exception e) {
			result = ERROR;
			e.printStackTrace();
		}
		
		return result;
	}
	@Override
	protected void onPostExecute(String result) {
		Logger.d("- HTTP Request (Result)- \n"
				+"requestURL = "+requestURL+"\n"
				+"result = "+result);
		
		if(dialogShow && isCancel == false) _progressDialog.dismiss();
		/*if(result.equals(FAIL)){
			errorAlert();
			return;
		}*/
		if(isCancel == false){
			if(_OnRequestComplete != null)_OnRequestComplete.onRequestComplete(result, task, params);
		}
		super.onPostExecute(result);
	}
	@Override
	protected void onCancelled() {
		
		if(dialogShow) _progressDialog.dismiss();
		if(isCancel == false){
			if(_OnRequestComplete != null)_OnRequestComplete.onRequestComplete(CANCEL, task, params);
		}
		super.onCancelled();
	}
	
	public void cancel(){
		this.isCancel = true;
	}
	
	/*private void errorAlert() {
		FRENzApp app = (FRENzApp)context.getApplicationContext();
		AlertDialog.Builder ad = new Builder(app.getMainAct());
		ad.setTitle(AppInfo.LOADING_ERROR_Title);
		ad.setMessage(AppInfo.LOADING_ERROR_MSG);
		ad.setNegativeButton("확인", null);
		ad.show();
	}*/
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		
		//this.cancel(true);
	}
}