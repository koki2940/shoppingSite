package com.example.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.domain.UserInfo;
import com.example.web.form.UserLoginForm;
import com.example.web.form.UserRegisterForm;

public interface UserMapper {

	/**
	 * ユーザー名の重複チェックを行うメソッド
	 * @param form 登録時のユーザー情報
	 * @return 登録時のユーザー名に該当するDB内のユーザー情報の件数 情報が存在しなかった場合、0が返る
	 */
	public int selectUserNameDuplicationCheck(UserRegisterForm form);
	
	/**
	 * メールアドレスの重複チェックを行うメソッド
	 * @param form 登録時の入力情報
	 * @return 登録時のメールアドレスに該当するDB内のユーザー情報の件数 情報が存在しなかった場合、0が返る
	 */
	public int selectMail(UserRegisterForm form);

	/**
	 * ユーザー情報を登録をするメソッド
	 * @param user 登録時のユーザー情報
	 */
	public void insertUserInfo(UserInfo user);

	/**
	 * ユーザー情報を取得し、アカウント有無を判定するメソッド
	 * @param form ログイン時の入力情報
	 * @return 入力情報に該当するユーザー情報 情報が取得できなかった場合、nullが返る
	 */
	public List<UserInfo> selectAccount(UserLoginForm form);

	/**
	 * チャージした金額をDBのgift_amountに加算するメソッド
	 * @param chargeAmount ギフト券金額
	 * @param userId ログイン中のユーザーId
	 */
	public void upDateAddGiftAmount(@Param("chargeAmount") Integer chargeAmount, @Param("userId") Integer userId);

	/**
	 * ギフト券の残高から商品の合計金額を減算するメソッド
	 * @param totalAmont 合計金額
	 * @param userId ログイン中のユーザーId
	 */
	public void upDateSubstractGiftAmount(@Param("totalAmount") Integer totalAmont, @Param("userId") Integer userId);

	/**
	 * ギフト券金額を取得するメソッド
	 * @param userId ログイン中のユーザーId
	 * @return ギフト券金額 情報が取得できなかった場合、nullが返る
	 */
	public Integer selectIdMatchGiftAmount(Integer userId);

	/**
	 * ログイン認証用ユーザー取得(SpringSecurity用)
	 * @param userName ログイン時に入力したユーザー名
	 * @return ユーザー情報(ユーサー名、パスワード、権限) 情報が取得できなかった場合、nullが返る
	 */
	public UserInfo findLoginUser(String userName);

	/**
	 * ログインしたユーザー情報を取得
	 * @param userName ログイン時に入力したユーザー名
	 * @return ユーザー情報(ユーザーId、権限、ギフト券金額) 情報が取得できなかった場合、nullが返る
	 */
	public UserInfo findLoginSucceedUser(String userName);

}
