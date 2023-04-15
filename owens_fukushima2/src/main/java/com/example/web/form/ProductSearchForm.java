package com.example.web.form;

import java.io.Serializable;

public class ProductSearchForm implements Serializable{

	//serialVersionUIDを設定
	private static final long serialVersionUID = 1L;

	private String productName; //商品名

	private Integer priceMin; //最小価格

	private Integer priceMax; //最大価格

	private Integer[] genre; //ジャンル番号

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
	 * @return priceMin 最小価格
	 */
	public Integer getPriceMin() {
		return priceMin;
	}
	/**
	 * @param priceMin セットする 最小価格
	 */
	public void setPriceMin(Integer priceMin) {
		this.priceMin = priceMin;
	}
	/**
	 * @return priceMax 最大価格
	 */
	public Integer getPriceMax() {
		return priceMax;
	}
	/**
	 * @param priceMax セットする 最大価格
	 */
	public void setPriceMax(Integer priceMax) {
		this.priceMax = priceMax;
	}
	/**
	 * @return genre ジャンル番号
	 */
	public Integer[] getGenre() {
		return genre;
	}
	/**
	 * @param genre セットする ジャンル番号
	 */
	public void setGenre(Integer[] genre) {
		this.genre = genre;
	}




}
