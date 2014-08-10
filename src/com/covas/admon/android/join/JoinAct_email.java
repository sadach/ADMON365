package com.covas.admon.android.join;

import java.util.regex.Pattern;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.covas.admon.android.App;
import com.covas.admon.android.R;
import com.covas.admon.android.base.FlipperBase;
import com.covas.admon.android.base.HeaderActivity;
import com.covas.admon.android.dao.JoinDao;
import com.covas.admon.android.util.BtnUtils;
import com.covas.admon.android.util.StringUtils;

public class JoinAct_email extends HeaderActivity implements OnClickListener {
	
	private JoinDao joinDao;
	
	private FlipperBase parent;
	
	private Button _btnNext;
	
	private EditText _et;

	@Override
	protected void init() {
		setContentView(R.layout.page_join_email);
        setHeaderTitle(getString(R.string.join_title));
        setHeaderBtnLeft(R.drawable.btn_header_back);
        
        parent = (FlipperBase) getParent();
        
        joinDao = JoinDao.getInstance();
        
        String sTitle = getString(R.string.txt_email);
        ((TextView) findViewById(R.id.join_tv_stitle)).setText(sTitle);
        
        _et = (EditText) findViewById(R.id.join_etv_email);
        
        _et.setText(joinDao.user.email);
        
        
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
				app.showSimpleAlert(parent, null, getString(R.string.join_msg_email_empty), getString(R.string.txt_confirm));
				return;
			}else if(checkEmail(_et.getText().toString()) == false){
				app.showSimpleAlert(parent, null, getString(R.string.join_msg_email_wrong), getString(R.string.txt_confirm));
				return;
			}
			
			joinDao.user.email = _et.getText().toString();
			
			parent.addActivity(new Intent(this, JoinAct_pwd.class));
			
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
		hideKeyboard();
		parent.popActivity();
	}
    
}
