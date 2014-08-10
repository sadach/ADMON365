package com.covas.admon.android.help;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.covas.admon.android.R;
import com.covas.admon.android.util.BtnUtils;

public class Help extends Activity implements OnClickListener {
	
	private ViewPager pager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_help);
		
		pager = new ViewPager(this);
		setContentView(pager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		ArrayList<ViewGroup> list = new ArrayList<ViewGroup>();
		
		for (int i = 1; i <= 4; i++) {
			
			ViewGroup vg = (ViewGroup) getLayoutInflater().inflate(R.layout.page_help, null);
			if( i == 1 ){
				vg.setBackgroundResource(R.drawable.help_1);
			}else if( i == 2 ){
				vg.setBackgroundResource(R.drawable.help_2);
			}else if( i == 3 ){
				vg.setBackgroundResource(R.drawable.help_3);
			}else if( i == 4 ){
				vg.setBackgroundResource(R.drawable.help_4);
				Button btn = (Button) BtnUtils.initBtnWithTouchEffect(this, vg.findViewById(R.id.help_4_btn_ok), true);
				btn.setVisibility(Button.VISIBLE);
			}
			
			BtnUtils.initBtnWithTouchEffect(this, vg.findViewById(R.id.help_btn_close), true);
			
			list.add(vg);
		}
		
		
		pager.setAdapter(new HelpPagerAdapter(list));
		pager.setCurrentItem(0);
	}

	@Override
	public void onClick(View v) {
		onBackPressed();
	}
	
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.hold, R.anim.fade_out);
	}

}
