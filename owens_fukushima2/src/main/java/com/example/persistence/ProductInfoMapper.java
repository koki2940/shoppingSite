package com.example.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.domain.ProductInfo;
import com.example.web.form.ProductCorrectForm;
import com.example.web.form.ProductRegisterForm;
import com.example.web.form.ProductSearchForm;

public interface ProductInfoMapper {

	/**
	 * 検索情報から商品情報を取得するメソッド
	 * @param form 商品の検索情報
	 * @return 商品情報リスト  検索条件に会わなかった場合、 nullが返る
	 */
	public List<ProductInfo> selectProductInfo(ProductSearchForm form);

	/**
	 * 商品IDに該当する商品情報を取得するメソッド
	 * @param productId 商品ID
	 * @return IDに該当する商品情報 情報が取得できなかった場合、nullが返る
	 */
	public ProductInfo selectIdMatchProductInfo(Integer productId);

	/**
	 * 商品IDに該当する在庫数を取得するメソッド
	 * @param productId 商品ID
	 * @return IDに該当する商品の在庫数 情報が取得できなかった場合、nullが返る
	 */
	public Integer selectIdMatchQuantity(Integer productId);

	/**
	 * 購入数から在庫数を減算するメソッド
	 * @param productInfo 購入する商品情報
	 */
	public void upDateSubstractQuantity(ProductInfo productInfo);

	/**
	 * 同じ商品名の数を取得するメソッド
	 * @param form 登録時の商品情報
	 * @return 登録時の商品名に該当するDB内の商品情報の件数 情報が存在しなかった場合、0が返る
	 */
	public Integer selectNameMatchCount(ProductRegisterForm form);

	/**
	 * 登録画面で入力した情報を商品情報テーブルに登録するメソッド
	 * @param form 登録時の商品情報
	 */
	public void insertProductInfo(ProductRegisterForm form);

	/**
	 * ジャンル名を取得するメソッド
	 * @param form 登録時の商品情報
	 * @return  ジャンル名 情報が取得できなかった場合、nullが返る
	 */
	public String selectGenreNumberMatchGenreName(ProductRegisterForm form);

	/**
	 * 全商品情報を取得するメソッド
	 * @return DB内の全商品情報 情報が取得できなかった場合、nullが返る
	 */
	public List<ProductInfo>selectProductInfoAll();

	/**
	 * 修正内容をproduct_infoテーブルに更新するメソッド
	 * @param form 修正対象の商品情報
	 * @param vesionNumber バージョン番号
	 * @return 更新件数 情報が存在しなかった場合、0が返る
	 */
	public int upDateProductInfo(@Param("correct")ProductCorrectForm form, @Param("versionNumber") Integer vesionNumber);

	/**
	 * 削除対象の商品のdelete_flagを更新するメソッド
	 * @param productId 商品Id
	 * @param versionNumber バージョン番号
	 * @return 更新件数 情報が存在しなかった場合、0が返る
	 */
	public int upDateDeleteFlag(Integer productId, Integer versionNumber);

	/**
	 * 購入対象の商品が更新されずにDB上に存在しているか
	 * @param productInfo 購入する商品情報
	 * @return 1の場合、 商品情報が更新されずに存在している。 0の場合、 商品情報が更新されている。
	 */
	public int selectPurchaseProductCount(ProductInfo productInfo);


	/**
	 * DB上の商品IDに該当する最新の「価格」を取得する
	 * @param productId 商品ID
	 * @return 価格 情報が存在しなかった場合、nullが返る
	 */
	public Integer selectLatestPrice(Integer productId);

	/**
	 * product_infoテーブルからcart_infoテーブルにいれる情報をDBから取得する
	 * @param productId 商品ID
	 * @return 商品IDに該当する商品情報
	 */
	public ProductInfo selectInCartInfo(Integer productId);


}
