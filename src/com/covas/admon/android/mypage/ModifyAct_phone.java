package com.covas.admon.android.mypage;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.covas.admon.android.R;
import com.covas.admon.android.base.HeaderActivity;
import com.covas.admon.android.dao.User;
import com.covas.admon.android.util.BtnUtils;
import com.covas.admon.android.util.StringUtils;

public class ModifyAct_phone extends HeaderActivity implements OnClickListener {
	
	private Button _btnNext;
	
	private EditText _et;
	
	private TextView tvPrevPhone;

	@Override
	protected void init() {
		setContentView(R.layout.page_modify_phone);
        setHeaderTitle(getString(R.string.modify_title_phone));
        setHeaderBtnLeft(R.drawable.btn_header_back);
        
        
        tvPrevPhone = (TextView) findViewById(R.id.modify_tv_prev_txt);
        _et = (EditText) findViewById(R.id.join_etv_phone);
        
        tvPrevPhone.setText(app.getUser().phone);
        
        
        _btnNext = (Button) findViewById(R.id.join_btn_next);
        
        
        BtnUtils.initBtnWithTouchEffect(this, _btnNext, true);
        
        
	}
	
	@Override
	public void onHeaderBtnLeftClick() {
		super.onHeaderBtnLeftClick();
		onBackPressed();
	}

	@Override
	public void onClick(View v) {
		if( v.getId() == _btnNext.getId() ){
			
			if(StringUtils.isEmpty(_et.getText().toString())){
				app.showSimpleAlert(this, null, getString(R.string.join_msg_phone_empty), getString(R.string.txt_confirm));
				return;
			}else if(_et.getText().toString().contains(",") || 
					_et.getText().toString().contains(";") || 
					_et.getText().toString().contains("#") || 
					_et.getText().toString().contains("*") || 
					(_et.getText().toString().length() < 9) ){
				app.showSimpleAlert(this, null, getString(R.string.join_msg_phone_wrong), getString(R.string.txt_confirm));
				return;
			}else if( app.getUser().phone.equals( _et.getText().toString() ) ){
				finish();
				return;
			}
			
			User user = app.getUser().clone();
			
			user.phone = _et.getText().toString();
			
			app.updateUserInfo( this, user );
		}
	}
	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
    
}
