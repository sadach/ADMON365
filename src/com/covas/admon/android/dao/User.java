/*
 * Copyright (c) DoRan Communications Co., Ltd. All rights reserved. 
 * author : 신기웅(moai.kiz@gmail.com)
 * This article may not be published, rewritten or redistributed.
 */

package com.covas.admon.android.dao;

import org.json.JSONException;
import org.json.JSONObject;

import com.covas.admon.android.mgr.WAS_Mgr;


/** 설명 
 * 
 * @author 신기웅(moai.kiz@gmail.com)
 * @since 2012. 12. 3.
 * @version 1.0.0
 */
public class User {
	
	public static final String MALE = "m";
	public static final String FEMALE = "f";
	
	public static final String DEGREE_SVIP = "s";
	public static final String DEGREE_VIP = "v";
	public static final String DEGREE_GOLD = "g";
	public static final String DEGREE_REGULAR = "r";
	
	
	public String id = "";
	public String email = "";
	public String byear = "";
	public String sex = "";
	public String location1 = "";
	public String location2 = "";
	public String passwd = "";
	public String phone = "";
	public String degree = "";
	
	public String point = "";
	public String seq = "";
	
	public User(){
		
	}
	
	public String getGrade(){
		if( degree.equalsIgnoreCase("r") ){
			return "Regular";
		}else if( degree.equalsIgnoreCase("g") ){
			return "Gold";
		}else if( degree.equalsIgnoreCase("v") ){
			return "VIP";
		}else if( degree.equalsIgnoreCase("s") ){
			return "SVIP";
		}
		return "Regular";
	}
	
	public void copy( User user ){
		this.byear = user.byear;
		this.degree = user.degree;
		this.email = user.email;
		this.id = user.id;
		this.location1 = user.location1;
		this.location2 = user.location2;
		this.passwd = user.passwd;
		this.phone = user.phone;
		this.sex = user.sex;
		
		this.point = user.point;
		this.seq = user.seq;
	}
	
	public JSONObject getUserJson() throws JSONException{
		JSONObject userJson = new JSONObject();
		
		userJson.put(WAS_Mgr.PARAM_EMAIL, email);
		userJson.put(WAS_Mgr.PARAM_ID, id);
		userJson.put(WAS_Mgr.PARAM_BYEAR, byear);
		userJson.put(WAS_Mgr.PARAM_SEX, sex);
		userJson.put(WAS_Mgr.PARAM_LOCATION1, location1);
		userJson.put(WAS_Mgr.PARAM_LOCATION2, location2);
		userJson.put(WAS_Mgr.PARAM_PWD, passwd);
		userJson.put(WAS_Mgr.PARAM_PHONE, phone);
		
		return userJson;
	}
	
	
	public User clone() {
		User user = new User();
		
		user.byear = this.byear;
		user.degree = this.degree;
		user.email = this.email;
		user.id = this.id;
		user.location1 = this.location1;
		user.location2 = this.location2;
		user.passwd = this.passwd;
		user.phone = this.phone;
		user.sex = this.sex;
		
		user.point = this.point;
		user.seq = this.seq;
		return user;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("id = "+id+"\n");
		sb.append("degree = "+degree+"\n");
		sb.append("passwd = "+passwd+"\n");
		
		
		sb.append("seq = "+seq+"\n");
		sb.append("email = "+email+"\n");
		sb.append("byear = "+byear+"\n");
		sb.append("sex = "+sex+"\n");
		sb.append("location1 = "+location1+"\n");
		sb.append("location2 = "+location2+"\n");
		sb.append("phone = "+phone+"\n");
		
		
		return sb.toString();
	}
}
