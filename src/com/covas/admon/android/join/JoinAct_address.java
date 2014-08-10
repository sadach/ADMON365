package com.covas.admon.android.join;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.covas.admon.android.R;
import com.covas.admon.android.base.FlipperBase;
import com.covas.admon.android.base.HeaderActivity;
import com.covas.admon.android.dao.JoinDao;
import com.covas.admon.android.util.BtnUtils;
import com.covas.admon.android.util.CitySpinnerWapper;
import com.covas.admon.android.util.StringUtils;

public class JoinAct_address extends HeaderActivity implements OnClickListener {
	
	private FlipperBase parent;
	
	private JoinDao joinDao;
	
	//private Button btn;
	
	private Button btnBack;
	private Button btnNext;
	
	private Spinner spinnerSiDO;
	private Spinner spinnerGUGUN;

	@Override
	protected void init() {
		setContentView(R.layout.page_join_address);
        setHeaderTitle(getString(R.string.join_title));
        setHeaderBtnLeft(R.drawable.btn_header_back);
        
        String sTitle = getString(R.string.txt_province_and_city);
        ((TextView) findViewById(R.id.join_tv_stitle)).setText(sTitle);
        
        joinDao = JoinDao.getInstance();
        
        parent = (FlipperBase) getParent();
        
        btnNext = (Button) findViewById(R.id.join_btn_next);
        
        BtnUtils.initBtnWithTouchEffect(this, btnNext, true);
        
        spinnerSiDO = (Spinner) findViewById(R.id.spinner_sido);
        spinnerGUGUN = (Spinner) findViewById(R.id.spinner_gugun);
        
        /*if( StringUtils.isEmpty(joinDao.user.location1) ){
        	joinDao.user.location1 = "北京";
        }
        if( StringUtils.isEmpty(joinDao.user.location2) ){
        	joinDao.user.location2 = "通州";
        }*/
        
        //거주지 선택
        CitySpinnerWapper.init(this, spinnerSiDO, spinnerGUGUN, joinDao.user.location1, joinDao.user.location2);
        
	}

	@Override
	public void onClick(View v) {
		if( v.getId() == btnNext.getId() ){
			
			if(spinnerSiDO.getSelectedItem() != null)
			{
				joinDao.user.location1 = spinnerSiDO.getSelectedItem().toString();
				joinDao.user.location2 = spinnerGUGUN.getSelectedItem().toString();
		    	
		    	parent.addActivity(new Intent(this, JoinAct_email.class));
			}
			else
			{
				Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.join_msg_address_empty), Toast.LENGTH_SHORT);
		    	toast.show();
		    	return;
			}
			
			
		}else if( v.getId() == btnBack.getId() ){
			parent.popActivity();
		}
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
