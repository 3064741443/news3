package com.yc.biz.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yc.bean.User;
import com.yc.biz.UserBiz;
import com.yc.dao.DBHelper;
import com.yc.utils.Encrypt;

public class UserBizImpl implements UserBiz{
	private DBHelper db=new DBHelper();
	public boolean reg(User user) throws Exception{
		if(user.getUname()==null){
			throw new Exception("ע����û�������Ϊ��");
		}
		
		if(user.getPwd()==null){
			throw new Exception("ע������벻��Ϊ��");
		}
		
		/*System.out.println(user.getPwd());
		System.out.println(user.getPwdagain());*/
		if(!user.getPwd().equals(user.getPwdagain())){
			throw new Exception("������������벻һ��");
		}
		
		
		String sql="insert into users values(seq_users_usid.nextval,?,?,?,1)";
	    List<Object> params=new ArrayList<>();
	    params.add(user.getUname());
	    params.add(Encrypt.md5(Encrypt.md5(user.getPwd())));
	    params.add(user.getEmail());
	    db.doUpdate(sql, params);
		return true;
	}
	@Override
	public User login(User user) {
	     String sql="select usid,email from users where uname=? and pwd=? and status=1";
		 List<Object> params=new ArrayList<>();
		 params.add(user.getUname());
		 params.add(Encrypt.md5(Encrypt.md5(user.getPwd())));
		 List<Map<String,Object>> list=db.findMultiObject(sql, params);
		 if(list==null){
			 return null;
		 }
		 System.out.println(user.getUname());
		 System.out.println(user.getPwd());
		 Map<String,Object> map=list.get(0);
	 	 user.setUsid(Integer.parseInt(map.get("USID").toString()));
		 user.setEmail(map.get("EMAIL").toString());
		 user.setStatus(1);
		 return user;
	}
}
