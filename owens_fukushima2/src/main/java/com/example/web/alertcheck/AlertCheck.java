package com.example.web.alertcheck;

import java.util.List;

import com.example.domain.ProductInfo;

public class AlertCheck {

	/**
	 * 商品の価格が、更新されているかを確認する
	 * @param purchasePrice 購入時の価格
	 * @param productPrice  現在の商品の価格
	 * @param productName   商品の名前
	 * @return 警告メッセージ メッセージが入っていない場合はnullが返る
	 */
	public String priceComparisonCheck(Integer purchasePrice, Integer latestPrice, String productName) {
		String priceAlertMsg = null;

		if (!purchasePrice.equals(latestPrice)) {
			priceAlertMsg = "「 " + productName + " 」の価格が更新されています。";
		}

		return priceAlertMsg;
	}

	/**
	 * カートの中に商品が入っているか確認する
	 * @param cartList カートリスト
	 * @return 警告メッセージ メッセージが入っていない場合はnullが返る
	 */
	public String cartCheck(List<ProductInfo> cartList) {
		 String alertMsg = null;

		 if (cartList.size() != 0) {
			//警告メッセージを設定
			alertMsg = "!カート内に商品が入っております。";

		 }

		 return alertMsg;

	}

}
