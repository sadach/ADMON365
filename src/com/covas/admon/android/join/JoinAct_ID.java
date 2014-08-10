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
import android.widget.Toast;

import com.covas.admon.android.App;
import com.covas.admon.android.R;
import com.covas.admon.android.base.FlipperBase;
import com.covas.admon.android.base.HeaderActivity;
import com.covas.admon.android.dao.JoinDao;
import com.covas.admon.android.mgr.WAS_Mgr;
import com.covas.admon.android.util.AsyncHttpRequest;
import com.covas.admon.android.util.BtnUtils;
import com.covas.admon.android.util.StringUtils;

public class JoinAct_ID extends HeaderActivity implements OnClickListener {
	
	private JoinDao joinDao;
	
	private FlipperBase parent;
	
	private Button _btnNext;
	
	private EditText _et;

	@Override
	protected void init() {
		setContentView(R.layout.page_join_name);
        setHeaderTitle(getString(R.string.join_title));
        setHeaderBtnLeft(R.drawable.btn_header_back);
        
        parent = (FlipperBase) getParent();
        
        joinDao = JoinDao.getInstance();
        
        String sTitle = getString(R.string.txt_name);
        ((TextView) findViewById(R.id.join_tv_stitle)).setText(sTitle);
        
        _et = (EditText) findViewById(R.id.join_etv_id);
        
        _et.setText(joinDao.user.id);
        
        
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
				app.showSimpleAlert(parent, null, getString(R.string.join_msg_id_empty), getString(R.string.txt_confirm));
				return;
			}
			
			execute();
		}
	}
	
	private void execute(){
		AsyncHttpRequest ar = new AsyncHttpRequest(parent);
		ar.addParam(WAS_Mgr.PARAM_UID, _et.getText().toString());
		
		ar.setOnRequestComplete(new AsyncHttpRequest.OnRequestComplete() {
			
			@Override
			public void onRequestComplete(String result, String task, List<NameValuePair> params) {
				if( app.checkError(parent, result) ) return;
				
				try {
					JSONObject rs = new JSONObject(result);
					
					if( WAS_Mgr.isFail(rs) ) {
						WAS_Mgr.showResultErrorAlert(parent, "", getString(R.string.join_msg_id_exist));
						return;
					}
					
					joinDao.user.id = _et.getText().toString();
					
					parent.addActivity(new Intent(parent, JoinAct_phone.class));
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		ar.execute(WAS_Mgr.CHECK_UID);
	}
	
	@Override
	public void onBackPressed() {
		hideKeyboard();
		parent.popActivity();
	}
    
}
