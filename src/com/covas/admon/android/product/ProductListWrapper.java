package com.covas.admon.android.product;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.covas.admon.android.R;
import com.covas.admon.android.product.Product_Mgr.OnProductCategoryChanged;
import com.covas.admon.android.product.Product_Mgr.OnProductListLoaded;
import com.covas.admon.android.util.Logger;

public class ProductListWrapper implements OnScrollListener, OnProductListLoaded, OnProductCategoryChanged {
	
	private Activity _activity;
	
	private ListView _listView;
	
	private ArrayList<Product> _ProductList;
	
	private ProductListAdepter _adapter;

	private LinearLayout _footerView;
	//private ProgressBar		 _pbLoading;
	//private TextView		 _tvFooterDesc;
	//private String		 	_loadingText;;
	
	private int _itemTotal = 0;
	
	private boolean isLoading = false;
	
	private Product_Mgr productMgr;
	
	private Category currentCategory;
	
	public ProductListWrapper(Activity activity) {
		_activity = activity;
		_listView = (ListView) _activity.findViewById(R.id.product_list);
		
		_footerView = (LinearLayout) _activity.getLayoutInflater().inflate(R.layout.product_list_footer_show_more, null);
		_listView.addFooterView(_footerView, null, false);
		
		/*_pbLoading = (ProgressBar) _footerView.findViewById(R.id.product_list_footer_progress);
		_tvFooterDesc = (TextView) _footerView.findViewById(R.id.product_list_footer_tv_desc);
		_loadingText = _activity.getString(R.string.product_msg_more_item_loading);*/
		
		_ProductList = new ArrayList<Product>();
		_adapter = new ProductListAdepter(_activity, _ProductList);
		_adapter.setNotifyOnChange(false);
		_listView.setAdapter(_adapter);
		
		_listView.setOnScrollListener(this);
	}
	
	public void initList(){
		productMgr = Product_Mgr.getInstance();
		productMgr.init();
		currentCategory = productMgr.getCurrentCategory();
		productMgr.setOnProductListLoaded(this);
		productMgr.setOnProductCategoryChanged(this);
		
		isLoading = true;
		productMgr.loadProducts(_activity, true);
		//getMoreItem();
	}
	
	@Override
	public void onProductListLoaded( Category category ) {
		
		final boolean isChanged = category.isChanged;
		category.isChanged = false;
		
		if( category != currentCategory ) return;
		
		setMoreViewVisible( true );
		
		_ProductList.clear();
		_ProductList.addAll(category.getProductList());
		
		/*for (Product product : _ProductList) {
			Logger.d("trace product = "+product.item_seq+", cate = "+product.item_category_id);
		}*/
		
		_adapter.notifyDataSetChanged();
		isLoading = false;
		
		_itemTotal = _ProductList.size();
		
		if( category.isNoMore() ){
			setMoreViewVisible( false );
		}
		
		if( isChanged ) {
			_listView.setSelection(0);
		}
	}
	
	@Override
	public void onProductCategoryChanged(Category category) {
		currentCategory = category;
		
		category.isChanged = true;
		/*if( category.getProductList().size() < 10 ){
			productMgr.loadProducts(_activity, true);
		}*/
		//onProductListLoaded( category );
		//_listView.setSelection(0);
		
		isLoading = true;
		productMgr.loadProducts(_activity, true);
		
	}
	
	private void getMoreItem(){
		if( _ProductList.isEmpty() ){
			setMoreViewVisible( false );
		}
		
		isLoading = true;
		productMgr.loadProducts(_activity, false);
	}
	
	
	private void setMoreViewVisible(boolean visible){
		if( visible ){
			_footerView.setVisibility( View.VISIBLE );
			
		}else{
			_footerView.setVisibility( View.GONE );
			//_listView.removeFooterView( _footerView );
		}
	}
	
	

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		
		if( _listView.getLastVisiblePosition() == _itemTotal && isLoading == false && currentCategory.isNoMore() == false ){
			
			getMoreItem();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
		/*if( isLoading == false &&_listView.getLastVisiblePosition() == _ProductList.size() && scrollState == SCROLL_STATE_IDLE){
			
			getMoreItem();
		}*/
	}
	
}
