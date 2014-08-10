package com.covas.admon.android.util;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListSortWithIntegerField implements Comparator<Object> { 
	
	private Field field;
	private String fieldName;
	
	private Order order;
	
	
	public ListSortWithIntegerField(String fieldName, Order order) {
		this.fieldName = fieldName;
		this.order = order;
	}
	
	public static void sortASC(List<?> list, String fieldName){
		Collections.sort( list, new ListSortWithIntegerField(fieldName, Order.ASC) );
	}
	
	public static void sortDESC(List<?> list, String fieldName){
		Collections.sort( list, new ListSortWithIntegerField(fieldName, Order.DESC) );
	}
	
	public int compare(Object o1, Object o2) {
		
		int by1 = order.default1;
		int by2 = order.default2;
		
		if( field == null ){
			try {
			field = o1.getClass().getDeclaredField(fieldName);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			by1 = field.getInt(o1);
			by2 = field.getInt(o2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//return by1 > by2 ? 1 : (by1 == by2 ? 0 : -1); // ascending 정렬.....
		//return by1 > by2 ? -1 : (by1 == by2 ? 0 : 1); // descending 정렬.....
		
		return by1 > by2 ? order.result1 : ( by1 == by2 ? 0 : order.result2 );
	}
	
	private enum Order{
		ASC( 1, 0, 1, -1 ),
		DESC( 0, 1, -1, 1 );
		
		private int default1;
		private int default2;
		private int result1;
		private int result2;
		
		private Order( int default1, int default2, int result1 ,int result2 ){
			this.default1 = default1;
			this.default2 = default2;
			this.result1 = result1;
			this.result2 = result2;
		}
	}
}
