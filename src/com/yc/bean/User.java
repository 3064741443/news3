package com.yc.bean;

import java.io.Serializable;

public class User implements Serializable{
	private int usid;
	private String uname;
	private String pwd;
	private String pwdagain;
	private String email;
	private int status;
	
	
	public int getUsid() {
		return usid;
	}
	public void setUsid(int usid) {
		this.usid = usid;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	
	public String getPwdagain() {
		return pwdagain;
	}
	public void setPwdagain(String pwdagain) {
		this.pwdagain = pwdagain;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public User(int usid, String uname, String pwd, String email, int status) {
		super();
		this.usid = usid;
		this.uname = uname;
		this.pwd = pwd;
		this.email = email;
		this.status = status;
	}
	public User() {
		super();
	}
	@Override
	public String toString() {
		return "User [usid=" + usid + ", uname=" + uname + ", pwd=" + pwd + ", pwdagain=" + pwdagain + ", email="
				+ email + ", status=" + status + "]";
	}
	
	
}
