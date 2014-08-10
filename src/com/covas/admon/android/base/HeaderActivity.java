package com.covas.admon.android.base;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.covas.admon.android.R;
import com.covas.admon.android.util.BtnUtils;

/**
 * 아이폰 스타일의(left,right버튼과 타이틀이 있는) Activity정의 (BaseActivity를 상속받음)
 * 
 * @author 신기웅(tlsrldnd0418@naver.com)
 * @since 2012. 3. 29.
 * @version 1.0.0
 */
public abstract class HeaderActivity extends BaseActivity implements HeaderActivityInterface{
	
	//protected ImageView headerTitle;
	
	//protected ImageView headerBtnLeft_l;
	//protected ImageView headerBtnLeft_r;
	protected Button headerBtnLeft;
	protected Button headerBtnRight;
	
	protected void setBase(){
	}
	
	@Override
	public void setHeaderTitle(int bgRID, int titleImgRID) {
		
		View vBG = findViewById( R.id.header_background );
		vBG.setBackgroundResource( bgRID );
		vBG.setVisibility(View.VISIBLE);
		
		View v = findViewById(R.id.header_imgv_title);
		v.setBackgroundResource(titleImgRID);
		
		app.initViewWithResize(v);
		v.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void setHeaderTitle( String titleStr ) {
		setHeaderTitle(R.drawable.header_bg_top, titleStr);
	}
	
	@Override
	public void setHeaderTitle(int bgRID, String titleStr) {
		
		View vBG = findViewById( R.id.header_background );
		vBG.setBackgroundResource( bgRID );
		vBG.setVisibility(View.VISIBLE);
		
		TextView tv = (TextView) findViewById(R.id.header_tv_title);
		tv.setText(titleStr);
		tv.setVisibility(View.VISIBLE);
	}
	
	/*@Override
	public void setHeaderBtnLeft() {
		
		setHeaderBtnLeft(R.drawable.btn_back);
		
		
		headerBtnLeft = (Button)findViewById(R.id.header_btn_left);
		headerBtnLeft.setBackgroundResource(R.drawable.btn_back);
		headerBtnLeft.setText("back");
		headerBtnLeft.setTextSize(24);
		headerBtnLeft.setVisibility(View.VISIBLE);
		
		
		headerBtnLeft_l = (ImageView)findViewById(R.id.header_btn_left_l);
		headerBtnLeft_r = (ImageView)findViewById(R.id.header_btn_left_r);
		headerBtnLeft_l.setVisibility(View.VISIBLE);
		headerBtnLeft_r.setVisibility(View.VISIBLE);
		
		//headerBtnLeft.setText(text);
		//app.initWithTouchedAni(headerBtnLeft);
		headerBtnLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onHeaderBtnLeftClick();
			}
		});
	}*/

	@Override
	public void setHeaderBtnLeft(int resourceID) {
		headerBtnLeft = (Button)findViewById(R.id.header_btn_left);
		headerBtnLeft.setBackgroundResource(resourceID);
		headerBtnLeft.setVisibility(View.VISIBLE);
		
		View.OnClickListener handler = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onHeaderBtnLeftClick();
			}
		};
		
		//app.initViewWithResize(headerBtnLeft);
		BtnUtils.initBtnWithTouchEffect(handler, headerBtnLeft, true);
	}

	@Override
	public void setHeaderBtnRight(int resourceID) {
		headerBtnRight = (Button)findViewById(R.id.header_btn_right);
		headerBtnRight.setVisibility(View.VISIBLE);
		headerBtnRight.setBackgroundResource(resourceID);
		
		View.OnClickListener handler = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onHeaderBtnRightClick();
			}
		};
		
		//app.initViewWithResize(headerBtnRight);
		BtnUtils.initBtnWithTouchEffect(handler, headerBtnRight, true);
	}

	//헤더 좌측 뒤로가기버튼 클릭
	@Override
	public void onHeaderBtnLeftClick() {
		hideKeyboard();
	}

	//헤더 우측 버튼 클릭
	@Override
	public void onHeaderBtnRightClick() {
		hideKeyboard();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		hideKeyboard();
	}
	
	@Override
	protected void onDestroy() {
		/*headerBtnLeft.setOnClickListener(null);
		headerBtnRight.setOnClickListener(null);
		headerTitle = null;
		headerBtnLeft = null;
		headerBtnLeft_l = null;
		headerBtnLeft_r = null;
		headerBtnRight = null;*/
		
		super.onDestroy();
	}
}