package com.covas.admon.android.util;


import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;


public class HTTPUtils implements Runnable {
	
	public static final String ENC_OPEN_API = "8859_1";	//open api 한글 encoding 방식 -.-;;;
	//public static final String ENCODING = "EUC-KR";	//open api 한글 encoding 방식 -.-;;;
	public static final String ENCODING = "UTF-8";	//open api 한글 encoding 방식 -.-;;;
	
	
	private String						_url;
	private String						_encoding;
	private Activity					_activity;
	private OnReceiveCompleteListner	_onReceiveComplete;
	
	public HTTPUtils(Activity activity, String url, String encoding) {
		_activity	= activity;
		_url		= url;
		_encoding	= encoding;
	}
	
	public HTTPUtils OnReceiveCompleteListner( OnReceiveCompleteListner onReceiveCompleteListner ) {
		_onReceiveComplete = onReceiveCompleteListner;
		return this;
	}
	
	public interface OnReceiveCompleteListner {

		public static final int SUCCESS		= 0;
		public static final int ERROR_HTTP	= 1;
		public static final int ERROR_IO 	= 2;
		public static final int ERROR_UNKNOWEN 	= 99;
		
		public void OnReceiveCompleteEvent(ReceiveCompleteEvent e);
		
	}
	
	public class ReceiveCompleteEvent {
	
		private int _statusCode;
		private String _statusDesc;
		private String _response;
		
		public ReceiveCompleteEvent(int status, String resp) {
			_statusCode = status;
			_response = resp;
			switch(_statusCode) {
				case OnReceiveCompleteListner.SUCCESS:			_statusDesc = "";	break;
				case OnReceiveCompleteListner.ERROR_HTTP:		_statusDesc = "네트워크의 연결 상태가 좋지 않았습니다. 연결 상태를 확인하신 후 다시 시도해 주세요.";	break;
				case OnReceiveCompleteListner.ERROR_IO:			_statusDesc = "네트워크의 연결 상태가 좋지 않았습니다. 연결 상태를 확인하신 후 다시 시도해 주세요.";	break;
				case OnReceiveCompleteListner.ERROR_UNKNOWEN:	_statusDesc = "수신 정보가 올바르지 않습니다. 확인하신 후 다시 시도해 주세요.";	break;
			}
		}
		
		public boolean isSuccess() {
			return _statusCode == OnReceiveCompleteListner.SUCCESS;
		}
		
		public int getSatusCode() {
			return _statusCode;
		}
		
		public String getStatus() {
			return _statusDesc;
		}
		
		public String getResponse() {
			return _response;
		}
		
	}
	
	public void send() {
		
		_activity.runOnUiThread(this);
		
	}
	
	@Override
	public void run() {
		
		try {
			
			String resp = HTTPUtils.doGet( _url, _encoding );
			
			Message msg = _handler.obtainMessage();
			msg.what = OnReceiveCompleteListner.SUCCESS;
			msg.obj	 = resp;
			
			_handler.sendMessage(msg);
			
		} catch (ClientProtocolException e) {
			
			_handler.sendMessage(Message.obtain(_handler, OnReceiveCompleteListner.ERROR_HTTP));
			//LogUtils.e(this, "run", e);

		} catch (IOException e) {
			
			_handler.sendMessage(Message.obtain(_handler, OnReceiveCompleteListner.ERROR_IO));
			//LogUtils.e(this, "run", e);
		
		} catch (Exception e) {
			
			_handler.sendMessage(Message.obtain(_handler, OnReceiveCompleteListner.ERROR_UNKNOWEN));
			//LogUtils.e(this, "run", e);
		
		}
		
	}
	
	
	
	Handler _handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			
			if ( _onReceiveComplete != null ) {

				if ( msg.what == OnReceiveCompleteListner.SUCCESS) {

					_onReceiveComplete.OnReceiveCompleteEvent( new ReceiveCompleteEvent(OnReceiveCompleteListner.SUCCESS, (String)msg.obj) );

				} else {

					_onReceiveComplete.OnReceiveCompleteEvent( new ReceiveCompleteEvent(msg.what, "") );

				}

			}

		}

	};
	
	
	public static String doGet(String url, String encoding) throws ClientProtocolException, SocketTimeoutException, IOException {
		
		String resp = HTTPUtils.doGet(url);
		
		return resp == null ? null : new String(resp.getBytes(encoding), ENCODING);	//좋은 코드 아니다.. 나중엔 바꾸자..(일단 임시방편)


	}
	
	public static String doGet(String url) throws ClientProtocolException, SocketTimeoutException, IOException {
		
		
		//LogUtils.i(HTTPUtils.class, "doGet", "send (url=%s)", url);

		return getResponse( "doGet", new HttpGet(url) );


	}
	
	public static String doPost(String url, String encoding) throws ClientProtocolException, IOException {
		
		return HTTPUtils.doPost(url, encoding, null);
	}
	
	
	public static String doPost(String url, String encoding, List<? extends NameValuePair> parameters) throws ClientProtocolException, IOException {
		
		String resp = doPost(url, parameters);
		
		return resp == null ? null : new String(resp.getBytes(encoding), ENCODING);	//좋은 코드 아니다.. 나중엔 바꾸자..(일단 임시방편)
		
	}
	
	
	public static String doPost(String url, List<? extends NameValuePair> parameters) throws ClientProtocolException, IOException {
		
		//LogUtils.i(HTTPUtils.class, "doPost", "send (url=%s)", url);
		
		//송신 파라메터 추가
		HttpPost post = new HttpPost(url);
		//post.addHeader("Content-Type", "application/json; charset=utf-8"); 
		
		if (parameters != null) {
			UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(parameters, ENCODING);
			post.setEntity(entityRequest);
		}
		

		return getResponse("doPost", post);

		
	}
	
	
	public static String doJSON(String url, org.json.JSONObject json) throws ClientProtocolException, IOException {
		
		
		//LogUtils.i(HTTPUtils.class, "doJSON", "send (url=%s, json=%s)", url, json);

		HttpPost post = new HttpPost(url);
		//post.addHeader("Content-Type", "application/json; charset=utf-8"); 
		
		if ( json != null) post.setEntity( new StringEntity( json.toString(), ENCODING) );
		
		
		return getResponse("doJSON", post);
		
		
	}
	
	
	private static HttpClient getHttpClient() {
		
		HttpClient client = new DefaultHttpClient();
		
		//클라이언트 파라메터 조정
		/*
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 5000);*/
		
		HttpParams params = client.getParams();
		params.setParameter("http.protocol.expect-continue", false);
		params.setParameter("http.connection.timeout", 7000);
		params.setParameter("http.socket.timeout", 7000);
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,HttpVersion.HTTP_1_1);
		HttpClient httpClient = new DefaultHttpClient(params); 
		
		return httpClient;
		

	}
	
	
	private static String getResponse(String method, HttpRequestBase req) throws ClientProtocolException, IOException {
		
		ResponseHandler<String> hander = new BasicResponseHandler(); 
		
		String resp = null;
		
		resp =  getHttpClient().execute(req, hander);
		
		//LogUtils.i(HTTPUtils.class, method, "response = %s", resp );
		
		return resp;
		
	}
	
}
