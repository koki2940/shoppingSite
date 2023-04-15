package com.example.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.domain.ProductInfo;
import com.example.domain.UserInfo;
import com.example.service.CartInfoDBService;
import com.example.service.UserDBService;
import com.example.web.alertcheck.AlertCheck;
import com.example.web.errorcheck.ErrorCheck;
import com.example.web.form.UserLoginForm;
import com.example.web.form.UserRegisterForm;

@Controller
@SessionAttributes({ "userRegisterForm", "productPurchaseForm" })
public class UserController {

	@Autowired
	private UserDBService userService;

	@Autowired
	private CartInfoDBService cartService;

	@Autowired
	private HttpSession session;

	@ModelAttribute("userLoginForm")
	public UserLoginForm setLoginForm() {
		return new UserLoginForm();
	}

	@ModelAttribute("userRegisterForm")
	public UserRegisterForm setRegisterForm() {
		return new UserRegisterForm();
	}

	//URL初回アクセス時
	@RequestMapping("/home")
	public String rogin() {
		//ログイン画面へ
		return "user/loginMenu";
	}

	//ログイン画面からユーザ登録画面
	@RequestMapping(value = "/register")
	public String userRegister() {
		//SpringSecurityデフォルトエラーメッセージを消すため
		session.removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");

		//ユーザー登録画面へ
		return "user/userRegister";
	}

	//ユーザー登録画面からユーザ登録確認画面
	@RequestMapping(value = "/user-register", params = "registerCheck")
	public String userCheck(@Validated @ModelAttribute("userRegisterForm") UserRegisterForm form,
			BindingResult result) {
		//エラーチェックを行うクラスをインスタンス化
		ErrorCheck error = new ErrorCheck();

		//ユーザー登録の入力チェック
		error.userRegisterErrorCheck(form, userService, result);

		if (result.hasErrors()) {
			//ユーザー登録画面へ
			return "user/userRegister";
		}

		//ユーザー登録確認画面へ
		return "user/userRegisterCheck";
	}

	//ユーザー登録画面からログイン画面
	@RequestMapping(value = "/user-register", params = "backloginMenu")
	public String backRogin(SessionStatus sessionStatus) {
		sessionStatus.setComplete();

		//ログイン画面へ
		return "user/loginMenu";
	}

	//ユーザー登録確認画面からユーザ登録完了画面
	@RequestMapping(value = "/user-register-check", params = "register")
	public String apply(@ModelAttribute("userRegisterForm") UserRegisterForm form) {
		UserInfo user = new UserInfo();

		BeanUtils.copyProperties(form, user);

		//データ登録を行うサービス処理を呼び出す
		userService.insertUser(user);

		return "redirect:/insertUser-end?finish";
	}

	//リダイレクト後に呼び出される処理メソッド
	@RequestMapping(value = "/insertUser-end", params = "finish")
	public String userApplyFinish(SessionStatus sessionStatus) {
		sessionStatus.setComplete();

		//ユーザー登録画面へ
		return "user/userRegisterFinish";
	}

	//ユーザー登録確認画面からユーザ登録画面へ
	@RequestMapping(value = "/user-register-check", params = "backuserRegister")
	public String backUserRegister() {
		//ユーザー登録画面へ
		return "user/userRegister";
	}

	//ユーザー登録完了画面からログイン画面へ
	@RequestMapping(value = "/user-register-finish", params = "loginMenu")
	public String loginMenu(SessionStatus sessionStatus) {
		sessionStatus.setComplete();

		//ログイン画面へ
		return "user/loginMenu";
	}

	//ログイン画面からメニュー画面へ
	@RequestMapping(value = "/login")
	public String menu(@Validated @ModelAttribute("userLoginForm") UserLoginForm form, BindingResult result,
			Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//ログインしたユーザー名を取得
		String userName = auth.getName();

		//ログインしたユーザー名を用いてDBからユーザー情報を取得する
		UserInfo userInfo = userService.getLoginSucceedUser(userName);

		//カート情報を保持するカートリスト作成
		List<ProductInfo> cartList = cartService.getCartInfo(userInfo.getUserId());

		AlertCheck alert = new AlertCheck();

		String alertMsg = alert.cartCheck(cartList);

		if(alertMsg != null) {
			model.addAttribute("alertMsg", alertMsg);

			Integer totalAmount = 0;

			//合計金額を計算
			for (ProductInfo productInfo : cartList) {
				totalAmount += productInfo.getPrice() * productInfo.getPurchaseQty();
			}

			session.setAttribute("totalAmount", totalAmount);
		}

		session.setAttribute("cartList", cartList);
		session.setAttribute("userId", userInfo.getUserId());
		session.setAttribute("authority", userInfo.getAuthority());
		session.setAttribute("giftAmount", userInfo.getGiftAmount());
		session.setAttribute("userName", userName);

		model.addAttribute("userName", userName);

		//ログインしたのが管理者だった場合
		if (userInfo.getAuthority() == 0) {
			//管理者用のメニュー画面
			return "user/adminUserMenu";
		}

		model.addAttribute("giftAmount", userInfo.getGiftAmount());
		model.addAttribute("cartList", cartList);

		//ユーザー用のメニュー画面へ
		return "user/generalUserMenu";
	}

	//ギフト券ボタン押下時
	@RequestMapping(value = "/header", params = "gift")
	public String giftCharge(Model model, SessionStatus sessionStatus) {
		int giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("cartList", cartList);

		session.removeAttribute("alertMsg");
		sessionStatus.setComplete();

		//ギフト券チャージ画面へ
		return "user/giftCharge";
	}

	//ギフト券チャージ画面からメニュー画面
	@RequestMapping(value = "/gift-charge", params = "menu")
	public String backMenu(Model model) {
		int giftAmount = this.getGiftAmountSession();
		String userName = this.getUserNameSession();
		List<ProductInfo> cartList = this.getCartListSession();

		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("userName", userName);
		model.addAttribute("cartList", cartList);

		//ユーザー用のメニュー画面へ
		return "user/generalUserMenu";
	}

	//ギフト券チャージ画面からチャージボタン押下時
	@RequestMapping(value = "/gift-charge", params = "charge")
	public String chargeFinish(@Param("chargeAmount") String chargeAmount, Model model) {
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		//エラーメッセージ格納リスト作成
		List<String> errorList = new ArrayList<String>();

		ErrorCheck error = new ErrorCheck();

		error.chargeAmountSelectCheck(chargeAmount, errorList);

		error.chargeAmountUpperLimit(chargeAmount, giftAmount, errorList);

		if (errorList.size() != 0) {
			model.addAttribute("giftAmount", giftAmount);
			model.addAttribute("errorList", errorList);

		} else {
			Integer userId = this.getUserIdSession();

			userService.upDateAddGiftAmount(Integer.parseInt(chargeAmount), userId);

			giftAmount = userService.selectGiftAmount(userId);

			session.setAttribute("giftAmount", giftAmount);

			model.addAttribute("giftAmount", giftAmount);
		}
		model.addAttribute("cartList", cartList);

		//ギフト券チャージ画面へ
		return "user/giftCharge";

	}

	//ログアウトボタン押下時
	@RequestMapping(value = "/logoff", params = "logout")
	public String logout(SessionStatus sessionStatus, HttpSession session, Model model) {
		sessionStatus.setComplete();
		session.invalidate();

		//ログイン画面へ
		return "user/loginMenu";
	}

	/**
	 * httpセッションからuserIdを取得する処理。
	 * @return ユーザーId
	 */
	private Integer getUserIdSession() {
		Integer userId = (Integer) session.getAttribute("userId");

		return userId;
	}

	/**
	 * httpセッションからuserNameを取得する処理。
	 * @return ユーザー名
	 */
	private String getUserNameSession() {
		String userName = (String) session.getAttribute("userName");

		return userName;
	}

	/**
	 * httpセッションからgiftAmountを取得する処理
	 * @return ギフト残高
	 */
	private Integer getGiftAmountSession() {
		Integer giftAmount = (Integer) session.getAttribute("giftAmount");

		return giftAmount;
	}

	/**
	 * httpセッションからauthorityを取得する処理
	 * @return 権限
	 */
	private Integer getAuthority() {
		Integer authority = (Integer) session.getAttribute("authority");

		return authority;
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
	 * @return 合計金額
	 */
	private Integer getTotalAmountSession() {
		Integer totalAmount = (Integer) session.getAttribute("totalAmount");

		return totalAmount;
	}

}
