package com.covas.admon.android;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.covas.admon.android.product.Category;
import com.covas.admon.android.product.Product_Mgr;
import com.covas.admon.android.product.Product_Mgr.OnCategoryLoaded;
import com.covas.admon.android.util.Logger;

public class MenuFragmentCategory extends ListFragment implements OnCategoryLoaded {

	private App app;
	
	private Product_Mgr productMgr;
	
	private ArrayList<Category> cateList;
	
	private ArrayList<RadioButton> radioBtnList;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		container.setBackgroundColor(0x000000);
		return inflater.inflate(R.layout.menu_category_list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		View header = getActivity().getLayoutInflater().inflate(R.layout.menu_category_list_item_header, null);
		this.getListView().addHeaderView(header, null, false);
		app = getApp();
		
		radioBtnList = new ArrayList<RadioButton>();
		
		productMgr = Product_Mgr.getInstance();
		productMgr.setOnCategoryLoaded(this);
		productMgr.loadCategorys(app);
	}
	
	@Override
	public void onCategoryLoaded(ArrayList<Category> cateList) {
		setList(cateList);
	}
	
	private void setList(ArrayList<Category> cateList){
		CategoryAdapter adapter = new CategoryAdapter(getActivity());
		this.cateList = cateList;
		
		adapter.clear();
		
		/**
		 * API 11 이후 지원되는 메소드
		 */
		//adapter.addAll(cateList);
		
		for ( Category category : cateList )
		{
			adapter.add( category );
		}
		
		setListAdapter(adapter);
		
		
	}
	

	public class CategoryAdapter extends ArrayAdapter<Category> {

		public CategoryAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_category_list_item, null);
				RadioButton rBtn = (RadioButton) convertView.findViewById(R.id.menu_category_rbtn_check);
				radioBtnList.add(rBtn);
				convertView.setTag(rBtn);
			}
			
			RadioButton rBtn = (RadioButton) convertView.getTag();
			
			Category category = getItem(position);
			Category currentCategory = productMgr.getCurrentCategory();
			if( category == currentCategory ){
				rBtn.setChecked(true);
			}
			
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).title);
			
			
			
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
		
		//Logger.d( "카테타이틀!! = "+cateList.get(position-1).title );
		productMgr.changeCategory( cateList.get(position-1) );
		
		RadioButton rBtn = (RadioButton) v.getTag();
		
		setRadioBtnChecked(null);
		rBtn.setChecked(true);
		
		
		getActivity().onBackPressed();
	}
	
	private void setRadioBtnChecked(RadioButton currentRbtn){
		for (RadioButton rBtn : radioBtnList) {
			/*if( currentRbtn != rBtn ){
				
			}*/
			rBtn.setChecked(false);
		}
	}

	
}
