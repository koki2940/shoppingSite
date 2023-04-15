package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.UserInfo;
import com.example.persistence.UserMapper;
import com.example.web.form.UserLoginForm;
import com.example.web.form.UserRegisterForm;

@Service
public class UserDBService {

	@Autowired
	UserMapper userMapper;

	@Autowired
	private PasswordEncoder encoder;

	/**
	 * 入力したユーザー情報を登録する
	 * @param user 入力情報
	 */
	@Transactional
	public void insertUser(UserInfo user) {
		//パスワードの暗号化 SpringSecurity
		String rawPassword = user.getPassword();
		user.setPassword(encoder.encode(rawPassword));
		userMapper.insertUserInfo(user);
	}

	/**
	 * ユーザー名の重複チェックを行う
	 * @param form 入力情報
	 * @return 入力したユーザー名に該当するDB内のデータ件数 情報が存在しなかった場合、0が返る
	 */
	public int userNameCheck(UserRegisterForm form) {
		int cnt = userMapper.selectUserNameDuplicationCheck(form);

		return cnt;
	}
	/**
	 * メールアドレスの重複チェックを行う
	 * @param form 入力情報
	 * @return 入力したメールアドレスに該当するDB内のデータ件数 情報が存在しなかった場合、0が返る
	 */
	public int mailCheck(UserRegisterForm form) {
		int cnt = userMapper.selectMail(form);
		return cnt;
		
	}

	/**
	 * ログインで入力したユーザー情報を取得する
	 *
	 * @param form ログイン時の入力情報
	 * @return  ユーザー情報 データが取得できなかった場合 nullが返る
	 */
	public List<UserInfo> accountJudge(UserLoginForm form) {
		//ユーザ名とパスワードに該当する情報件数を取得
		List<UserInfo> userList = userMapper.selectAccount(form);

		return userList;
	}

	/**
	 * チャージした金額分を加算する
	 *
	 * @param giftAmount チャージ金額
	 * @param userId ユーザーID
	 */
	@Transactional
	public void upDateAddGiftAmount(Integer chargeAmount, Integer userId) {
		userMapper.upDateAddGiftAmount(chargeAmount, userId);
	}

	/**
	 * ギフト券の残高から商品の合計金額分を減算する
	 *
	 * @param giftAmount 商品の合計金額
	 * @param userId ユーザーID
	 */
	@Transactional
	public void upDateSubstractGiftAmount(Integer totalAmount, Integer userId) {
		userMapper.upDateSubstractGiftAmount(totalAmount, userId);
	}

	/**
	 * 最新のギフト券の金額を取得する
	 * @param userId ユーザーID
	 * @return ギフト券金額 データが取得できなかった場合 nullが返る
	 */
	public Integer selectGiftAmount(Integer userId) {
		Integer giftAmount = userMapper.selectIdMatchGiftAmount(userId);

		return giftAmount;
	}

	/**
	 * ログイン時の認証時にユーザー名に該当するユーザー情報を取得する(SpringSecurity)
	 * @param userName ログイン時のユーザー名
	 * @return ユーザー情報 データが取得できなかった場合 nullが返る
	 */
	public UserInfo getLoginUser(String userName) {
		UserInfo info = userMapper.findLoginUser(userName);

		return info;
	}

	/**
	 * ログインしたユーザー情報を取得する(SpringSecurity)
	 * @param userName ログイン時に入力したユーザー名
	 * @return ユーザー情報 データが取得できなかった場合 nullが返る
	 */
	public UserInfo getLoginSucceedUser(String userName) {
		UserInfo info = userMapper.findLoginSucceedUser(userName);

		return info;
	}

}
