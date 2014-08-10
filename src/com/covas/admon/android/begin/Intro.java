package com.covas.admon.android.begin;

import com.covas.admon.android.R;
import com.covas.admon.android.R.anim;
import com.covas.admon.android.R.layout;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class Intro extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        
        setContentView(R.layout.page_intro);
	    //세로고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    
	    Handler h = new Handler();
	    h.postDelayed(new splashhandler(), 2000);
	}
	
	private class splashhandler implements Runnable{
		public void run()
		{
			//startActivity(new Intent(getApplication(), main.class));
			Intro.this.finish();
			Intro.this.overridePendingTransition(R.anim.hold, R.anim.push_down_out);
			//Intro.this.overridePendingTransition(R.anim.hold, R.anim.fade_out);
		}
	}

}
