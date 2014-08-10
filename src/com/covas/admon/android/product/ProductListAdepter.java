package com.covas.admon.android.product;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.covas.admon.android.App;
import com.covas.admon.android.R;
import com.covas.admon.android.dao.User;
import com.covas.admon.android.mgr.WAS_Mgr;
import com.covas.admon.android.util.BtnUtils;
import com.covas.admon.android.util.Image_Mgr;
import com.covas.admon.android.util.Image_Mgr.OnImageLoadComplete;
import com.covas.admon.android.web.ProductDetailAct;
import com.covas.admon.android.wechat.WeChatMgr;

public class ProductListAdepter extends ArrayAdapter<Product> implements OnClickListener, OnImageLoadComplete {
	
	private ArrayList<Product> _productList;
	
	private Activity _activity;
	
	private LayoutInflater _inflater;
	
	private App app;
	
	private Image_Mgr imgMgr;
	
	//private WeChatMgr wechatMgr;
	
	public ProductListAdepter(Activity activity, ArrayList<Product> productList) {
		super(activity, 0, productList);
		_activity = activity;
		_productList = productList;
		
		_inflater = _activity.getLayoutInflater();
		
		app = (App) _activity.getApplicationContext();
		
		imgMgr = app.getImgMgr();
		imgMgr.setOnImageLoadCompleteListener(this);
		
		//wechatMgr = WeChatMgr.getInstance( activity );
	}
	
	public class ViewHolder {
		
		public ImageView 	ivImg;
		public ProgressBar	pbLoading;
		
		public TextView 		tvSold;
		public TextView 		tvEndDate;
		public TextView 		tvTitle;
		public TextView 		tvDesc;
		public TextView 		tvDiscountS;
		public TextView 		tvDiscountB;
		public Button 			btnShowDetail;
		public Button 			btnPoint;
		public Button 			btnPercentage;
		
		public int				position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null)
		{
			convertView = initItemView();
		}
		
		holder = (ViewHolder) convertView.getTag();  
		
		Product product = _productList.get(position);
		
		String soldTxt = _activity.getString(R.string.product_sold)+" : "+product.totalSaleCount;
		String endDateTxt = _activity.getString(R.string.product_enddate)+" : "+product.saleEnd;
		String titleTxt = product.itemName;
		String descTxt = product.itemDesc;
		
		int dcRate = Integer.parseInt(product.discountRate) + Integer.parseInt(product.getGradeDcRate(app.getUser().degree));
		
		String discountTxt = dcRate+"%";
		
		String adFee = product.getGradeAdFee(app.getUser().degree);
		String rewardRate = product.getGradeRewardRate(app.getUser().degree);
		
		//String btnPointText = _activity.getString(R.string.product_friend_notify_head)+adFee+_activity.getString(R.string.product_friend_notify_tail);
		//FIXME 친구 구입 시 리워드 받아서 처리하는걸로 변경해라
		//String btnBuyRewardText = _activity.getString(R.string.product_friend_buy_head)+rewardRate+_activity.getString(R.string.product_friend_buy_tail);
		
		holder.position = position;
		
		Object[] obj = new Object[2];
		obj[0] = holder;
		obj[1] = position;
		Bitmap bitmap = getImage(product, obj);
		
		if( bitmap == null ){
			holder.pbLoading.setVisibility( View.VISIBLE );
			holder.ivImg.setImageBitmap(null);
		}else{
			holder.pbLoading.setVisibility( View.GONE );
			holder.ivImg.setImageBitmap(bitmap);
		}
		
		holder.ivImg.setTag(position);
		holder.btnShowDetail.setTag(position);
		holder.btnPoint.setTag(position);
		holder.btnPercentage.setTag(position);
		
		
		holder.tvSold.setText(soldTxt);
		holder.tvEndDate.setText(endDateTxt);
		holder.tvTitle.setText(titleTxt);
		holder.tvDesc.setText(descTxt);
		holder.tvDiscountS.setText(discountTxt);
		holder.tvDiscountB.setText(discountTxt);
		
		//holder.btnPoint.setText(btnPointText);
		//holder.btnPercentage.setText(btnBuyRewardText);
		
		setRewardBtnText(holder.btnPoint, _activity.getString(R.string.product_friend_notify_head), adFee, _activity.getString(R.string.product_friend_notify_tail));
		setRewardBtnText(holder.btnPercentage, _activity.getString(R.string.product_friend_buy_head), rewardRate, _activity.getString(R.string.product_friend_buy_tail));
		
		//String btnPointText = _activity.getString(R.string.product_friend_notify_head)+adFee+_activity.getString(R.string.product_friend_notify_tail);
		//FIXME 친구 구입 시 리워드 받아서 처리하는걸로 변경해라
		//String btnBuyRewardText = _activity.getString(R.string.product_friend_buy_head)+rewardRate+_activity.getString(R.string.product_friend_buy_tail);
				
		
		return convertView; 
	}
	
	private void setRewardBtnText( Button btn, String pStr, String value, String tStr ){
		SpannableStringBuilder sb = new SpannableStringBuilder();
		String blank1 = "  ";
		String blank2 = " ";
		String str = pStr+blank1+value+blank2+tStr;
		int colorStartIndex = pStr.length();
		sb.append(str);
		sb.setSpan(new ForegroundColorSpan(Color.RED), colorStartIndex, colorStartIndex+value.length()+blank1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		btn.setText(sb);
	}
	
	
	private Bitmap getImage(Product product, Object target){
		return imgMgr.loadImage(product.mainImgURL, product.imgKey, target);
	}
	
	@Override
	public void onImageLoaded(String imgName, Bitmap bitmap, Object target) {
		Object[] obj = (Object[]) target;
		ViewHolder holder = ((ViewHolder) obj[0]);
		int position1 = holder.position;
		int position2 = (Integer) obj[1];
		
		if( position1 == position2 ){
			holder.ivImg.setImageBitmap(bitmap);
			holder.pbLoading.setVisibility(View.GONE);
		}
	}
	
	private View initItemView(){
		ViewHolder holder = new ViewHolder();
		View convertView = _inflater.inflate(R.layout.product_list_item, null);
		holder.ivImg = (ImageView) convertView.findViewById(R.id.product_iv_image); 
			
		holder.tvSold = (TextView) convertView.findViewById(R.id.product_item_tv_sold); 
		holder.tvEndDate = (TextView) convertView.findViewById(R.id.product_item_tv_enddate);
		holder.tvTitle = (TextView) convertView.findViewById(R.id.product_item_tv_title);
		holder.tvDesc = (TextView) convertView.findViewById(R.id.product_item_tv_desc);
		
		holder.tvDiscountS = (TextView) convertView.findViewById(R.id.product_item_tv_discount_small);
		holder.tvDiscountB = (TextView) convertView.findViewById(R.id.product_item_tv_discount_big);
		
		holder.btnShowDetail = (Button) convertView.findViewById(R.id.product_item_btn_show_detail);
		holder.btnPoint = (Button) convertView.findViewById(R.id.product_item_btn_point);
		holder.btnPercentage = (Button) convertView.findViewById(R.id.product_item_btn_percentage);
		
		holder.pbLoading = (ProgressBar) convertView.findViewById(R.id.product_item_pb_loading);
		
		holder.tvTitle.setSelected(true);
		holder.tvDesc.setSelected(true);
		
		app.initViewWithResize( convertView.findViewById(R.id.product_iv_top) );
		app.initViewWithResize( holder.ivImg );
		app.initViewWithResize( holder.tvDiscountS );
		app.initViewWithResize( holder.tvDiscountB );
		app.initViewWithResize( holder.btnShowDetail );
		app.initViewWithResize( holder.btnPoint );
		app.initViewWithResize( holder.btnPercentage );
		app.initViewWithResize( convertView.findViewById(R.id.product_iv_shadow) );
		
		
		//BtnUtils.initBtnWithTouchEffect(this, holder.ivImg, true);
		holder.ivImg.setOnClickListener(this);
		BtnUtils.initBtnWithTouchEffect(this, holder.btnShowDetail, true);
		BtnUtils.initBtnWithTouchEffect(this, holder.btnPoint, true);
		BtnUtils.initBtnWithTouchEffect(this, holder.btnPercentage, true);
		
		convertView.setTag(holder);
		
		return convertView;
	}
	
	
	public static class Item{
		
		public String pID = "";
		public String sold = "";
		public String endDate = "";
		public String title = "";
		public String desc = "";
		public String discount = "";
		public String toPoint = "";
		public String toPercentage = "";
		
		public Item() {
			
		}
	}

	@Override
	public void onClick(View v) {
		
		int position = (Integer) v.getTag();
		Product product = _productList.get(position);
		
		/*if( v.getId() == R.id.product_item_btn_show_detail )
		{
			
		}
		else */
		if( v.getId() == R.id.product_item_btn_point || v.getId() == R.id.product_item_btn_percentage )
		{
			User user = app.getUser();
			WeChatMgr.sendViralMsg( _activity, product, user );
		}else {
			showDetailPage(product);
		}
		
		
	}

	private void showDetailPage(Product product){
		
		Intent intent = new Intent(_activity, ProductDetailAct.class);
		intent.putExtra(Product.PARAM_ITEM_SEQ, product.item_seq);
		_activity.startActivity( intent );
	}
}
