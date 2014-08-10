package com.covas.admon.android.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;

import com.covas.admon.android.App;
import com.covas.admon.android.R;
import com.covas.admon.android.mgr.WAS_Mgr;
import com.covas.admon.android.util.AsyncHttpRequest;
import com.covas.admon.android.util.Logger;
import com.covas.admon.android.util.Utils;

public class Product_Mgr {
	
	private static Product_Mgr _instance;
	public static Product_Mgr getInstance() {
		if (_instance == null) {
			synchronized (Product_Mgr.class) {
				if (_instance == null) {
					_instance = new Product_Mgr();
				}
			}
		}
		return _instance;
	}
	
	private HashMap<Integer, Category> categorys;
	private HashMap<Long, Product> products;
	
	private OnCategoryLoaded				onCategoryLoaded;
	private OnProductListLoaded 			onProductListLoaded;
	private OnProductCategoryChanged 	onProductCategoryChanged;
	
	private Category currentCategory;
	
	private Product_Mgr(){
		
		init();	
	}
	
	public void init(){
		categorys = new HashMap<Integer, Category>();
		onCategoryLoaded = null;
		products = new HashMap<Long, Product>();
		onProductListLoaded = null;
		
		/*if( currentCategory == null ){
			currentCategory = getCategory( -1 );
		}*/
		currentCategory = getCategory( -1 );
	}
	
	public Category getCurrentCategory(){
		return currentCategory;
	}
	
	public interface OnCategoryLoaded{
		void onCategoryLoaded( ArrayList<Category> cateList );
	}
	public interface OnProductListLoaded{
		void onProductListLoaded( Category category );
	}
	
	public interface OnProductCategoryChanged{
		void onProductCategoryChanged( Category category );
	}
	
	public void setOnCategoryLoaded(OnCategoryLoaded onCategoryLoaded){
		this.onCategoryLoaded = onCategoryLoaded;
	}
	
	public void setOnProductListLoaded(OnProductListLoaded onProductListLoaded){
		this.onProductListLoaded = onProductListLoaded;
	}
	
	public void setOnProductCategoryChanged(OnProductCategoryChanged onProductCategoryChanged){
		this.onProductCategoryChanged = onProductCategoryChanged;
	}
	
	public void changeCategory( Category category ){
		
		//if( category != currentCategory ) currentCategory.clear();
		currentCategory.clear();
		
		//카테고리 변경 시 기존 카테고리 아이템을 날려줌
		
		
		currentCategory = category;
		if( onCategoryLoaded != null ) onProductCategoryChanged.onProductCategoryChanged( category );
	}
	
	
	public void loadCategorys(final Context context){
		final App app = (App) context.getApplicationContext();
		AsyncHttpRequest ar = new AsyncHttpRequest(app, "", false);
		
		ar.setOnRequestComplete(new AsyncHttpRequest.OnRequestComplete() {
			
			@Override
			public void onRequestComplete(String result, String task, List<NameValuePair> params) {
				if(app.checkError(null, result)) return;
				
				JSONObject rs = null;
				try {
					rs = new JSONObject(result);
					
					String data = rs.getString(WAS_Mgr.RESULT_KEY_DATA);
					JSONArray cateJArr = new JSONArray(data);
					
					int arrLength = cateJArr.length();
					
					ArrayList<Category> cateList = new ArrayList<Category>();
					
					Category showAllCategory = getCategory(-1);
					showAllCategory.title = context.getString(R.string.menu_category_show_all);
					cateList.add(showAllCategory);
					categorys.put(showAllCategory.id, showAllCategory);
					
					
					for (int i = 0; i < arrLength; i++) {
						JSONObject cateJobj = cateJArr.getJSONObject(i);
						
						int cateID = cateJobj.getInt(Category.PARAM_ID);
						
						Category cate = getCategory(cateID);
						cate.title = cateJobj.getString(Category.PARAM_TITLE);
						cate.order = cateJobj.getInt(Category.PARAM_ORDER);
						
						cateList.add(cate);
						categorys.put(cateID, cate);
						
					}
					
					if( onCategoryLoaded != null ) onCategoryLoaded.onCategoryLoaded( cateList );
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
		ar.execute(WAS_Mgr.GET_ITEM_CATEGORY_LIST);
		
	}
	
	public synchronized Category getCategory(int categoryID){
		Category cate = categorys.get(categoryID);
		
		if( cate == null ){
			cate = new Category();
			cate.id = categoryID;
			categorys.put(categoryID, cate);
		}
		return cate;
	}
	
	public synchronized Product getProduct(long productID){
		Product product = products.get(productID);
		
		if( product == null ){
			product = new Product();
			product.item_seq = productID;
			products.put(productID, product);
			
		}
		return product;
	}
	
	
	public void loadProducts(Activity activity, boolean isShowDialog){
		final App app = (App) activity.getApplicationContext();
		AsyncHttpRequest ar = new AsyncHttpRequest(Utils.getTopParent(activity), "", isShowDialog);
		
		//Category targetCategory = getCategory(currentCategory.id);
		final int targetCateID = currentCategory.id;
		
		ar.addParam(Product.PARAM_ITEM_CATEGORY_ID, currentCategory.id+"");
		ar.addParam(Product.PARAM_LAST_ITEM_SEQ, currentCategory.getLastProductSeq()+"");
		
		//Logger.d("cateID = "+currentCategory.id+", lastSeq = "+targetCategory.getLastProductSeq());
		
		ar.setOnRequestComplete(new AsyncHttpRequest.OnRequestComplete() {
			
			@Override
			public void onRequestComplete(String result, String task, List<NameValuePair> params) {
				if(app.checkError(null, result)) return;
				
				JSONObject rs = null;
				try {
					rs = new JSONObject(result);
					
					String data = rs.getString(WAS_Mgr.RESULT_KEY_DATA);
					JSONArray productJArr = new JSONArray(data);
					
					int arrLength = productJArr.length();
					
					//ArrayList<Product> productList = new ArrayList<Product>();
					
					long last_seq = Product.LAST_ITEM_SEQ;
					
					Category category = getCategory(targetCateID);
					
					for (int i = 0; i < arrLength; i++) {
						JSONObject productJobj = productJArr.getJSONObject(i);
						
						long productID = productJobj.getLong(Product.PARAM_ITEM_SEQ);
						
						Product product = getProduct(productID);
						
						product.setData(productJobj);
						
						Logger.d("순서가 어떻게되냐!! = "+product.item_seq);
						
						
						category.addProduct(product);
						
						if( product.item_seq < last_seq || last_seq == 0 ) last_seq = product.item_seq;
						
						/*
						//전체 카테고리 요청일 시 여러 카테고리의 product를 받기때문에 각 카테고리에도 담아주는 처리
						if( targetCategory == totalCategory ){
							Category cate = getCategory(product.item_category_id);
							cate.addProduct(product);
							if( product.item_seq < cate.getLastProductSeq() || cate.getLastProductSeq() == 0 ) cate.setLastProductSeq(product.item_seq);
						}else{
							totalCategory.addProduct( product );
						}
						*/
					}
					
					category.setLastProductSeq(last_seq);
					
					//Logger.d("마지막놈!! = "+targetCategory.getLastProductSeq());
					
					if( onProductListLoaded != null ) onProductListLoaded.onProductListLoaded( category );
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
		ar.execute(WAS_Mgr.GET_ITEM_LIST_BY_CATEGOTY);
	}
	
	class DataComparator implements java.util.Comparator<Long> {

		@Override
		public int compare(Long lhs, Long rhs) {
			return lhs > rhs ? 1 : (lhs == rhs ? 0 : -1); // descending 정렬.....
		}
    }
	
}
