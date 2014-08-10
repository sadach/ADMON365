package com.covas.admon.android.mypage;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.covas.admon.android.R;
import com.covas.admon.android.base.HeaderActivity;
import com.covas.admon.android.dao.User;
import com.covas.admon.android.mgr.WAS_Mgr;
import com.covas.admon.android.util.AsyncHttpRequest;
import com.covas.admon.android.util.BtnUtils;
import com.covas.admon.android.web.PointInfoAct;

public class MyPageAct_Main extends HeaderActivity implements OnClickListener{
	
	private Button btnCredit;
	private Button btnEmail;
	private Button btnBirthday;
	private Button btnResidence;
	private Button btnPhone;
	private Button btnPWD;
	
	private TextView tvID;
	private TextView tvPoint;
	private TextView tvGrade;
	
	private User user;
	
	@Override
	protected void init() {
		setContentView(R.layout.page_mypage_main);
		setHeaderTitle(getString(R.string.mypage_title));
		setHeaderBtnLeft(R.drawable.btn_header_back);
		
		btnCredit = (Button) findViewById(R.id.mypage_btn_credit);
		btnEmail = (Button) findViewById(R.id.mypage_btn_email);
		btnBirthday = (Button) findViewById(R.id.mypage_btn_birthday_gender);
		btnResidence = (Button) findViewById(R.id.mypage_btn_residence);
		btnPhone = (Button) findViewById(R.id.mypage_btn_phone);
		btnPWD = (Button) findViewById(R.id.mypage_btn_pwd);
		
		
		tvID 		= (TextView)findViewById(R.id.mypage_tv_name);
		tvPoint 	= (TextView)findViewById(R.id.mypage_tv_mypoint);
		tvGrade 	= (TextView)findViewById(R.id.mypage_tv_mygrade);
		
		
		//app.initViewWithResize(btnCredit);
		app.initViewWithResize(btnEmail);
		app.initViewWithResize(btnBirthday);
		app.initViewWithResize(btnResidence);
		app.initViewWithResize(btnPhone);
		app.initViewWithResize(btnPWD);
		
		BtnUtils.initBtnWithTouchEffect(this, btnCredit, true);
		BtnUtils.initBtnWithTouchEffect(this, btnEmail, true);
		BtnUtils.initBtnWithTouchEffect(this, btnBirthday, true);
		BtnUtils.initBtnWithTouchEffect(this, btnResidence, true);
		BtnUtils.initBtnWithTouchEffect(this, btnPhone, true);
		BtnUtils.initBtnWithTouchEffect(this, btnPWD, true);
		
		initUserInfo();
	}
	
	private void initUserInfo(){
		
		user = app.getUser();
		
		AsyncHttpRequest ar = new AsyncHttpRequest(this, "", true);
		ar.addParam(WAS_Mgr.PARAM_UID, user.id);
		
		ar.setOnRequestComplete(new AsyncHttpRequest.OnRequestComplete() {
			
			@Override
			public void onRequestComplete(String result, String task, List<NameValuePair> params) {
				if( app.checkError(MyPageAct_Main.this, result) ){
					finish();
					return;
				}
				
				try {
					JSONObject rs = new JSONObject(result);
					
					if( WAS_Mgr.isFail(rs) ) {
						String errorMsg = rs.getString(WAS_Mgr.RESULT_KEY_MSG);
						WAS_Mgr.showResultErrorAlert(MyPageAct_Main.this, "", errorMsg);
						finish();
						return;
					}
					
					JSONArray userJarr = rs.getJSONArray(WAS_Mgr.RESULT_KEY_DATA);
					JSONObject userJobj = userJarr.getJSONObject(0);
					
					user.byear 		= userJobj.getString("byear");
					user.degree		= userJobj.getString("degree");
					user.email 		= userJobj.getString("email");
					user.id 			= userJobj.getString("id");
					user.location1 	= userJobj.getString("location1");
					user.location2 	= userJobj.getString("location2");
					user.passwd 	= userJobj.getString("passwd");
					user.phone 		= userJobj.getString("phone");
					user.point 		= userJobj.getString("point");
					user.seq 		= userJobj.getString("seq");
					user.sex 		= userJobj.getString("sex");
					
					
					app.saveUser( user );
					
					
					setData();
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		ar.execute(WAS_Mgr.GET_MY_INFO);
	}
	
	private void setData(){
		
		tvID.setText(user.id);
		tvPoint.setText(user.point+" point");
		tvGrade.setText(user.getGrade());
	}
	
	@Override
	public void onHeaderBtnLeftClick() {
		super.onHeaderBtnLeftClick();
		onBackPressed();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	public void onClick(View v) {
		if( v.getId() == btnCredit.getId() )
		{
			startActivity( new Intent(this, PointInfoAct.class) );
		}
		else if( v.getId() == btnEmail.getId() )
		{
			startActivity( new Intent(this, ModifyAct_email.class) );
		}
		else if( v.getId() == btnBirthday.getId() )
		{
			startActivity( new Intent(this, ModifyAct_age.class) );
		}
		else if( v.getId() == btnResidence.getId() )
		{
			startActivity( new Intent(this, ModifyAct_address.class) );
		}
		else if( v.getId() == btnPhone.getId() )
		{
			startActivity( new Intent(this, ModifyAct_phone.class) );
		}
		else if( v.getId() == btnPWD.getId() )
		{
			startActivity( new Intent(this, ModifyAct_pwd.class) );
		}
	}
}
