package com.covas.admon.android.mypage;

import java.util.regex.Pattern;

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

public class ModifyAct_email extends HeaderActivity implements OnClickListener {
	
	private Button _btnNext;
	
	private EditText _et;
	
	private TextView tvPrevEmail;

	@Override
	protected void init() {
		setContentView(R.layout.page_modify_email);
        setHeaderTitle(getString(R.string.modify_title_email));
        setHeaderBtnLeft(R.drawable.btn_header_back);
        
        
        tvPrevEmail = (TextView) findViewById(R.id.modify_tv_prev_txt);
        _et = (EditText) findViewById(R.id.join_etv_email);
        
        tvPrevEmail.setText(app.getUser().email);
        
        
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
			
			if( StringUtils.isEmpty(_et.getText().toString()) ){
				app.showSimpleAlert(this, null, getString(R.string.join_msg_email_empty), getString(R.string.txt_confirm));
				return;
			}else if( checkEmail(_et.getText().toString()) == false ){
				app.showSimpleAlert(this, null, getString(R.string.join_msg_email_wrong), getString(R.string.txt_confirm));
				return;
			}else if( app.getUser().email.equals( _et.getText().toString() ) ){
				finish();
				return;
			}
			
			
			User user = app.getUser().clone();
			
			user.email = _et.getText().toString();
			
			app.updateUserInfo( this, user );
		}
	}
	
	private boolean checkEmail(String email) {   
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	} 
 
	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
		"[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +       
		"\\@" +           
		"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +         
		"(" +      
		"\\." +    
		"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +        
		")+"     
	);
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
    
}
