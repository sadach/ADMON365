/*
 * Copyright (c) 2011 Incruit Co., Ltd. All rights reserved. 
 * 
 * This article may not be published, rewritten or redistributed.
 * 
 * 상기 저작권에 대한 사항은 인크루트(주)에 있으며 사전 승인 없이 무단전재 및 재배포 경우,
 * 관련 법령에 의거 제재를 받을 수 있습니다. 
 */
package com.covas.admon.android.util;

import java.util.Locale;
import java.util.regex.Pattern;

import android.util.Log;

public class StringUtils {
	
	public static final String BLANK = "";
	public static final char FORMAT = "#".charAt(0);
	
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	public static final Pattern IS_ALPHABET = Pattern.compile("[a-z]");
	public static final Pattern IS_HANGUL = Pattern.compile("[ㄱ-힣]");
	
	// ㄱ ㄲ ㄴ ㄷ ㄸ ㄹ ㅁ ㅂ ㅃ ㅅ ㅆ ㅇ ㅈ ㅉ ㅊ ㅋ ㅌ ㅍ ㅎ
	final static char[] ChoSung = { 0x3131, 0x3132, 0x3134, 0x3137, 0x3138, 0x3139, 0x3141, 0x3142, 0x3143, 0x3145, 0x3146,
		0x3147, 0x3148, 0x3149, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e };
	// ㅏ ㅐ ㅑ ㅒ ㅓ ㅔ ㅕ ㅖ ㅗ ㅘ ㅙ ㅚ ㅛ ㅜ ㅝ ㅞ ㅟ ㅠ ㅡ ㅢ ㅣ
	final static char[] JwungSung = { 0x314f, 0x3150, 0x3151, 0x3152, 0x3153, 0x3154, 0x3155, 0x3156, 0x3157, 0x3158, 0x3159,
		0x315a, 0x315b, 0x315c, 0x315d, 0x315e, 0x315f, 0x3160, 0x3161, 0x3162, 0x3163 };
	// ㄱ ㄲ ㄳ ㄴ ㄵ ㄶ ㄷ ㄹ ㄺ ㄻ ㄼ ㄽ ㄾ ㄿ ㅀ ㅁ ㅂ ㅄ ㅅ ㅆ ㅇ ㅈ ㅊ ㅋ ㅌ ㅍ ㅎ
	final static char[] JongSung = { 0, 0x3131, 0x3132, 0x3133, 0x3134, 0x3135, 0x3136, 0x3137, 0x3139, 0x313a, 0x313b, 0x313c,
		0x313d, 0x313e, 0x313f, 0x3140, 0x3141, 0x3142, 0x3144, 0x3145, 0x3146, 0x3147, 0x3148, 0x314a, 0x314b, 0x314c,
		0x314d, 0x314e };
	
	public static boolean isEmpty(String value) {
		
		if ( value == null)
			return true;
		else
			return  BLANK.equals(value.trim());
	}
	public static boolean isEmpty(CharSequence value) {
		
		if ( value == null ) return true;
		return isEmpty(value.toString());
	}
	
	public static String timeFormat(String value){
		int num = Integer.parseInt(value);
		StringBuffer sb = new StringBuffer(value);
		if(num < 10){
			sb.insert(0, "00:0");
		}else if(num < 100){
			sb.insert(0, "00:");
		}else if(num < 1000){
			sb.insert(1, ":");
			sb.insert(0, "0");
		}else if(num >= 1000){
			sb.insert(2, ":");
		}
		return sb.toString();
	}
	
	public static String dateFormat(String value){
		StringBuffer sb = new StringBuffer(value);
		sb.insert(6, "-");
		sb.insert(4, "-");
		return sb.toString();
	}
	
	public static String timeToString(int num){
		String value = String.valueOf(num);
		if(value.length() == 1){
			value = "0"+value;
		}
		return value;
	}
	
	public static boolean isHangul(char _char){
		return (_char >= 0xAC00 && _char <= 0xD7A3 || _char >= 0x3131 && _char <= 0x3163);
	}
	public static boolean isAlphabet(char _char){
		return (_char >= 0x61 && _char <= 0x7a || _char >= 0x41 && _char <= 0x5a);
	}
	public static boolean isCharacter(char _char){
		
		if(isAlphabet(_char)){
			return true;
		}else if(isHangul(_char)){
			return true;
		}
		return false;
	}
	/*public static boolean isHangul(String singleStr){
		return IS_HANGUL.matcher(singleStr).matches();
	}
	public static boolean isAlphabet(String singleStr){
		return IS_ALPHABET.matcher(singleStr.toLowerCase()).matches();
	}*/
	/*public static boolean isCharacter(String singleStr){
		
		if(IS_ALPHABET.matcher(singleStr.toLowerCase()).matches()){
			return true;
		}else if(IS_HANGUL.matcher(singleStr).matches()){
			return true;
		}
		return false;
	}*/
			
	public static String hangulToJaso(char _char) { // 유니코드 한글 문자열을 입력 받음
		int a, b; // 자소 버퍼: 초성/종성
		String result = "";
		
		if (_char >= 0xAC00 && _char <= 0xD7A3) { // "AC00:가" ~ "D7A3:힣" 에 속한 글자면 분해
			
			b = _char - 0xAC00;
			a = b / (21 * 28);
			result = String.valueOf(ChoSung[a]);
			
		} else {
			result = String.valueOf(_char);
		}
		return result;
	}
	
	public static boolean compareString(String str1, String str2, int index){
		Log.d("FRENz", "str1 = "+str1+", str2 = "+str2);
		boolean flag = false;
		
		String _str1 = StringUtils.hangulToJaso(str1.charAt(index)).toLowerCase();
		String _str2 = StringUtils.hangulToJaso(str2.charAt(index)).toLowerCase();
		
		if(_str1.compareTo(_str2) > 0){
			flag = true;
		}else if(_str1.compareTo(_str2) == 0){
			
			String __str1 = str1.substring(index, index+1).toLowerCase();
			String __str2 = str2.substring(index, index+1).toLowerCase();
			
			if(__str1.compareTo(__str2) > 0){
				flag = true;
			}else if(__str1.compareTo(__str2) == 0 && str1.length()-1 != index && str2.length()-1 != index){
				flag = compareString(str1, str2, index+1);
			}
		}
		
		return flag;
	}
	
	public static String toLowerCase(String str){
		return str.toLowerCase(Locale.ENGLISH);
	}
}
