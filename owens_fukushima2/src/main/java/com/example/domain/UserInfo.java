package com.example.domain;

public class UserInfo {

	private Integer userId;  //ユーザーID

	private String userName; //ユーザ名

	private String password; //パスワード

	private Integer authority; //権限

	private Integer giftAmount; //ギフト券金額

	private String postNumber; //郵便番号

	private String address; //住所

	private String mail; //メール

	/**
	 * @return userId ユーザーID
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * @param userId セットする ユーザーID
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * @return userName  ユーザー名
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
	 * @return authority 権限
	 */
	public Integer getAuthority() {
		return authority;
	}
	/**
	 * @param authority セットする 権限
	 */
	public void setAuthority(Integer authority) {
		this.authority = authority;
	}
	/**
	 * @return giftAmount ギフト券金額
	 */
	public Integer getGiftAmount() {
		return giftAmount;
	}
	/**
	 * @param giftAmount セットする ギフト券金額
	 */
	public void setGiftAmount(Integer giftAmount) {
		this.giftAmount = giftAmount;
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
	 * @param mail セットする mail メール
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}


}
