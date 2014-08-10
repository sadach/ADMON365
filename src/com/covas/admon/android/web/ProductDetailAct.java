package com.covas.admon.android.web;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.covas.admon.android.BaseWebView;
import com.covas.admon.android.R;
import com.covas.admon.android.base.HeaderActivity;
import com.covas.admon.android.dao.User;
import com.covas.admon.android.mgr.WAS_Mgr;
import com.covas.admon.android.product.Product;
import com.covas.admon.android.product.Product_Mgr;
import com.covas.admon.android.util.BtnUtils;
import com.covas.admon.android.wechat.WeChatMgr;

public class ProductDetailAct extends HeaderActivity implements OnClickListener{

	private FrameLayout contentView;
	private BaseWebView webView;
	
	private Product product;
	
	private Button btnNotify;
	private TextView tvDesc;
	
	private LinearLayout llNotiContainer;
	
	@Override
	protected void init() {
		setContentView(R.layout.page_product_detail);
		setHeaderTitle(getString(R.string.webview_title_product_detail));
		setHeaderBtnLeft(R.drawable.btn_header_back);
		setHeaderBtnRight(R.drawable.product_detail_header_btn_home);
		
		contentView = (FrameLayout) findViewById(R.id.product_detail_fl_container);
		webView = new BaseWebView(this);
		contentView.addView(webView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		llNotiContainer = (LinearLayout) findViewById(R.id.product_detail_ll_container_notify);
		btnNotify = (Button) findViewById(R.id.product_detail_btn_notify);
		tvDesc = (TextView) findViewById(R.id.product_detail_tv_desc);
		
		btnNotify.setClickable( false );
		btnNotify.setFocusable( false );
		
		//BtnUtils.initBtnWithTouchEffect(this, btnNotify, true);
		//BtnUtils.initBtnWithTouchEffect(this, tvDesc, true);
		BtnUtils.initBtnWithTouchEffect(this, llNotiContainer, true);
		
		initPage();
	}
	
	private void initPage(){
		long item_seq = getIntent().getLongExtra(Product.PARAM_ITEM_SEQ, 0);
		
		if( item_seq == -1 ){
			
		}
		
		product = Product_Mgr.getInstance().getProduct(item_seq);
		
		String rewardVal = product.getGradeAdFee(app.getUser().degree);
		
		String btnPointText = getString(R.string.product_friend_notify_head)+rewardVal+getString(R.string.product_friend_notify_tail);
		//FIXME 친구 구입 시 리워드 받아서 처리하는걸로 변경해라
		String btnBuyRewardText = getString(R.string.product_friend_buy_head)+product.getGradeRewardRate(app.getUser().degree)+"%"+getString(R.string.product_friend_buy_tail);
		
		String rewardTxt = btnPointText+"\n"+btnBuyRewardText;
		
		tvDesc.setText(rewardTxt);
		
		User user = app.getUser();
		String url = WAS_Mgr.WEBPAGE_PRODUCT_DETAIL;
		url += "?"+WAS_Mgr.PARAM_UID_WEBPAGE+"="+user.id;
		url += "&"+Product.PARAM_ITEM_SEQ+"="+item_seq;
		
		webView.loadUrl( url );
	}
	
	@Override
	public void onHeaderBtnLeftClick() {
		super.onHeaderBtnLeftClick();
		onBackPressed();
	}
	
	@Override
	public void onHeaderBtnRightClick() {
		super.onHeaderBtnRightClick();
		super.onBackPressed();
	}
	
	@Override
	public void onBackPressed() {
		if( webView.canGoBack() ){
			webView.goBack();
			return;
		}
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		if( v.getId() == llNotiContainer.getId() ){
			User user = app.getUser();
			WeChatMgr.sendViralMsg( this, product, user );
		}
	}

}
