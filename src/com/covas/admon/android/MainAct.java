package com.covas.admon.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.covas.admon.android.base.HeaderSlideMenuActivity;
import com.covas.admon.android.product.ProductListWrapper;
import com.covas.admon.android.product.Product_Mgr;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainAct extends HeaderSlideMenuActivity {
	
	private ProductListWrapper pListWrapper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		App app = (App) getApplicationContext();
		app.initialize();
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	protected void init() {

		setContentView(R.layout.page_main);

		setHeaderTitle(getString(R.string.app_name));
		setHeaderBtnLeft(R.drawable.btn_header_menu);
		setHeaderBtnRight(R.drawable.btn_header_cate);
		
		
		
		if (app.isLogin() == false)  // 로그인정보 없음 가입페이지로 보냄
		{ 
			app.showJoinPage(this);
			finish();
			return;
		}
		else  // 로그인정보 있음 시작
		{
			app.restoreUser();
			GCMIntentService.init(this);
			
			if (getIntent().hasExtra(App.FLAG_NOT_SHOW_INTRO) == false) {
				// 스플래시(인트로)
				app.showIntro(this);
			}
		}
		
		
		initMenu();
		initList();
	}
	
	private void initList(){
		pListWrapper = new ProductListWrapper(this);
		pListWrapper.initList();
	}
	
	@Override
	protected Fragment initMenuLeft() {
		
		return new MenuFragmentLeft();
	}
	
	@Override
	protected Fragment initMenuRight() {
		return new MenuFragmentCategory();
	}

	@Override
	public void onHeaderBtnLeftClick() {
		super.onHeaderBtnLeftClick();
		getSlidingMenu().showMenu(true);
	}
	
	@Override
	public void onHeaderBtnRightClick() {
		super.onHeaderBtnRightClick();
		getSlidingMenu().showSecondaryMenu(true);
	}

	
	@Override
	public void onBackPressed() {
		if( getSlidingMenu().isMenuShowing() ){
			getSlidingMenu().toggle(true);
			return;
		}
		app.exitAlert(this);
	}

	@Override
	protected void initMenuSetting(SlidingMenu sm) {
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}
}
