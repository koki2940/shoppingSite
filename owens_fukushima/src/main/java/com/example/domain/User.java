package com.example.domain;

public class User {

	private Integer userId; // ユーザーId
	private String userName; // ユーザ名
	private String password; // パスワード
	private String mail; // メールアドレス
	private String postNumber; // 郵便番号
	private String address; // 住所
	private Integer authority; // 権限
	private Integer giftAmount; // ギフト券金額

	/**
	 * @return userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId セットする userId
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

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

	/**
	 * @return authority
	 */
	public Integer getAuthority() {
		return authority;
	}

	/**
	 * @param authority セットする authority
	 */
	public void setAuthority(Integer authority) {
		this.authority = authority;
	}

	/**
	 * @return giftAmount
	 */
	public Integer getGiftAmount() {
		return giftAmount;
	}

	/**
	 * @param giftAmount セットする giftAmount
	 */
	public void setGiftAmount(Integer giftAmount) {
		this.giftAmount = giftAmount;
	}

}
