package com.example.web.form;

import java.util.ArrayList;
import java.util.List;

public class ProductPurchaseForm {

	private Integer productId;
	
	private String productName;
	
	private Integer price;
	
	private Integer quantity;
	
	private String genreName;
	
	private Integer purchaseQty;
	
	private Integer totalAmount;

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
	 * @return totalAmount
	 */
	public Integer getTotalAmount() {
		return totalAmount;
	}

	/**
	 * @param totalAmount セットする totalAmount
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
