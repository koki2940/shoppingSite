package com.example.web.form;

public class UserLoginForm {

	private String userName; //ユーザー名

	private String password; //パスワード
	/**
	 * @return userName //ユーザー名
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName セットする ユーザー名
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return password パスワード
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password セットする パスワード
	 */
	public void setPassword(String password) {
		this.password = password;
	}



}
