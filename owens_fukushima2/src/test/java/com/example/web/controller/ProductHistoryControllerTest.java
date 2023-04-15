package com.example.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.domain.ProductInfo;
import com.example.persistence.CartInfoMapper;
import com.example.persistence.ProductInfoMapper;
import com.example.persistence.PurchaseHistoryMapper;
import com.example.persistence.UserMapper;
import com.example.service.CartInfoDBService;
import com.example.service.ProductInfoDBService;
import com.example.service.PurchaseHistoryDBService;
import com.example.service.UserDBService;
import com.example.web.alertcheck.AlertCheck;
import com.example.web.errorcheck.ErrorCheck;
import com.example.web.form.ProductPurchaseForm;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration
@WebAppConfiguration //コンテキストを使う準備
@SpringBootTest
class ProductHistoryControllerTest extends ProductHistoryController {

	//MockMvcオブジェクト
	private MockMvc mockMvc;

	//Mock化するクラスを宣言
	@Mock
	private ProductInfoMapper productMapper;

	@Mock
	private CartInfoMapper cartInfoMapper;

	@Mock
	private PurchaseHistoryMapper purchaseHistoryMapper;

	@Mock
	private UserMapper userMapper;

	//Mock化されたクラスを利用するクラスの宣言
	@InjectMocks
	private ProductInfoDBService productService;

	@InjectMocks
	private CartInfoDBService cartService;

	@InjectMocks
	private PurchaseHistoryDBService purchaseService;

	@InjectMocks
	private UserDBService userService;

	//各テスト実行毎に実行する
	@BeforeEach
	void setup() {
		//Mockの初期化
		MockitoAnnotations.initMocks(this);

		//MockMvcオブジェクトにテスト対象メソッドを設定
		mockMvc = MockMvcBuilders.standaloneSetup(new ProductHistoryController()).build();

	}

	@Test
	void purchaseHistoryTest() throws Exception {
		Integer userId = this.getUserIdSession();
		String userName = this.getUserNameSession();
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		ProductInfo productInfo = new ProductInfo();
		productInfo.setPurchaseId(1);
		productInfo.setProductId(31);
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(200);
		productInfo.setPurchaseQty(1);
		productInfo.setPurchaseDateTime("2021/10/22 11:20:23");
		productInfo.setQuantity(59);
		productInfo.setDeleteFlag(1);

		List<ProductInfo> setPurchaseHistoryList = new ArrayList<ProductInfo>();

		//引数・戻り値の設定
		when(purchaseHistoryMapper.selectPurchaseHistory(1)).thenReturn(setPurchaseHistoryList);

		//実行
		List<ProductInfo> purchaseHistoryList = purchaseService.selectPurchaseHistory(1);

		mockMvc.perform(MockMvcRequestBuilders.post("/select-btn")
				.param("purchaseHistory", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「history/purchaseHistoryList」であることを確認
				.andExpect(view().name("history/purchaseHistoryList"));
		//modelに正しい変数が入れられているか
		//				.andExpect(model().attribute("purchaseHistoryList", purchaseHistoryList))
		//				.andExpect(model().attribute("userName", userName))
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("cartList", cartList)

	}

	//エラー発生
	@Test
	void historyDeleteCheckTest() throws Exception {
		//		Integer giftAmount = this.getGiftAmountSession();
		//		List<ProductInfo> cartList = this.getCartListSession();
		//
		//		ProductInfo setProductInfo = new ProductInfo();
		//
		//		setProductInfo.setPurchaseId(131);
		//		setProductInfo.setProductName("テスト商品");
		//		setProductInfo.setPrice(100);
		//		setProductInfo.setPurchaseQty(1);
		//		setProductInfo.setPurchaseDateTime("2021/10/14 11:45:50");
		//
		//		//引数・戻り値の設定
		//		when(purchaseHistoryMapper.selectIdMatchHistory("131")).thenReturn(setProductInfo);
		//
		//		ProductInfo productInfo = purchaseService.getIdMatchPurchaseHistory("131");

		mockMvc.perform(MockMvcRequestBuilders.post("/purchase-history-list")
				.param("delete", "")
				.param("purchaseId", "131"))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「history/historyDeleteCheck」であることを確認
				.andExpect(view().name("history/historyDeleteCheck"));
		//modelに正しい変数が入れられているか
		//				.andExpect(model().attribute("purchaseInfo", productInfo))
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("cartList", cartList)
	}

	@Test
	void deleteHistoryTest() throws Exception {
		Integer userId = this.getUserIdSession();
		String userName = this.getUserNameSession();
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		mockMvc.perform(MockMvcRequestBuilders.post("/purchase-history-list")
				.param("backMenu", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「user/generalUserMenu」であることを確認
				.andExpect(view().name("user/generalUserMenu"));
		//modelに正しい変数が入れられているか
		//				.andExpect(model().attribute("userName", userName))
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("cartList", cartList)

	}

	@Test
	void backPurchaseHistoryTest() throws Exception {
		Integer userId = this.getUserIdSession();
		String userName = this.getUserNameSession();
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		ProductInfo productInfo = new ProductInfo();
		productInfo.setPurchaseId(131);
		productInfo.setProductId(31);
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(100);
		productInfo.setPurchaseQty(1);
		productInfo.setPurchaseDateTime("2021/10/14 11:45:50");
		productInfo.setQuantity(59);
		productInfo.setDeleteFlag(0);

		List<ProductInfo> setPurchaseHistoryList = new ArrayList<ProductInfo>();

		setPurchaseHistoryList.add(productInfo);

		when(purchaseHistoryMapper.selectPurchaseHistory(1)).thenReturn(setPurchaseHistoryList);

		List<ProductInfo> purchaseHistoryList = purchaseService.selectPurchaseHistory(1);

		mockMvc.perform(MockMvcRequestBuilders.post("/history-delete-check", "/repurchase-check")
				.param("backHistoryList", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「history/purchaseHistoryList」であることを確認
				.andExpect(view().name("history/purchaseHistoryList"));
		//modelに正しい変数が入れられているか
		//				.andExpect(model().attribute("purchaseHistoryList", purchaseHistoryList))
		//				.andExpect(model().attribute("userName", userName))
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("cartList", cartList)
	}

	@Test
	void deletePurchaseHistoryTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		mockMvc.perform(MockMvcRequestBuilders.post("/history-delete-check")
				.param("delete", "")
				.param("purchaseId", "131"))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「history/historyDeleteFinish」であることを確認
				.andExpect(view().name("history/historyDeleteFinish"));
		//modelに正しい変数が入れられているか
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("cartList", cartList)
	}

	@Test
	void returnMenuTest() throws Exception {
		String userName = this.getUserNameSession();
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		mockMvc.perform(MockMvcRequestBuilders.post("/history-delete-finish")
				.param("selectMenu", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「user/generalUserMenu」であることを確認
				.andExpect(view().name("user/generalUserMenu"));
		//modelに正しい変数が入れられているか
		//		.andExpect(model().attribute("userName", userName))
		//		.andExpect(model().attribute("giftAmount", giftAmount))
		//		.andExpect(model().attribute("cartList", cartList)

	}

	@Test
	void returnPurchaseHistoryTest() throws Exception {
		Integer userId = this.getUserIdSession();
		Integer giftAmount = this.getGiftAmountSession();
		String userName = this.getUserNameSession();
		List<ProductInfo> cartList = this.getCartListSession();

		ProductInfo productInfo = new ProductInfo();
		productInfo.setPurchaseId(131);
		productInfo.setProductId(31);
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(100);
		productInfo.setPurchaseQty(1);
		productInfo.setPurchaseDateTime("2021/10/14 11:45:50");
		productInfo.setQuantity(59);
		productInfo.setDeleteFlag(0);

		List<ProductInfo> setPurchaseHistoryList = new ArrayList<ProductInfo>();

		setPurchaseHistoryList.add(productInfo);

		when(purchaseHistoryMapper.selectPurchaseHistory(1)).thenReturn(setPurchaseHistoryList);

		List<ProductInfo> purchaseHistoryList = purchaseService.selectPurchaseHistory(1);

		mockMvc.perform(MockMvcRequestBuilders.post("/history-delete-finish")
				.param("selectPurchaseHistory", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「history/purchaseHistoryList」であることを確認
				.andExpect(view().name("history/purchaseHistoryList"));
		//modelに正しい変数が入れられているか
		//		.andExpect(model().attribute("purchaseHistoryList", purchaseHistoryList))
		//		.andExpect(model().attribute("userName", userName))
		//		.andExpect(model().attribute("giftAmount", giftAmount))
		//		.andExpect(model().attribute("cartList", cartList)

	}

	@Test
	void repurcaseCheckTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		ProductPurchaseForm productPurchaseForm = new ProductPurchaseForm();

		AlertCheck alert = new AlertCheck();

		String alertMsg = alert.priceComparisonCheck(300, 200, "いろはす");

		ProductInfo productInfo = new ProductInfo();
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(200);
		productInfo.setQuantity(59);
		productInfo.setGenreName("食品・飲料");

		//引数・戻り値の設定
		when(productMapper.selectIdMatchProductInfo(31)).thenReturn(productInfo);

		//実行
		ProductInfo resultProductInfo = productService.getIdMatchProductData(31);

		MvcResult result = mockMvc.perform(post("/purchase-history-list")
				.param("repurchase", "")
				.param("productId", "31")
				.param("purchaseQty", "1")
				.param("price", "200"))

				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「history/repurchaseCheck」であることを確認
				.andExpect(view().name("history/repurchaseCheck"))
				//modelに正しい変数を詰められているか
				//				.andExpect(model().attribute("giftAmount", giftAmount))
				//				.andExpect(model().attribute("cartList", cartList))
				//				.andExpect(model().attribute("alertMsg", alertMsg))
				//				.andExpect(model().attribute("productPurchaseForm", productPurchaseForm))
				.andReturn();

		ProductPurchaseForm resultForm = (ProductPurchaseForm) result.getModelAndView().getModel()
				.get("productPurchaseForm");

		//formの値の検証
		//		assertEquals(resultForm.getProductName(), resultProductInfo.getProductName());
		//		assertEquals(resultForm.getPrice(), resultProductInfo.getPrice());
		//		assertEquals(resultForm.getGenreName(), resultProductInfo.getGenreName());
		//		assertEquals(resultForm.getTotalAmount(), resultForm.getPrice() * resultForm.getPurchaseQty());
		//		assertEquals(resultForm.getPurchaseQty(), 1);

	}

	@Test
	void repurchaseFinishTest() throws Exception {
		//		Integer userId = this.getUserIdFromSession();
		//		Integer giftAmount = this.getGiftAmountSession();
		//		List<ProductInfo> cartList = this.getCartListSession();

		mockMvc.perform(MockMvcRequestBuilders.post("/repurchase-check")
				.param("purchase", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「generalProduct/productPurchaseFinish」であることを確認
				.andExpect(view().name("generalProduct/productPurchaseFinish"));
		//modelに正しい変数が入れられているか
		//				.andExpect(model().attribute("userId", userId))
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("cartList", cartList)
		//				.andExpect(request().sessionAttribute("giftAmount", giftAmount))

	}

	@Test
	void InputShoppingCartTest() throws Exception {
		//		Integer userId = this.getUserIdFromSession();
		//		Integer giftAmount = this.getGiftAmountSession();
		//		List<ProductInfo> cartList = this.getCartListSession();

		//Integer totalAmount = 5000;

		//		AlertCheck alert = new AlertCheck();
		//
		//		String priceAlertMsg = alert.priceComparisonCheck(300, 200, "いろはす");

		mockMvc.perform(MockMvcRequestBuilders.post("/purchase-history-list")
				.param("cart", "")
				.param("productId", "31")
				.param("purchaseQty", "200")
				.param("price", "1"))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「generalProduct/productPurchaseFinish」であることを確認
				.andExpect(view().name("generalProduct/shoppingCart"));
		//.andExpect(model().attribute("giftAmount", giftAmount))
		//.andExpect(model().attribute("cartList", cartList))
		//.andExpect(model().attribute("priceAlertMsg", priceAlertMsg))
		//.andExpect(model().attribute("totalAmount", totalAmount))
		//.andExpect(request().sessionAttribute("totalAmount", totalAmount))
		//.andExpect(request().sessionAttribute("cartList", cartList))
		//.andExpect(request().sessionAttribute("priceAlertMsg", priceAlertMsg))

	}

	@Test
	void AlertCheckクラスのpriceComparisonCheckメソッドのメッセージが有効な場合のテスト() {
		final String expectedAlertMsg = "「 いろはす 」の価格が更新されています。";
		final Integer purchasePrice = 300;
		final Integer productPrice = 200;
		final String  productName = "いろはす";

		AlertCheck alert = new AlertCheck();

		String actualAlertMsg = alert.priceComparisonCheck(purchasePrice, productPrice, productName);

		//値の検証
		assertEquals(actualAlertMsg, expectedAlertMsg);

	}

	@Test
	void AlertCheckクラスのpriceComparisonCheckメソッドのメッセージが無効な場合のテスト() {
		final String expectedAlertMsg = null;
		final Integer purchasePrice = 200;
		final Integer productPrice = 200;
		final String  productName = "いろはす";

		AlertCheck alert = new AlertCheck();

		String actualAlertMsg = alert.priceComparisonCheck(purchasePrice, productPrice, productName);

		//値の検証
		assertEquals(actualAlertMsg, expectedAlertMsg);

	}

	@Test
	void ErrorCheckクラスのpurcharseQtyOverメソッドのメッセージが有効な場合のテスト() {
		final int expectedListSize = 1;
		final Integer quantity = 30;
		final Integer purchaseQty = 60;
		final String productName = "テスト商品";

		List<String> errorList = new ArrayList<String>();

		ErrorCheck error = new ErrorCheck();

		error.purcharseQtyOver(quantity, purchaseQty, productName, errorList);

		int actualListSize = errorList.size();

		assertEquals(actualListSize, expectedListSize);

	}

	@Test
	void ErrorCheckクラスのpurcharseQtyOverメソッドのメッセージが無効な場合のテスト() {
		final int expectedListSize = 0;
		final Integer quantity = 100;
		final Integer purchaseQty = 60;
		final String productName = "テスト商品";

		List<String> errorList = new ArrayList<String>();

		ErrorCheck error = new ErrorCheck();

		error.purcharseQtyOver(quantity, purchaseQty, productName, errorList);

		int actualListSize = errorList.size();

		assertEquals(actualListSize, expectedListSize);

	}

	@Test
	void ErrorCheckクラスのcompareAmountメソッドのメッセージが有効な場合のテスト() {
		final int expectedListSize = 1;
		final Integer totalAmount = 4000;
		final Integer giftAmount = 3000;

		List<String> errorList = new ArrayList<String>();

		ErrorCheck error = new ErrorCheck();

		error.compareAmounts(totalAmount, giftAmount, errorList);

		int actualListSize = errorList.size();

		assertEquals(actualListSize, expectedListSize);

	}

	@Test
	void ErrorCheckクラスのcompareAmountメソッドのメッセージが無効な場合のテスト() {
		final int expectedListSize = 0;
		final Integer totalAmount = 4000;
		final Integer giftAmount = 4000;

		List<String> errorList = new ArrayList<String>();

		ErrorCheck error = new ErrorCheck();

		error.compareAmounts(totalAmount, giftAmount, errorList);

		int actualListSize = errorList.size();

		assertEquals(actualListSize, expectedListSize);

	}

	@Test
	void PurchaseHistoryDBServiceクラスのselectPurchaseHistoryメソッドの正常系テスト() {
		//期待する戻り値を設定
		ProductInfo productInfo = new ProductInfo();

		productInfo.setPurchaseId(131);
		productInfo.setProductId(31);
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(100);
		productInfo.setPurchaseQty(1);
		productInfo.setPurchaseDateTime("2021/10/14 11:45:50");
		productInfo.setQuantity(56);
		productInfo.setDeleteFlag(0);

		List<ProductInfo> purchaseHistoryList = new ArrayList<ProductInfo>();

		purchaseHistoryList.add(productInfo);

		//引数・戻り値の設定
		when(purchaseHistoryMapper.selectPurchaseHistory(1)).thenReturn(purchaseHistoryList);

		//実行
		List<ProductInfo> expectedPurchaseHistoryList = purchaseService.selectPurchaseHistory(1);

		//検証
		assertAll(
				() -> verify(purchaseHistoryMapper, times(1)).selectPurchaseHistory(1),
				() -> assertEquals(131, expectedPurchaseHistoryList.get(0).getPurchaseId()),
				() -> assertEquals(31, expectedPurchaseHistoryList.get(0).getProductId()),
				() -> assertEquals("テスト商品", expectedPurchaseHistoryList.get(0).getProductName()),
				() -> assertEquals(100, expectedPurchaseHistoryList.get(0).getPrice()),
				() -> assertEquals(1, expectedPurchaseHistoryList.get(0).getPurchaseQty()),
				() -> assertEquals("2021/10/14 11:45:50", expectedPurchaseHistoryList.get(0).getDateTime()),
				() -> assertEquals(56, expectedPurchaseHistoryList.get(0).getQuantity()),
				() -> assertEquals(0, expectedPurchaseHistoryList.get(0).getDeleteFlag()));
	}

	@Test
	void PurchaseHistoryDBServiceクラスのselectPurchaseHistoryメソッドの異常系テスト() {
		//期待する戻り値を設定
		ProductInfo productInfo = new ProductInfo();

		productInfo.setPurchaseId(null);
		productInfo.setProductId(null);
		productInfo.setProductName(null);
		productInfo.setPrice(null);
		productInfo.setPurchaseQty(null);
		productInfo.setPurchaseDateTime(null);
		productInfo.setQuantity(null);
		productInfo.setDeleteFlag(null);

		List<ProductInfo> purchaseHistoryList = new ArrayList<ProductInfo>();

		purchaseHistoryList.add(productInfo);

		//引数・戻り値の設定
				when(purchaseHistoryMapper.selectPurchaseHistory(111)).thenReturn(purchaseHistoryList);

//				//実行
//				List<ProductInfo> expectedPurchaseHistoryList = purchaseService.selectPurchaseHistory(111);
//
//				//検証
//				assertAll(
//						() -> verify(purchaseHistoryMapper, times(1)).selectPurchaseHistory(111),
//						() -> assertEquals(null, expectedPurchaseHistoryList.get(0).getPurchaseId()),
//						() -> assertEquals(null, expectedPurchaseHistoryList.get(0).getProductId()),
//						() -> assertEquals(null, expectedPurchaseHistoryList.get(0).getProductName()),
//						() -> assertEquals(null, expectedPurchaseHistoryList.get(0).getPrice()),
//						() -> assertEquals(null, expectedPurchaseHistoryList.get(0).getPurchaseQty()),
//						() -> assertEquals(null, expectedPurchaseHistoryList.get(0).getDateTime()),
//						() -> assertEquals(null, expectedPurchaseHistoryList.get(0).getQuantity()),
//						() -> assertEquals(null, expectedPurchaseHistoryList.get(0).getDeleteFlag()));
	}

	@Test
	void PurchaseHistoryDBServiceクラスのgetIdMatchPurchaseHistoryメソッドの正常系テスト() {
		final String purchaseId = "131";
		//期待する戻り値を設定
		ProductInfo productInfo = new ProductInfo();

		productInfo.setPurchaseId(131);
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(100);
		productInfo.setPurchaseQty(1);
		productInfo.setPurchaseDateTime("2021/10/14 11:45:50");

		//引数・戻り値の設定
		when(purchaseHistoryMapper.selectIdMatchPurchaseHistory(purchaseId)).thenReturn(productInfo);

		//実行
		ProductInfo expectedProductInfo = purchaseService.getIdMatchPurchaseHistory(purchaseId);

		//検証
		assertAll(
				() -> verify(purchaseHistoryMapper, times(1)).selectIdMatchPurchaseHistory(purchaseId),
				() -> assertEquals(131, expectedProductInfo.getPurchaseId()),
				() -> assertEquals("テスト商品", expectedProductInfo.getProductName()),
				() -> assertEquals(100, expectedProductInfo.getPrice()),
				() -> assertEquals(1, expectedProductInfo.getPurchaseQty()),
				() -> assertEquals("2021/10/14 11:45:50", expectedProductInfo.getDateTime()));
	}

	@Test
	void PurchaseHistoryDBServiceクラスのgetIdMatchPurchaseHistoryメソッドの異常系テスト() {
		final String purchaseId = "1310";

		//期待する戻り値を設定
		ProductInfo productInfo = new ProductInfo();

		productInfo.setPurchaseId(null);
		productInfo.setProductName(null);
		productInfo.setPrice(null);
		productInfo.setPurchaseQty(null);
		productInfo.setPurchaseDateTime(null);

		//引数・戻り値の設定
		when(purchaseHistoryMapper.selectIdMatchPurchaseHistory(purchaseId)).thenReturn(productInfo);

		//実行
		ProductInfo expectedProductInfo = purchaseService.getIdMatchPurchaseHistory(purchaseId);

		//検証
		//		assertAll(
		//				() -> verify(purchaseHistoryMapper, times(1)).selectIdMatchHistory(purchaseId),
		//				() -> assertEquals(null, expectedProductInfo.getPurchaseId()),
		//				() -> assertEquals(null, expectedProductInfo.getProductName()),
		//				() -> assertEquals(null, expectedProductInfo.getPrice()),
		//				() -> assertEquals(null, expectedProductInfo.getPurchaseQty()),
		//				() -> assertEquals(null, expectedProductInfo.getDateTime()));
	}

	@Test
	void ProductInfoDBServiceクラスのgetIdMatchProductDataメソッドの正常系テスト() {
		final Integer productId = 31;
		//期待する戻り値を設定
		ProductInfo productInfo = new ProductInfo();
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(200);
		productInfo.setQuantity(59);
		productInfo.setGenreName("食品・飲料");

		//引数・戻り値の設定
		when(productMapper.selectIdMatchProductInfo(productId)).thenReturn(productInfo);

		//実行
		ProductInfo expectedProductInfo = productService.getIdMatchProductData(productId);

		//検証
		assertAll(
				() -> verify(productMapper, times(1)).selectIdMatchProductInfo(productId),
				() -> assertEquals("テスト商品", expectedProductInfo.getProductName()),
				() -> assertEquals(200, expectedProductInfo.getPrice()),
				() -> assertEquals(59, expectedProductInfo.getQuantity()),
				() -> assertEquals("食品・飲料", expectedProductInfo.getGenreName()));

	}

	@Test
	void ProductInfoDBServiceクラスのgetIdMatchProductDataメソッドの異常系テスト() {
		final Integer productId = 310;
		//期待する戻り値を設定
		ProductInfo productInfo = new ProductInfo();
		productInfo.setProductName(null);
		productInfo.setPrice(null);
		productInfo.setQuantity(null);
		productInfo.setGenreName(null);

		//引数・戻り値の設定
		when(productMapper.selectIdMatchProductInfo(productId)).thenReturn(productInfo);

		//実行
		ProductInfo expectedProductInfo = productService.getIdMatchProductData(productId);

		//検証
		assertAll(
				() -> verify(productMapper, times(1)).selectIdMatchProductInfo(productId),
				() -> assertEquals(null, expectedProductInfo.getProductName()),
				() -> assertEquals(null, expectedProductInfo.getPrice()),
				() -> assertEquals(null, expectedProductInfo.getQuantity()),
				() -> assertEquals(null, expectedProductInfo.getGenreName()));

	}

	@Test
	void ProductInfoDBServiceクラスのgetIdMatchQuantityメソッドの正常系テスト() {
		final Integer productId = 31;
		final Integer actualQuantity = 59;
		//期待する戻り値を設定
		final Integer quantity = 59;

		//引数・戻り値の設定
		when(productMapper.selectIdMatchQuantity(productId)).thenReturn(quantity);

		//実行
		Integer expectedQuantity = productService.getIdMatchQuantity(productId);

		// 検証
		assertAll(
				() -> verify(productMapper, times(1)).selectIdMatchQuantity(productId),
				() -> assertEquals(actualQuantity, expectedQuantity));

	}

	@Test
	void ProductInfoDBServiceクラスのgetIdMatchQuantityメソッドの異常系テスト() {
		final Integer productId = 200;
		final Integer actualQuantity = null;
		//期待する戻り値を設定
		final Integer quantity = null;

		//引数・戻り値の設定
		when(productMapper.selectIdMatchQuantity(productId)).thenReturn(quantity);

		//実行
		Integer expectedQuantity = productService.getIdMatchQuantity(productId);

		// 検証
		assertAll(
				() -> verify(productMapper, times(1)).selectIdMatchQuantity(productId),
				() -> assertEquals(actualQuantity, expectedQuantity));

	}

	@Test
	void ProductInfoDBServiceクラスのpurchaseProductUpDateCheckメソッドのテスト() {
		final int actualCnt = 1;
		//期待する戻り値を設定
		final int cnt = 1;

		ProductInfo productInfo = new ProductInfo();

		productInfo.setProductName("テスト商品");
		productInfo.setPrice(100);

		//引数・戻り値の設定
		when(productMapper.selectPurchaseProductCount(productInfo)).thenReturn(cnt);

		//実行
		Integer expectedCnt = productService.purchaseProductUpDateCheck(productInfo);

		// 検証
		assertAll(
				() -> verify(productMapper, times(1)).selectPurchaseProductCount(productInfo),
				() -> assertEquals(actualCnt, expectedCnt));

	}

	@Test
	void ProductInfoDBServiceクラスのgetLatestPriceメソッドの正常系テスト() {
		final Integer productId = 31;
		final Integer actualLatestPrice = 200;
		//期待する戻り値を設定
		Integer latestPrice = 200;

		//引数・戻り値の設定
		when(productMapper.selectLatestPrice(productId)).thenReturn(latestPrice);

		//実行
		Integer expectedLatestPrice = productService.getLatestPrice(productId);

		// 検証
		assertAll(
				() -> verify(productMapper, times(1)).selectLatestPrice(productId),
				() -> assertEquals(actualLatestPrice, expectedLatestPrice));

	}

	@Test
	void ProductInfoDBServiceクラスのgetLatestPriceメソッドの異常系テスト() {
		final Integer productId = 310;
		final Integer actualLatestPrice = null;
		//期待する戻り値を設定
		final Integer latestPrice = null;

		//引数・戻り値の設定
		when(productMapper.selectLatestPrice(productId)).thenReturn(latestPrice);

		//実行
		Integer expectedlLatestPrice = productService.getLatestPrice(productId);

		// 検証
		assertAll(
				() -> verify(productMapper, times(1)).selectLatestPrice(productId),
				() -> assertEquals(actualLatestPrice, expectedlLatestPrice));

	}

	@Test
	void ProductInfoDBServiceクラスのgetIdMatchInCartInfoメソッドの正常系テスト() {
		final Integer productId = 31;
		//期待する戻り値を設定
		ProductInfo productInfo = new ProductInfo();
		productInfo.setProductId(31);
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(200);

		//引数・戻り値の設定
		when(productMapper.selectInCartInfo(productId)).thenReturn(productInfo);

		//実行
		ProductInfo expectedProductInfo = productService.getIdMatchInCartInfo(productId);

		//検証
		assertAll(
				() -> verify(productMapper, times(1)).selectInCartInfo(productId),
				() -> assertEquals(31, expectedProductInfo.getProductId()),
				() -> assertEquals("テスト商品", expectedProductInfo.getProductName()),
				() -> assertEquals(200, expectedProductInfo.getPrice()));
	}

	@Test
	void ProductInfoDBServiceクラスのgetIdMatchInCartInfoメソッドの異常系テスト() {
		final Integer productId = 310;
		//期待する戻り値を設定
		ProductInfo productInfo = new ProductInfo();
		productInfo.setProductId(null);
		productInfo.setProductName(null);
		productInfo.setPrice(null);

		//引数・戻り値の設定
		when(productMapper.selectInCartInfo(productId)).thenReturn(productInfo);

		//実行
		ProductInfo expectedProductInfo = productService.getIdMatchInCartInfo(productId);

		//検証
		assertAll(
				() -> verify(productMapper, times(1)).selectInCartInfo(productId),
				() -> assertEquals(null, expectedProductInfo.getProductId()),
				() -> assertEquals(null, expectedProductInfo.getProductName()),
				() -> assertEquals(null, expectedProductInfo.getPrice()));
	}

	@Test
	void CartInfoDBServiceクラスのgetIdMatchCartInfoメソッドのテスト() {
		final Integer productId = 31;
		final Integer userId = 1;
		//期待する戻り値を設定
		ProductInfo productInfo = new ProductInfo();
		productInfo.setCartId(1);
		productInfo.setProductId(31);
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(200);
		productInfo.setPurchaseQty(1);

		//引数・戻り値の設定
		when(cartInfoMapper.selectIdMatchCartInfo(productId, userId)).thenReturn(productInfo);

		//実行
		ProductInfo expectedProductInfo = cartService.getIdMatchCartInfo(productId, userId);

		//検証
		assertAll(
				() -> verify(cartInfoMapper, times(1)).selectIdMatchCartInfo(productId, userId),
				() -> assertEquals(1, expectedProductInfo.getCartId()),
				() -> assertEquals(31, expectedProductInfo.getProductId()),
				() -> assertEquals("テスト商品", expectedProductInfo.getProductName()),
				() -> assertEquals(200, expectedProductInfo.getPrice()),
				() -> assertEquals(1, expectedProductInfo.getPurchaseQty()));

	}

	@Test
	void PurchaseHistoryDBServiceクラスのdeletePurchaseHistoryメソッドが呼び出されているかのテスト() {
		final String purchaseId = "131";

		purchaseService.deletePurchaseHistory(purchaseId);

		verify(purchaseHistoryMapper, times(1)).deletePurchaseHistory(purchaseId);
	}


	@Test
	void PurchaseHistoryDBServiceクラスのinsertPurchaseHistoryメソッドが呼び出されているかのテスト() {
		final Integer userId = 1;
		ProductPurchaseForm productPurchaseForm = new ProductPurchaseForm();

		purchaseService.insertPurchaseHistory(productPurchaseForm, userId);

		verify(purchaseHistoryMapper, times(1)).insertPurchaseProductInfo(productPurchaseForm, userId);

	}

	@Test
	void CartInfoDBServiceクラスのcartInfoUpdateメソッドが呼び出されているかのテスト() {
		final Integer price = 200;
		final Integer purchaseQty = 1;
		final Integer productId = 31;
		final Integer userId = 1;

		cartService.cartInfoUpdate(price, purchaseQty, productId, userId);

		verify(cartInfoMapper, times(1)).upDateCartInfo(price, purchaseQty, productId, userId);
	}

	@Test
	void CartInfoDBServiceクラスのcartInfoRegisterメソッドが呼び出されているかのテスト() {
		final Integer userId = 1;

		ProductInfo productInfo = new ProductInfo();

		cartService.cartInfoRegister(userId, productInfo);

		verify(cartInfoMapper, times(1)).insertCartInfo(userId, productInfo);
	}

	@Test
	void ProductInfoDBServiceクラスのupDateSubstractQuantityメソッドが呼び出されているかのテスト() {
		ProductInfo productInfo = new ProductInfo();

		productService.upDateSubstractQuantity(productInfo);

		verify(productMapper, times(1)).upDateSubstractQuantity(productInfo);
	}

	@Test
	void UserDBServiceクラスのupDateSubstractGiftAmountメソッドが呼び出されているかのテスト() {
		final Integer userId = 1;

		ProductPurchaseForm productPurchaseForm = new ProductPurchaseForm();

		userService.upDateSubstractGiftAmount(productPurchaseForm.getTotalAmount(), userId);

		verify(userMapper, times(1)).upDateSubstractGiftAmount(productPurchaseForm.getTotalAmount(), userId);

	}

	private Integer getGiftAmountSession() {
		//セッションに入れる値を設定
		Integer setGiftAmount = 3000;

		MockHttpSession mockSession = new MockHttpSession();

		mockSession.setAttribute("giftAmount", setGiftAmount);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(mockSession);

		Integer giftAmount = (Integer) mockSession.getAttribute("giftAmount");

		return giftAmount;
	}

	private List<ProductInfo> getCartListSession() {
		//セッションに入れるリストを作成
		List<ProductInfo> setCartList = new ArrayList<ProductInfo>();

		ProductInfo productInfo = new ProductInfo();

		productInfo.setProductName("テスト商品");

		setCartList.add(productInfo);

		MockHttpSession mockSession = new MockHttpSession();

		mockSession.setAttribute("cartList", setCartList);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(mockSession);

		List<ProductInfo> cartList = (List<ProductInfo>) mockSession.getAttribute("cartList");

		return cartList;

	}

	private Integer getUserIdSession() {
		//セッションに入れる値を設定
		Integer setUserId = 1;

		MockHttpSession mockSession = new MockHttpSession();

		mockSession.setAttribute("userId", setUserId);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(mockSession);

		Integer userId = (Integer) mockSession.getAttribute("userId");

		return userId;
	}

	private String getUserNameSession() {
		//セッションに入れる値を設定
		String setUserName = "koki";

		MockHttpSession mockSession = new MockHttpSession();

		mockSession.setAttribute("userName", setUserName);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(mockSession);

		String userName = (String) mockSession.getAttribute("userName");

		return userName;
	}

}
