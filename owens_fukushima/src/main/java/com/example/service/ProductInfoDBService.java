package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.ProductInfo;
import com.example.persistence.ProductInfoMapper;
import com.example.web.form.ProductCorrectForm;
import com.example.web.form.ProductRegisterForm;
import com.example.web.form.ProductSearchForm;

@Service
public class ProductInfoDBService {

	@Autowired
	ProductInfoMapper productMapper;

	/**
	 * 検索情報から商品情報を取得する
	 *
	 * @param form 商品検索の検索条件
	 * @return 商品情報のリスト
	 */
	public List<ProductInfo> getProductList(ProductSearchForm form){

		List<ProductInfo> list = productMapper.selectProductInfo(form);

		return list;
	}

	/**
	 * 商品IDに紐づく商品情報を取得する
	 *
	 * @param productId 商品ID
	 *
	 * @return 商品情報
	 */
	public ProductInfo getIdMatchProductData(String productId) {

		ProductInfo info = productMapper.selectIdMatchProductInfo(productId);

		return info;
	}

	/**
	 * 商品IDに紐ずく在庫数を取得する
	 *
	 * @param productId 商品ID
	 * @return 在庫数
	 */
	public Integer getIdMatchQuantity(Integer productId) {

		Integer quantity = productMapper.selectIdMatchQuantity(productId);

		return  quantity;
	}

	/**
	 * 在庫数を減算する
	 *
	 * @param form 購入する商品の情報
	 */
	@Transactional
	public void upDateSubstractQuantity(Integer productId, Integer purchaseQty) {

		productMapper.updateSubstractQuantity(productId, purchaseQty);
	}

//	/**
//	 * 在庫数を加算する
//	 *
//	 * @param form 削除する商品の購入数とId
//	 */
//	@Transactional
//	public void upDateAddQuantity(Integer purchaseQty, String productId) {
//
//		productMapper.updateAddQuantity(purchaseQty, productId);
//	}


	/**
	 * データベース上の同じ商品名のデータの件数を取得する
	 *
	 * @param form 商品登録の登録情報
	 * @return 商品名に該当する件数
	 */
	public Integer selectNameMatchProductInfo(ProductRegisterForm form) {

		Integer count = productMapper.selectNameMatchCount(form);

		return count;
	}

	/**
	 * 登録情報から商品テーブルに商品情報を登録する
	 *
	 * @param form 登録情報
	 */
	@Transactional
	public void insertProductInfo(ProductRegisterForm form) {
		productMapper.insertProductInfo(form);
	}

	/**
	 * 登録確認で表示するジャンル名を取得する
	 *
	 * @param form 登録情報
	 * @return ジャンル名
	 */
	public String selectGenreName(ProductRegisterForm form) {
		String genreName = productMapper.selectGenreName(form);

		return genreName;
	}

	/**
	 * DBに登録してある全商品情報を取得する
	 * @return 商品情報リスト
	 */
	public List<ProductInfo> getProductListAll(){
		List<ProductInfo> productList  = productMapper.selectProductInfoAll();

		return productList;
	}

	/**
	 * 修正した商品情報をデータベースに更新する
	 * @param form
	 */
	@Transactional
	public void correctProductInfo(ProductCorrectForm form) {
		productMapper.updateProductInfo(form);

	}



}
