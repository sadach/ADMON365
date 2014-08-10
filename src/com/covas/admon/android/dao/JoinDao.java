package com.covas.admon.android.dao;


public class JoinDao {
	
	private static JoinDao _instance;
	
	/*public String id = "";
	public String email = "";
	public String sex = "";
	public String byear = "";
	public String location1 = "";
	public String location2 = "";
	public String passwd = "";
	public String phone = "";*/
	
	public User user;

	private JoinDao() {
		init();
	}
	
	public void init(){
		user = new User();
		user.id = "";
		user.email = "";
		user.sex = "";
		user.byear = "2000";
		user.location1 = "서울";
		user.location2 = "송파구";
		user.passwd = "";
		user.phone = "";
	}

	public static JoinDao getInstance() {
		if (_instance == null) {
			synchronized (JoinDao.class) {
				if (_instance == null) {
					_instance = new JoinDao();
				}
			}
		}
		return _instance;
	}
}
