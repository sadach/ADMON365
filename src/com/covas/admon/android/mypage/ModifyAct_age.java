package com.covas.admon.android.mypage;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.covas.admon.android.R;
import com.covas.admon.android.base.HeaderActivity;
import com.covas.admon.android.dao.User;
import com.covas.admon.android.util.BtnUtils;

public class ModifyAct_age extends HeaderActivity implements OnClickListener {
	
	private Button _btnNext;
	
	private Spinner spn;
	
	private Button btnMale;
	private Button btnFemale;

	private String gender = "";
	
	@Override
	protected void init() {
		setContentView(R.layout.page_modify_age);
        setHeaderTitle(getString(R.string.modify_title_birth_gender));
        setHeaderBtnLeft(R.drawable.btn_header_back);
        
        gender = app.getUser().sex;
        
        
        _btnNext = (Button) findViewById(R.id.join_btn_next);
        
        BtnUtils.initBtnWithTouchEffect(this, _btnNext, true);
        
        
        initSpinner();
        
        btnMale = (Button) findViewById(R.id.join_btn_male);
        btnFemale = (Button) findViewById(R.id.join_btn_female);
        
    	
        btnMale.setOnClickListener(this);
        btnFemale.setOnClickListener(this);
    	
        if( app.getUser().sex.equalsIgnoreCase(User.MALE) ){
        	btnMale.setSelected(true);
        	btnFemale.setSelected(false);
        }else if( app.getUser().sex.equalsIgnoreCase(User.FEMALE) ){
        	btnMale.setSelected(false);
        	btnFemale.setSelected(true);
        }
	}
	
	private void initSpinner(){
		spn = (Spinner) findViewById(R.id.join_spinner);	
		ArrayList<String> arrAge = new ArrayList<String>();
		int age = Integer.parseInt(app.getUser().byear);
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
			gender = User.MALE;
			btnMale.setSelected(true);
        	btnFemale.setSelected(false);
			return;
		}else if( v.getId() == btnFemale.getId() ){
			gender = User.FEMALE;
			btnMale.setSelected(false);
        	btnFemale.setSelected(true);
			return;
		}
		
		if( v.getId() == _btnNext.getId() ){
		    
			
			String birth_year = spn.getSelectedItem().toString();
			
		    if( gender.equals( app.getUser().sex ) && birth_year.equals( app.getUser().byear ) )
		    {
		    	finish();
		    	return;
		    }
		    
		    User user = app.getUser().clone();
			
			user.byear = birth_year;
			user.sex = gender;
			
			app.updateUserInfo( this, user );
			
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
    
}
