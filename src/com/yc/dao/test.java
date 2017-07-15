package com.yc.dao;

public class test {
	public static void main(String[] args) {
		DBHelper db=new DBHelper();
	     System.out.println(db.findMultiObject("select * from newsType where status=1 order by tid", null));
	}
     
}
