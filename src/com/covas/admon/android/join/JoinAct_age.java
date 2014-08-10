package com.covas.admon.android.join;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.covas.admon.android.R;
import com.covas.admon.android.base.FlipperBase;
import com.covas.admon.android.base.HeaderActivity;
import com.covas.admon.android.dao.JoinDao;
import com.covas.admon.android.dao.User;
import com.covas.admon.android.util.BtnUtils;
import com.covas.admon.android.util.StringUtils;
import com.covas.admon.android.util.Utils;

public class JoinAct_age extends HeaderActivity implements OnClickListener {
	
	private JoinDao joinDao;
	
	private FlipperBase parent;
	
	private Button _btnNext;
	
	private Spinner spn;
	
	private Button btnMale;
	private Button btnFemale;

	@Override
	protected void init() {
		setContentView(R.layout.page_join_age);
        setHeaderTitle(getString(R.string.join_title));
        setHeaderBtnLeft(R.drawable.btn_header_back);
        
        parent = (FlipperBase) getParent();
        
        joinDao = JoinDao.getInstance();
        
        String sTitle = getString(R.string.txt_birth_year);
        ((TextView) findViewById(R.id.join_tv_stitle)).setText(sTitle);
        
        
        _btnNext = (Button) findViewById(R.id.join_btn_next);
        
        BtnUtils.initBtnWithTouchEffect(this, _btnNext, true);
        
        
        initSpinner();
        
        btnMale = (Button) findViewById(R.id.join_btn_male);
        btnFemale = (Button) findViewById(R.id.join_btn_female);
        
    	
        btnMale.setOnClickListener(this);
        btnFemale.setOnClickListener(this);
    	
        if( joinDao.user.sex.equalsIgnoreCase(User.MALE) ){
        	btnMale.setSelected(true);
        	btnFemale.setSelected(false);
        }else if( joinDao.user.sex.equalsIgnoreCase(User.FEMALE) ){
        	btnMale.setSelected(false);
        	btnFemale.setSelected(true);
        }
	}
	
	private void initSpinner(){
		spn = (Spinner) findViewById(R.id.join_spinner);	
		ArrayList<String> arrAge = new ArrayList<String>();
		int age = Integer.parseInt(joinDao.user.byear);
        int position = 0;
        for(int i=2013; i >= 1900 ; i--){
    		
    		if(age < i) position++;
    		arrAge.add(String.valueOf(i));
    		
    	}
    	
    	ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrAge);
    	aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spn.setAdapter(aa);
    	spn.setPrompt(getString(R.string.txt_birth_year));
        
    	spn.setSelection(position, true);
	}
	
	@Override
	public void onHeaderBtnLeftClick() {
		super.onHeaderBtnLeftClick();
		onBackPressed();
	}

	@Override
	public void onClick(View v) {
		
		if( v.getId() == btnMale.getId() ){
			joinDao.user.sex = User.MALE;
			btnMale.setSelected(true);
        	btnFemale.setSelected(false);
			return;
		}else if( v.getId() == btnFemale.getId() ){
			joinDao.user.sex = User.FEMALE;
			btnMale.setSelected(false);
        	btnFemale.setSelected(true);
			return;
		}
		
		if( v.getId() == _btnNext.getId() ){
		    
		    if( StringUtils.isEmpty(joinDao.user.sex) )
		    {
		    	Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.join_msg_gender_empty), Toast.LENGTH_SHORT);
		    	toast.show();
		    	return;
		    }
		    
		    if(spn.getSelectedItem() != null)
		    {
		    	
		    	//final String bYear = Utils.getBirthYear( spn.getSelectedItem().toString() );
		    	final String bYear = spn.getSelectedItem().toString();
		    	
		    	joinDao.user.byear = bYear;
		    	
		    	parent.addActivity(new Intent(this, JoinAct_address.class));
		    }
		    else
		    {
		    	Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.join_msg_byear_empty), Toast.LENGTH_SHORT);
		    	toast.show();
		    	return;
		    }
			
		}
	}
	
	@Override
	public void onBackPressed() {
		hideKeyboard();
		parent.popActivity();
	}
    
}
