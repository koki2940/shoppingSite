package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.ProductInfo;
import com.example.persistence.PurchaseHistoryMapper;
import com.example.web.form.ProductPurchaseForm;

@Service
public class PurchaseHistoryDBService {


	@Autowired
	PurchaseHistoryMapper purchaseMapper;

	/**
	 * 購入履歴テーブルに商品情報とユーザーIdを登録する
	 *
	 * @param form 購入した商品情報
	 * @param userId ユーザId
	 */
	@Transactional
	public void insertPurchaseHistory(ProductPurchaseForm form, Integer userId) {
		purchaseMapper.insertPurchaseProductInfo(form, userId);
	}

	/**
	 * 購入履歴テーブルにカートに入れた商品情報とユーザー情報を登録する
	 *
	 * @param form 購入した商品情報
	 * @param userId ユーザーId
	 */
	@Transactional
	public void insertCartPurchaseHistory(ProductInfo info, Integer userId) {
		purchaseMapper.insertCartPurchaseProductInfo(info, userId);
	}

	/**
	 * ユーザの購入した商品情報を取得する
	 *
	 * @param userId ユーザーId
	 * @return 購入した商品のリスト データが取得できなかった場合 nullが返る
	 */
	public List<ProductInfo> selectPurchaseHistory(Integer userId){
		List<ProductInfo> list = purchaseMapper.selectPurchaseHistory(userId);

		return list;

	}

	/**
	 * 削除する履歴情報を取得する
	 *
	 * @param purchaseId 購入Id
	 * @return 購入した商品の情報 データが取得できなかった場合 nullが返る
	 */
	public ProductInfo getIdMatchPurchaseHistory(String purchaseId) {
		ProductInfo info = purchaseMapper.selectIdMatchPurchaseHistory(purchaseId);

		return info;
	}
	/**
	 * 購入Idに該当する履歴情報を削除する
	 *
	 * @param purchaseId 購入Id
	 */
	@Transactional
	public void deletePurchaseHistory(String purchaseId) {
		purchaseMapper.deletePurchaseHistory(purchaseId);
	}
}
