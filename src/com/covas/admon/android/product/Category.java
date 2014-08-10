package com.covas.admon.android.product;

import java.util.ArrayList;


public class Category {
	
	public static final String PARAM_ID = "it_cat_seq";
	public static final String PARAM_TITLE = "it_cat_title";
	public static final String PARAM_ORDER = "it_cat_order";
	
	//public TreeMap<Long, Product> cateProducts;
	private ArrayList<Product> products;
	
	public int 		id;
	public String 	title;
	public int 		order;
	
	private long 	lastProductSeq;
	
	private boolean noMore = false;
	
	public boolean isChanged = false;
	

	public Category(){
		//cateProducts = new TreeMap<Long, Product>();
		products = new ArrayList<Product>();
		lastProductSeq = 0;
		order = -1;
	}
	
	/*public static ArrayList<Category> getNewArrayList(Context context){
		ArrayList<Category> cateList = new ArrayList<Category>();
		
		Category cate = new Category();
		cate.id = -1;
		cate.order = -1;
		cate.title = context.getString(R.string.menu_category_show_all);
		cateList.add(cate);
		
		return cateList;
	}*/
	
	public void clear(){
		products.clear();
		lastProductSeq = 0;
	}
	
	public Category(int id, String title, int order){
		this();
		this.id = id;
		this.title = title;
		this.order = order;
	}
	
	public ArrayList<Product> getProductList(){
		return products;
	}
	
	public void addProduct(Product product){
		if( products.contains( product ) ) return;
		
		products.add(product);
	}
	
	public void setLastProductSeq(long lastProductSeq){
		this.lastProductSeq = lastProductSeq;
		if( lastProductSeq == Product.LAST_ITEM_SEQ ){
			noMore = true;
		}else{
			noMore = false;
		}
	}
	
	public long getLastProductSeq() {
		return lastProductSeq;
	}
	
	public boolean isNoMore(){
		return noMore;
	}
	
}
