package com.example.domain;

public class ProductInfo {

	private Integer productId; //商品ID

	private String productName; //商品名

	private Integer price;//価格

	private Integer genre; //ジャンル番号

	private String genreName; //ジャンル名

	private Integer quantity; //在庫数

	private Integer purchaseId; //購入ID

	private Integer purchaseQty; //購入数

	private String purchaseDateTime; //購入日時

	private Integer versionNumber; //バージョン番号

	private Integer deleteFlag; //削除フラグ

	private Integer cartId; //カートID

	/**
	 * @return productId 商品ID
	 */
	public Integer getProductId() {
		return productId;
	}
	/**
	 * @param productId セットする 商品ID
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	/**
	 * @return productName 商品名
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * @param productName セットする 商品名
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * @return price 価格
	 */
	public Integer getPrice() {
		return price;
	}
	/**
	 * @param price セットする 価格
	 */
	public void setPrice(Integer price) {
		this.price = price;
	}
	/**
	 * @return genre ジャンル番号
	 */
	public Integer getGenre() {
		return genre;
	}
	/**
	 * @param genre セットする ジャンル番号
	 */
	public void setGenre(Integer genre) {
		this.genre = genre;
	}
	/**
	 * @return genreName ジャンル名
	 */
	public String getGenreName() {
		return genreName;
	}
	/**
	 * @param genreName セットする ジャンル名
	 */
	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}
	/**
	 * @return quantity 在庫数
	 */
	public Integer getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity セットする 在庫数
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return purchaseId 購入ID
	 */
	public Integer getPurchaseId() {
		return purchaseId;
	}
	/**
	 * @param purchaseId セットする 購入ID
	 */
	public void setPurchaseId(Integer purchaseId) {
		this.purchaseId = purchaseId;
	}

	/**
	 * @return purchaseQty 購入数
	 */
	public Integer getPurchaseQty() {
		return purchaseQty;
	}
	/**
	 * @param purchaseQty セットする 購入数
	 */
	public void setPurchaseQty(Integer purchaseQty) {
		this.purchaseQty = purchaseQty;
	}
	/**
	 * @return purchaseDateTime 購入日時
	 */
	public String getPurchaseDateTime() {
		return purchaseDateTime;
	}
	/**
	 * @param purchaseDateTime セットする 購入日時
	 */
	public void setPurchaseDateTime(String purchaseDateTime) {
		this.purchaseDateTime = purchaseDateTime;
	}

	/**
	 * @return versionNumber バージョン番号
	 */
	public Integer getVersionNumber() {
		return versionNumber;
	}

	/**
	 * @param versionNumber セットする バージョン番号
	 */
	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	/**
	 * @return deleteFlag 削除フラグ
	 */
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	/**
	 * @param deleteFlag セットする 削除フラグ
	 */
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	/**
	 * @return cartId カートID
	 */
	public Integer getCartId() {
		return cartId;
	}
	/**
	 * @param cartId セットする カートID
	 */
	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}



	public String getDateTime() {

		String[] dateTime = purchaseDateTime.split("\\.");

		return dateTime[0];

	}

}
