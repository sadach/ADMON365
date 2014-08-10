package com.covas.admon.android.mypage;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.covas.admon.android.R;
import com.covas.admon.android.base.HeaderActivity;
import com.covas.admon.android.dao.User;
import com.covas.admon.android.util.BtnUtils;
import com.covas.admon.android.util.StringUtils;

public class ModifyAct_pwd extends HeaderActivity implements OnClickListener {
	
	private Button _btnNext;
	
	private EditText etv_pwd;
	private EditText etv_pwd2;
	private EditText etv_pwd3;

	@Override
	protected void init() {
		setContentView(R.layout.page_modify_pwd);
        setHeaderTitle(getString(R.string.modify_title_pwd));
        setHeaderBtnLeft(R.drawable.btn_header_back);
        
        
        etv_pwd = (EditText) findViewById(R.id.join_etv_pwd);
        etv_pwd.setText("");
        etv_pwd2 = (EditText) findViewById(R.id.join_etv_pwd2);
        etv_pwd2.setText("");
        etv_pwd3 = (EditText) findViewById(R.id.join_etv_pwd3);
        etv_pwd3.setText("");
        
        
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
			
			if( StringUtils.isEmpty(etv_pwd.getText().toString()) ){
				app.showSimpleAlert(this, null, getString(R.string.join_msg_pwd_empty), getString(R.string.txt_confirm));
				return;
			}else if( app.getUser().passwd.equals(etv_pwd.getText().toString()) == false ){
				app.showSimpleAlert(this, null, getString(R.string.modify_msg_wrong_prev_pwd), getString(R.string.txt_confirm));
				return;
			}else if( etv_pwd2.getText().equals(etv_pwd3) == false ){
				app.showSimpleAlert(this, null, getString(R.string.join_msg_pwd_mismatch), getString(R.string.txt_confirm));
				return;
			}else if( etv_pwd.getText().toString().length() < 6 || etv_pwd2.getText().toString().length() < 6 || etv_pwd3.getText().toString().length() < 6 ){
				app.showSimpleAlert(this, null, getString(R.string.join_msg_pwd_wrong), getString(R.string.txt_confirm));
				return;
			}
			
			
			
			User user = app.getUser().clone();
			
			user.passwd = etv_pwd.getText().toString();
			
			app.updateUserInfo( this, user );
			
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
    
}
