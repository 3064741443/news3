package com.yc.biz;

import com.yc.bean.User;

public interface UserBiz {
	/*
	 * 将user对象存到数据库，成功 ，返回TRUE ，失败，返回false
	 */
	public boolean reg(User user) throws Exception;

	public User login(User user);
}
