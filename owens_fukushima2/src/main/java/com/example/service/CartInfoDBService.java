package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.ProductInfo;
import com.example.persistence.CartInfoMapper;

@Service
public class CartInfoDBService {

	@Autowired
	 CartInfoMapper  cartMapper;

	/**
	 * カートにいれる商品情報をcart_infoテーブルへ新規登録する
	 * @param userId ユーザーID
	 * @param cartInfo 商品情報
	 */
	@Transactional
	public void cartInfoRegister(Integer userId, ProductInfo productInfo) {

		cartMapper.insertCartInfo(userId, productInfo);
	}

	/**
	 * 同じ商品をカートにいれる場合は価格・購入数を更新する
	 * @param price 価格
	 * @param purchaseQty 購入数
	 * @param productId 商品ID
	 */
	@Transactional
	public void cartInfoUpdate(Integer price, Integer purchaseQty, Integer productId, Integer userId) {
		cartMapper.upDateCartInfo(price, purchaseQty, productId, userId);
	}

	/**
	 * ログインしたユーザーに該当するカート情報をDBから取得
	 * @param userId ユーザーID
	 * @return カートに保持している商品情報のリスト  該当する情報がない場合nullが返る
	 */
	public List<ProductInfo> getCartInfo(Integer userId) {
		List<ProductInfo> cartList = cartMapper.selectCartInfo(userId);

		return cartList;
	}

	/**
	 * カートIdに該当するカート情報をDBから削除する
	 * @param cartId カートId
	 */
	public void deleteIdMatchCartInfo(Integer cartId) {
		cartMapper.deleteCartInfo(cartId);
	}

	/**
	 * ログイン中のユーザーが保持しているカート情報をDBから削除する
	 * @param userId ユーザーID
	 */
	@Transactional
	public void allDeleteCartInfo(Integer userId) {
		cartMapper.deleteAllCartInfo(userId);
	}


	/**
	 * カートに新規でいれた情報をDBから取得する
	 * @param productId 商品ID
	 * @param userId ユーザーID
	 * @return カートに新規でいれた情報
	 */
	public ProductInfo getIdMatchCartInfo(Integer productId, Integer userId) {
		ProductInfo productInfo = cartMapper.selectIdMatchCartInfo(productId, userId);

		return productInfo;
	}

}
