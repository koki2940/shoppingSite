package com.example.web.form;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class ProductCorrectForm implements Serializable{

	//serialVersionUIDを設定
	private static final long serialVersionUID = 1L;

	private Integer productId;

	private String productName;

	@NotNull
	private Integer price;

	private String genreName;

	@NotNull
	private Integer quantity;

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
}
