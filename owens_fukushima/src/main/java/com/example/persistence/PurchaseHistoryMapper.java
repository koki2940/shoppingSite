package com.example.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.domain.ProductInfo;
import com.example.web.form.ProductPurchaseForm;

public interface PurchaseHistoryMapper {

	//購入したユーザーのIDと商品情報をテーブルに挿入するメソッド
	public void insertHistory(@Param("p") ProductPurchaseForm form, @Param("userId") Integer userId);

	//購入したユーザーのIDと商品情報をテーブルに挿入するメソッド
	public void insertCartHistory(@Param("p") ProductInfo info, @Param("userId") Integer userId);

	//ログインしているユーザーの購入履歴情報を取得する
	public List<ProductInfo> selectPurchaseHistory(Integer userId);

	//削除対象の履歴情報を取得するメソッド
	public ProductInfo selectIdMatchHistory(String purchaseId);

	//購入履歴の削除を行うメソッド
	public void deletePurchaseHistory(String purchaseId);
}
