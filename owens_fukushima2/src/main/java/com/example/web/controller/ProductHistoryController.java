package com.example.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.domain.ProductInfo;
import com.example.service.CartInfoDBService;
import com.example.service.ProductInfoDBService;
import com.example.service.PurchaseHistoryDBService;
import com.example.service.UserDBService;
import com.example.web.alertcheck.AlertCheck;
import com.example.web.errorcheck.ErrorCheck;
import com.example.web.form.ProductPurchaseForm;

@SessionAttributes("productPurchaseForm")
@Controller
public class ProductHistoryController {

	@Autowired
	private ProductInfoDBService productService;

	@Autowired
	private PurchaseHistoryDBService purchaseService;

	@Autowired
	private UserDBService userService;

	@Autowired
	private CartInfoDBService cartService;

	@Autowired
	private HttpSession session;

	@ModelAttribute("productPurchaseForm")
	public ProductPurchaseForm setPurchaseForm() {
		return new ProductPurchaseForm();
	}

	// メニュー画面から購入履歴画面へ
	@RequestMapping(value = "/select-btn", params = "purchaseHistory")
	public String purchaseHistory(Model model) {
		Integer userId = this.getUserIdSession();
		String userName = this.getUserNameSession();
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		// ログインしたユーザのIDから購入履歴を取得する処理
		List<ProductInfo> purchaseHistoryList = purchaseService.selectPurchaseHistory(userId);

		// 取得した購入日の情報を表示用に整える
		for (ProductInfo date : purchaseHistoryList) {
			date.setPurchaseDateTime(date.getDateTime());
		}

		model.addAttribute("purchaseHistoryList", purchaseHistoryList);
		model.addAttribute("userName", userName);
		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("cartList", cartList);

		// 購入履歴画面へ
		return "history/purchaseHistoryList";
	}

	// 購入履歴画面から削除確認画面
	@RequestMapping(value = "/purchase-history-list", params = "delete")
	public String historyDeleteCheck(@RequestParam("purchaseId") String purchaseId, Model model) {
		// リクエストから送られてきた購入IDに該当する履歴情報を取得する
		ProductInfo productInfo = purchaseService.getIdMatchPurchaseHistory(purchaseId);

		productInfo.setPurchaseDateTime(productInfo.getDateTime());

		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		model.addAttribute("purchaseInfo", productInfo);
		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("cartList", cartList);

		// 削除確認画面へ
		return "history/historyDeleteCheck";

	}

	// 購入履歴画面からメニュー画面へ
	@RequestMapping(value = "/purchase-history-list", params = "backMenu")
	public String deleteHistory(Model model) {
		String userName = this.getUserNameSession();
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		model.addAttribute("userName", userName);
		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("cartList", cartList);

		// メニュー画面へ
		return "user/generalUserMenu";
	}

	// 削除確認・購入確認画面から購入履歴画面
	@RequestMapping(value = { "/history-delete-check", "/repurchase-check" }, params = "backHistoryList")
	public String backPurchaseHistory(Model model, SessionStatus sessionStatus) {
		Integer userId = this.getUserIdSession();
		String userName = this.getUserNameSession();
		Integer giftAmount = this.getGiftAmountSession();
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

		sessionStatus.setComplete();

		// 購入履歴画面へ
		return "history/purchaseHistoryList";

	}

	// 削除確認画面から削除完了画面
	@RequestMapping(value = "/history-delete-check", params = "delete")
	public String deletePurchaseHistory(@RequestParam("purchaseId") String purchaseId, Model model) {
		// リクエストから送られてきた購入Idに該当する履歴情報を削除する処理
		purchaseService.deletePurchaseHistory(purchaseId);

		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("cartList", cartList);

		// 削除完了画面へ
		return "history/historyDeleteFinish";
	}

	// 削除完了からメニュー画面
	@RequestMapping(value = "/history-delete-finish", params = "selectMenu")
	public String returnMenu(Model model) {
		String userName = this.getUserNameSession();
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		model.addAttribute("userName", userName);
		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("cartList", cartList);

		// メニュー画面へ
		return "user/generalUserMenu";
	}

	// 削除完了・購入完了から購入履歴一覧
	@RequestMapping(value = "/history-delete-finish", params = "selectPurchaseHistory")
	public String returnPurchaseHistory(Model model) {
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

		// 購入履歴画面へ
		return "history/purchaseHistoryList";
	}

	// 購入履歴画面から購入確認画面
	@RequestMapping(value = "/purchase-history-list", params = "repurchase", method = RequestMethod.POST)
	public String repurcaseCheck(@Param("productId") Integer productId, @Param("purchaseQty") Integer purchaseQty,
			@Param("price") Integer price, @ModelAttribute("productPurchaseForm") ProductPurchaseForm form,
			Model model) {
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		// リクエストから送られてきた商品IDを使ってDBから商品情報を取得
		ProductInfo productInfo = productService.getIdMatchProductData(productId);
		AlertCheck alert = new AlertCheck();
		String alertMsg = alert.priceComparisonCheck(price, productInfo.getPrice(), productInfo.getProductName());

		form.setProductName(productInfo.getProductName());
		form.setPrice(productInfo.getPrice());
		form.setGenreName(productInfo.getGenreName());
		form.setPurchaseQty(purchaseQty);
		form.setTotalAmount(form.getPrice() * form.getPurchaseQty());

		model.addAttribute("productPurchaseForm", form);
		model.addAttribute("cartList", cartList);
		model.addAttribute("giftAmount", giftAmount);

		// 購入確認画面へ
		return "history/repurchaseCheck";
	}

	// 購入確認画面から購入完了画面
	@RequestMapping(value = "/repurchase-check", params = "purchase")
	public String repurchaseFinish(@ModelAttribute("productPurchaseForm") ProductPurchaseForm form, Model model,
			SessionStatus sessionStatus) {

		Integer userId = this.getUserIdSession();
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		model.addAttribute("cartList", cartList);

		List<String> errorList = new ArrayList<String>();

		ErrorCheck error = new ErrorCheck();

		// ギフト券の金額が不足していないか確認
		error.compareAmounts(form.getTotalAmount(), giftAmount, errorList);

		// 在庫数をDBから取得
		Integer quantity = productService.getIdMatchQuantity(form.getProductId());

		// カート内の商品の購入数が在庫数より多いか判定
		error.purcharseQtyOver(quantity, form.getPurchaseQty(), form.getProductName(), errorList);

		ProductInfo productInfo = new ProductInfo();

		BeanUtils.copyProperties(form, productInfo);

		// 購入対象の商品情報が管理者に更新・削除されていないかを判定
		int cnt = productService.purchaseProductUpDateCheck(productInfo);

		if (cnt == 0) {
			errorList.add("※「 " + form.getProductName() + "」の情報が管理者によって修正・削除された可能性があります。最新情報を取得してください。");
		}

		if (errorList.size() != 0) {
			model.addAttribute("giftAmount", giftAmount);
			model.addAttribute("errorList", errorList);

			// 購入確認画面へ
			return "history/repurchaseCheck";
		}

		// 購入対象の商品を購入履歴テーブルに挿入する処理
		purchaseService.insertPurchaseHistory(form, userId);

		// 商品情報から購入数量の数だけ在庫数を減らす処理
		productService.upDateSubstractQuantity(productInfo);

		// 商品の金額だけギフト券の金額を減算する
		userService.upDateSubstractGiftAmount(form.getTotalAmount(), userId);

		// 更新したギフト券の金額を取得する
		giftAmount = userService.selectGiftAmount(userId);

		session.setAttribute("giftAmount", giftAmount);

		model.addAttribute("giftAmount", giftAmount);

		sessionStatus.setComplete();

		// 購入完了画面へ
		return "generalProduct/productPurchaseFinish";
	}

	// 購入履歴画面からカート確認画面
	@RequestMapping(value = "/purchase-history-list", params = "cart")
	public String InputShoppingCart(@Param("productId") Integer productId, @Param("purchaseQty") Integer purchaseQty,
			@Param("price") Integer price, Model model) {
		Integer giftAmount = this.getGiftAmountSession();
		Integer userId = this.getUserIdSession();
		List<ProductInfo> cartList = this.getCartListSession();

		String priceAlertMsg = null;

		AlertCheck alert = new AlertCheck();

		// 商品重複フラグ
		boolean duplication = false;

		for (ProductInfo productInfo : cartList) {
			// 同じ商品をカートにいれた場合は、数量だけ加算する。
			if (productInfo.getProductId() == productId) {
				// データベースから最新の「価格」を取得する
				Integer latestPrice = productService.getLatestPrice(productId);

				duplication = true;

				productInfo.setPrice(latestPrice);
				productInfo.setPurchaseQty(productInfo.getPurchaseQty() + purchaseQty);

				priceAlertMsg = alert.priceComparisonCheck(price, latestPrice, productInfo.getProductName());

				cartService.cartInfoUpdate(latestPrice, purchaseQty, productId, userId);
			}
		}
		
		 //同じ商品でなかったら
		 if (!duplication) {
		 //リクエストから送られてきた商品IDを使ってDBから商品情報を取得
		 ProductInfo productInfo = productService.getIdMatchInCartInfo(productId);
		
		 productInfo.setPurchaseQty(purchaseQty);
		
		 priceAlertMsg = alert.priceComparisonCheck(price, productInfo.getPrice(),
		 productInfo.getProductName());
		
		 cartService.cartInfoRegister(userId, productInfo);
		
		 productInfo = cartService.getIdMatchCartInfo(productId, userId);
		
		 cartList.add(productInfo);
		 }
		
		 Integer totalAmount = 0;
		
		 //合計金額の計算
		 for (ProductInfo productInfo : cartList) {
		 totalAmount += productInfo.getPrice() * productInfo.getPurchaseQty();
		 }
		 session.setAttribute("totalAmount", totalAmount);
		 session.setAttribute("cartLizst", cartList);
		 session.setAttribute("priceAlertMsg", priceAlertMsg);
		
		 model.addAttribute("giftAmount", giftAmount);
		 model.addAttribute("cartList", cartList);
		 model.addAttribute("totalAmount", totalAmount);
		 model.addAttribute("priceAlertMsg", priceAlertMsg);

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
	private Integer getGiftAmountSession() {
		Integer giftAmount = (Integer) session.getAttribute("giftAmount");

		return giftAmount;
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

}
