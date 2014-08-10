package com.covas.admon.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.covas.admon.android.help.Help;
import com.covas.admon.android.mypage.MyPageAct_Main;
import com.covas.admon.android.web.BuyInfoAct;
import com.covas.admon.android.web.NoticeAct;

public class MenuFragmentLeft extends ListFragment {

	private App app;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_left_list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		View header = getActivity().getLayoutInflater().inflate(R.layout.menu_left_list_item_header, null);
		this.getListView().addHeaderView(header, null, false);
		
		
		SampleAdapter adapter = new SampleAdapter(getActivity());
		for (int i = 1; i <= 5; i++) {
			SampleItem item = null;
			/*if( i == 1 ){
				item = new SampleItem(getString(R.string.menu_home), R.drawable.menu_icon_home);
			}*/
			if(  i == 1 ){
				item = new SampleItem(getString(R.string.menu_myorders), R.drawable.menu_icon_myorders);
			}else if(  i == 2 ){
				item = new SampleItem(getString(R.string.menu_mypage), R.drawable.menu_icon_mypage);
			}else if(  i == 3 ){
				item = new SampleItem(getString(R.string.menu_notice), R.drawable.menu_icon_notice);
			}else if(  i == 4 ){
				item = new SampleItem(getString(R.string.menu_logout), R.drawable.menu_icon_logout);
			}else if(  i == 5 ){
				item = new SampleItem(getString(R.string.menu_help), R.drawable.menu_icon_help);
			}
			adapter.add( item );
		}
		setListAdapter(adapter);
	}

	private class SampleItem {
		public String tag;
		public int iconRes;
		public SampleItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}

	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_left_list_item, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);

			return convertView;
		}

	}
	
	private App getApp(){
		try{
			if( app == null ) app = (App) getActivity().getApplicationContext();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		
		return app;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		//position == 0  = header
		if( position == 0 ){
			Intent intent = new Intent( getActivity(), MainAct.class );
			intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
			getActivity().startActivity(intent);
		}else if( position == 1 ){
			getActivity().startActivity(new Intent(getActivity(), BuyInfoAct.class));
		}else if( position == 2 ){
			getActivity().startActivity(new Intent(getActivity(), MyPageAct_Main.class));
			//getActivity().overridePendingTransition(R.anim.fade_in, R.anim.hold);
		}else if( position == 3 ){
			getActivity().startActivity(new Intent(getActivity(), NoticeAct.class));
		}else if( position == 4 ){
			getApp().showLogoutDialog(getActivity());
		}else if( position == 5 ){
			getActivity().startActivity(new Intent(getActivity(), Help.class));
			getActivity().overridePendingTransition(R.anim.fade_in, R.anim.hold);
		}
	}
}
