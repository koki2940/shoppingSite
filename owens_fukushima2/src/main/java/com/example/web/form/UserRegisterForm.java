package com.example.web.form;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class UserRegisterForm implements Serializable {

	//seriaVersionUIDを指定
	private static final long serialVersionUID = 1L;

	@NotEmpty
	private String userName; //ユーザー名
	@NotEmpty
	private String password; //パスワード
	@NotEmpty
	private String passwordConf; //パスワード確認用
	@NotEmpty
	private String postNumber; //郵便番号
	@NotEmpty
	private String address; //住所
	@NotEmpty
	@Email
	private String mail; //メール

	/**
	 * @return userName ユーザー名
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
	/**
	 * @return passwordConf パスワード確認用
	 */
	public String getPasswordConf() {
		return passwordConf;
	}
	/**
	 * @param passwordConf セットする パスワード確認用
	 */
	public void setPasswordConf(String passwordConf) {
		this.passwordConf = passwordConf;
	}
	/**
	 * @return postNumber 郵便番号
	 */
	public String getPostNumber() {
		return postNumber;
	}
	/**
	 * @param postNumber セットする 郵便番号
	 */
	public void setPostNumber(String postNumber) {
		this.postNumber = postNumber;
	}
	/**
	 * @return address 住所
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address セットする 住所
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return mail メール
	 */
	public String getMail() {
		return mail;
	}
	/**
	 * @param mail セットする メール
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}


}
