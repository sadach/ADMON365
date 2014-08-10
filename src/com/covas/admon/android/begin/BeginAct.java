package com.covas.admon.android.begin;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.covas.admon.android.R;
import com.covas.admon.android.base.HeaderActivity;
import com.covas.admon.android.join.JoinAct_ID;
import com.covas.admon.android.util.BtnUtils;
import com.covas.admon.android.util.Utils;

public class BeginAct extends HeaderActivity implements OnClickListener {
	
	private Button btnJoin;
	private Button btnLogin;
	
	private BeginMain parent;
	
	
	
	@Override
	protected void init() {
		setContentView(R.layout.page_join_or_login);
        
        parent = (BeginMain) getParent();
        
        btnLogin = (Button) findViewById(R.id.begin_btn_login);
        btnJoin = (Button) findViewById(R.id.begin_btn_join);
        
        app.initViewWithResize(btnLogin);
        app.initViewWithResize(btnJoin);
        
        BtnUtils.initBtnWithTouchEffect(this, btnLogin, true);
        BtnUtils.initBtnWithTouchEffect(this, btnJoin, true);
        
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == btnJoin.getId()){
			parent.addActivity(new Intent(parent, JoinAct_ID.class));
		}else if(v.getId() == btnLogin.getId()){
			parent.addActivity(new Intent(parent, LoginAct.class));
		}
	}
	
	
}
