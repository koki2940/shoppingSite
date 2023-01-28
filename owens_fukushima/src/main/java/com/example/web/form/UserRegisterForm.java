package com.example.web.form;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserRegisterForm implements Serializable {

	//seriaVersionUIDを指定
	private static final long serialVersionUID = 1L;

	@NotEmpty
	private String userName;
	@NotEmpty
	private String password;

	private String passwordConf;
	@Email
	@NotEmpty
	private String mail; //メールアドレス
	@NotEmpty
	private String postNumber; //郵便番号
	@NotEmpty
	private String address; //住所

	private Integer authority; //権限

	/**
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName セットする userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password セットする password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return passwordConf
	 */
	public String getPasswordConf() {
		return passwordConf;
	}

	/**
	 * @param passwordConf セットする passwordConf
	 */
	public void setPasswordConf(String passwordConf) {
		this.passwordConf = passwordConf;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPostNumber() {
		return postNumber;
	}

	public void setPostNumber(String postNumber) {
		this.postNumber = postNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
