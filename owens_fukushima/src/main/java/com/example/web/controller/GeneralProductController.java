package com.example.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

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
import com.example.service.ProductInfoDBService;
import com.example.service.PurchaseHistoryDBService;
import com.example.service.UserDBService;
import com.example.web.error.ErrorCheck;
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
	HttpSession session;

	@ModelAttribute("productSearchForm")
	public ProductSearchForm setSearchForm() {
		return new ProductSearchForm();
	}

	@ModelAttribute("productPurchaseForm")
	public ProductPurchaseForm setPurchaseForm() {
		return new ProductPurchaseForm();
	}

	//メニュー画面から商品検索画面
	@RequestMapping(value = "/select-btn", params = "productSearch")
	public String productSearch(Model model) {
		String userName = this.getUserNameFromSession();
		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("userName", userName);
		model.addAttribute("giftAmount", giftAmount);

		//商品検索画面へ
		return "generalProduct/productSearch";
	}

	//商品検索画面から商品一覧画面へ
	@RequestMapping(value = "/product-search", params = "search")
	public String productList(@Validated @ModelAttribute("productSearchForm") ProductSearchForm form,
			BindingResult result, Model model) {

		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("giftAmount", giftAmount);

		ErrorCheck error = new ErrorCheck();

		//商品検索時の「価格」の入力チェックを行う
		error.priceInputCheck(form, result);

		if (result.hasErrors()) {
			//講座検索画面へ
			return "generalProduct/productSearch";

		}

		session.setAttribute("productSearchForm", form);

		//検索条件に該当する商品情報をDBから取得
		List<ProductInfo> list = productService.getProductList(form);

		model.addAttribute("productList", list);

		//商品一覧画面へ
		return "generalProduct/productList";
	}

	//商品検索画面からメニュー画面へ
	@RequestMapping(value = "/product-search", params = "back")
	public String backMenu(Model model) {
		session.removeAttribute("courseSearchForm");

		String userName = this.getUserNameFromSession();
		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("userName", userName);
		model.addAttribute("giftAmount", giftAmount);

		//メニュー画面へ
		return "user/generalUserMenu";
	}

	//商品一覧画面から商品検索画面
	@RequestMapping(value = "/back-product-list", params = "backProductSearch")
	public String backProductSearch(Model model) {
		ProductSearchForm form = (ProductSearchForm) session.getAttribute("productSearchForm");

		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("productSearchForm", form);
		model.addAttribute("giftAmount", giftAmount);

		//商品検索画面へ
		return "generalProduct/productSearch";
	}

	//商品一覧画面から商品詳細画面へ
	@RequestMapping(value = "/product-detail", params = "detail")
	public String productDetail(@ModelAttribute("productPurchaseForm") ProductPurchaseForm form,
			@RequestParam("productId") String productId, Model model) {
		session.setAttribute("productId", productId);

		Integer giftAmount = this.getGiftAmountSession();

		//リクエストから送られた商品IDに該当する商品情報を取得する
		ProductInfo info = productService.getIdMatchProductData(productId);

		//データベースから取得した商品情報をFormに格納
		form.setProductName(info.getProductName());
		form.setPrice(info.getPrice());
		form.setGenreName(info.getGenreName());
		form.setQuantity(info.getQuantity());

		model.addAttribute("productId", productId);
		model.addAttribute("giftAmount", giftAmount);

		return "generalProduct/productDetail";

	}

	//商品詳細画面から商品一覧画面
	@RequestMapping(value = "/product-purchase", params = "backProductList")
	public String backproductList(Model model, SessionStatus sessionStatus) {
		ProductSearchForm form = (ProductSearchForm) session.getAttribute("productSearchForm");
		Integer giftAmount = this.getGiftAmountSession();

		List<ProductInfo> list = productService.getProductList(form);

		model.addAttribute("productList", list);
		model.addAttribute("giftAmount", giftAmount);

		sessionStatus.setComplete();

		return "generalProduct/productList";

	}

	//商品詳細から購入確認画面
	@RequestMapping(value = "/product-purchase", params = "purchase")
	public String productDetail(@Validated @ModelAttribute("productPurchaseForm") ProductPurchaseForm form,
			BindingResult result, Model model) {
		ErrorCheck error = new ErrorCheck();

		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("giftAmount", giftAmount);

		//「購入数」が選択されているか判定する
		error.purchaseQtySelectCheck(form, result);

		if (result.hasErrors()) {
			//詳細画面へ
			return "generalProduct/productDetail";
		}

		//合計金額を計算する処理
		form.setTotalAmount(form.getPrice() * form.getPurchaseQty());

		//購入確認画面へ
		return "generalProduct/productPurchaseCheck";
	}

	//購入確認画面から商品詳細画面
	@RequestMapping(value = "product-purchase-finish", params = "backDetail")
	public String backProductDetail(@ModelAttribute("productPurchaseForm") ProductPurchaseForm form,
			Model model) {
		String productId = (String) session.getAttribute("productId");
		Integer giftAmount = this.getGiftAmountSession();

		//リクエストから送られてきた商品IDに該当する商品情報を取得する
		ProductInfo info = productService.getIdMatchProductData(productId);

		//データベースから取得した商品情報をFormに格納
		form.setProductName(info.getProductName());
		form.setPrice(info.getPrice());
		form.setGenreName(info.getGenreName());
		form.setQuantity(info.getQuantity());

		model.addAttribute("productId", productId);
		model.addAttribute("giftAmount", giftAmount);

		return "generalProduct/productDetail";

	}

	//購入確認画面から購入完了画面
	@RequestMapping(value = "product-purchase-finish", params = "purchase")
	public String productPurchaseFinish(@ModelAttribute("productPurchaseForm") ProductPurchaseForm form,
			SessionStatus sessionStatus, Model model) {
		Integer userId = this.getUserIdFromSession();
		Integer giftAmount = this.getGiftAmountSession();

		List<String> errorList = new ArrayList<String>();

		ErrorCheck error = new ErrorCheck();
		//ギフト券の金額が不足していないか確認
		error.compareAmounts(form.getTotalAmount(), giftAmount,errorList);

		if (errorList.size() != 0) {
			model.addAttribute("giftAmount", giftAmount);
			model.addAttribute("errorList", errorList);

			return "generalProduct/productPurchaseCheck";
		}

		//購入対象の商品を購入履歴テーブルに挿入する処理
		purchaseService.insertPurchaseHistory(form, userId);

		//商品情報から購入数量の数だけ在庫数を減らす処理
		productService.upDateSubstractQuantity(form.getProductId(), form.getPurchaseQty());

		//商品の金額だけギフト券の金額を減算する
		userService.updateSubstractGiftAmount(form.getTotalAmount(), userId);

		//更新したギフト券の金額を取得する
		giftAmount = userService.selectGiftAmount(userId);

		//セッションを上書き
		session.setAttribute("giftAmount", giftAmount);

		model.addAttribute("giftAmount", giftAmount);

		sessionStatus.setComplete();

		return "generalProduct/productPurchaseFinish";

	}

	//購入完了画面から商品検索画面
	@RequestMapping(value = "select-btn", params = "selectProductSearch")
	public String returnProductSearch(Model model) {
		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("giftAmount", giftAmount);

		session.removeAttribute("productSearchForm");

		//商品検索画面へ
		return "generalProduct/productSearch";
	}

	//購入完了画面から商品一覧画面
	@RequestMapping(value = "select-btn", params = "selectProductList")
	public String returnProductList(Model model) {
		ProductSearchForm form = (ProductSearchForm) session.getAttribute("productSearchForm");

		Integer giftAmount = this.getGiftAmountSession();

		//検索条件に該当する商品情報をDBから取得
		List<ProductInfo> list = productService.getProductList(form);

		model.addAttribute("productList", list);
		model.addAttribute("giftAmount", giftAmount);

		return "generalProduct/productList";

	}

	//購入完了画面からメニュー画面
	@RequestMapping(value = "select-btn", params = "selectMenu")
	public String returnMenu(Model model) {
		String userName = this.getUserNameFromSession();
		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("userName", userName);
		model.addAttribute("giftAmount", giftAmount);

		//メニュー画面へ
		return "user/generalUserMenu";
	}

	//商品詳細からカート確認画面
	@RequestMapping(value = "/product-purchase", params = "cart")
	public String InputShoppingCart(@Validated @ModelAttribute("productPurchaseForm") ProductPurchaseForm form,
			BindingResult result, @RequestParam("productId") String productId, Model model,
			SessionStatus sessionStatus) {
		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("giftAmount", giftAmount);

		ErrorCheck error = new ErrorCheck();

		//「購入数」が選択されているか判定する
		error.purchaseQtySelectCheck(form, result);

		if (result.hasErrors()) {
			//詳細画面へ
			return "generalProduct/productDetail";
		}

		sessionStatus.setComplete();

		//セッションからカートリストを取得
		List<ProductInfo> cartList = this.getCartListSession();

		//重複フラグ
		boolean duplication = false;

		for (ProductInfo productInfo : cartList) {
			//同じ商品をカートにいれた場合は、数量だけ加算する。
			if (productInfo.getProductId() == form.getProductId()) {
				duplication = true;
				productInfo.setPurchaseQty(productInfo.getPurchaseQty() + form.getPurchaseQty());
			}
		}

		//同じ商品でなかったら
		if (!duplication) {
			//商品IDに該当する商品をデータベースから取得する
			ProductInfo info = productService.getIdMatchProductData(productId);

			info.setPurchaseQty(form.getPurchaseQty());
			cartList.add(info);
		}

		Integer totalAmount = 0;
		//合計金額の計算
		for (ProductInfo productInfo : cartList) {
			totalAmount += productInfo.getPrice() * productInfo.getPurchaseQty();
		}

		//セッションの更新
		session.setAttribute("cartList", cartList);
		session.setAttribute("totalAmount", totalAmount);

		model.addAttribute("cartList", cartList);
		model.addAttribute("totalAmount", totalAmount);

		return "generalProduct/shoppingCart";
	}

	//カート確認画面から「削除」押下時、カート確認画面へ
	@RequestMapping(value = "/cart-product-delete", params = "delete")
	public String cartProductInfoDelete(@RequestParam("productId") String productId, Model model) {
		List<ProductInfo> cartList = this.getCartListSession();
		Integer totalAmount = this.getTotalAmountSession();

		//リクエストで渡された商品IDに該当する商品情報をカートから削除し、合計金額から減算する
		for (int i = 0; i < cartList.size(); i++) {
			if (cartList.get(i).getProductId() == Integer.parseInt(productId)) {
				totalAmount -= cartList.get(i).getPrice() * cartList.get(i).getPurchaseQty();

				cartList.remove(i);
			}
		}

		Integer giftAmount = this.getGiftAmountSession();

		//セッションを更新
		session.setAttribute("cartList", cartList);
		session.setAttribute("totalAmount", totalAmount);

		model.addAttribute("totalAmount", totalAmount);
		model.addAttribute("cartList", cartList);
		model.addAttribute("giftAmount", giftAmount);

		return "generalProduct/shoppingCart";
	}

	//カート確認画面から検索画面
	@RequestMapping(value = "/cart-product-purchase", params = "productSearch")
	public String cartReturnProductSearch(Model model) {
		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("giftAmount", giftAmount);

		return "generalProduct/productSearch";
	}

	//カート確認画面から購入完了画面
	@RequestMapping(value = "/cart-product-purchase", params = "purchase")
	public String cartProductPurchase(Model model) {
		Integer userId = this.getUserIdFromSession();
		Integer totalAmount = this.getTotalAmountSession();
		Integer giftAmount = this.getGiftAmountSession();

		List<ProductInfo> cartList = this.getCartListSession();

		List<String> errorList = new ArrayList<String>();

		ErrorCheck error = new ErrorCheck();

		//ギフト券の金額が不足していないか確認
		error.compareAmounts(totalAmount, giftAmount, errorList);

		//カート内の商品の購入数が在庫数より超過しているか
		for(ProductInfo productInfo : cartList) {
			Integer quantity = productService.getIdMatchQuantity(productInfo.getProductId());

			error.purcharseQtyOver(quantity, productInfo.getPurchaseQty(), productInfo.getProductName(), errorList);

		}

		if (errorList.size() != 0) {
			model.addAttribute("errorList", errorList);
			model.addAttribute("totalAmount", totalAmount);
			model.addAttribute("giftAmount", giftAmount);
			model.addAttribute("cartList", cartList);

			return "generalProduct/shoppingCart";

		} else {
			for (ProductInfo productInfo : cartList) {
				purchaseService.insertCartPurchaseHistory(productInfo, userId);

				productService.upDateSubstractQuantity(productInfo.getProductId(), productInfo.getPurchaseQty());

			}
			cartList.removeAll(cartList);

			//ギフト券金額からカート内の合計金額分を減算
			userService.updateSubstractGiftAmount(totalAmount, userId);

			//更新したギフト券の金額を取得
			giftAmount = userService.selectGiftAmount(userId);

			session.setAttribute("cartList", cartList);
			session.setAttribute("giftAmount", giftAmount);

			model.addAttribute("giftAmount", giftAmount);

		}

		return "generalProduct/productPurchaseFinish";
	}

	//ヘッダー部からカート確認画面
	@RequestMapping(value = "/header", params = "cart")
	public String CheckShoppingCart(Model model) {
		List<ProductInfo> cartList = this.getCartListSession();
		Integer totalAmount = this.getTotalAmountSession();
		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("cartList", cartList);
		model.addAttribute("totalAmount", totalAmount);
		model.addAttribute("giftAmount", giftAmount);

		return "generalProduct/shoppingCart";
	}


	/**
	 * httpセッションからuseridを取得する処理。
	 * @return ユーザid
	 */
	private Integer getUserIdFromSession() {
		Integer userId = (Integer) session.getAttribute("userId");

		return userId;
	}

	/**
	 * httpセッションからuserNameを取得する処理。
	 * @return ユーザ名
	 */
	private String getUserNameFromSession() {
		String userName = (String) session.getAttribute("userName");

		return userName;
	}

	/**
	 * httpセッションからcartListを取得する処理。
	 * @return カートリスト
	 */
	private List<ProductInfo> getCartListSession() {
		List<ProductInfo> cartList = (List<ProductInfo>) session.getAttribute("cartList");

		return cartList;
	}

	/**
	 * httpセッションからtotalAmountを取得する処理
	 *
	 */
	private Integer getTotalAmountSession() {
		Integer totalAmount = (Integer) session.getAttribute("totalAmount");

		return totalAmount;
	}

	private Integer getGiftAmountSession() {
		Integer giftAmount = (Integer) session.getAttribute("giftAmount");

		return giftAmount;
	}


}
