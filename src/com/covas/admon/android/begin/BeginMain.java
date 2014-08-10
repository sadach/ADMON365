package com.covas.admon.android.begin;

import com.covas.admon.android.App;
import com.covas.admon.android.base.FlipperBase;

/**
 * 회원가입단계를 담는 회원가입 메인클래스로 가입 시 입력받은 
 * 모든 데이터 및 회원가입 정보를 담고있다가 완료시 일괄 처리하는 클래스 
 * 
 * @author 신기웅(tlsrldnd0418@naver.com)
 * @since 2012. 4. 2.
 * @version 1.0.0
 */
public class BeginMain extends FlipperBase {
	
	@Override
	protected void init() {
		
		if(getIntent().hasExtra(App.FLAG_NOT_SHOW_INTRO) == false){
			app.showIntro(this);
		}
		
		addActivity(newIntent(BeginAct.class));
	}
	
	@Override
	public void onBackPressed() {
		
		popActivity();
	}
}