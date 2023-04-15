package com.example.web.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.domain.ProductInfo;
import com.example.service.ProductInfoDBService;
import com.example.web.errorcheck.ErrorCheck;
import com.example.web.form.ProductCorrectForm;
import com.example.web.form.ProductRegisterForm;

@Controller
//セッションで管理するオブジェクトのキー名を指定
@SessionAttributes({ "productRegisterForm", "productCorrectForm" })
public class AdminProductController {

	@Autowired
	ProductInfoDBService productService;

	@Autowired
	HttpSession session;

	//Modelオブジェクトへクラスの自動追加
	@ModelAttribute("productRegisterForm")
	public ProductRegisterForm setRegisterForm() {
		return new ProductRegisterForm();

	}

	@ModelAttribute("productCorrectForm")
	public ProductCorrectForm setCorrectForm() {
		return new ProductCorrectForm();
	}

	//メニュー画面から商品登録画面
	@RequestMapping(value = "/select-btn", params = "productRegister")
	public String productRegister(Model model) {
		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("giftAmount", giftAmount);
		//商品登録画面へ
		return "adminProduct/productRegister";
	}

	//商品登録画面からメニュー画面
	@RequestMapping(value = "/product-register", params = "backMenu")
	public String backMenu(Model model, SessionStatus sessionStatus) {
		sessionStatus.setComplete();

		String userName = this.getUserNameSession();
		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("userName", userName);
		model.addAttribute("giftAmount", giftAmount);

		//メニュー画面へ
		return "user/adminUserMenu";
	}

	//商品登録画面から登録確認画面
	@RequestMapping(value = "/product-register", params = "check")
	public String productRegisterCheck(@Validated @ModelAttribute("productRegisterForm") ProductRegisterForm form,
			BindingResult result, Model model) {
		ErrorCheck error = new ErrorCheck();

		if (!form.getProductName().isEmpty()) {
			//DBに同じ商品名がないか判定する
			error.selectProductName(form, productService, result);
		}

		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("giftAmount", giftAmount);

		if (result.hasErrors()) {
			//商品登録画面へ
			return "adminProduct/productRegister";
		}

		//入力時に選択したジャンル番号に該当するジャンル名を取得
		form.setGenreName(productService.selectGenreName(form));

		//商品登録確認画面へ
		return "adminProduct/productRegisterCheck";

	}

	//登録確認画面から登録画面
	@RequestMapping(value = "/product-register-check", params = "backRegister")
	public String backProductRegister(@ModelAttribute("productRegisterForm") ProductRegisterForm form, Model model) {
		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("giftAmount", giftAmount);

		//登録画面へ
		return "adminProduct/productRegister";
	}

	//登録確認から登録完了画面
	@RequestMapping(value = "/product-register-check", params = "register")
	public String registerFinish(@ModelAttribute("productRegisterForm") ProductRegisterForm form,
			SessionStatus sessionStatus) {

		//商品情報テーブルに入力情報を登録する
		productService.insertProductInfo(form);

		sessionStatus.setComplete();

		return "redirect:/insert-end?finish";

	}

	//リダイレクト後に呼び出される処理メソッド
	@RequestMapping(value = "/insert-end", params = "finish")
	public String productRegisterFinish(Model model) {
		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("giftAmount", giftAmount);
		//商品登録完了画面へ
		return "adminProduct/productRegisterFinish";
	}

	//登録完了画面から登録画面
	@RequestMapping(value = "/product-register-finish", params = "addProductRegister")
	public String returnProductRegister(Model model) {
		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("giftAmount", giftAmount);
		//商品登録画面へ
		return "adminProduct/productRegister";
	}

	//メニューから商品修正削除一覧画面
	@RequestMapping(value = "/select-btn", params = "productCorrectDelete")
	public String productCorrectDeleteList(Model model) {
		Integer giftAmount = this.getGiftAmountSession();

		List<ProductInfo> productList = productService.getProductListAll();

		session.setAttribute("productList", productList);

		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("productList", productList);

		//商品修正削除一覧画面へ
		return "adminProduct/productCorrectDeletetList";
	}

	//商品修正削除一覧画面からメニュー画面
	@RequestMapping(value = "/back-product-list", params = "backAdminMenu")
	public String backAdminMenu(Model model) {
		String userName = this.getUserNameSession();
		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("userName", userName);
		model.addAttribute("giftAmount", giftAmount);

		session.removeAttribute("productList");

		//メニュー画面へ
		return "user/adminUserMenu";
	}

	//商品修正削除一覧画面から修正画面
	@RequestMapping(value = "/product-correct-delete", params = "correct")
	public String ProductCorrect(@Param("productId") Integer productId, @Param("versionNumber") Integer versionNumber,
			@ModelAttribute("productCorrectForm") ProductCorrectForm form, Model model) {
		Integer giftAmount = this.getGiftAmountSession();

		List<ProductInfo> productList = this.getProductListSession();

		for (ProductInfo productInfo : productList) {
			if (productInfo.getProductId() == productId) {
				form.setProductId(productInfo.getProductId());
				form.setProductName(productInfo.getProductName());
				form.setPrice(productInfo.getPrice());
				form.setGenreName(productInfo.getGenreName());
				form.setQuantity(productInfo.getQuantity());

				break;
			}
		}
		session.setAttribute("versionNumber", versionNumber);

		model.addAttribute("giftAmount", giftAmount);
		//商品修正画面へ
		return "adminProduct/productCorrect";
	}

	//商品修正画面から修正確認画面
	@RequestMapping(value = "/product-correct", params = "check")
	public String productCorrectCheck(@Validated @ModelAttribute("productCorrectForm") ProductCorrectForm form,
			BindingResult result, Model model) {
		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("giftAmount", giftAmount);

		if (result.hasErrors()) {
			//商品修正画面へ
			return "adminProduct/productCorrect";
		}

		//商品修正確認画面へ
		return "adminProduct/productCorrectCheck";

	}

	//修正確認画面から商品修正画面
	@RequestMapping(value = "/correct-check", params = "backProductCorrect")
	public String backProductCorrect(@ModelAttribute("productCorrectForm") ProductCorrectForm form,
			Model model) {
		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("giftAmount", giftAmount);

		//商品修正画面へ
		return "adminProduct/productCorrect";
	}

	//修正確認画面から修正完了画面
	@RequestMapping(value = "/correct-check", params = "corret")
	public String correctFinish(@ModelAttribute("productCorrectForm") ProductCorrectForm form,
			SessionStatus sessionStatus, Model model) {
		Integer versionNumber = this.getVersionNumberSession();

		int upDateCnt = productService.correctProductInfo(form, versionNumber);

		ErrorCheck error = new ErrorCheck();

		String errorMsg = error.upDateProductInfoCheck(upDateCnt);

		if (errorMsg != null) {
			model.addAttribute("errorMsg", errorMsg);
			//商品修正確認画面へ
			return "adminProduct/productCorrectCheck";
		}

		sessionStatus.setComplete();
		session.removeAttribute("versionNumber");

		//修正完了画面へのリダイレクト処理
		return "redirect:/correct-end?finish";
	}

	//リダイレクト後に呼び出される処理メソッド
	@RequestMapping(value = "/correct-end", params = "finish")
	public String productCorrectFinish(Model model) {
		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("giftAmount", giftAmount);
		//修正完了画面へ
		return "adminProduct/productCorrectFinish";
	}

	//修正完了画面から商品修正削除一覧画面
	@RequestMapping(value = "/product-correct-finish", params = "returnList")
	public String returnProductCorrectDeleteList(Model model) {
		Integer giftAmount = this.getGiftAmountSession();

		List<ProductInfo> productList = productService.getProductListAll();

		session.setAttribute("productList", productList);

		model.addAttribute("productList", productList);
		model.addAttribute("giftAmount", giftAmount);

		//商品修正削除一覧画面へ
		return "adminProduct/productCorrectDeletetList";
	}

	//商品修正削除一覧画面から削除確認画面
	@RequestMapping(value = "/product-correct-delete", params = "delete")
	public String productDeleteCheck(@Param("productId") Integer productId,
			@Param("versionNumber") Integer versionNumber, Model model) {
		List<ProductInfo> productList = this.getProductListSession();
		Integer giftAmount = this.getGiftAmountSession();

		ProductInfo productInfo = new ProductInfo();

		for (ProductInfo info : productList) {
			if (info.getProductId() == productId) {
				productInfo.setProductId(info.getProductId());
				productInfo.setProductName(info.getProductName());
				productInfo.setPrice(info.getPrice());
				productInfo.setGenreName(info.getGenreName());
				productInfo.setQuantity(info.getQuantity());

				break;
			}
		}

		session.setAttribute("productInfo", productInfo);
		session.setAttribute("productId", productId);
		session.setAttribute("versionNumber", versionNumber);

		model.addAttribute("productInfo", productInfo);
		model.addAttribute("giftAmount", giftAmount);

		//商品削除確認画面へ
		return "adminProduct/productDeleteCheck";
	}

	//削除確認画面から削除完了画面
	@RequestMapping(value = "/delete-check", params = "delete")
	public String deleteFinish(Model model) {
		Integer productId = this.getProductIdSession();
		Integer versionNumber = this.getVersionNumberSession();

		int updateCnt = productService.deleteProductInfo(productId, versionNumber);

		ErrorCheck error = new ErrorCheck();

		String errorMsg = error.upDateProductInfoCheck(updateCnt);

		if (errorMsg != null) {
			ProductInfo productInfo = (ProductInfo) session.getAttribute("productInfo");

			model.addAttribute("productInfo", productInfo);

			model.addAttribute("errorMsg", errorMsg);

			//商品削除確認画面へ
			return "adminProduct/productDeleteCheck";

		}
		session.removeAttribute("versionNumber");
		session.removeAttribute("productId");

		return "redirect:/delete-end?finish";

	}

	//リダイレクト後に呼び出される処理メソッド
	@RequestMapping(value = "/delete-end", params = "finish")
	public String productDeleteFinish(Model model) {
		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("giftAmount", giftAmount);
		//削除完了画面へ
		return "adminProduct/productDeleteFinish";
	}

	//商品修正・削除確認画面から商品修正削除一覧画面
	@RequestMapping(value = { "/product-correct", "/delete-check" }, params = "backList")
	public String backProductCorrectDeleteList(Model model) {
		List<ProductInfo> productList = productService.getProductListAll();

		Integer giftAmount = this.getGiftAmountSession();

		session.setAttribute("productList", productList);

		model.addAttribute("productList", productList);
		model.addAttribute("giftAmount", giftAmount);

		//商品修正削除一覧画面へ
		return "adminProduct/productCorrectDeletetList";
	}

	//登録完了・修正完了・削除完了画面からメニュー画面へ
	@RequestMapping(value = { "/product-register-finish", "/product-correct-finish",
			"/product-delete-finish" }, params = "returnMenu")
	public String returnMenu(Model model) {
		String userName = this.getUserNameSession();
		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("userName", userName);
		model.addAttribute("giftAmount", giftAmount);

		session.removeAttribute("productList");

		//メニュー画面へ
		return "user/adminUserMenu";

	}

	/**
	 * httpセッションからuserNameを取得する処理。
	 * @return 商品ID
	 */
	private String getUserNameSession() {
		String userName = (String) session.getAttribute("userName");

		return userName;
	}

	/**
	 * httpセッションからproductIdを取得する処理。
	 * @return 商品ID
	 */
	private Integer getProductIdSession() {
		Integer productId = (Integer) session.getAttribute("productId");

		return productId;
	}

	/**
	 * httpセッションからを取得する処理。
	 * @return ギフト券金額
	 */
	private Integer getGiftAmountSession() {
		Integer giftAmount = (Integer) session.getAttribute("giftAmount");

		return giftAmount;
	}

	/**
	 * httpセッションからversionNumberを取得する処理。
	 * @return バージョン番号
	 */
	private Integer getVersionNumberSession() {
		Integer versionNumber = (Integer) session.getAttribute("versionNumber");

		return versionNumber;
	}

	private List<ProductInfo> getProductListSession(){
		List<ProductInfo> productList = (List<ProductInfo>) session.getAttribute("productList");

		return  productList;
	}



}
