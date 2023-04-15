package com.example.web.form;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class ProductCorrectForm implements Serializable{

	//serialVersionUIDを設定
	private static final long serialVersionUID = 1L;

	private Integer productId; //商品ID

	private String productName; //商品名

	@NotNull
	private Integer price; //価格

	private String genreName; //ジャンル名

	@NotNull
	private Integer quantity; //在庫数

	private Integer versionNumber; //バージョン番号

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
}
