package com.esp.socialintegrationdemo.fblogin;

import java.io.Serializable;

public class UserBean implements Serializable {
	private static final long serialVersionUID = 1L;

	public int id;
	public String socialId;
	public int socialType;
	public String fname;
	public String lname;
	public String email;
	public String profilePic;
	public String gender;

}
