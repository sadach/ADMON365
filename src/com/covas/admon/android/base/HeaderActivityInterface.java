package com.covas.admon.android.base;


public interface HeaderActivityInterface {
	
	/**
	 * 좌측 헤더버튼 셋팅 (뒤로가기)
	 */
	//void setHeaderBtnLeft();
	
	/**
	 * 좌측 헤더버튼 셋팅 (뒤로가기)
	 */
	void setHeaderBtnLeft(int resourceID);
	
	/**
	 * drawable 리소스를 받아서 우측 헤더버튼 셋팅
	 * @param resourceID
	 */
	void setHeaderBtnRight(int resourceID);
	
	/**
	 * 좌측 헤더버튼 클릭시 동작하는 메소드
	 */
	void onHeaderBtnLeftClick();
	
	/**
	 * 우측 헤더버튼 클릭시 동작하는 메소드
	 */
	void onHeaderBtnRightClick();
	
	/**
	 * 배경이미지와 타이틀 텍스트 설정
	 * @param bgRID
	 * @param titleStr
	 */
	void setHeaderTitle(int bgRID, String titleStr);
	
	/**
	 * 배경이미지와 타이틀 이미지 설정
	 * @param bgRID
	 * @param titleImgRID
	 */
	void setHeaderTitle(int bgRID, int titleImgRID);
	
	/**
	 * 디폴트 타이틀과 가운데 타이틀 텍스트를 셋팅
	 * @param txtResID
	 * @param bgResID
	 */
	void setHeaderTitle(String titleStr);
}