package com.covas.admon.android;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.covas.admon.android.util.Logger;
import com.covas.admon.android.util.NetworkStateChecker;

/**
 * 설명
 * 
 * @author 신기웅(moai.kiz@gmail.com)
 * @since 2013. 1. 18.
 * @version 1.0.0
 */
public class BaseWebView extends WebView {

	protected boolean isFinished = false;

	private ProgressBar pb;

	private Activity activity;
	
	private App app;
	
	private static final String JAVA_SCRIPT_TARGET = "android";
	
	/**
	 * @param context
	 */
	public BaseWebView(Activity activity) {
		super(activity);

		this.activity = activity;
		this.app = (App) activity.getApplicationContext();
		
		
		pb = (ProgressBar) activity.getLayoutInflater().inflate(R.layout.webview_progressbar, null);
		
		this.addView( pb, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 12) );
		
		getSettings().setCacheMode(WebSettings.LOAD_NORMAL);
		
		getSettings().setJavaScriptEnabled(true);
		// webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		setVerticalScrollbarOverlay(true);

		//getSettings().setPluginState(null);
		
		addJavascriptInterface(new BaseJavaScriptInterface(), JAVA_SCRIPT_TARGET);
		
		
		//추가한 옵션들
		getSettings().setAppCacheEnabled(true);
		getSettings().setRenderPriority(RenderPriority.HIGH);
		getSettings().setAllowFileAccess(true);
		getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		getSettings().setLoadsImagesAutomatically(true);
		
		getSettings().setLightTouchEnabled(true); 
		//getSettings().setSupportZoom(true);
		//getSettings().setBuiltInZoomControls(true);
		//getSettings().setGeolocationEnabled(true); //--- 위치 정보 사용 허용
		//getSettings().setDomStorageEnabled(true);  //--- HTML5 DOM Storage 허용
	    
	    //String appBaseDirPath = activity.getDir("", Context.MODE_PRIVATE).getAbsolutePath();
	    
	    
		
		setWebViewClient(new WebViewClient() {
			
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				
				if(NetworkStateChecker.isConnected(BaseWebView.this.activity)){
					return false;
				} else {
					showNetworkNotAvailablePage();
					return true;
				}
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				Logger.d("onPageStarted, url = " + url);
				if (isFinished == false)
					pb.setVisibility(View.VISIBLE);

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				Logger.d("onPageFinished, url = " + url);
				if (isFinished == false)
					pb.setVisibility(View.GONE);

			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				//super.onReceivedError(view, errorCode, description, failingUrl);
				Logger.d("onReceivedError, url = " + failingUrl);
				if(NetworkStateChecker.isConnected(BaseWebView.this.activity)){
					//TODO 에러 페이지 삽입
					showNetworkNotAvailablePage();
				} else {
					showNetworkNotAvailablePage();
				}
				
			}
			
			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				super.onReceivedSslError(view, handler, error);
				Logger.d("onReceivedSslError, url = " + error);
			}
			
			@Override
			public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
				super.onTooManyRedirects(view, cancelMsg, continueMsg);
				Logger.d("onTooManyRedirects, url = " + cancelMsg);
			}
		});

		setWebChromeClient(new WebChromeClient() {
			
			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				
				/**
				 * 기본 js Alert는 타이틀에 http://~ 사이트에서 발생한 메시지 라는<br>
				 * 텍스트가 찍히기 때문에 보기 좋지 않아 메시지만 받아서 직접 Alert를 보여준다.<br>
				 * result.confirm() 을 해주지 않으면 웹뷰가 먹통됨
				 */
				app.showSimpleAlert(BaseWebView.this.activity, null, message, "확인");
				result.confirm();
				return true;
			}
			
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (isFinished == false)
					pb.setProgress(newProgress);
			}

		});

	}
	
	public void showNetworkNotAvailablePage(){
		Logger.d(this, "showErrorPage()");
		//loadUrl("file:///android_asset/errorPage/networkNotAvailable.html");
		app.showErrorAlertAndFinish(activity);
	}
	
	public class BaseJavaScriptInterface {
		
		public BaseJavaScriptInterface() {}
		
		public void notifyMyFriend(String infoJson) {
			//app.notifyMyFriend(activity, infoJson);
		}
		
		/**
		 * networkNotAvailable.html 에서 '확인' 버튼을 눌렀을 경우<br>
		 * 네트워크 체크 후 처리<br>
		 *  - 연결되지 않았을 경우 : 토스트만 보여줌
		 *  - 연결됐을 경우
		 *   1. 로그인이 돼있던 상태일 경우 : 뒤로이동
		 *   2. 로그인이 돼있지 않았을 경우 :
		 *    - 자동로그인 모드일 경우 : 자동로그인 시도
		 *    - 자동로그인 모드가 아닐 경우 : 홈으로 이동
		 */
		public void checkNetWork(){
			if(NetworkStateChecker.isConnected(BaseWebView.this.activity) == false ){
				Toast.makeText(activity,"네트워크가 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
				return;
			}
			
			/*if(app.isLogin()){
				Logger.d("로그인 된 상태!!");
				if(BaseWebView.this.canGoBack()){
					BaseWebView.this.goBack();
					return;
				}
				
			}else{
				Logger.d("로그인 안된 상태!!");
				if(app.isAutoLogin()){
					Logger.d("자동 로그인인 상태!!");
					try {
						app.autoLogin();
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}*/
			
			//loadUrl("javascript:location.replace('"+AppInfo.APP_HOME_URL+"')");
		}
	}
	
	
	public void release(){
		isFinished = true;
		
		removeAllViews();
		pb = null;
	}
}
