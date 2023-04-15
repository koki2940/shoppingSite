package com.example.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

import com.example.domain.ProductInfo;
import com.example.persistence.CartInfoMapper;
import com.example.persistence.ProductInfoMapper;
import com.example.persistence.PurchaseHistoryMapper;
import com.example.persistence.UserMapper;
import com.example.service.CartInfoDBService;
import com.example.service.ProductInfoDBService;
import com.example.service.PurchaseHistoryDBService;
import com.example.service.UserDBService;
import com.example.web.errorcheck.ErrorCheck;
import com.example.web.form.ProductPurchaseForm;
import com.example.web.form.ProductSearchForm;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration
@WebAppConfiguration //コンテキストを使う準備
@SpringBootTest
class GeneralProductControllerTest extends GeneralProductController {

	//MockMvcオブジェクト
	private MockMvc mockMvc;

	//Mock化するクラスを宣言
	@Mock
	private PurchaseHistoryMapper purchaseHistoryMapper;

	@Mock
	private ProductInfoMapper productMapper;

	@Mock
	private UserMapper userMapper;

	@Mock
	private CartInfoMapper cartInfoMapper;

	//Mock化されたクラスを利用するクラスの宣言
	@InjectMocks
	private PurchaseHistoryDBService purchaseHistoryService;

	@InjectMocks
	private ProductInfoDBService productService;

	@InjectMocks
	private UserDBService userService;

	@InjectMocks
	private CartInfoDBService cartInfoService;

	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);

		//リクエストを取得できるように設定
		HttpServletRequest request;

		request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletWebRequest(request));

		//MockMvcオブジェクトにテスト対象メソッドを設定
		mockMvc = MockMvcBuilders.standaloneSetup(new GeneralProductController()).build();

	}

	@Test
	void productSearchTest() throws Exception {
		String userName = this.getUserNameSession();
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		mockMvc.perform(MockMvcRequestBuilders.post("/select-btn")
				.param("productSearch", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「generalProduct/productSearch」であることを確認
				.andExpect(view().name("generalProduct/productSearch"));
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("cartList", cartList))
		//				.andExpect(model().attribute("userName", userName))

	}

	//ValidationError
	@Test
	void productListTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/product-search")
				.param("search", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「generalProduct/productList」であることを確認
				.andExpect(view().name("generalProduct/productList"));
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("cartList", cartList))
		//				.andExpect(model().attribute("userName", userName))
	}

	@Test
	void backMenuTest() throws Exception {
		String userName = this.getUserNameSession();
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		mockMvc.perform(MockMvcRequestBuilders.post("/product-search")
				.param("back", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「user/generalUserMenu」であることを確認
				.andExpect(view().name("user/generalUserMenu"));
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("cartList", cartList))
		//				.andExpect(model().attribute("userName", userName))

	}

	@Test
	void backProductSearchTest() throws Exception {
		ProductSearchForm form = (ProductSearchForm) session.getAttribute("productSearchForm");

		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		mockMvc.perform(MockMvcRequestBuilders.post("/back-product-list")
				.param("backProductSearch", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「generalProduct/productSearch」であることを確認
				.andExpect(view().name("generalProduct/productSearch"));
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("cartList", cartList))
		//				.andExpect(model().attribute("productSearchForm", form))

	}

	@Test
	void productDetailTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();
		List<ProductInfo> productList = (List<ProductInfo>) session.getAttribute("productList");

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/product-detail")
				.param("detail", "")
				.param("productId", "31"))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「generalProduct/productDetail」であることを確認
				.andExpect(view().name("generalProduct/productDetail"))
				.andReturn();
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("cartList", cartList))
		//				.andExpect(model().attribute("productSearchForm", form))
		//				.andExpect(request().sessionAttribute("productId", productId))

		// ここでmodelに詰められたformの値を取得
		//ProductPurchaseForm resultForm = (ProductPurchaseForm) result.getModelAndView().getModel().get("productPurchaseForm");

		//formの値の検証
		//		assertEquals(resultForm.getProductName(), "テスト商品");
		//		assertEquals(resultForm.getPrice(), 200);
		//		assertEquals(resultForm.getGenreName(), "食品・飲料");
		//		assertEquals(resultForm.getPurchaseQty(), 1);
	}

	@Test
	void backproductListTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		ProductInfo productInfo = new ProductInfo();

		productInfo.setProductId(31);
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(200);
		productInfo.setQuantity(56);
		productInfo.setVersionNumber(29);
		productInfo.setGenreName("食品・飲料");

		List<ProductInfo> productList = new ArrayList<ProductInfo>();

		productList.add(productInfo);

		mockMvc.perform(MockMvcRequestBuilders.post("/product-purchase")
				.param("backProductList", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「generalProduct/productList」であることを確認
				.andExpect(view().name("generalProduct/productList"));
		//		                .andExpect(model().attribute("productList", productList))
		//						.andExpect(model().attribute("giftAmount", giftAmount))
		//						.andExpect(model().attribute("cartList", cartList))
		//						.andExpect(request().sessionAttribute("productList", productList))

	}

	//ValidationError
	@Test
	void purchaseCheckTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/product-purchase")
				.param("purchase", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「generalProduct/productPurchaseCheck」であることを確認
				.andExpect(view().name("generalProduct/productPurchaseCheck"))
				//						.andExpect(model().attribute("giftAmount", giftAmount))
				//						.andExpect(model().attribute("cartList", cartList))
				.andReturn();

		// ここでmodelに詰められたformの値を取得
		//	ProductPurchaseForm resultForm = (ProductPurchaseForm) result.getModelAndView().getModel().get("productPurchaseForm");

		//formの値の検証
		//	assertEquals(resultForm.getTotalAmount(), 200);

	}

	@Test
	void backProductDetailTest() throws Exception {
		//セッションにセットする値を設定a
		Integer setProductId = 31;

		MockHttpSession mockSession = new MockHttpSession();

		mockSession.setAttribute("productId", setProductId);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(mockSession);

		Integer productId = (Integer) mockSession.getAttribute("productId");
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/product-purchase-finish")
				.param("backDetail", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「generalProduct/productDetail」であることを確認
				.andExpect(view().name("generalProduct/productDetail"))
				//						.andExpect(model().attribute("productId", productId))
				//						.andExpect(model().attribute("giftAmount", giftAmount))
				//						.andExpect(model().attribute("cartList", cartList))
				.andReturn();

		// ここでmodelに詰められたformの値を取得
		//		ProductPurchaseForm resultForm = (ProductPurchaseForm) result.getModelAndView().getModel().get("productPurchaseForm");
		//
		//		//formの値の検証
		//		assertEquals(resultForm.getProductName(), "テスト商品");
		//		assertEquals(resultForm.getPrice(), 200);
		//		assertEquals(resultForm.getGenreName(), "食品・飲料");
		//		assertEquals(resultForm.getQuantity(), 56);
	}

	@Test
	void productPurchaseFinishTest() throws Exception {
		Integer userId = this.getUserIdSession();
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		List<String> errorList = new ArrayList<String>();

		errorList.add("aa");

		mockMvc.perform(MockMvcRequestBuilders.post("/product-purchase-finish")
				.param("purchase", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「generalProduct/productPurchaseFinish」であることを確認
				.andExpect(view().name("generalProduct/productPurchaseFinish"));

		//.andExpect(model().attribute("giftAmount", giftAmount))
		//.andExpect(model().attribute("cartList", cartList))
	}

	@Test
	void returnProductSearchTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		mockMvc.perform(MockMvcRequestBuilders.post("/select-btn")
				.param("selectProductSearch", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「generalProduct/productSearch」であることを確認
				.andExpect(view().name("generalProduct/productSearch"));
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("cartList", cartList));

	}

	@Test
	void returnPurchaseHistoryListTest() throws Exception {
		ProductInfo productInfo = new ProductInfo();
		List<ProductInfo> list = new ArrayList<ProductInfo>();

		productInfo.setPurchaseId(221);
		productInfo.setProductId(31);
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(200);
		productInfo.setPurchaseQty(1);
		productInfo.setPurchaseDateTime("2021-10-22 11:36:56");
		productInfo.setQuantity(59);
		productInfo.setDeleteFlag(0);

		list.add(productInfo);

		//引数・戻り値の設定
		when(purchaseHistoryMapper.selectPurchaseHistory(1)).thenReturn(list);

		//実行
		List<ProductInfo> purchaseHistoryList = purchaseHistoryService.selectPurchaseHistory(1);

		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();
		Integer userId = this.getUserIdSession();
		String userName = this.getUserNameSession();

		mockMvc.perform(MockMvcRequestBuilders.post("/select-btn")
				.param("selectPurchaseHistoryList", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「history/purchaseHistoryList」であることを確認
				.andExpect(view().name("history/purchaseHistoryList"));
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("cartList", cartList))
		//				.andExpect(model().attribute("userName", userName))
		//				.andExpect(model().attribute("purchaseHistoryList", purchaseHistoryList));

	}

	@Test
	void returnMenuTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();
		String userName = this.getUserNameSession();

		mockMvc.perform(MockMvcRequestBuilders.post("/select-btn")
				.param("selectMenu", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「user/generalUserMenu」であることを確認
				.andExpect(view().name("user/generalUserMenu"));
		//		.andExpect(model().attribute("userName", userName))
		//		.andExpect(model().attribute("giftAmount", giftAmount))
		//		.andExpect(model().attribute("cartList", cartList));

	}

	//ValidationErrorあり
	@Test
	void inputShoppingCartTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();
		Integer userId = this.getUserIdSession();
		List<ProductInfo> cartList = this.getCartListSession();

		mockMvc.perform(MockMvcRequestBuilders.post("/product-purchase")
				.param("cart", "")
				.param("productId", "31"))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「generalProduct/shoppingCart」であることを確認
				.andExpect(view().name("generalProduct/shoppingCart"));
		//		.andExpect(model().attribute("giftAmount", giftAmount))
		//		.andExpect(model().attribute("cartList", cartList))
		//		.andExpect(request().sessionAttribute("totalAmount", totalAmount))

	}

	@Test
	void cartProductInfoDeleteTest() throws Exception {
		List<ProductInfo> cartList = this.getCartListSession();
		Integer totalAmount = this.getTotalAmountSession();
		Integer giftAmount = this.getGiftAmountSession();

		mockMvc.perform(MockMvcRequestBuilders.post("/cart-product-delete")
				.param("delete", "")
				.param("cartId", "1"))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「generalProduct/shoppingCart」であることを確認
				.andExpect(view().name("generalProduct/shoppingCart"));
		//	        .andExpect(request().sessionAttribute("cartList", cartList))
		//	        .andExpect(request().sessionAttribute("totalAmount", totalAmount))
		//		.andExpect(model().attribute("giftAmount", giftAmount))
		//		.andExpect(model().attribute("cartList", cartList))
		//		.andExpect(model().attribute("totalAmount", totalAmount))

	}

	@Test
	void backGeneralUserMenuTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();
		String userName = this.getUserNameSession();
		List<ProductInfo> cartList = this.getCartListSession();

		mockMvc.perform(MockMvcRequestBuilders.post("/cart-product-purchase")
				.param("backGeneralUserMenu", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「user/generalUserMenu」であることを確認
				.andExpect(view().name("user/generalUserMenu"));
		//		.andExpect(model().attribute("giftAmount", giftAmount))
		//		.andExpect(model().attribute("userName", userName))
		//		.andExpect(model().attribute("cartList", cartList))
	}

	@Test
	void cartProductPurchaseTest() throws Exception {
		Integer userId = this.getUserIdSession();
		Integer totalAmount = this.getTotalAmountSession();
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		String errorMsg = "※「テスト商品」の情報が管理者によって修正・削除された可能性があります。カートから削除し、一覧から再度商品をお買い求めください。";

		List<String> errorList = new ArrayList<String>();
		errorList.add(errorMsg);

		mockMvc.perform(MockMvcRequestBuilders.post("/cart-product-purchase")
				.param("purchase", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「generalProduct/productPurchaseFinish」であることを確認
				.andExpect(view().name("generalProduct/productPurchaseFinish"));
		//		.andExpect(model().attribute("giftAmount", giftAmount))
		//		.andExpect(model().attribute("totalAmount", totalAmount))
		//		.andExpect(model().attribute("errorList", errorList))
		//		.andExpect(request().sessionAttribute("giftAmount", giftAmount))
		//		.andExpect(request().sessionAttribute("cartList", cartList))

	}

	@Test
	void deleteAllCartTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();
		Integer userId = this.getUserIdSession();
		List<ProductInfo> cartList = this.getCartListSession();

		mockMvc.perform(MockMvcRequestBuilders.post("/cart-product-purchase")
				.param("deleteCart", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「generalProduct/shoppingCart」であることを確認
				.andExpect(view().name("generalProduct/shoppingCart"));
		//		.andExpect(model().attribute("giftAmount", giftAmount))
		//		.andExpect(model().attribute("cartList", cartList))

	}

	@Test
	void checkShoppingCartTest() throws Exception {
		List<ProductInfo> cartList = this.getCartListSession();
		Integer totalAmount = this.getTotalAmountSession();
		Integer giftAmount = this.getGiftAmountSession();

		String setPriceAlertMsg = "「テスト商品」の価格が更新されています";

		MockHttpSession mockSession = new MockHttpSession();

		//giftAmountの値をセッションにセット
		mockSession.setAttribute("priceAlertMsg", setPriceAlertMsg);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(mockSession);

		String priceAlertMsg = (String) mockSession.getAttribute("priceAlertMsg");

		mockMvc.perform(MockMvcRequestBuilders.post("/header")
				.param("cart", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「generalProduct/shoppingCart」であることを確認
				.andExpect(view().name("generalProduct/shoppingCart"));
		//				.andExpect(model().attribute("cartList", cartList))
		//				.andExpect(model().attribute("totalAmount", totalAmount))
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("priceAlertMsg", priceAlertMsg))

	}

	@Test
	void ErrorCheckクラスのcompareAmountsメソッドのエラーメッセージが有効な場合のテスト() {
		final int expectedErrorListSize = 1;
		final Integer totalAmount = 5000;
		final Integer giftAmount = 3000;
		List<String> errorList = new ArrayList<String>();
		ErrorCheck error = new ErrorCheck();

		error.compareAmounts(totalAmount, giftAmount, errorList);

		int actualErrorSize = errorList.size();

		assertEquals(actualErrorSize, expectedErrorListSize);

	}

	@Test
	void ErrorCheckクラスのcompareAmountsメソッドのエラーメッセージが無効な場合のテスト() {
		final int expectedErrorListSize = 0;
		final Integer totalAmount = 2000;
		final Integer giftAmount = 3000;
		List<String> errorList = new ArrayList<String>();
		ErrorCheck error = new ErrorCheck();

		error.compareAmounts(totalAmount, giftAmount, errorList);

		int actualListSize = errorList.size();

		assertEquals(actualListSize, expectedErrorListSize);

	}

	@Test
	void ErrorCheckクラスのpurcharseQtyOverメソッドのエラーメッセージが有効な場合のテスト() {
		final int expectedErrorListSize = 1;
		final Integer quantity = 10;
		final Integer purchaseQty = 20;
		final String productName = "テスト商品";

		List<String> errorList = new ArrayList<String>();
		ErrorCheck error = new ErrorCheck();

		error.purcharseQtyOver(quantity, purchaseQty, productName, errorList);

		int actualListSize = errorList.size();

		assertEquals(actualListSize, expectedErrorListSize);
	}

	@Test
	void ErrorCheckクラスのpurcharseQtyOverメソッドのエラーメッセージが無効な場合のテスト() {
		final int expectedErrorListSize = 0;
		final Integer quantity = 10;
		final Integer purchaseQty = 5;
		final String productName = "テスト商品";

		List<String> errorList = new ArrayList<String>();
		ErrorCheck error = new ErrorCheck();

		error.purcharseQtyOver(quantity, purchaseQty, productName, errorList);

		int actualListSize = errorList.size();

		assertEquals(actualListSize, expectedErrorListSize);
	}

	@Test
	void ProductInfoDBServiceクラスのgetProductListメソッドの正常系テスト() {
		//期待する戻り値を設定
		ProductInfo productInfo = new ProductInfo();
		productInfo.setProductId(31);
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(200);
		productInfo.setQuantity(56);
		productInfo.setVersionNumber(29);
		productInfo.setGenreName("食品・飲料");

		List<ProductInfo> productList = new ArrayList<ProductInfo>();

		productList.add(productInfo);

		ProductSearchForm productSearchForm = new ProductSearchForm();
		productSearchForm.setProductName("テスト商品");
		productSearchForm.setPriceMin(null);
		productSearchForm.setPriceMax(null);
		productSearchForm.setGenre(null);

		//引数・戻り値の設定
		when(productMapper.selectProductInfo(productSearchForm)).thenReturn(productList);

		//実行
		List<ProductInfo> expectedProductList = productService.getProductList(productSearchForm);

		//検証
		assertAll(
				() -> verify(productMapper, times(1)).selectProductInfo(productSearchForm),
				() -> assertEquals(31, expectedProductList.get(0).getProductId()),
				() -> assertEquals("テスト商品", expectedProductList.get(0).getProductName()),
				() -> assertEquals(200, expectedProductList.get(0).getPrice()),
				() -> assertEquals(56, expectedProductList.get(0).getQuantity()),
				() -> assertEquals(29, expectedProductList.get(0).getVersionNumber()),
				() -> assertEquals("食品・飲料", expectedProductList.get(0).getGenreName()));
	}

	@Test
	void ProductInfoDBServiceクラスのgetProductListメソッドの異常系テスト() {
		//期待する戻り値を設定
		ProductInfo productInfo = new ProductInfo();
		productInfo.setProductId(null);
		productInfo.setProductName(null);
		productInfo.setPrice(null);
		productInfo.setQuantity(null);
		productInfo.setVersionNumber(null);
		productInfo.setGenreName(null);

		List<ProductInfo> productList = new ArrayList<ProductInfo>();

		productList.add(productInfo);

		ProductSearchForm productSearchForm = new ProductSearchForm();
		productSearchForm.setProductName("abc");
		productSearchForm.setPriceMin(null);
		productSearchForm.setPriceMax(null);
		productSearchForm.setGenre(null);

		//引数・戻り値の設定
		when(productMapper.selectProductInfo(productSearchForm)).thenReturn(productList);

		//実行
		List<ProductInfo> expectedProductList = productService.getProductList(productSearchForm);

		//検証
		assertAll(
				() -> verify(productMapper, times(1)).selectProductInfo(productSearchForm),
				() -> assertEquals(null, expectedProductList.get(0).getProductId()),
				() -> assertEquals(null, expectedProductList.get(0).getProductName()),
				() -> assertEquals(null, expectedProductList.get(0).getPrice()),
				() -> assertEquals(null, expectedProductList.get(0).getQuantity()),
				() -> assertEquals(null, expectedProductList.get(0).getVersionNumber()),
				() -> assertEquals(null, expectedProductList.get(0).getGenreName()));
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
		final Integer productId = 310;
		final Integer actualQuantity = null;

		//戻り値を設定
		Integer quantity = null;

		//引数・戻り値の設定
		when(productMapper.selectIdMatchQuantity(productId)).thenReturn(quantity);

		//実行
		Integer expectedQuantity = productService.getIdMatchQuantity(productId);

		// 検証
		assertAll(
				() -> verify(productMapper, times(1)).selectIdMatchQuantity(productId),
				() -> assertEquals(null, expectedQuantity));

	}

	@Test
	void ProductInfoDBServiceクラスのpurchaseProductUpDateCheckメソッドの正常系テスト() {
		final int cnt = 0;

		ProductInfo productInfo = new ProductInfo();

		productInfo.setProductId(31);
		productInfo.setPrice(200);

		//期待する戻り値を設定
		final int actualCnt = 0;

		//引数・戻り値の設定
		when(productMapper.selectPurchaseProductCount(productInfo)).thenReturn(cnt);

		//実行
		int expectedCnt = productService.purchaseProductUpDateCheck(productInfo);

		//検証
		assertAll(
				() -> verify(productMapper, times(1)).selectPurchaseProductCount(productInfo),
				() -> assertEquals(actualCnt, expectedCnt));

	}

	@Test
	void ProductInfoDBServiceクラスのpurchaseProductUpDateCheckメソッドの異常系テスト() {
		final int cnt = 1;

		ProductInfo productInfo = new ProductInfo();

		productInfo.setProductId(31);
		productInfo.setPrice(1000);

		//期待する戻り値を設定
		final int actualCnt = 1;

		//引数・戻り値の設定
		when(productMapper.selectPurchaseProductCount(productInfo)).thenReturn(cnt);

		//実行
		int expectedCnt = productService.purchaseProductUpDateCheck(productInfo);

		//検証
		assertAll(
				() -> verify(productMapper, times(1)).selectPurchaseProductCount(productInfo),
				() -> assertEquals(actualCnt, expectedCnt));

	}

	@Test
	void PurchaseHistoryDBServiceクラスのselectPurchaseHistoryメソッドのテスト() {
		final Integer userId = 1;
		//期待する戻り値を設定
		ProductInfo productInfo = new ProductInfo();
		List<ProductInfo> list = new ArrayList<ProductInfo>();

		productInfo.setPurchaseId(221);
		productInfo.setProductId(31);
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(200);
		productInfo.setPurchaseQty(1);
		productInfo.setPurchaseDateTime("2021-10-22 11:36:56");
		productInfo.setQuantity(59);
		productInfo.setDeleteFlag(0);

		list.add(productInfo);

		//引数・戻り値の設定
		when(purchaseHistoryMapper.selectPurchaseHistory(userId)).thenReturn(list);

		//実行
		List<ProductInfo> expectedPurchaseHistoryList = purchaseHistoryService.selectPurchaseHistory(userId);

		//検証
		assertAll(
				() -> verify(purchaseHistoryMapper, times(1)).selectPurchaseHistory(userId),
				() -> assertEquals(221, expectedPurchaseHistoryList.get(0).getPurchaseId()),
				() -> assertEquals(31, expectedPurchaseHistoryList.get(0).getProductId()),
				() -> assertEquals("テスト商品", expectedPurchaseHistoryList.get(0).getProductName()),
				() -> assertEquals(200, expectedPurchaseHistoryList.get(0).getPrice()),
				() -> assertEquals(1, expectedPurchaseHistoryList.get(0).getPurchaseQty()),
				() -> assertEquals("2021-10-22 11:36:56", expectedPurchaseHistoryList.get(0).getDateTime()),
				() -> assertEquals(0, expectedPurchaseHistoryList.get(0).getDeleteFlag()));

	}

	@Test
	void CartInfoDBServiceクラスのgetIdMatchCartInfoメソッドのテスト() {
		final Integer userId = 1;
		final Integer productId = 31;
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
		ProductInfo expectedProductInfo = cartInfoService.getIdMatchCartInfo(productId, userId);

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
	void UserDBServiceクラスのselectGiftAmountメソッドの正常系テスト() {
		final Integer userId = 1;
		final Integer actualGiftAmount = 483720;

		//期待する戻り値を設定
		final Integer giftAmount = 483720;

		//引数・戻り値の設定
		when(userMapper.selectIdMatchGiftAmount(userId)).thenReturn(giftAmount);

		//実行
		Integer expectedGiftAmount = userService.selectGiftAmount(userId);

		//検証
		assertAll(
				() -> verify(userMapper, times(1)).selectIdMatchGiftAmount(userId),
				() -> assertEquals(actualGiftAmount, expectedGiftAmount));
	}

	@Test
	void UserDBServiceクラスのselectGiftAmountメソッドの異常系テスト() {
		final Integer userId = 1;
		final Integer actualGiftAmount = null;

		//期待する戻り値を設定
		final Integer giftAmount = null;

		//引数・戻り値の設定
		when(userMapper.selectIdMatchGiftAmount(userId)).thenReturn(giftAmount);

		//実行
		Integer expectedGiftAmount = userService.selectGiftAmount(userId);

		//検証
		assertAll(
				() -> verify(userMapper, times(1)).selectIdMatchGiftAmount(userId),
				() -> assertEquals(actualGiftAmount, expectedGiftAmount));
	}

	@Test
	void PurchaseHistoryDBServiceクラスのinsertPurchaseHistoryメソッドが呼び出されているかのテスト() {
		final Integer userId = 1;

		ProductPurchaseForm productPurchaseForm = new ProductPurchaseForm();

		productPurchaseForm.setProductName("テスト商品");
		productPurchaseForm.setPrice(200);
		productPurchaseForm.setPurchaseQty(1);

		purchaseHistoryService.insertPurchaseHistory(productPurchaseForm, userId);

		verify(purchaseHistoryMapper, times(1)).insertPurchaseProductInfo(productPurchaseForm, userId);
		;

	}

	@Test
	void PurchaseHistoryDBServiceクラスのinsertCartPurchaseHistoryメソッドが呼び出されているかのテスト() {
		final Integer userId = 1;

		ProductInfo productInfo = new ProductInfo();

		productInfo.setProductId(31);
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(200);
		productInfo.setPurchaseQty(1);

		purchaseHistoryService.insertCartPurchaseHistory(productInfo, userId);

		verify(purchaseHistoryMapper, times(1)).insertCartPurchaseProductInfo(productInfo, userId);

	}

	@Test
	void ProductInfoDBServiceクラスのupDateSubstractQuantityメソッドが呼び出されているかのテスト() {
		ProductInfo productInfo = new ProductInfo();

		productInfo.setProductId(31);
		productInfo.setPrice(200);
		productInfo.setPurchaseQty(1);

		productService.upDateSubstractQuantity(productInfo);

		verify(productMapper, times(1)).upDateSubstractQuantity(productInfo);

	}

	@Test
	void UserDBServiceクラスのupDateSubstractGiftAmountメソッドが呼び出されているかのテスト() {
		final Integer totalAmount = 10000;
		final Integer userId = 1;

		userService.upDateSubstractGiftAmount(totalAmount, userId);

		verify(userMapper, times(1)).upDateSubstractGiftAmount(totalAmount, userId);

	}

	@Test
	void CartInfoDBServiceクラスのallDeleteCartInfoメソッドが呼び出されているかのテスト() {
		final Integer userId = 1;

		cartInfoService.allDeleteCartInfo(userId);

		verify(cartInfoMapper, times(1)).deleteAllCartInfo(userId);
	}

	@Test
	void CartInfoDBServiceクラスのcartInfoUpDateメソッドが呼び出されているかのテスト() {
		final Integer price = 200;
		final Integer purchaseQty = 1;
		final Integer productId = 31;
		final Integer userId = 1;

		cartInfoService.cartInfoUpdate(price, purchaseQty, productId, userId);

		verify(cartInfoMapper, times(1)).upDateCartInfo(price, purchaseQty, productId, userId);

	}

	@Test
	void CartInfoDBServiceクラスのcartInfoRegisterメソッドが呼び出されているかのテスト() {
		final Integer userId = 1;

		ProductInfo productInfo = new ProductInfo();

		productInfo.setProductId(31);
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(200);
		productInfo.setPurchaseQty(1);

		cartInfoService.cartInfoRegister(userId, productInfo);

		verify(cartInfoMapper, times(1)).insertCartInfo(userId, productInfo);

	}

	@Test
	void CartInfoDBServiceクラスのdeleteIdMatchCartInfoメソッドが呼び出されているかのテスト() {
		final Integer cartId = 1;

		cartInfoService.deleteIdMatchCartInfo(cartId);

		verify(cartInfoMapper, times(1)).deleteCartInfo(cartId);

	}

	private Integer getGiftAmountSession() {
		//セッションに入れる値を設定
		Integer setGiftAmount = 3000;

		MockHttpSession mockSession = new MockHttpSession();

		//giftAmountの値をセッションにセット
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
		Integer setUserId = 1;

		MockHttpSession mockSession = new MockHttpSession();

		mockSession.setAttribute("userId", setUserId);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(mockSession);

		Integer userId = (Integer) mockSession.getAttribute("userId");

		return userId;
	}

	private String getUserNameSession() {
		String setUserName = "koki";

		MockHttpSession mockSession = new MockHttpSession();

		mockSession.setAttribute("userName", setUserName);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(mockSession);

		String userName = (String) mockSession.getAttribute("userName");

		return userName;
	}

	private Integer getTotalAmountSession() {
		Integer setTotalAmount = 5000;

		MockHttpSession mockSession = new MockHttpSession();

		mockSession.setAttribute("totalAmount", setTotalAmount);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(mockSession);

		Integer totalAmount = (Integer) mockSession.getAttribute("totalAmount");

		return totalAmount;

	}

}
