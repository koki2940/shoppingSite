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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import com.example.domain.ProductInfo;
import com.example.persistence.ProductInfoMapper;
import com.example.service.ProductInfoDBService;
import com.example.web.errorcheck.ErrorCheck;
import com.example.web.form.ProductCorrectForm;
import com.example.web.form.ProductRegisterForm;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration
@WebAppConfiguration //コンテキストを使う準備
@SpringBootTest
class AdminProductControllerTest extends AdminProductController {
	//MockMvc
	private MockMvc mockMvc;

	@Mock
	private ProductInfoMapper productMapper;

	@InjectMocks
	private ProductInfoDBService productService;

	@Autowired
	Validator validator;

	//各テスト実行毎に実行する
	@BeforeEach
	void setup() {
		//Mockの初期化
		MockitoAnnotations.initMocks(this);

		//MockMvcオブジェクトにテスト対象メソッドを設定
		mockMvc = MockMvcBuilders.standaloneSetup(new AdminProductController()).build();

	}

	@Test
	void productRegisterTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();

		mockMvc.perform(post("/select-btn")
				.param("productRegister", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「adminProduct/productRegister」であることを確認
				.andExpect(view().name("adminProduct/productRegister"));
		//.andExpect(model().attribute("giftAmount", giftAmount));
	}

	@Test
	void backMenuTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();
		String userName = this.getUserNameSession();

		mockMvc.perform(post("/product-register")
				.param("backMenu", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「user/adminUserMenu」であることを確認
				.andExpect(view().name("user/adminUserMenu"));
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("userName", userName));
	}

	//ValidationErrorあり
	@Test
	void productRegisterCheckTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();
		int cnt = 0;

		//		ProductRegisterForm productRegisterForm = new ProductRegisterForm();
		//		productRegisterForm.setProductName("テスト商品2");
		//		productRegisterForm.setPrice(100);
		//		productRegisterForm.setGenre(1);
		//		productRegisterForm.setQuantity(100);
		//
		//	    BindingResult bindingResult = new BindException(productRegisterForm, "productRegisterForm");
		//
		//		 validator.validate(productRegisterForm, bindingResult);
		//
		//		 assertNull(bindingResult.getFieldError());

		MvcResult result = mockMvc.perform(post("/product-register")
				.param("check", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「adminProduct/productRegister」であることを確認
				.andExpect(view().name("adminProduct/productRegister"))
				//				.andExpect(model().attribute("giftAmount", giftAmount))
				.andReturn();

		ProductRegisterForm resultForm = (ProductRegisterForm) result.getModelAndView().getModel()
				.get("productRegisterForm");
	}

	@Test
	void backProductRegisterTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();

		mockMvc.perform(post("/product-register-check")
				.param("backRegister", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「adminProduct/productRegister」であることを確認
				.andExpect(view().name("adminProduct/productRegister"));
		//				.andExpect(model().attribute("giftAmount", giftAmount));

	}

	@Test
	void registerFinishTest() throws Exception {
		mockMvc.perform(post("/product-register-check")
				.param("register", ""))
				//リダイレクト先が「redirect:/insert-end?finish」であることを確認
				.andExpect(view().name("redirect:/insert-end?finish"));
	}

	@Test
	void productRegisterFinishTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();

		mockMvc.perform(post("/insert-end")
				.param("finish", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「adminProduct/productRegisterFinish」であることを確認
				.andExpect(view().name("adminProduct/productRegisterFinish"));
		//				.andExpect(model().attribute("giftAmount", giftAmount));

	}

	@Test
	void returnProductRegisterTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();

		mockMvc.perform(post("/product-register-finish")
				.param("addProductRegister", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「adminProduct/productRegister」であることを確認
				.andExpect(view().name("adminProduct/productRegister"));
		//.andExpect(model().attribute("giftAmount", giftAmount));
	}

	@Test
	void productCorrectDeleteListTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();

		ProductInfo productInfo = new ProductInfo();

		productInfo.setProductId(31);
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(200);
		productInfo.setQuantity(100);
		productInfo.setVersionNumber(1);
		productInfo.setGenreName("食品・飲料");

		List<ProductInfo> list = new ArrayList<ProductInfo>();

		list.add(productInfo);

		when(productMapper.selectProductInfoAll()).thenReturn(list);

		//実行
		List<ProductInfo> productList = productService.getProductListAll();

		mockMvc.perform(post("/select-btn")
				.param("productCorrectDelete", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「adminProduct/productCorrectDeletetList」であることを確認
				.andExpect(view().name("adminProduct/productCorrectDeletetList"));
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("productList", productList))
		//				.andExpect(request().sessionAttribute("productList", productList));

	}

	@Test
	void backAdminMenuTest() throws Exception {
		String userName = this.getUserNameSession();
		Integer giftAmount = this.getGiftAmountSession();

		mockMvc.perform(post("/back-product-list")
				.param("backAdminMenu", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「user/adminUserMenu」であることを確認
				.andExpect(view().name("user/adminUserMenu"));
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("userName", userName));

	}

	@Test
	void productCorrectTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> productList = this.getProductListSession();

		MvcResult result = mockMvc.perform(post("/product-correct-delete")
				.param("correct", "")
				.param("productId", "31")
				.param("versionNumber", "1"))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「adminProduct/productCorrect」であることを確認
				.andExpect(view().name("adminProduct/productCorrect"))
				//				.andExpect(model().attribute("giftAmount", giftAmount))
				//				.andExpect(request().sessionAttribute("versionNumber", 1))
				.andReturn();

		//		ProductCorrectForm resultForm = (ProductCorrectForm) result.getModelAndView().getModel()
		//				.get("productCorrectForm");
		//
		//		//formの値の検証
		//		assertEquals(resultForm.getProductId(), productList.get(0).getProductId());
		//		assertEquals(resultForm.getProductName(), productList.get(0).getProductName());
		//		assertEquals(resultForm.getPrice(), productList.get(0).getPrice());
		//		assertEquals(resultForm.getGenreName(), productList.get(0).getGenreName());
		//		assertEquals(resultForm.getQuantity(), productList.get(0).getQuantity());
	}

	//ValidationErrorあり
	@Test
	void productCorrectCheckTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();

		mockMvc.perform(post("/product-correct")
				.param("check", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「adminProduct/productCorrect」であることを確認
				.andExpect(view().name("adminProduct/productCorrect"));
		//.andExpect(model().attribute("giftAmount", giftAmount));

	}

	@Test
	void backProductCorrectTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();

		mockMvc.perform(post("/correct-check")
				.param("backProductCorrect", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「adminProduct/productCorrect」であることを確認
				.andExpect(view().name("adminProduct/productCorrect"));
		//.andExpect(model().attribute("giftAmount", giftAmount));

	}

	@Test
	void correctFinishTest() throws Exception {
		Integer versionNumber = this.getVersionNumberSession();

		mockMvc.perform(post("/correct-check")
				.param("corret", ""))
				//リダイレクト先が「redirect:/correct-end?finish」であることを確認
				.andExpect(view().name("redirect:/correct-end?finish"));
	}

	@Test
	void productCorrectFinishTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();

		mockMvc.perform(post("/correct-end")
				.param("finish", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「adminProduct/productCorrectFinish」であることを確認
				.andExpect(view().name("adminProduct/productCorrectFinish"));
		//				.andExpect(model().attribute("giftAmount", giftAmount));
	}

	@Test
	void returnProductCorrectDeleteListTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();

		ProductInfo productInfo = new ProductInfo();

		productInfo.setProductId(31);
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(200);
		productInfo.setQuantity(100);
		productInfo.setVersionNumber(1);
		productInfo.setGenreName("食品・飲料");

		List<ProductInfo> list = new ArrayList<ProductInfo>();

		list.add(productInfo);

		when(productMapper.selectProductInfoAll()).thenReturn(list);

		mockMvc.perform(post("/product-correct-finish")
				.param("returnList", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「adminProduct/productCorrectDeletetList」であることを確認
				.andExpect(view().name("adminProduct/productCorrectDeletetList"));
		//				.andExpect(request().sessionAttribute("productList", list));
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("productList", productList));
	}

	@Test
	void productDeleteCheckTest() throws Exception {
		List<ProductInfo> productList = this.getProductListSession();
		Integer giftAmount = this.getGiftAmountSession();

		final Integer productId = 31;

		ProductInfo productInfo = new ProductInfo();

		for (ProductInfo info : productList) {
			if (info.getProductId() == productId) {
				productInfo.setProductId(productInfo.getProductId());
				productInfo.setProductName(productInfo.getProductName());
				productInfo.setPrice(productInfo.getPrice());
				productInfo.setGenreName(productInfo.getGenreName());
				productInfo.setQuantity(productInfo.getQuantity());

				break;
			}
		}

		mockMvc.perform(post("/product-correct-delete")
				.param("delete", "")
				.param("productId", "31")
				.param("versionNumber", "29"))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「adminProduct/productDeleteCheck」であることを確認
				.andExpect(view().name("adminProduct/productDeleteCheck"));
		//						.andExpect(request().sessionAttribute("productInfo", productInfo))
		//						.andExpect(request().sessionAttribute("productId", 31))
		//						.andExpect(request().sessionAttribute("versionNumber", 29))
		//						.andExpect(model().attribute("productInfo", productInfo))
		//				.andExpect(model().attribute("giftAmount", giftAmount))

	}

	@Test
	void deleteFinishTest() throws Exception {
		Integer productId = this.getProductIdSession();
		Integer versionNumber = this.getVersionNumberSession();

		mockMvc.perform(post("/delete-check")
				.param("delete", ""))
				//リダイレクト先が「redirect:/delete-end?finish」であることを確認
				.andExpect(view().name("redirect:/delete-end?finish"));
	}

	@Test
	void productDeleteFinishTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();

		mockMvc.perform(post("/delete-end")
				.param("finish", ""))
				//次画面の遷移先が「adminProduct/productDeleteFinish」であることを確認
				.andExpect(view().name("adminProduct/productDeleteFinish"));
		//	.andExpect(model().attribute("giftAmount", giftAmount));
	}

	@Test
	void backProductCorrectDeleteListTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();

		ProductInfo productInfo = new ProductInfo();

		productInfo.setProductId(31);
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(200);
		productInfo.setQuantity(100);
		productInfo.setVersionNumber(1);
		productInfo.setGenreName("食品・飲料");

		List<ProductInfo> setProductList = new ArrayList<ProductInfo>();

		setProductList.add(productInfo);

		when(productMapper.selectProductInfoAll()).thenReturn(setProductList);

		List<ProductInfo> productList = productService.getProductListAll();

		mockMvc.perform(post("/product-correct", "/delete-check")
				.param("backList", ""))
				//次画面の遷移先が「adminProduct/productCorrectDeletetList」であることを確認
				.andExpect(view().name("adminProduct/productCorrectDeletetList"));
		//				.andExpect(request().attribute("productList", productList))
		//				.andExpect(model().attribute("productList", productList))
		//				.andExpect(model().attribute("giftAmount", giftAmount));

	}

	@Test
	void returnMenuTest() throws Exception {
		String userName = this.getUserNameSession();
		Integer giftAmount = this.getGiftAmountSession();

		mockMvc.perform(post("/product-register-finish", "/product-correct-finish", "/product-delete-finish")
				.param("returnMenu", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「user/adminUserMenu」であることを確認
				.andExpect(view().name("user/adminUserMenu"));
		//	.andExpect(model().attribute("giftAmount", giftAmount))
		//	.andExpect(model().attribute("userName", userName));

	}

	@Test
	void ErrorCheckクラスのupdateProductInfoCheckメソッドのエラーメッセージが有効な場合のテスト() {
		String expectedErrorMsg = "※この情報は既に別の管理者によって更新されています。一覧画面に戻り最新の情報を取得してください。";
		int upDateCnt = 0;
		ErrorCheck error = new ErrorCheck();

		String actualErrorMsg = error.upDateProductInfoCheck(upDateCnt);

		assertEquals(actualErrorMsg, expectedErrorMsg);

	}

	@Test
	void ErrorCheckクラスのupdateProductInfoCheckメソッドのエラーメッセージが無効な場合のテスト() {
		String expectedErrorMsg = null;
		int upDateCnt = 1;
		ErrorCheck error = new ErrorCheck();

		String actualErrorMsg = error.upDateProductInfoCheck(upDateCnt);

		assertEquals(actualErrorMsg, expectedErrorMsg);

	}

	@Test
	void ProductInfoDBServiceクラスのselectGenreNameメソッドのテスト() {
		ProductRegisterForm productRegisterForm = new ProductRegisterForm();

		productRegisterForm.setGenre(1);

		//引数・戻り値の設定
		when(productMapper.selectGenreNumberMatchGenreName(productRegisterForm)).thenReturn("食品・飲料");

		//実行
		String expectedGenreName = productService.selectGenreName(productRegisterForm);

		//検証
		assertAll(
				() -> verify(productMapper, times(1)).selectGenreNumberMatchGenreName(productRegisterForm),
				() -> assertEquals("食品・飲料", expectedGenreName));

	}

	@Test
	void ProductInfoDBServiceクラスのcorrectProductInfoメソッドの正常系テスト() {
		ProductCorrectForm productCorrectForm = new ProductCorrectForm();

		productCorrectForm.setProductId(31);
		productCorrectForm.setPrice(200);
		productCorrectForm.setQuantity(300);

		//引数・戻り値の設定
		when(productMapper.upDateProductInfo(productCorrectForm, 29)).thenReturn(1);

		//実行
		int expectedUpDateCnt = productService.correctProductInfo(productCorrectForm, 29);

		//検証
		assertAll(
				() -> verify(productMapper, times(1)).upDateProductInfo(productCorrectForm, 29),
				() -> assertEquals(1, expectedUpDateCnt));
	}

	@Test
	void ProductInfoDBServiceクラスのcorrectProductInfoメソッドの異常系テスト() {
		ProductCorrectForm productCorrectForm = new ProductCorrectForm();

		productCorrectForm.setProductId(31);
		productCorrectForm.setPrice(200);
		productCorrectForm.setQuantity(300);

		//引数・戻り値の設定
		when(productMapper.upDateProductInfo(productCorrectForm, 29)).thenReturn(1);

		//実行
		int expectedUpDateCnt = productService.correctProductInfo(productCorrectForm, 29);

		//検証
		assertAll(
				() -> verify(productMapper, times(1)).upDateProductInfo(productCorrectForm, 29),
				() -> assertEquals(1, expectedUpDateCnt));

	}

	@Test
	void ProductInfoDBServiceクラスのdeleteProductInfoメソッドのテスト() {
		//引数・戻り値の設定
		when(productMapper.upDateDeleteFlag(31, 1)).thenReturn(1);

		//実行
		int expectedUpDateCnt = productService.deleteProductInfo(31, 1);

		//検証
		assertAll(
				() -> verify(productMapper, times(1)).upDateDeleteFlag(31, 1),
				() -> assertEquals(1, expectedUpDateCnt));
	}

	@Test
	void ProductInfoDBServiceクラスのgetProductListAllメソッドのテスト() {
		//期待する戻り値を設定
		ProductInfo productInfo = new ProductInfo();

		productInfo.setProductId(31);
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(200);
		productInfo.setQuantity(100);
		productInfo.setVersionNumber(1);
		productInfo.setGenreName("食品・飲料");

		List<ProductInfo> productList = new ArrayList<ProductInfo>();

		productList.add(productInfo);

		when(productMapper.selectProductInfoAll()).thenReturn(productList);

		//実行
		List<ProductInfo> expectedProductList = productService.getProductListAll();

		//検証
		assertAll(
				() -> verify(productMapper, times(1)).selectProductInfoAll(),
				() -> assertEquals(31, expectedProductList.get(0).getProductId()),
				() -> assertEquals("テスト商品", expectedProductList.get(0).getProductName()),
				() -> assertEquals(200, expectedProductList.get(0).getPrice()),
				() -> assertEquals(100, expectedProductList.get(0).getQuantity()),
				() -> assertEquals(1, expectedProductList.get(0).getVersionNumber()),
				() -> assertEquals("食品・飲料", expectedProductList.get(0).getGenreName()));
	}

	@Test
	void ProductInfoDBServiceクラスのgetProductListAllメソッドの異常系テスト() {
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

		when(productMapper.selectProductInfoAll()).thenReturn(productList);

		//実行
		List<ProductInfo> expectedProductList = productService.getProductListAll();

		//検証
		assertAll(
				() -> verify(productMapper, times(1)).selectProductInfoAll(),
				() -> assertEquals(null, expectedProductList.get(0).getProductId()),
				() -> assertEquals(null, expectedProductList.get(0).getProductName()),
				() -> assertEquals(null, expectedProductList.get(0).getPrice()),
				() -> assertEquals(null, expectedProductList.get(0).getQuantity()),
				() -> assertEquals(null, expectedProductList.get(0).getVersionNumber()),
				() -> assertEquals(null, expectedProductList.get(0).getGenreName()));
	}

	@Test
	void ProductInfoDBServiceクラスのinsertProductInfoメソッドが呼び出されているかのテスト() {
		ProductRegisterForm productRegisterForm = new ProductRegisterForm();

		productRegisterForm.setProductName("テスト商品3");
		productRegisterForm.setPrice(1000);
		productRegisterForm.setGenre(1);
		productRegisterForm.setQuantity(100);

		productService.insertProductInfo(productRegisterForm);;

		verify(productMapper, times(1)).insertProductInfo(productRegisterForm);
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

	private String getUserNameSession() {
		//セッションに入れる値を設定
		String setUserName = "koki";

		MockHttpSession mockSession = new MockHttpSession();

		mockSession.setAttribute("setUserName", setUserName);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(mockSession);

		String userName = (String) mockSession.getAttribute("userName");

		return userName;
	}

	private List<ProductInfo> getProductListSession() {
		//セッションに入れる値を設定
		ProductInfo productInfo = new ProductInfo();
		productInfo.setProductId(31);
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(200);
		productInfo.setQuantity(100);
		productInfo.setVersionNumber(1);
		productInfo.setGenreName("食品・飲料");

		List<ProductInfo> setProductList = new ArrayList<ProductInfo>();

		setProductList.add(productInfo);

		MockHttpSession mockSession = new MockHttpSession();

		mockSession.setAttribute("productList", setProductList);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(mockSession);

		List<ProductInfo> productList = (List<ProductInfo>) mockSession.getAttribute("productList");

		return productList;
	}

	private Integer getVersionNumberSession() {
		//セッションに入れる値を設定
		Integer setVersionNumber = 1;

		MockHttpSession mockSession = new MockHttpSession();

		mockSession.setAttribute("versionNumber", setVersionNumber);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(mockSession);

		Integer versionNumber = (Integer) mockSession.getAttribute("versionNumber");

		return versionNumber;
	}

	private Integer getProductIdSession() {
		//セッションに入れる値を設定
		Integer setProductId = 31;

		MockHttpSession mockSession = new MockHttpSession();

		mockSession.setAttribute("productId", setProductId);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(mockSession);

		Integer productId = (Integer) mockSession.getAttribute("productId");

		return productId;

	}

}
