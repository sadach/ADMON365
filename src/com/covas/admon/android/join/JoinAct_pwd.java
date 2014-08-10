package com.covas.admon.android.join;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.covas.admon.android.R;
import com.covas.admon.android.base.FlipperBase;
import com.covas.admon.android.base.HeaderActivity;
import com.covas.admon.android.dao.JoinDao;
import com.covas.admon.android.dao.User;
import com.covas.admon.android.mgr.WAS_Mgr;
import com.covas.admon.android.util.AsyncHttpRequest;
import com.covas.admon.android.util.BtnUtils;
import com.covas.admon.android.util.StringUtils;

public class JoinAct_pwd extends HeaderActivity implements OnClickListener {
	
	private JoinDao joinDao;
	
	private FlipperBase parent;
	
	private Button _btnNext;
	
	private EditText etv_pwd;
	private EditText etv_pwd2;

	@Override
	protected void init() {
		setContentView(R.layout.page_join_pwd);
        setHeaderTitle(getString(R.string.join_title));
        setHeaderBtnLeft(R.drawable.btn_header_back);
        
        parent = (FlipperBase) getParent();
        
        joinDao = JoinDao.getInstance();
        
        String sTitle = getString(R.string.txt_pwd);
        ((TextView) findViewById(R.id.join_tv_stitle)).setText(sTitle);
        
        etv_pwd = (EditText) findViewById(R.id.join_etv_pwd);
        etv_pwd.setText(joinDao.user.passwd);
        etv_pwd2 = (EditText) findViewById(R.id.join_etv_pwd2);
        etv_pwd2.setText(joinDao.user.passwd);
        
        
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
			
			String pwd = etv_pwd.getText().toString();
			String pwd2 = etv_pwd2.getText().toString();
			
			if( StringUtils.isEmpty( pwd ) ){
				app.showSimpleAlert(parent, null, getString(R.string.join_msg_pwd_empty), getString(R.string.txt_confirm));
				return;
			}else if( pwd.equals( pwd2 ) == false ){
				app.showSimpleAlert(parent, null, getString(R.string.join_msg_pwd_mismatch), getString(R.string.txt_confirm));
				return;
			}else if( pwd.length() < 6 || pwd2.length() < 6 ){
				app.showSimpleAlert(parent, null, getString(R.string.join_msg_pwd_wrong), getString(R.string.txt_confirm));
				return;
			}
			
			hideKeyboard(); 
			joinDao.user.passwd = pwd;
			
			try {
				execute();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private void execute() throws JSONException{
		AsyncHttpRequest ar = new AsyncHttpRequest(parent);
		
		JSONObject userJson = joinDao.user.getUserJson();
		
		ar.addParam(WAS_Mgr.PARAM_USER_JSON, userJson.toString());
		
		ar.setOnRequestComplete(new AsyncHttpRequest.OnRequestComplete() {
			
			@Override
			public void onRequestComplete(String result, String task, List<NameValuePair> params) {
				if( app.checkError(parent, result) ) return;
				
				try {
					JSONObject rs = new JSONObject(result);
					
					if( WAS_Mgr.isFail(rs) ) {
						//TODO 가입 실패한 경우  실패 이유 서버에서 받아서 보여주게 처리
						
						String errorMsg = rs.getString(WAS_Mgr.RESULT_KEY_MSG);
						WAS_Mgr.showResultErrorAlert(parent, "", errorMsg);
						return;
					}
					
					String degree = rs.getString(WAS_Mgr.RESULT_KEY_DATA);
					joinSuccess(degree);
					
					
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		ar.execute(WAS_Mgr.REGISTER_MEMBER);
	}
	
	private void joinSuccess(String degree){
		
		joinDao.user.degree = degree;
		
		app.saveUser(joinDao.user);
		app.startMain(parent);
	}
	
	@Override
	public void onBackPressed() {
		hideKeyboard();
		parent.popActivity();
	}
    
}
