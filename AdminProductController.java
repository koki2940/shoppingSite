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
import com.example.web.error.ErrorCheck;
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
	public String productRegister() {
		return "adminProduct/productRegister";
	}

	//商品登録画面からメニュー画面
	@RequestMapping(value = "/product-register", params = "backMenu")
	public String backMenu(Model model, SessionStatus sessionStatus) {
		sessionStatus.setComplete();

		String userName = this.getUserNameFromSession();

		model.addAttribute("userName", userName);

		//メニュー画面へ
		return "user/adminUserMenu";
	}

	//商品登録画面から登録確認画面
	@RequestMapping(value = "/product-register", params = "check")
	public String productRegisterCheck(@Validated @ModelAttribute("productRegisterForm") ProductRegisterForm form,
			BindingResult result) {
		ErrorCheck error = new ErrorCheck();

		if (!form.getProductName().isEmpty()) {
			//DBに同じ商品名がないか判定する
			error.selectProductName(form, productService, result);
		}

		if (result.hasErrors()) {
			//登録画面へ
			return "adminProduct/productRegister";
		}

		//入力時に選択したジャンル番号に該当するジャンル名を取得
		form.setGenreName(productService.selectGenreName(form));

		//登録確認画面へ
		return "adminProduct/productRegisterCheck";

	}

	//登録確認画面から登録画面
	@RequestMapping(value = "/product-register-check", params = "backRegister")
	public String backProductRegister(@ModelAttribute("productRegisterForm") ProductRegisterForm form) {
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

		//登録完了画面へのリダイレクト処理
		return "redirect:/insert-end?finish";

	}

	//リダイレクト後に呼び出される処理メソッド
	@RequestMapping(value = "/insert-end", params = "finish")
	public String productRegisterFinish() {
		//登録完了画面へ
		return "adminProduct/productRegisterFinish";
	}

	//登録完了画面から登録画面
	@RequestMapping(value = "/product-register-finish", params = "selectProductRegister")
	public String returnProductRegister() {
		return "adminProduct/productRegister";
	}

	//登録完了画面からメニュー画面へ
	@RequestMapping(value = "/product-register-finish", params = "selectMenu")
	public String returnMenu(Model model) {
		String userName = this.getUserNameFromSession();

		model.addAttribute("userName", userName);

		return "user/adminUserMenu";

	}

	//メニューから商品修正削除一覧画面
	@RequestMapping(value = "/select-btn", params = "productCorrectDelete")
	public String productCorrectDeleteList(Model model) {
		List<ProductInfo> productList = productService.getProductListAll();

		model.addAttribute("productList", productList);

		return "adminProduct/productCorrectDeletetList";
	}

	//商品修正削除一覧画面からメニュー画面
	@RequestMapping(value = "back-product-list", params = "backAdminMenu")
	public String backAdminMenu(Model model) {
		String userName = this.getUserNameFromSession();

		model.addAttribute("userName", userName);

		return "user/adminUserMenu";
	}

	//商品修正削除一覧画面から修正画面
	@RequestMapping(value = "/product-correct-delete", params = "correct")
	public String ProductCorrect(@Param("productId") String productId, @Param("versionNumber") String versionNumber,
			@ModelAttribute("productCorrectForm") ProductCorrectForm form, Model model) {
		//リクエストから送られた商品IDに該当する商品情報を取得する
		ProductInfo info = productService.getIdMatchProductData(productId);

		//データベースから取得した商品情報をFormに格納
		form.setProductName(info.getProductName());
		form.setPrice(info.getPrice());
		form.setGenreName(info.getGenreName());
		form.setQuantity(info.getQuantity());

		session.setAttribute("versionNumber", versionNumber);

		session.setAttribute("productCorrectForm", form);

		model.addAttribute("productId", productId);

		return "adminProduct/productCorrect";
	}

	//商品修正画面から商品修正削除一覧画面
	@RequestMapping(value = "/product-correct", params = "backList")
	public String backProductCorrectDeleteList(Model model) {
		List<ProductInfo> productList = productService.getProductListAll();

		model.addAttribute("productList", productList);

		return "adminProduct/productCorrectDeletetList";
	}

	//商品修正画面から修正確認画面
	@RequestMapping(value = "/product-correct", params = "check")
	public String productCorrectCheck(@Validated @ModelAttribute("productCorrectForm") ProductCorrectForm form,
			BindingResult result) {
		if (result.hasErrors()) {
			return "adminProduct/productCorrect";
		}

		return "adminProduct/productCorrectCheck";

	}

	//修正確認画面から商品修正画面
	@RequestMapping(value = "/correct-check", params = "backProductCorrect")
	public String backProductCorrect(@ModelAttribute("productCorrectForm") ProductCorrectForm form) {

		return "adminProduct/productCorrect";
	}

	//修正確認画面から修正完了画面
	@RequestMapping(value = "/correct-check", params = "corret")
	public String correctFinish(@ModelAttribute("productCorrectForm") ProductCorrectForm form,
			SessionStatus sessionStatus) {
		//Integer versionNumber = this.getVersionNumberSession();

		productService.correctProductInfo(form);

		sessionStatus.setComplete();
		//修正完了画面へのリダイレクト処理
		return "redirect:/update-end?finish";
	}

	//リダイレクト後に呼び出される処理メソッド
	@RequestMapping(value = "/update-end", params = "finish")
	public String productCorrectFinish() {
		//修正完了画面へ
		return "adminProduct/productCorrectFinish";
	}

	//修正完了画面からメニュー画面
	@RequestMapping(value = "/product-correct-finish", params = "returnMenu")
	public String returnMenu() {

		return "user/generalUserMenu";

	}

	//修正完了画面から商品修正削除一覧画面
	@RequestMapping(value = "/product-correct-finish", params = "returnList")
	public String returnproductCorrectDeleteList(Model model) {
		List<ProductInfo> productList = productService.getProductListAll();

		model.addAttribute("productList", productList);

		return  "adminProduct/productCorrectDeletetList";
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
	 * httpセッションからversionNumberを取得する処理。
	 * @return バージョン番号
	 */
	private Integer getVersionNumberSession() {
		Integer versionNumber = (Integer) session.getAttribute("versionNumber");

		return versionNumber;
	}

}
