package com.yc.biz;

import com.yc.bean.User;

public interface UserBiz {
	/*
	 * ��user����浽���ݿ⣬�ɹ� ������TRUE ��ʧ�ܣ�����false
	 */
	public boolean reg(User user) throws Exception;

	public User login(User user);
}
