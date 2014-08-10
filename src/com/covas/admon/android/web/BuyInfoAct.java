package com.covas.admon.android.web;

import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.covas.admon.android.BaseWebView;
import com.covas.admon.android.R;
import com.covas.admon.android.base.HeaderActivity;
import com.covas.admon.android.dao.User;
import com.covas.admon.android.mgr.WAS_Mgr;

public class BuyInfoAct extends HeaderActivity{

	private FrameLayout contentView;
	private BaseWebView webView;
	
	@Override
	protected void init() {
		setContentView(R.layout.page_web_common);
		setHeaderTitle(getString(R.string.app_name));
		setHeaderBtnLeft(R.drawable.btn_header_back);
		
		contentView = (FrameLayout) findViewById(R.id.page_web_common_fl_content);
		webView = new BaseWebView(this);
		contentView.addView(webView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		initPage();
	}
	
	private void initPage(){
		
		User user = app.getUser();
		String url = WAS_Mgr.WEBPAGE_BUY_HISTORY;
		url += "?"+WAS_Mgr.PARAM_UID_WEBPAGE+"="+user.id;
		
		webView.loadUrl( url );
	}
	
	@Override
	public void onHeaderBtnLeftClick() {
		super.onHeaderBtnLeftClick();
		onBackPressed();
	}
	
	@Override
	public void onBackPressed() {
		if( webView.canGoBack() ){
			webView.goBack();
			return;
		}
		super.onBackPressed();
	}

}
