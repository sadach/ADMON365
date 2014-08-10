package com.covas.admon.android.wxapi;

import com.covas.admon.android.util.Logger;
import com.covas.admon.android.wechat.WeChatMgr;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/** 설명 
 * 
 * @author 신기웅(moai.kiz@gmail.com)
 * @since 2013. 11. 27.
 * @version 1.0.0
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler
{
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		
		IWXAPI api = WeChatMgr.getIWXAPI( this );
		
		api.handleIntent( getIntent(), this );
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		finish();
	}

	@Override
	public void onReq( BaseReq arg0 )
	{
		// TODO Auto-generated method stub
		Logger.d(this, "onReq", arg0);
	}

	@Override
	public void onResp( BaseResp arg0 )
	{
		// TODO Auto-generated method stub
		Logger.d(this, "onResp", arg0);
	}
}
