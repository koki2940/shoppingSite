package com.example.web.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.ProductInfo;
import com.example.service.PurchaseHistoryDBService;

@Controller
public class ProductHistoryController {

	@Autowired
	private PurchaseHistoryDBService purchaseService;

	@Autowired
	private HttpSession session;

	//メニュー画面から購入履歴画面へ
	@RequestMapping(value = "/select-btn", params = "purchaseHistory")
	public String purchaseHistory(Model model) {
		Integer userId = this.getUserIdFromSession();
		String userName = this.getUserNameFromSession();
		Integer giftAmount = this.getGiftAmountSession();

		//ログインしたユーザのIDから購入履歴を取得する処理
		List<ProductInfo> list = purchaseService.selectPurchaseHistory(userId);

		//取得した購入日の情報を表示用に整える
		for (ProductInfo date : list) {
//			LocalDateTime localDateTime = LocalDateTime.parse(date.getDateTime());
//		String newDateTime = localDateTime.format(DateTimeFormatter.ofPattern("uuuu年M月d日(E)HH時mm分ss秒", Locale.JAPAN));
			date.setPurchaseDateTime(date.getDateTime());
		}

		model.addAttribute("purchaseHistoryList", list);
		model.addAttribute("userName", userName);
		model.addAttribute("giftAmount", giftAmount);

		//購入履歴画面へ
		return "history/purchaseHistoryList";
	}

	//購入履歴画面から削除確認画面
	@RequestMapping(value = "/purchase-history-list", params = "delete")
	public String historyDeleteCheck(@RequestParam("purchaseId") String purchaseId, Model model) {
		//リクエストから送られてきた購入IDに該当する履歴情報を取得する
		ProductInfo info = purchaseService.getIdMatchPurchaseHistory(purchaseId);

		//取得した購入日の情報を表示用に整える
//		LocalDate localDate = LocalDate.parse(info.getPurchaseDateTime());
//		String newDate = localDate.format(DateTimeFormatter.ofPattern("uuuu年M月d日(E)", Locale.JAPAN));
		info.setPurchaseDateTime(info.getDateTime());

		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("purchaseInfo", info);
		model.addAttribute("giftAmount", giftAmount);

		//削除確認画面へ
		return "history/historyDeleteCheck";

	}
	
	//購入履歴画面から購入確認画面
	@RequestMapping(value = "/purchase-history-list", params = "repurchase")
	public String rePurchase() {
		
		return "generalProduct/test";
	}
	//購入履歴画面からカート確認画面
	@RequestMapping(value = "/purchase-history-list", params = "cart")
	public String cart() {
		
		return "generalProduct/test";
	}

	//購入履歴画面からメニュー画面へ
	@RequestMapping(value = "purchase-history-list", params = "backMenu")
	public String deleteHistory(Model model) {
		String userName = this.getUserNameFromSession();
		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("userName", userName);
		model.addAttribute("giftAmount", giftAmount);

		//メニュー画面へ
		return "user/generalUserMenu";
	}

	//削除確認画面から購入履歴画面
	@RequestMapping(value = "/history-delete-check", params = "backpurchase")
	public String backPurchaseHistory(Model model) {
		Integer userId = this.getUserIdFromSession();
		String userName = this.getUserNameFromSession();
		Integer giftAmount = this.getGiftAmountSession();

		//ログインしたユーザのIDから購入履歴を取得
		List<ProductInfo> list = purchaseService.selectPurchaseHistory(userId);

		//取得した購入日の情報を表示用に整える
	for (ProductInfo date : list) {
//			LocalDate localDate = LocalDate.parse(date.getPurchaseDateTime());
//			String newDate = localDate.format(DateTimeFormatter.ofPattern("uuuu年M月d日(E)", Locale.JAPAN));
			date.setPurchaseDateTime(date.getDateTime());
	}

		model.addAttribute("purchaseHistoryList", list);
		model.addAttribute("userName", userName);
		model.addAttribute("giftAmount", giftAmount);

		//購入履歴画面へ
		return "history/purchaseHistoryList";

	}

	//削除確認画面から削除完了画面
	@RequestMapping(value = "/history-delete-check", params = "delete")
	public String deletePurchaseHistory(@RequestParam("purchaseId") String purchaseId, Model model) {
		//リクエストから送られてきた購入Idに該当する履歴情報を削除する処理
		purchaseService.deletePurchaseHistory(purchaseId);

		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("giftAmount", giftAmount);

		//削除完了画面へ
		return "history/historyDeleteFinish";
	}

	//削除完了画面からメニュー画面
	@RequestMapping(value = "/history-delete-finish", params = "selectMenu")
	public String returnMenu(Model model) {
		String userName = this.getUserNameFromSession();
		Integer giftAmount = this.getGiftAmountSession();

		model.addAttribute("userName", userName);
		model.addAttribute("giftAmount", giftAmount);

		//メニュー画面へ
		return "user/generalUserMenu";
	}

	//削除完了から購入履歴一覧
	@RequestMapping(value = "/history-delete-finish", params = "selectPurchaseHistory")
	public String returnPurchaseHistory(Model model) {
		Integer userId = this.getUserIdFromSession();
		Integer giftAmount = this.getGiftAmountSession();
		String userName = (String) session.getAttribute("userName");

		//ログインしたユーザのIDから購入履歴を取得
		List<ProductInfo> list = purchaseService.selectPurchaseHistory(userId);

		//取得した購入日の情報を表示用に整える
		for (ProductInfo date : list) {
//			LocalDate localDate = LocalDate.parse(date.getPurchaseDateTime());
//			String newDate = localDate.format(DateTimeFormatter.ofPattern("uuuu年M月d日(E)", Locale.JAPAN));
			date.setPurchaseDateTime(date.getDateTime());
		}

		model.addAttribute("purchaseHistoryList", list);
		model.addAttribute("userName", userName);
		model.addAttribute("giftAmount", giftAmount);

		//購入履歴画面へ
		return "history/purchaseHistoryList";
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

	private Integer getGiftAmountSession() {
		Integer giftAmount = (Integer)session.getAttribute("giftAmount");

		return giftAmount;
	}

}
