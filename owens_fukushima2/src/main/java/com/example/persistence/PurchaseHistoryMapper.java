package com.example.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.domain.ProductInfo;
import com.example.web.form.ProductPurchaseForm;

public interface PurchaseHistoryMapper {

	/**
	 * 購入したユーザーのIDと商品情報をテーブルに挿入するメソッド
	 * @param form 購入する商品情報
	 * @param userId ログイン中のユーザーId
	 */
	public void insertPurchaseProductInfo(@Param("p") ProductPurchaseForm form, @Param("userId") Integer userId);

	/**
	 * 購入したユーザーのIDとカート内の商品情報をテーブルに挿入するメソッド
	 * @param info カート内の商品情報
	 * @param userId ログイン中のユーザーId
	 */
	public void insertCartPurchaseProductInfo(@Param("p") ProductInfo info, @Param("userId") Integer userId);

	/**
	 * ログインしているユーザーの購入履歴情報を取得する
	 * @param userId ログイン中のユーザーId
	 * @return 購入履歴の商品情報 情報が取得できなかった場合、nullが返る
	 */
	public List<ProductInfo> selectPurchaseHistory(Integer userId);

	/**
	 * 削除対象の履歴情報を取得するメソッド
	 * @param purchaseId 削除する購入履歴情報のId
	 * @return 削除対象の履歴情報 情報が取得できなかった場合、nullが返る
	 */
	public ProductInfo selectIdMatchPurchaseHistory(String purchaseId);

	/**
	 * 購入履歴の削除を行うメソッド
	 * @param purchaseId 削除する購入履歴情報のId
	 */
	public void deletePurchaseHistory(String purchaseId);
}
