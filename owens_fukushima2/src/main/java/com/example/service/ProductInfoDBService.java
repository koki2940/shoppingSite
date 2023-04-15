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
	 * @return 商品情報のリスト  データが取得できなかった場合 nullが返る
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
	 * @return 商品情報 データが取得できなかった場合 nullが返る
	 */
	public ProductInfo getIdMatchProductData(Integer productId) {

		ProductInfo info = productMapper.selectIdMatchProductInfo(productId);

		return info;
	}

	/**
	 * 商品IDに紐ずく在庫数を取得する
	 *
	 * @param productId 商品ID
	 * @return 在庫数 情報が存在しなかった場合、0が返る
	 */
	public Integer getIdMatchQuantity(Integer productId) {

		Integer quantity = productMapper.selectIdMatchQuantity(productId);

		return  quantity;
	}

	/**
	 * 在庫数を減算する
	 *
	 * @param form 購入する商品の情報
	 * @return 更新件数 1の場合 商品が存在する 0の場合 商品が存在しない
	 */
	@Transactional
	public void upDateSubstractQuantity(ProductInfo productInfo) {

		 productMapper.upDateSubstractQuantity(productInfo);

	}

	/**
	 * データベース上の同じ商品名のデータの件数を取得する
	 *
	 * @param form 商品登録の登録情報
	 * @return 商品名に該当する件数  情報が存在しなかった場合、0が返る
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
	 * @return ジャンル名 データが取得できなかった場合 nullが返る
	 */
	public String selectGenreName(ProductRegisterForm form) {
		String genreName = productMapper.selectGenreNumberMatchGenreName(form);

		return genreName;
	}

	/**
	 * DBに登録してある全商品情報を取得する
	 * @return 商品情報リスト データが取得できなかった場合 nullが返る
	 */
	public List<ProductInfo> getProductListAll(){
		List<ProductInfo> productList  = productMapper.selectProductInfoAll();

		return productList;
	}

	/**
	 * 修正した商品情報をデータベースに更新する
	 * @param form 修正対象の商品情報
	 * @return 更新件数 正常に更新が行えなかった場合、0が返る
	 */
	@Transactional
	public int correctProductInfo(ProductCorrectForm form, Integer versionNumber) {
		int updateCnt = productMapper.upDateProductInfo(form, versionNumber);

		return updateCnt;
	}

	/**
	 * 削除対象の商品情報を削除する(論理削除)
	 * @param productId 商品Id
	 * @param versionNumber バージョン番号
	 * @return 更新件数  情報が存在しなかった場合、0が返る
	 */
	@Transactional
	public int deleteProductInfo(Integer productId, Integer versionNumber) {
		int updateCnt = productMapper.upDateDeleteFlag(productId, versionNumber);

		return updateCnt;
	}


	/**
	 * 購入対象の商品情報が管理者によってデータ更新がされているかを判定する
	 * @param productInfo 購入対象の商品情報
	 * @return 購入する商品とDB上の同じ情報を持つ情報の件数 情報が存在しなかった場合、0が返る
	 */
	@Transactional
	public int purchaseProductUpDateCheck(ProductInfo productInfo) {
		int numberOfInfo = productMapper.selectPurchaseProductCount(productInfo);

		return numberOfInfo;
	}

	/**
	 * DBから商品IDに該当する最新の「価格」情報を取得する
	 * @param productId 商品ID
	 * @return 商品IDに該当する価格 情報が存在しなかった場合、nullが返る
	 */
	public Integer getLatestPrice(Integer productId) {
		Integer latestPrice = null;

		latestPrice = productMapper.selectLatestPrice(productId);

		return latestPrice;
	}

	/**
	 * カートにいれる商品の最新の情報をDBから取得する
	 * @param productId 商品ID
	 * @return 商品IDに該当する商品情報
	 */
	public ProductInfo getIdMatchInCartInfo(Integer productId) {
		ProductInfo productInfo = productMapper.selectInCartInfo(productId);

		return productInfo;
	}


}
