package com.example.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
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
import com.example.domain.User;
import com.example.service.UserDBService;
import com.example.web.error.ErrorCheck;
import com.example.web.form.UserLoginForm;
import com.example.web.form.UserRegisterForm;

@Controller
@SessionAttributes("userRegisterForm")
public class UserController {

	@Autowired
	private UserDBService userService;

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

	//ログイン画面(スタート時)
	@RequestMapping("/login")
	public String rogin() {
		//ログイン画面へ
		return "user/loginMenu";
	}

	//ログイン画面からユーザー登録画面
	@RequestMapping(value = "/login-menu", params = "userRegister")
	public String userRegister() {
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
		//データ登録に利用するドメインクラスをインスタンス化
		User user = new User();

		BeanUtils.copyProperties(form, user);

		//データ登録を行うサービス処理を呼び出す
		userService.insertUser(user);

		return "redirect:/insertUser-end?finish";
	}

	//リダイレクト後に呼び出される処理メソッド
	@RequestMapping(value = "/insertUser-end", params = "finish")
	public String userApplyFinish(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		//登録完了画面へ
		return "user/userRegisterFinish";
	}

	//ユーザー登録確認画面からユーザ登録画面へ
	@RequestMapping(value = "/user-register-check", params = "backuserRegister")
	public String backuserRegister() {
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
	@RequestMapping(value = "login-menu", params = "login")
	public String Menu(@Validated @ModelAttribute("userLoginForm") UserLoginForm form, BindingResult result,
			Model model) {
		//エラーチェックを行うクラスをインスタンス化
		ErrorCheck error = new ErrorCheck();

		//入力チェックとアカウントが存在するかをUserIdを取得して判定
		List<User> userList = error.loginErrorCheck(form, userService, result);

		if (result.hasErrors()) {
			//ログイン画面へ
			return "user/loginMenu";

		}

		//ショッピングカート用のリスト作成
		List<ProductInfo> cartList = new ArrayList<ProductInfo>();

		//カートリストをセッションに保持
		session.setAttribute("cartList", cartList);
		session.setAttribute("userId", userList.get(0).getUserId());
		session.setAttribute("authority", userList.get(0).getAuthority());
		session.setAttribute("giftAmount", userList.get(0).getGiftAmount());
		session.setAttribute("userName", form.getUserName());

		String userName = this.getUserNameFromSession();
		int giftAmount = this.getGiftAmountSession();

		model.addAttribute("userName", userName);
		model.addAttribute("giftAmount", giftAmount);

		//管理者か一般ユーザーかのチェックする
		if (userList.get(0).getAuthority() == 0) {
			return "user/adminUserMenu";
		}

		//メニュー画面へ
		return "user/generalUserMenu";
	}

	//ログアウトボタン押下時
	@RequestMapping(value = "/header", params = "logout")
	public String logout(SessionStatus sessionStatus, HttpSession session) {
		sessionStatus.setComplete();
		session.invalidate();

		//ログイン画面へ
		return "user/loginMenu";
	}

	//ギフト券ボタン押下時
	@RequestMapping(value = "/header", params = "gift")
	public String giftCharge(Model model) {
		int giftAmount = this.getGiftAmountSession();
		model.addAttribute("giftAmount", giftAmount);

		return "user/giftCharge";
	}

	//ギフト券チャージ画面からメニュー画面
	@RequestMapping(value = "/gift-charge", params = "menu")
	public String backMenu(Model model) {
		int giftAmount = this.getGiftAmountSession();
		String userName = this.getUserNameFromSession();
		int authority = this.getAuthority();

		model.addAttribute("giftAmount", giftAmount);
		model.addAttribute("userName", userName);

		//管理者ユーザーの場合
		if (authority == 0) {
			return "user/adminUserMenu";
		}

		return "user/generalUserMenu";
	}

	//ギフト券チャージ画面からチャージボタン押下時
	@RequestMapping(value = "/gift-charge", params = "charge")
	public String chageFinish(@Param("chargeAmount") String chargeAmount, Model model) {
		Integer giftAmount = this.getGiftAmountSession();

		//エラーメッセージ格納リスト作成
		List<String> errorList = new ArrayList<String>();

		ErrorCheck error = new ErrorCheck();

		error.chargeAmountSelectCheck(chargeAmount, errorList);

		error.chargeAmountUpperLimit(chargeAmount, giftAmount, errorList);

		if (errorList.size() != 0) {
			model.addAttribute("giftAmount", giftAmount);
			model.addAttribute("errorList", errorList);

		} else {

			Integer userId = this.getUserIdFromSession();

			userService.updateAddGiftAmount(Integer.parseInt(chargeAmount), userId);

			giftAmount = userService.selectGiftAmount(userId);

			session.setAttribute("giftAmount", giftAmount);

			model.addAttribute("giftAmount", giftAmount);
		}
		return "user/giftCharge";

	}

	/**
	 * httpセッションからuserIdを取得する処理。
	 * @return ユーザーId
	 */
	private Integer getUserIdFromSession() {
		Integer userId = (Integer) session.getAttribute("userId");

		return userId;
	}

	/**
	 * httpセッションからuserNameを取得する処理。
	 * @return ユーザー名
	 */
	private String getUserNameFromSession() {
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

}
