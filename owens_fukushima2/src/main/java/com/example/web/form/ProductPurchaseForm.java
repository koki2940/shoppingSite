package com.example.web.form;

import java.util.ArrayList;
import java.util.List;

public class ProductPurchaseForm {

	private Integer productId; //商品ID

	private String productName; //商品名

	private Integer price; //価格

	private Integer quantity; //在庫数

	private String genreName; //ジャンル名

	private Integer purchaseQty; //購入数

	private Integer totalAmount; //合計金額

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
	 * @return totalAmount 合計金額
	 */
	public Integer getTotalAmount() {
		return totalAmount;
	}

	/**
	 * @param totalAmount セットする 合計金額
	 */
	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * セレクトボックスに購入できる数量を表示するメソッド
	 * @return  在庫数までの数のリスト
	 */
	public List<Integer> purchaseQtyList() {
		//購入数量を格納するリストを作成
		List<Integer> purchaseQtyList = new ArrayList<Integer>();

		for (int i = 1; i <= quantity; i++) {
			//リストに追加
			purchaseQtyList.add(i);
		}

		return purchaseQtyList;
	}


}
