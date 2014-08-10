package com.covas.admon.android.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;

import com.covas.admon.android.R;

/**
 * 로딩표시 다이얼로그와 비동기처리의 틀만 가지고있고
 * 실제 연산 및 완료콜백등을 개별 정의할 수 있는 클래스
 * 
 * @author 신기웅(tlsrldnd0418@naver.com)
 * @since 2012. 4. 2.
 * @version 1.0.0
 */
public class AsyncProgress extends AsyncTask<Object, Integer, Object> implements OnDismissListener{

	private OnRequestComplete _OnRequestComplete;
	private ProgressDialog _progressDialog;
	private Context context;
	private String task="";
	private String param="";
	
	private final String FAIL = "fail";
	private final String CANCEL = "cancel";
	
	private String loadingMsg = "waiting...";
	
	private boolean dialogShow = false;
	
	public interface OnRequestComplete{
		void onRequestComplete(Object result, String task, String param);
		Object process(String task, String param);
	}
	public void setOnRequestComplete(OnRequestComplete callback){
		_OnRequestComplete = callback;
	}
	
	public AsyncProgress(Context context, String task, String param){
		this.context = context;
		this.task = task;
		this.param = param;
		this.loadingMsg = context.getString(R.string.msg_loading);
	}
	public AsyncProgress(Context context){
		this(context, "", "");
	}
	
	public void setDialogVisible(boolean dialogShow){
		this.dialogShow = dialogShow;
	}
	
	@Override
	protected void onPreExecute() {
		
		if(dialogShow == true){
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
	protected Object doInBackground(Object... url) {
		
		Object value = FAIL;
		value = _OnRequestComplete.process(task, param);
		return value;
	}
	@Override
	protected void onPostExecute(Object result) {
		if(dialogShow == true) _progressDialog.dismiss();
		_OnRequestComplete.onRequestComplete(result, task, param);
		super.onPostExecute(result);
	}
	@Override
	protected void onCancelled() {
		if(dialogShow == true) _progressDialog.dismiss();
		_OnRequestComplete.onRequestComplete(CANCEL, task, param);
		super.onCancelled();
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		//this.cancel(true);
	}
}