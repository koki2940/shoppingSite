package com.example.web.form;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ProductRegisterForm implements Serializable {

	//serialVersionUIDを設定
	private static final long serialVersionUID = 1L;
	@NotEmpty
	private String productName;
	@NotNull
	private Integer price;
	@NotNull
	private Integer genre;

	private String genreName;
	@NotNull
	private Integer quantity;

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

	//「ジャンル」のセレクトボックス作成メソッド
	public Map<Integer,String> getGenreMap() {
		//ジャンルを格納するリストを作成
		Map<Integer,String> genre = new HashMap<Integer, String>();

		//マップに追加
		genre.put(1, "食品・飲料");
		genre.put(2, "衣服類");
		genre.put(3, "本・マンガ・雑誌");
		genre.put(4, "電化製品");
		genre.put(5, "家具");
		genre.put(6, "美容品");
		genre.put(7, "おもちゃ・ゲーム");


		return genre;
	}



}
