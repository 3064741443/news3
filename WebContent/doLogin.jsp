<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.yc.biz.impl.*,com.yc.bean.*"%>
<%
     //设置编码
     request.setCharacterEncoding("utf-8");
     //2.取出参数
     String uname=request.getParameter("uname");
     String pwd=request.getParameter("pwd");
    
     User u=new User();
     u.setUname(uname);
     u.setPwd(pwd);
 
     UserBizImpl userBizImpl=new  UserBizImpl();
     
     try{
    	 u=userBizImpl.login(u);
    	 out.print("登录成功...<a href='javascript:history.go(-1)'>返回上一页<a/>");
     }catch(Exception e){
    	 out.print(e.getMessage());
    	 out.print("登录失败...<a href='javascript:history.go(-1)'>返回上一页<a/>");
     }
%>