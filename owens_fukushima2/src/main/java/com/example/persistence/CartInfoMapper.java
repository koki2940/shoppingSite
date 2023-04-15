package com.example.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.domain.ProductInfo;

public interface CartInfoMapper {


	/**
	 * カートに新規で商品を入れる時、cart_infoテーブルに情報を登録する
	 * @param userId ユーザーID
	 * @param productInfo 商品情報
	 */
	public void insertCartInfo(@Param("userId")Integer userId, @Param("product") ProductInfo productInfo);

	/**
	 * カート内に入っている同じ商品を入れる時、価格と購入数を更新する
	 * @param price 価格
	 * @param purchaseQty 購入数
	 * @param productId 商品ID
	 * @param userId ユーザーID
	 */
	public void upDateCartInfo(Integer price, Integer purchaseQty, Integer productId, Integer userId);

	/**
	 * ログインしているユーザーがカートにいれた情報をcart_infoテーブルから取得する
	 * @param userId ユーザーID
	 * @return カートに保持している商品情報のリスト
	 */
	public List<ProductInfo> selectCartInfo(Integer userId);

	/**
	 * [削除]ボタンを押下した該当の情報をcart_infoテーブルから削除する
	 * @param cartId カートID
	 */
	public void deleteCartInfo(Integer cartId);

	/**
	 * ログインしているユーザーが保持している全てのカート情報をcart_infoテーブルから削除する
	 * @param userId ユーザーID
	 */
	public void deleteAllCartInfo(Integer userId);

	/**
	 * cart_infoテーブルに登録した情報を取得する
	 * @param productId 商品ID
	 * @param userId ユーザーID
	 * @return 商品情報
	 */
	public ProductInfo selectIdMatchCartInfo(Integer productId, Integer userId);






}
