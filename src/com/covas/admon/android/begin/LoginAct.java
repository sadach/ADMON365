package com.covas.admon.android.begin;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.covas.admon.android.R;
import com.covas.admon.android.base.FlipperBase;
import com.covas.admon.android.base.HeaderActivity;
import com.covas.admon.android.dao.User;
import com.covas.admon.android.mgr.WAS_Mgr;
import com.covas.admon.android.util.AsyncHttpRequest;
import com.covas.admon.android.util.BtnUtils;
import com.covas.admon.android.util.StringUtils;

public class LoginAct extends HeaderActivity implements OnClickListener {
	
	private Button btnLogin;
	
	private EditText etID;
	private EditText etPWD;
	
	private FlipperBase parent;

	@Override
	protected void init() {
		
		setContentView(R.layout.page_login);
        setHeaderTitle(getString(R.string.login_title));
        setHeaderBtnLeft(R.drawable.btn_header_back);
		
        parent = (FlipperBase) getParent();
        
        String sTitle = getString(R.string.login_title);
       ((TextView) findViewById(R.id.join_tv_stitle)).setText(sTitle);
        
        etID = (EditText) findViewById(R.id.login_etv_id);
        etPWD = (EditText) findViewById(R.id.login_etv_pwd);
        
        btnLogin = (Button) findViewById(R.id.login_btn_submit);
        
        
        BtnUtils.initBtnWithTouchEffect(this, btnLogin, true);
	}
	
	@Override
	public void onClick(View v) {
		if( v.getId() == btnLogin.getId() ){
			
			if( StringUtils.isEmpty(etID.getText().toString()) ){
				app.showSimpleAlert(parent, null, getString(R.string.join_msg_id_empty), getString(R.string.txt_confirm));
				return;
			}else if( StringUtils.isEmpty(etPWD.getText().toString()) ){
				app.showSimpleAlert(parent, null, getString(R.string.join_msg_pwd_empty), getString(R.string.txt_confirm));
				return;
			}
			
			
			hideKeyboard();
			login();
		}
	}
	
	
	private void login(){
		
		AsyncHttpRequest ar = new AsyncHttpRequest(parent, "", true);
		
		ar.addParam(WAS_Mgr.PARAM_UID, etID.getText().toString());
		ar.addParam(WAS_Mgr.PARAM_PWD, etPWD.getText().toString());
		
		ar.setOnRequestComplete(new AsyncHttpRequest.OnRequestComplete() {
			
			@Override
			public void onRequestComplete(String result, String task, List<NameValuePair> params) {
				if( app.checkError(parent, result) ) return;
				
				try {
					JSONObject rs = new JSONObject(result);
					
					if( WAS_Mgr.isFail(rs) ) {
						WAS_Mgr.showResultErrorAlert(parent, "", getString(R.string.login_msg_wrong_input));
						return;
					}
					
					User user = new User();
					
					user.id = etID.getText().toString();
					user.degree = rs.getString(WAS_Mgr.RESULT_KEY_DATA);
					user.passwd = etPWD.getText().toString();
					
					app.saveUser(user);
					app.startMain(parent);
					
				}catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		ar.execute(WAS_Mgr.LOGIN);
	}
	
	@Override
	public void onHeaderBtnLeftClick() {
		super.onHeaderBtnLeftClick();
		onBackPressed();
	}
	
	@Override
	public void onBackPressed() {
		hideKeyboard();
		parent.popActivity();
	}

	
    
}
