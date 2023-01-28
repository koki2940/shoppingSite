package com.example.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.domain.ProductInfo;
import com.example.web.form.ProductCorrectForm;
import com.example.web.form.ProductRegisterForm;
import com.example.web.form.ProductSearchForm;

public interface ProductInfoMapper {

	//検索情報から商品情報を取得するメソッド
	public List<ProductInfo> selectProductInfo(ProductSearchForm form);

	//商品IDに該当する商品情報を取得するメソッド
	public ProductInfo selectIdMatchProductInfo(String productId);

	//商品IDに該当する在庫数を取得するメソッド
	public Integer selectIdMatchQuantity(Integer productId);

	//購入数から在庫数を減算するメソッド
	public void updateSubstractQuantity(Integer productId, Integer purchaseQty);

	//購入数から在庫数を加算するメソッド
	public void updateAddQuantity(@Param("purchaseQty") Integer purchaseQty, @Param("productId") String productId);

	//同じ商品名の数を取得するメソッド
	public Integer selectNameMatchCount(ProductRegisterForm form);

	//登録画面で入力した情報を商品情報テーブルに登録するメソッド
	public void insertProductInfo(ProductRegisterForm form);

	//ジャンル名を取得するメソッド
	public String selectGenreName(ProductRegisterForm form);

	//全商品情報を取得するメソッド
	public List<ProductInfo>selectProductInfoAll();

	//修正内容をproduct_infoテーブルに更新するメソッド
	public void updateProductInfo(ProductCorrectForm form);


}
