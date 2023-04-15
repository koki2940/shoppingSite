package com.example.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.domain.ProductInfo;
import com.example.service.CartInfoDBService;
import com.example.service.ProductInfoDBService;
import com.example.service.PurchaseHistoryDBService;
import com.example.service.UserDBService;
import com.example.web.errorcheck.ErrorCheck;
import com.example.web.form.ProductPurchaseForm;
import com.example.web.form.ProductSearchForm;

@Controller
@SessionAttributes("productPurchaseForm")
public class GeneralProductController {

	@Autowired
	private ProductInfoDBService productService;

	@Autowired
	private PurchaseHistoryDBService purchaseService;

	@Autowired
	private UserDBService userService;

	@Autowired
	private CartInfoDBService cartService;

	@Autowired
	HttpSession session;

	@ModelAttribute("productSearchForm")
	public ProductSearchForm setSearchForm() {
		return new ProductSearchForm();
	}

	@ModelAttribute("productPurchaseForm")
	public ProductPurchaseForm setPurchaseForm() {
		return new ProductPurchaseForm();
	}

	// メニュー画面から商品検索画面
	@RequestMapping(value = "/select-btn", params = "productSearch")
	public String productSearch(Model model) {
		String userName = this.getUserNameSession();
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		model.addAttribute("userName", userName);
		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("cartList", cartList);

		// 商品検索画面へ
		return "generalProduct/productSearch";
	}

	// 商品検索画面から商品一覧画面
	@RequestMapping(value = "/product-search", params = "search")
	public String productList(@Validated @ModelAttribute("productSearchForm") ProductSearchForm form,
			BindingResult result, Model model) {
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("cartList", cartList);

		ErrorCheck error = new ErrorCheck();

		// 商品検索時の「価格」の入力チェックを行う
		error.priceInputCheck(form, result);

		if (result.hasErrors()) {
			// 講座検索画面へ
			return "generalProduct/productSearch";

		}

		session.setAttribute("productSearchForm", form);

		// 検索条件に該当する商品情報をDBから取得
		List<ProductInfo> productList = productService.getProductList(form);

		session.setAttribute("productList", productList);

		model.addAttribute("productList", productList);

		// 商品一覧画面へ
		return "generalProduct/productList";
	}

	// 商品検索画面からメニュー画面
	@RequestMapping(value = "/product-search", params = "back")
	public String backMenu(Model model) {
		session.removeAttribute("courseSearchForm");

		String userName = this.getUserNameSession();
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		model.addAttribute("userName", userName);
		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("cartList", cartList);

		// メニュー画面へ
		return "user/generalUserMenu";
	}

	// 商品一覧画面から商品検索画面
	@RequestMapping(value = "/back-product-list", params = "backProductSearch")
	public String backProductSearch(Model model) {
		ProductSearchForm form = (ProductSearchForm) session.getAttribute("productSearchForm");

		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		model.addAttribute("productSearchForm", form);
		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("cartList", cartList);

		session.removeAttribute("productList");

		// 商品検索画面へ
		return "generalProduct/productSearch";
	}

	// 商品一覧画面から商品詳細画面
	@RequestMapping(value = "/product-detail", params = "detail")
	public String productDetail(@ModelAttribute("productPurchaseForm") ProductPurchaseForm form,
			@RequestParam("productId") Integer productId, Model model) {
		session.setAttribute("productId", productId);

		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		List<ProductInfo> productList = (List<ProductInfo>) session.getAttribute("productList");

		for (ProductInfo productInfo : productList) {
			if (productInfo.getProductId() == productId) {
				form.setProductName(productInfo.getProductName());
				form.setPrice(productInfo.getPrice());
				form.setGenreName(productInfo.getGenreName());
				form.setQuantity(productInfo.getQuantity());

				break;
			}
		}

		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("cartList", cartList);

		// 商品詳細画面へ
		return "generalProduct/productDetail";

	}

	// 商品詳細画面から商品一覧画面
	@RequestMapping(value = "/product-purchase", params = "backProductList")
	public String backproductList(Model model, SessionStatus sessionStatus) {
		ProductSearchForm form = (ProductSearchForm) session.getAttribute("productSearchForm");
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		List<ProductInfo> productList = productService.getProductList(form);

		session.setAttribute("productList", productList);

		model.addAttribute("productList", productList);
		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("cartList", cartList);

		sessionStatus.setComplete();

		// 商品一覧画面へ
		return "generalProduct/productList";

	}

	// 商品詳細から購入確認画面
	@RequestMapping(value = "/product-purchase", params = "purchase")
	public String purchaseCheck(@Validated @ModelAttribute("productPurchaseForm") ProductPurchaseForm form,
			BindingResult result, Model model) {
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("cartList", cartList);

		ErrorCheck error = new ErrorCheck();
		// 「購入数」が選択されているか判定する
		error.purchaseQtySelectCheck(form, result);

		if (result.hasErrors()) {
			// 詳細画面へ
			return "generalProduct/productDetail";
		}

		form.setTotalAmount(form.getPrice() * form.getPurchaseQty());

		// 購入確認画面へ
		return "generalProduct/productPurchaseCheck";
	}

	// 購入確認画面から商品詳細画面
	@RequestMapping(value = "/product-purchase-finish", params = "backDetail")
	public String backProductDetail(@ModelAttribute("productPurchaseForm") ProductPurchaseForm form, Model model) {
		Integer productId = (Integer) session.getAttribute("productId");
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		List<ProductInfo> productList = (List<ProductInfo>) session.getAttribute("productList");

		for (ProductInfo productInfo : productList) {
			if (productInfo.getProductId() == productId) {
				form.setProductName(productInfo.getProductName());
				form.setPrice(productInfo.getPrice());
				form.setGenreName(productInfo.getGenreName());
				form.setQuantity(productInfo.getQuantity());

				break;
			}
		}

		model.addAttribute("productId", productId);
		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("cartList", cartList);

		// 商品詳細画面へ
		return "generalProduct/productDetail";

	}

	// 購入確認画面から購入完了画面
	@RequestMapping(value = "/product-purchase-finish", params = "purchase")
	public String productPurchaseFinish(@ModelAttribute("productPurchaseForm") ProductPurchaseForm form,
			SessionStatus sessionStatus, Model model) {
		Integer userId = this.getUserIdSession();
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		model.addAttribute("cartList", cartList);

		List<String> errorList = new ArrayList<String>();

		ErrorCheck error = new ErrorCheck();
		// ギフト券の金額が不足していないか確認
		error.compareAmounts(form.getTotalAmount(), giftAmount, errorList);

		Integer quantity = productService.getIdMatchQuantity(form.getProductId());

		error.purcharseQtyOver(quantity, form.getPurchaseQty(), form.getProductName(), errorList);

		ProductInfo productInfo = new ProductInfo();

		BeanUtils.copyProperties(form, productInfo);

		// 購入対象の商品情報が管理者によって更新・削除されていないかを判定
		int numberOfInfo = productService.purchaseProductUpDateCheck(productInfo);

		if (numberOfInfo == 0) {
			errorList.add("※「 " + form.getProductName() + "」の情報が管理者によって修正・削除された可能性があります。一覧から再度商品をお買い求めください。");
		}

		if (errorList.size() != 0) {
			model.addAttribute("giftAmount", giftAmount);
			model.addAttribute("errorList", errorList);

			// 商品購入確認画面へ
			return "generalProduct/productPurchaseCheck";
		}

		// 購入対象の商品を購入履歴テーブルに挿入する処理
		purchaseService.insertPurchaseHistory(form, userId);

		// 商品情報から購入数量の数だけ在庫数を減らす処理
		productService.upDateSubstractQuantity(productInfo);

		// 商品の金額だけギフト券の金額を減算する
		userService.upDateSubstractGiftAmount(form.getTotalAmount(), userId);

		// ログイン中のユーザーIDに該当するcart_infoテーブルの情報を削除する
		cartService.allDeleteCartInfo(userId);

		// 更新したギフト券の金額を取得する
		giftAmount = userService.selectGiftAmount(userId);

		session.setAttribute("giftAmount", giftAmount);

		model.addAttribute("giftAmount", giftAmount);

		sessionStatus.setComplete();

		// 商品購入完了画面へ
		return "generalProduct/productPurchaseFinish";

	}

	// 購入完了画面から商品検索画面
	@RequestMapping(value = "/select-btn", params = "selectProductSearch")
	public String returnProductSearch(Model model) {
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("cartList", cartList);

		session.removeAttribute("productSearchForm");

		// 商品検索画面へ
		return "generalProduct/productSearch";
	}

	// 購入完了画面から購入履歴一覧画面
	@RequestMapping(value = "/select-btn", params = "selectPurchaseHistoryList")
	public String returnPurchaseHistoryList(Model model) {
		Integer userId = this.getUserIdSession();
		Integer giftAmount = this.getGiftAmountSession();
		String userName = this.getUserNameSession();
		List<ProductInfo> cartList = this.getCartListSession();

		// ログインしたユーザのIDから購入履歴を取得
		List<ProductInfo> list = purchaseService.selectPurchaseHistory(userId);

		// 取得した購入日の情報を表示用に整える
		for (ProductInfo date : list) {
			date.setPurchaseDateTime(date.getDateTime());
		}

		model.addAttribute("purchaseHistoryList", list);
		model.addAttribute("userName", userName);
		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("cartList", cartList);

		// 購入履歴一覧画面へ
		return "history/purchaseHistoryList";

	}

	// 購入完了画面からメニュー画面
	@RequestMapping(value = "/select-btn", params = "selectMenu")
	public String returnMenu(Model model) {
		String userName = this.getUserNameSession();
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		session.removeAttribute("productList");

		model.addAttribute("userName", userName);
		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("cartList", cartList);

		// メニュー画面へ
		return "user/generalUserMenu";
	}

	// 商品詳細からカート確認画面
	@RequestMapping(value = "/product-purchase", params = "cart")
	public String inputShoppingCart(@Validated @ModelAttribute("productPurchaseForm") ProductPurchaseForm form,
			BindingResult result, @RequestParam("productId") Integer productId, Model model,
			SessionStatus sessionStatus) {
		Integer giftAmount = this.getGiftAmountSession();
		Integer userId = this.getUserIdSession();
		List<ProductInfo> cartList = this.getCartListSession();

		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("cartList", cartList);

		ErrorCheck error = new ErrorCheck();

		// 「購入数」が選択されているか判定する
		error.purchaseQtySelectCheck(form, result);

		if (result.hasErrors()) {
			// 商品詳細画面へ
			return "generalProduct/productDetail";
		}

		sessionStatus.setComplete();

		// 商品重複フラグ
		boolean duplication = false;

		for (ProductInfo productInfo : cartList) {
			// 同じ商品をカートにいれた場合は、数量だけ加算する。
			if (productInfo.getProductId() == form.getProductId()) {
				duplication = true;

				productInfo.setPurchaseQty(productInfo.getPurchaseQty() + form.getPurchaseQty());

				cartService.cartInfoUpdate(form.getPrice(), form.getPurchaseQty(), productId, userId);
			}
		}

		// 同じ商品でなかったら
		if (!duplication) {
			ProductInfo productInfo = new ProductInfo();

			BeanUtils.copyProperties(form, productInfo);

			cartService.cartInfoRegister(userId, productInfo);

			productInfo = cartService.getIdMatchCartInfo(productId, userId);

			cartList.add(productInfo);
		}

		Integer totalAmount = 0;

		// 合計金額の計算
		for (ProductInfo productInfo : cartList) {
			totalAmount += productInfo.getPrice() * productInfo.getPurchaseQty();
		}
		session.setAttribute("cartList", cartList);
		session.setAttribute("totalAmount", totalAmount);

		model.addAttribute("cartList", cartList);
		model.addAttribute("totalAmount", totalAmount);

		// カート確認画面へ
		return "generalProduct/shoppingCart";
	}

	// カート確認画面から「削除」押下時、カート確認画面
	@RequestMapping(value = "/cart-product-delete", params = "delete")
	public String cartProductInfoDelete(@RequestParam("cartId") Integer cartId, Model model) {
		List<ProductInfo> cartList = this.getCartListSession();
		Integer totalAmount = this.getTotalAmountSession();
		Integer giftAmount = this.getGiftAmountSession();

		// リクエストで渡された商品IDに該当する商品情報をカートから削除し、合計金額から減算する
		for (int i = 0; i < cartList.size(); i++) {
			if (cartList.get(i).getCartId() == cartId) {
				totalAmount -= cartList.get(i).getPrice() * cartList.get(i).getPurchaseQty();

				cartList.remove(i);

				cartService.deleteIdMatchCartInfo(cartId);
			}
		}

		session.setAttribute("cartList", cartList);
		session.setAttribute("totalAmount", totalAmount);

		model.addAttribute("totalAmount", totalAmount);
		model.addAttribute("cartList", cartList);
		model.addAttribute("giftAmount", giftAmount);

		// カート確認画面へ
		return "generalProduct/shoppingCart";
	}

	// カート確認画面から一般ユーザーメニュー画面
	@RequestMapping(value = "/cart-product-purchase", params = "backGeneralUserMenu")
	public String backGeneralUserMenu(Model model) {
		Integer giftAmount = this.getGiftAmountSession();
		String userName = this.getUserNameSession();
		List<ProductInfo> cartList = this.getCartListSession();

		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("userName", userName);
		model.addAttribute("cartList", cartList);

		session.removeAttribute("priceAlertMsg");

		// メニュー画面へ
		return "user/generalUserMenu";
	}

	// カート確認画面から購入完了画面
	@RequestMapping(value = "/cart-product-purchase", params = "purchase")
	public String cartProductPurchase(Model model) {
		Integer userId = this.getUserIdSession();
		Integer totalAmount = this.getTotalAmountSession();
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		model.addAttribute("cartList", cartList);

		List<String> errorList = new ArrayList<String>();

		ErrorCheck error = new ErrorCheck();

		// ギフト券の金額が不足していないか確認
		error.compareAmounts(totalAmount, giftAmount, errorList);

		// カート内の商品の購入数が在庫数より超過しているか
		for (ProductInfo productInfo : cartList) {
			Integer quantity = productService.getIdMatchQuantity(productInfo.getProductId());

			error.purcharseQtyOver(quantity, productInfo.getPurchaseQty(), productInfo.getProductName(), errorList);

			// 購入対象の商品情報が新たに更新・削除されていないかを判定
			int numberOfInfo = productService.purchaseProductUpDateCheck(productInfo);

			if (numberOfInfo == 0) {
				errorList.add("※「" + productInfo.getProductName() + "」"
						+ "の情報が管理者によって修正・削除された可能性があります。カートから削除し、一覧から再度商品をお買い求めください。");
			}

		}

		if (errorList.size() != 0) {
			model.addAttribute("errorList", errorList);
			model.addAttribute("totalAmount", totalAmount);
			model.addAttribute("giftAmount", giftAmount);

			// カート確認画面へ
			return "generalProduct/shoppingCart";

		} else {
			for (ProductInfo productInfo : cartList) {
				purchaseService.insertCartPurchaseHistory(productInfo, userId);

				productService.upDateSubstractQuantity(productInfo);
			}
			cartList.removeAll(cartList);
			cartService.allDeleteCartInfo(userId);

			// ギフト券金額からカート内の合計金額分を減算
			userService.upDateSubstractGiftAmount(totalAmount, userId);

			// 更新したギフト券の金額を取得
			giftAmount = userService.selectGiftAmount(userId);

			session.setAttribute("cartList", cartList);
			session.setAttribute("giftAmount", giftAmount);

			model.addAttribute("giftAmount", giftAmount);
		}

		session.removeAttribute("priceAlertMsg");

		// 商品購入確認画面へ
		return "generalProduct/productPurchaseFinish";
	}

	// カート確認画面から「カートを空にする」ボタン押下時、カート確認画面
	@RequestMapping(value = "/cart-product-purchase", params = "deleteCart")
	public String deleteAllCart(Model model) {
		Integer giftAmount = this.getGiftAmountSession();
		Integer userId = this.getUserIdSession();
		List<ProductInfo> cartList = this.getCartListSession();

		cartList.removeAll(cartList);

		cartService.allDeleteCartInfo(userId);

		model.addAttribute("cartList", cartList);
		model.addAttribute("giftAmount", giftAmount);

		// カート確認画面へ
		return "generalProduct/shoppingCart";
	}

	// ヘッダー部からカート確認画面
	@RequestMapping(value = "/header", params = "cart")
	public String checkShoppingCart(Model model, SessionStatus sessionStatus) {
		List<ProductInfo> cartList = this.getCartListSession();
		Integer totalAmount = this.getTotalAmountSession();
		Integer giftAmount = this.getGiftAmountSession();

		String priceAlertMsg = (String) session.getAttribute("priceAlertMsg");

		model.addAttribute("cartList", cartList);
		model.addAttribute("totalAmount", totalAmount);
		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("priceAlertMsg", priceAlertMsg);

		sessionStatus.setComplete();

		// カート確認画面へ
		return "generalProduct/shoppingCart";
	}

	/**
	 * httpセッションからuseridを取得する処理。
	 * 
	 * @return ユーザid
	 */
	private Integer getUserIdSession() {
		Integer userId = (Integer) session.getAttribute("userId");

		return userId;
	}

	/**
	 * httpセッションからuserNameを取得する処理。
	 * 
	 * @return ユーザ名
	 */
	private String getUserNameSession() {
		String userName = (String) session.getAttribute("userName");

		return userName;
	}

	/**
	 * httpセッションからcartListを取得する処理。
	 * 
	 * @return カートリスト
	 */
	private List<ProductInfo> getCartListSession() {
		List<ProductInfo> cartList = (List<ProductInfo>) session.getAttribute("cartList");

		return cartList;
	}

	/**
	 * httpセッションからtotalAmountを取得する処理
	 * 
	 * @return 合計金額
	 */
	private Integer getTotalAmountSession() {
		Integer totalAmount = (Integer) session.getAttribute("totalAmount");

		return totalAmount;
	}

	/**
	 * httpセッションからgiftAmountを取得する処理
	 * 
	 * @return ギフト券金額
	 */
	private Integer getGiftAmountSession() {
		Integer giftAmount = (Integer) session.getAttribute("giftAmount");

		return giftAmount;
	}

	/**
	 * httpセッションからversionNumberを取得する処理
	 * 
	 * @return バージョン番号
	 */
	private Integer getVersionNumberSession() {
		Integer versionNumber = (Integer) session.getAttribute("versionNumber");

		return versionNumber;
	}

}
