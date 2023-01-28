package com.example.web.error;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.validation.BindingResult;

import com.example.domain.User;
import com.example.service.ProductInfoDBService;
import com.example.service.UserDBService;
import com.example.web.form.ProductPurchaseForm;
import com.example.web.form.ProductRegisterForm;
import com.example.web.form.ProductSearchForm;
import com.example.web.form.UserLoginForm;
import com.example.web.form.UserRegisterForm;

public class ErrorCheck {

	/**
	 * ログイン時の入力チェック、アカウントの重複チェックを行うメソッド
	 * @param form 入力情報
	 * @param userService  ユーザテーブルに接続するサービスクラス
	 * @param result エラーメッセージ設定
	 * @return ユーザーID,権限
	 */
	public List<User> loginErrorCheck(UserLoginForm form, UserDBService userService, BindingResult result) {

		List<User> userList = new ArrayList<User>();

		//ユーザ名とパスワードの入力チェック
		if (form.getUserName().isEmpty() || form.getPassword().isEmpty()) {
			result.reject("error.invalid.roginError");

		} //ユーザ名とパスワードが存在する場合
		else {
			//アカウントが存在するかをチェック
			userList = userService.accountJudge(form);

			//アカウントがが存在していなかった場合
			if (userList.size() == 0) {
				//エラーメッセージ設定
				result.reject("error.invalid.notAccount");
			}
		}

		//ユーザー情報を返す
		return userList;

	}

	/**
	 * ユーザ登録のエラーチェックを行うメソッド
	 * @param form  入力情報
	 * @param service ユーザーテーブルにアクセスするためのサービスクラス
	 * @param result エラーメッセージ設定
	 */
	public void userRegisterErrorCheck(UserRegisterForm form, UserDBService userService, BindingResult result) {

		//ユーザIDが存在していたら重複しているかをチェック
		if (!form.getUserName().isEmpty()) {
			if (1 <= userService.userNameCheck(form)) {
				//カウントが1件以上存在していればエラーメッセージを設定
				result.reject("errors.invalid.userName");
			}
		}

		//パスワードとパスワード確認用どちらか入力されていたら一致しているかを判定する
		if (!form.getPassword().isEmpty() || !form.getPasswordConf().isEmpty()) {
			if (!form.getPassword().equals(form.getPasswordConf())) {
				// エラーメッセージの設定
				result.reject("errors.invalid.password");
			}
		}
		
		//郵便番号の入力形式チェック
		if (!form.getPostNumber().isEmpty()) {
			boolean res = true;
			
	        Pattern pattern = Pattern.compile("^[0-9]+$|-[0-9]+$");
	        res = pattern.matcher(form.getPostNumber()).matches();
	        
				if(form.getPostNumber().length() < 7 || !res) {
					result.reject("error.invaild.postNumber");
				}
		    }
		

	}

	/**
	 * 「価格」の入力チェックを行う
	 * @param form 入力情報
	 * @param result エラーメッセージ設定
	 */
	public void priceInputCheck(ProductSearchForm form, BindingResult result) {

		//どちらか片方が入力されていたら
		if (form.getPriceMin() == null && form.getPriceMax() != null
				|| form.getPriceMin() != null && form.getPriceMax() == null) {
			if (!result.hasErrors()) {
				// エラーメッセージの設定
				result.reject("errors.invalid.price");
			}

		}

		//最小価格・最大価格がどちらも入力されていた場合
		if (form.getPriceMin() != null && form.getPriceMax() != null) {
			//最大価格が最小価格より小さい場合
			if (form.getPriceMax() < form.getPriceMin()) {
				//エラーメッセージ設定
				result.reject("errors.invalid.bigPriceMin");
			}
			//負の値が入力されていたら
			if (form.getPriceMin() < 0 || form.getPriceMax() < 0) {
				result.reject("errors.invalid.negativePrice");

			}

		} else if (form.getPriceMin() != null) {
			if (form.getPriceMin() < 0) {
				result.reject("errors.invalid.negativePrice");

			}
		} else if (form.getPriceMax() != null) {
			if (form.getPriceMax() < 0) {
				result.reject("errors.invalid.negativePrice");
			}
		}

	}

	/**
	 * 商品の購入数が選択されているかを判定
	 * @param form 購入数 入力値
	 * @param result エラーメッセージ設定
	 */
	public void purchaseQtySelectCheck(ProductPurchaseForm form, BindingResult result) {

		//購入数量が未選択だったら
		if (form.getPurchaseQty() == null) {
			//エラーメッセージ設定
			result.reject("errors.invalid.notQty");
		}

	}

	/**
	 * 商品登録時、同じ商品名がDBに登録されていないかを判定
	 * @param form 登録情報
	 * @param productService productテーブルにつなぐサービスクラス
	 * @param result エラーメッセージ設定
	 */
	public void selectProductName(ProductRegisterForm form, ProductInfoDBService productService, BindingResult result) {

		//データベースから同盟の商品名があるかを確認
		Integer count = productService.selectNameMatchProductInfo(form);

		//もし件数が1件でもある場合
		if (1 <= count) {
			//エラーメッセージを設定
			result.reject("error.invalid.overlapName");
		}

	}

	/**
	 * チャージ時、金額が選択されているか判定を行う
	 * @param chargeAmount チャージ金額の値
	 * @param errorMessages エラーメッセージを格納するリスト
	 */
	public void chargeAmountSelectCheck(String chargeAmount, List<String> errorList) {
		if (chargeAmount == null) {
			//エラーメッセージ設定
			errorList.add("金額が選択されていません");
		}
	}

	/**
	 * チャージ金額が上限に達しているか判定を行う
	 * @param chargeAmount チャージ金額
	 * @param giftAmount ギフト券の金額
	 * @param errorList エラーメッセージを格納するリスト
	 */
	public void chargeAmountUpperLimit(String chargeAmount, Integer giftAmount, List<String> errorList) {
		if (chargeAmount != null) {
			if (Integer.valueOf(chargeAmount) + giftAmount > 500000) {
				errorList.add("これ以上チャージできません");
			}
		}

	}

	/**
	 * 商品の合計金額とギフト券の金額の比較を行う
	 * @param totalAmount
	 * @param giftAmount
	 * @param errorList エラーメッセージを格納するリスト
	 */
	public void compareAmounts(Integer totalAmount, Integer giftAmount, List<String> errorList) {
		if (giftAmount < totalAmount ) {
			errorList.add("金額が不足しています");
		}
	}

	/**
	 * 購入数が在庫数より超過していないか
	 * @param quantity 在庫数
	 * @param purchaseQty 購入数
	 * @param productName 商品名
	 * @param errorList エラーメッセージを格納するリスト
	 */
	public void purcharseQtyOver(Integer quantity, Integer purchaseQty, String productName, List<String> errorList) {
		if(quantity < purchaseQty) {
			//Integer numberOvers = purchaseQty - quantity;

			errorList.add("※" +productName + "の購入数が超過しています。上限は" + quantity + "までです。");
		}

	}

}
