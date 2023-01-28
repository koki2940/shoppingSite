package com.example.web.form;

import java.io.Serializable;

public class ProductSearchForm implements Serializable{

	//serialVersionUIDを設定
	private static final long serialVersionUID = 1L;

	private String productName;

	private Integer priceMin;

	private Integer priceMax;

	private Integer[] genre;

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
	public Integer getPriceMin() {
		return priceMin;
	}
	/**
	 * @param price セットする price
	 */
	public void setPriceMin(Integer priceMin) {
		this.priceMin = priceMin;
	}
	/**
	 * @return priceMax
	 */
	public Integer getPriceMax() {
		return priceMax;
	}
	/**
	 * @param priceMax セットする priceMax
	 */
	public void setPriceMax(Integer priceMax) {
		this.priceMax = priceMax;
	}
	/**
	 * @return genre
	 */
	public Integer[] getGenre() {
		return genre;
	}
	/**
	 * @param genre セットする genre
	 */
	public void setGenre(Integer[] genre) {
		this.genre = genre;
	}




}
