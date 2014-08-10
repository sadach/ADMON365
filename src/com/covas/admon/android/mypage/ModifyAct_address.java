package com.covas.admon.android.mypage;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;

import com.covas.admon.android.R;
import com.covas.admon.android.base.HeaderActivity;
import com.covas.admon.android.dao.User;
import com.covas.admon.android.util.BtnUtils;
import com.covas.admon.android.util.CitySpinnerWapper;

public class ModifyAct_address extends HeaderActivity implements OnClickListener {
	
	private Button btnNext;
	
	private Spinner spinnerSiDO;
	private Spinner spinnerGUGUN;

	@Override
	protected void init() {
		setContentView(R.layout.page_modify_address);
        setHeaderTitle(getString(R.string.modify_title_location));
        setHeaderBtnLeft(R.drawable.btn_header_back);
        
        btnNext = (Button) findViewById(R.id.join_btn_next);
        
        BtnUtils.initBtnWithTouchEffect(this, btnNext, true);
        
        spinnerSiDO = (Spinner) findViewById(R.id.spinner_sido);
        spinnerGUGUN = (Spinner) findViewById(R.id.spinner_gugun);
        
        
        //거주지 선택
        CitySpinnerWapper.init(this, spinnerSiDO, spinnerGUGUN, app.getUser().location1, app.getUser().location2);
        
	}

	@Override
	public void onClick(View v) {
		if( v.getId() == btnNext.getId() ){
			
			String province = spinnerSiDO.getSelectedItem().toString();
			String city = spinnerGUGUN.getSelectedItem().toString();
			
			if( province.equals(app.getUser().location1) && city.equals(app.getUser().location2) )
			{
				finish();
				return;
				//FIXME 수정 페이지 모두 변경사항 없을때 처리 변경 (현재 해당페이지 그냥 닫게 처리됨)
				/*Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.join_msg_address_empty), Toast.LENGTH_SHORT);
		    	toast.show();*/
			}
			
			User user = app.getUser().clone();
			
			user.location1 = province;
			user.location2 = city;
			
			app.updateUserInfo( this, user );
		}
	}
	
	@Override
	public void onHeaderBtnLeftClick() {
		super.onHeaderBtnLeftClick();
		onBackPressed();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
    
}
