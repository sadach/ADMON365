package com.covas.admon.android.product;

import org.json.JSONException;
import org.json.JSONObject;

import com.covas.admon.android.dao.User;

public class Product {
	
	public static final String PARAM_LAST_ITEM_SEQ = "lastItemSeq";
	public static final String PARAM_ITEM_CATEGORY_ID = "item_category_id";
	public static final String PARAM_ITEM_SEQ = "item_seq";
	
	/**
	 * 마지막 아이템인지 판별하는 기준
	 */
	public static final long 	LAST_ITEM_SEQ = 0;
	
	public long 		item_seq;
	public String 	itemName;
	public String 	itemDesc;
	public int	 		totalSaleCount;
	public int	 		totalQtyCount;
	public String		saleStart;
	public String		saleEnd;
	public String		mainImgURL;
	public String		originalPrice;
	public String		salePrice;
	public String		supplyPrice;
	public int			item_category_id;
	public int			itemViewCount;
	public String		discountRate;
	public String		itemCode;
		
	public String		SvipAdFee;
	public String		VipAdFee;
	public String		GoldAdFee;
	public String		SilverAdFee;
	public String		orderNum;
	public String		landingURL;
	public String		defaultAdText;
	
	public String		svipRewardRate;
	public String		vipRewardRate;
	public String		goldRewardRate;
	public String		silverRewardRate;
	
	public String		SvipDcRate;
	public String		VipDcRate;
	public String		GoldDcRate;
	public String		SilverDcRate;
	
	public String	imgKey;
	
	//FIXME 친구 구입 시 리워드 받아서 처리하는걸로 변경해라
	//public String	friendBuyReward = "5";
	
	
	public String getGradeAdFee(String degree){
		
		if( User.DEGREE_SVIP.equalsIgnoreCase( degree ) ){
			return SvipAdFee;
		}else if( User.DEGREE_VIP.equalsIgnoreCase( degree ) ){
			return VipAdFee;
		}else if( User.DEGREE_GOLD.equalsIgnoreCase( degree ) ){
			return GoldAdFee;
		}else if( User.DEGREE_REGULAR.equalsIgnoreCase( degree ) ){
			return SilverAdFee;
		}
		
		return SilverAdFee;
	}

	public String getGradeRewardRate(String degree){
		
		if( User.DEGREE_SVIP.equalsIgnoreCase( degree ) ){
			return svipRewardRate;
		}else if( User.DEGREE_VIP.equalsIgnoreCase( degree ) ){
			return vipRewardRate;
		}else if( User.DEGREE_GOLD.equalsIgnoreCase( degree ) ){
			return goldRewardRate;
		}else if( User.DEGREE_REGULAR.equalsIgnoreCase( degree ) ){
			return silverRewardRate;
		}
		
		return silverRewardRate;
	}
	
	public String getGradeDcRate(String degree){
		
		if( User.DEGREE_SVIP.equalsIgnoreCase( degree ) ){
			return SvipDcRate;
		}else if( User.DEGREE_VIP.equalsIgnoreCase( degree ) ){
			return VipDcRate;
		}else if( User.DEGREE_GOLD.equalsIgnoreCase( degree ) ){
			return GoldDcRate;
		}else if( User.DEGREE_REGULAR.equalsIgnoreCase( degree ) ){
			return SilverDcRate;
		}
		
		return SilverDcRate;
	}
	
	
	public void setData(JSONObject productJobj) throws JSONException{
		item_seq = productJobj.getLong( "item_seq" );
		itemName = productJobj.getString( "itemName" );
		itemDesc = productJobj.getString( "itemDesc" );
		totalSaleCount = productJobj.getInt( "totalSaleCount" );
		totalQtyCount = productJobj.getInt( "totalQtyCount" );
		saleStart = productJobj.getString( "saleStart" );
		saleEnd = productJobj.getString( "saleEnd" );
		mainImgURL = productJobj.getString( "mainImgURL" );
		originalPrice = productJobj.getString( "originalPrice" );
		salePrice = productJobj.getString( "salePrice" );
		supplyPrice = productJobj.getString( "supplyPrice" );
		item_category_id = productJobj.getInt( "item_category_id" );
		itemViewCount = productJobj.getInt( "itemViewCount" );
		discountRate = productJobj.getString( "discountRate" );
		itemCode = productJobj.getString( "itemCode" );
	
		SvipAdFee = productJobj.getString( "SvipAdFee" );
		VipAdFee = productJobj.getString( "VipAdFee" );
		GoldAdFee = productJobj.getString( "GoldAdFee" );
		SilverAdFee = productJobj.getString( "SilverAdFee" );
		orderNum = productJobj.getString( "orderNum" );
		landingURL = productJobj.getString( "landingURL" );
		defaultAdText = productJobj.getString( "defaultAdText" );
		
		svipRewardRate = productJobj.getString( "svipRewardRate" );
		vipRewardRate = productJobj.getString( "vipRewardRate" );
		goldRewardRate = productJobj.getString( "goldRewardRate" );
		silverRewardRate = productJobj.getString( "silverRewardRate" );
		
		SvipDcRate = productJobj.getString( "SvipDcRate" );
		VipDcRate = productJobj.getString( "VipDcRate" );
		GoldDcRate = productJobj.getString( "GoldDcRate" );
		SilverDcRate = productJobj.getString( "SilverDcRate" );
		
		
		imgKey = mainImgURL.substring( (mainImgURL.lastIndexOf("/")+1) );
	}
}
