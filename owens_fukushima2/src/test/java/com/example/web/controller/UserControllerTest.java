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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import com.example.domain.ProductInfo;
import com.example.domain.UserInfo;
import com.example.persistence.CartInfoMapper;
import com.example.persistence.UserMapper;
import com.example.service.CartInfoDBService;
import com.example.service.UserDBService;
import com.example.web.alertcheck.AlertCheck;
import com.example.web.errorcheck.ErrorCheck;
import com.example.web.form.UserRegisterForm;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
class UserControllerTest extends UserController {

	//MockMvc
	private MockMvc mockMvc;

	@Autowired
	Validator validator;

	//Mockするクラス
	@Mock
	UserMapper userMapper;

	@Mock
	CartInfoMapper cartInfoMapper;

	//Mockを使うクラス
	@InjectMocks
	UserDBService userService;

	@InjectMocks
	CartInfoDBService cartInfoService;

	private UserRegisterForm userRegisterForm = new UserRegisterForm();

	private BindingResult bindingResult = new BindException(userRegisterForm, "UserRegisterForm");

	//各テスト実行毎に実行する
	@BeforeEach
	void setup() {
		//Mockの初期化
		MockitoAnnotations.initMocks(this);

		//MockMvcオブジェクトにテスト対象メソッドを設定
		mockMvc = MockMvcBuilders.standaloneSetup(new UserController()).build();

	}

	@Test
	void roginTest() throws Exception {
		mockMvc.perform(post("/home"))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「user/loginMenu」であることを確認
				.andExpect(view().name("user/loginMenu"));
	}

	@Test
	void userRegisterTest() throws Exception {
		mockMvc.perform(post("/register"))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「user/userRegister」であることを確認
				.andExpect(view().name("user/userRegister"));

	}

	//※ValidationError
	@Test
	void userCheckTest() throws Exception {
		userRegisterForm.setUserName("");
		validator.validate(userRegisterForm, bindingResult);

		//		 assertEquals(bindingResult.getFieldError().getDefaultMessage(), "error.invalid.userName");

		mockMvc.perform(post("/user-register")
				.param("registerCheck", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「user/userRegister」であることを確認
				.andExpect(view().name("user/userRegister"));

	}

	@Test
	void backRoginTest() throws Exception {
		mockMvc.perform(post("/user-register")
				.param("backloginMenu", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//次画面の遷移先が「user/loginMenu」であることを確認
				.andExpect(view().name("user/loginMenu"));
	}

	@Test
	void applyTest() throws Exception {
		mockMvc.perform(post("/user-register-check")
				.param("register", ""))
				//リダイレクトパスが「redirect:/insertUser-end?finish」であることを確認
				.andExpect(view().name("redirect:/insertUser-end?finish"));

	}

	@Test
	void userApplyFinishTest() throws Exception {
		mockMvc.perform(post("/insertUser-end")
				.param("finish", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//遷移先が「user/userRegisterFinish」であることを確認
				.andExpect(view().name("user/userRegisterFinish"));

	}

	@Test
	void backUserRegisterTest() throws Exception {
		mockMvc.perform(post("/user-register-check")
				.param("backuserRegister", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//遷移先が「user/userRegister」であることを確認
				.andExpect(view().name("user/userRegister"));

	}

	@Test
	void loginMenuTest() throws Exception {
		mockMvc.perform(post("/user-register-finish")
				.param("loginMenu", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//遷移先が「user/loginMenu」であることを確認
				.andExpect(view().name("user/loginMenu"));
	}

	@Test
	void menuTest() throws Exception {
		Integer totalAmount = 5000;
		String userName = "koki";
		List<ProductInfo> cartList = new ArrayList<ProductInfo>();

		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(1);
		userInfo.setAuthority(1);
		userInfo.setGiftAmount(5000);

		//引数・戻り値の設定
		when(userMapper.findLoginSucceedUser(userName)).thenReturn(userInfo);

		//実行
		UserInfo resultuserInfo = userService.getLoginSucceedUser(userName);

		mockMvc.perform(post("/login"))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//遷移先が「user/generalUserMenu」であることを確認
				.andExpect(view().name("user/generalUserMenu"));
//				.andExpect(request().sessionAttribute("totalAmount", totalAmount))
//				.andExpect(request().sessionAttribute("userId", resultuserInfo.getUserId()))
//				.andExpect(request().sessionAttribute("authority", resultuserInfo.getAuthority()))
//				.andExpect(request().sessionAttribute("giftAmount", resultuserInfo.getGiftAmount()))
//				.andExpect(request().sessionAttribute(userName, "userName"))
//				.andExpect(model().attribute("userName", userName))
//				.andExpect(model().attribute("giftAmount", resultuserInfo.getGiftAmount()))
//				.andExpect(model().attribute("cartList", cartList));

	}

	@Test
	void giftChargeTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();

		mockMvc.perform(post("/header")
				.param("gift", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//遷移先が「user/giftCharge」であることを確認
				.andExpect(view().name("user/giftCharge"));
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("cartList", cartList));
	}

	@Test
	void backMenuTest() throws Exception {
		int giftAmount = this.getGiftAmountSession();
		String userName = this.getUserNameSession();
		List<ProductInfo> cartList = this.getCartListSession();

		mockMvc.perform(post("/header")
				.param("gift", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//遷移先が「user/giftCharge」であることを確認
				.andExpect(view().name("user/giftCharge"));
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("cartList", cartList));

	}

	@Test
	void chargeFinishTest() throws Exception {
		Integer giftAmount = this.getGiftAmountSession();
		List<ProductInfo> cartList = this.getCartListSession();
		Integer userId = this.getUserIdSession();

		List<String> errorList = new ArrayList<String>();

		mockMvc.perform(post("/gift-charge")
				.param("charge", "")
				.param("chargeAmount", "1500"))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//遷移先が「user/giftCharge」であることを確認
				.andExpect(view().name("user/giftCharge"));
		//				.andExpect(model().attribute("giftAmount", giftAmount))
		//				.andExpect(model().attribute("cartList", cartList))
		//				.andExpect(model().attribute("errorList", errorList));

	}

	@Test
	void logoutTest() throws Exception {
		mockMvc.perform(post("/logoff")
				.param("logout", ""))
				//HTTPステータスがOKであることを確認
				.andExpect(status().isOk())
				//遷移先が「user/loginMenu」であることを確認
				.andExpect(view().name("user/loginMenu"));

	}

	@Test
	void AlertCheckクラスのcartCheckメソッドの警告メッセージが有効な場合のテスト() {
		final String expectedAlertMsg = "!カート内に商品が入っております。";

		ProductInfo productInfo = new ProductInfo();
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(200);
		productInfo.setPurchaseQty(1);

		List<ProductInfo> cartList = new ArrayList<ProductInfo>();

		cartList.add(productInfo);

		AlertCheck alert = new AlertCheck();

		String actualAlertMsg = alert.cartCheck(cartList);

		//検証
		assertEquals(actualAlertMsg, expectedAlertMsg);

	}

	@Test
	void AlertCheckクラスのcartCheckメソッドの警告メッセージが無効な場合のテスト() {
		final String expectedAlertMsg = null;

		List<ProductInfo> cartList = new ArrayList<ProductInfo>();

		AlertCheck alert = new AlertCheck();

		String actualAlertMsg = alert.cartCheck(cartList);

		//検証
		assertEquals(actualAlertMsg, expectedAlertMsg);

	}

	@Test
	void ErrorCheckクラスのchargeAmountSelectCheckメソッドのエラーメッセージが有効な場合のテスト() {
		List<String> errorList = new ArrayList<String>();

		final String chargeAmount = null;
		final int  expectedListSize = 1;

		ErrorCheck error = new ErrorCheck();

		error.chargeAmountSelectCheck(chargeAmount, errorList);

		int actualListSize = errorList.size();

		assertEquals(actualListSize, expectedListSize);
	}

	@Test
	void ErrorCheckクラスのchargeAmountSelectCheckメソッドのエラーメッセージが無効な場合のテスト() {
		List<String> errorList = new ArrayList<String>();

		final String chargeAmount = "1500";
		final int    expectedListSize = 0;

		ErrorCheck error = new ErrorCheck();

		error.chargeAmountSelectCheck(chargeAmount, errorList);

		int actualListSize = errorList.size();

		assertEquals(actualListSize, expectedListSize);
	}

	@Test
	void ErrorCheckクラスのchargeAmountUpperLimitメッセージが有効な場合のテスト() {
		List<String> errorList = new ArrayList<String>();

		ErrorCheck error = new ErrorCheck();

		final String chargeAmount = "15000";
		final Integer giftAmount = 495000;
		final int expectedListSize = 1;

		error.chargeAmountUpperLimit(chargeAmount, giftAmount, errorList);

		int actualListSize = errorList.size();

		assertEquals(actualListSize, expectedListSize);
	}

	@Test
	void ErrorCheckクラスのchargeAmountUpperLimitメッセージが無効な場合のテスト() {
		List<String> errorList = new ArrayList<String>();

		ErrorCheck error = new ErrorCheck();

		final String chargeAmount = "15000";
		final Integer giftAmount = 47000;
		final int expectedListSize = 0;

		error.chargeAmountUpperLimit(chargeAmount, giftAmount, errorList);

		int actualListSize = errorList.size();

		assertEquals(actualListSize, expectedListSize);
	}

	@Test
	void UserServiceクラスのinsertUserメソッドが呼び出されているかテスト() {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName("koki");
		userInfo.setPassword("2940");
		userInfo.setMail("futago2940.kou1002@gmail.com");
		userInfo.setPostNumber("350-1126");
		userInfo.setAddress("埼玉県");

		userService.insertUser(userInfo);

		verify(userMapper, times(1)).insertUserInfo(userInfo);

	}

	@Test
	void UserServiceクラスのgetLoginSucceedUserメソッドの正常系テスト() {
		final String userName = "koki";
		//期待する戻り値を設定
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(1);
		userInfo.setAuthority(1);
		userInfo.setGiftAmount(495000);

		//引数・戻り値の設定
		when(userMapper.findLoginSucceedUser(userName)).thenReturn(userInfo);

		//実行
		UserInfo expectedUserInfo = userService.getLoginSucceedUser(userName);

		//検証
		assertAll(
				() -> verify(userMapper, times(1)).findLoginSucceedUser(userName),
				() -> assertEquals(1, expectedUserInfo.getUserId()),
				() -> assertEquals(1, expectedUserInfo.getAuthority()),
				() -> assertEquals(495000, expectedUserInfo.getGiftAmount()));
	}

	@Test
	void UserServiceクラスのgetLoginSucceedUserメソッドの異常系テスト() {
		final String userName = "koko";
		//期待する戻り値を設定
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(null);
		userInfo.setAuthority(null);
		userInfo.setGiftAmount(null);

		//引数・戻り値の設定
		when(userMapper.findLoginSucceedUser(userName)).thenReturn(userInfo);

		//実行
		UserInfo expectedUserInfo = userService.getLoginSucceedUser(userName);

		//検証
		assertAll(
				() -> verify(userMapper, times(1)).findLoginSucceedUser(userName),
				() -> assertEquals(null, expectedUserInfo.getUserId()),
				() -> assertEquals(null, expectedUserInfo.getAuthority()),
				() -> assertEquals(null, expectedUserInfo.getGiftAmount()));
	}

	@Test
	void CartInfoServiceクラスのgetCartInfoメソッドの正常系テスト() {
		List<ProductInfo> cartList = new ArrayList<ProductInfo>();
		ProductInfo productInfo = new ProductInfo();

		final Integer userId = 1;

		productInfo.setCartId(1);
		productInfo.setProductId(31);
		productInfo.setProductName("テスト商品");
		productInfo.setPrice(200);
		productInfo.setPurchaseQty(1);

		cartList.add(productInfo);

		//引数・戻り値の設定
		when(cartInfoMapper.selectCartInfo(userId)).thenReturn(cartList);

		//実行
		List<ProductInfo> expectedCartList = cartInfoService.getCartInfo(userId);

		//検証
		assertAll(
				() -> verify(cartInfoMapper, times(1)).selectCartInfo(userId),
				() -> assertEquals(1, expectedCartList.get(0).getCartId()),
				() -> assertEquals(31, expectedCartList.get(0).getProductId()),
				() -> assertEquals("テスト商品", expectedCartList.get(0).getProductName()),
				() -> assertEquals(200, expectedCartList.get(0).getPrice()),
				() -> assertEquals(1, expectedCartList.get(0).getPurchaseQty()));
	}

	@Test
	void CartInfoServiceクラスのgetCartInfoメソッドの異常系テスト() {
		List<ProductInfo> cartList = new ArrayList<ProductInfo>();
		ProductInfo productInfo = new ProductInfo();

		final Integer userId = 111;

		productInfo.setCartId(null);
		productInfo.setProductId(null);
		productInfo.setProductName(null);
		productInfo.setPrice(null);
		productInfo.setPurchaseQty(null);

		cartList.add(productInfo);

		//引数・戻り値の設定
		when(cartInfoMapper.selectCartInfo(userId)).thenReturn(cartList);

		//実行
		List<ProductInfo> expectedCartList = cartInfoService.getCartInfo(userId);

		//検証
		assertAll(
				() -> verify(cartInfoMapper, times(1)).selectCartInfo(userId),
				() -> assertEquals(null, expectedCartList.get(0).getCartId()),
				() -> assertEquals(null, expectedCartList.get(0).getProductId()),
				() -> assertEquals(null, expectedCartList.get(0).getProductName()),
				() -> assertEquals(null, expectedCartList.get(0).getPrice()),
				() -> assertEquals(null, expectedCartList.get(0).getPurchaseQty()));
	}

	@Test
	void UserServiceクラスのupDateAddGiftAmountメソッドが呼び出されているかのテスト() {
		final Integer chargeAmount = 1500;
		final Integer userId = 1;

		userService.upDateAddGiftAmount(chargeAmount, userId);


		verify(userMapper, times(1)).upDateAddGiftAmount(chargeAmount, userId);

	}

	@Test
	void UserServiceクラスのselectGiftAmountメソッドの正常系テスト() {
		final Integer giftAmount = 3000;
		final Integer userId = 1;
		final Integer actualGiftAmount = 3000;
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
	void UserServiceクラスのselectGiftAmountメソッドの異常系テスト() {
		final Integer giftAmount = null;
		final Integer userId = 111;

		//引数・戻り値の設定
		when(userMapper.selectIdMatchGiftAmount(userId)).thenReturn(giftAmount);

		//実行
		Integer expectedGiftAmount = userService.selectGiftAmount(userId);
		//検証
		assertAll(
				() -> verify(userMapper, times(1)).selectIdMatchGiftAmount(userId),
				() -> assertEquals(null, expectedGiftAmount));
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
		//セッションに入れる値を設定
		Integer setUserId = 1;

		MockHttpSession mockSession = new MockHttpSession();

		mockSession.setAttribute("userId", setUserId);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(mockSession);

		Integer userId = (Integer) mockSession.getAttribute("userId");

		return userId;
	}

	private String getUserNameSession () {
		//セッションに入れる値を設定
		String setUserName = "koki";

		MockHttpSession mockSession = new MockHttpSession();

		mockSession.setAttribute("userName", setUserName);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(mockSession);

		String  userName = (String) mockSession.getAttribute("userName");

		return userName;
	}

}
