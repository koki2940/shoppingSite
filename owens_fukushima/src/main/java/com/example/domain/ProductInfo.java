package com.example.domain;

public class ProductInfo {

	private Integer productId;

	private String productName;

	private Integer price;

	private Integer genre;

	private String genreName;

	private Integer quantity;

	private Integer purchaseId;

	private Integer purchaseQty;

	private String purchaseDateTime;

	private Integer versionNumber;
	/**
	 * @return productId
	 */
	public Integer getProductId() {
		return productId;
	}
	/**
	 * @param productId セットする productId
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	/**
	 * @return productName
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * @param productName セットする productName
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * @return price
	 */
	public Integer getPrice() {
		return price;
	}
	/**
	 * @param price セットする price
	 */
	public void setPrice(Integer price) {
		this.price = price;
	}
	/**
	 * @return genre
	 */
	public Integer getGenre() {
		return genre;
	}
	/**
	 * @param genre セットする genre
	 */
	public void setGenre(Integer genre) {
		this.genre = genre;
	}
	/**
	 * @return genreName
	 */
	public String getGenreName() {
		return genreName;
	}
	/**
	 * @param genreName セットする genreName
	 */
	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}
	/**
	 * @return quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity セットする quantity
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return purchaseQty
	 */
	public Integer getPurchaseQty() {
		return purchaseQty;
	}
	/**
	 * @param purchaseQty セットする purchaseQty
	 */
	public void setPurchaseQty(Integer purchaseQty) {
		this.purchaseQty = purchaseQty;
	}
	/**
	 * @return purchaseDate
	 */
	public String getPurchaseDateTime() {
		return purchaseDateTime;
	}
	/**
	 * @param purchaseDate セットする purchaseDate
	 */
	public void setPurchaseDateTime(String purchaseDateTime) {
		this.purchaseDateTime = purchaseDateTime;
	}
	/**
	 * @return purchaseId
	 */
	public Integer getPurchaseId() {
		return purchaseId;
	}
	/**
	 * @param purchaseId セットする purchaseId
	 */
	public void setPurchaseId(Integer purchaseId) {
		this.purchaseId = purchaseId;
	}


	/**
	 * @return versionNumber
	 */
	public Integer getVersionNumber() {
		return versionNumber;
	}
	/**
	 * @param versionNumber セットする versionNumber
	 */
	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getDateTime() {

		String[] dateTime = purchaseDateTime.split("\\.");

		return dateTime[0];

	}










}
