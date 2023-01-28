package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.User;
import com.example.persistence.UserMapper;
import com.example.web.form.UserLoginForm;
import com.example.web.form.UserRegisterForm;

@Service
public class UserDBService {

	@Autowired
	UserMapper userMapper;

	/**
	 * 入力したユーザー情報を登録する
	 * @param user 入力情報
	 */
	@Transactional
	public void insertUser(User user) {
		userMapper.insert(user);
	}

	/**
	 * ユーザー名の重複チェックを行う
	 * @param form 入力情報
	 * @return ユーザー名に該当するDB内のデータ件数
	 */
	public int userNameCheck(UserRegisterForm form) {
		int cnt = userMapper.selectUserName(form);

		return cnt;
	}


	/**
	 * ログインで入力したユーザー情報を取得する
	 *
	 * @param form ログイン時の入力情報
	 * @return  ユーザー情報
	 */
	public List<User> accountJudge(UserLoginForm form) {
		//ユーザ名とパスワードに該当する情報件数を取得
		List<User>userList = userMapper.selectAccount(form);

		return userList;
	}

	/**
	 * チャージした金額分を加算する
	 *
	 * @param giftAmount チャージ金額
	 * @param userId ユーザーID
	 */
	@Transactional
	public void updateAddGiftAmount(Integer chargeAmount, Integer userId) {
		userMapper.updateAddAmount(chargeAmount, userId);
	}

	/**
	 * ギフト券の残高から商品の合計金額分を減算する
	 *
	 * @param giftAmount 商品の合計金額
	 * @param userId ユーザーID
	 */
	@Transactional
	public void updateSubstractGiftAmount(Integer totalAmount, Integer userId) {
		userMapper.updateSubstractAmount(totalAmount, userId);
	}


	/**
	 * 最新のギフト券の金額を取得する
	 * @param userId ユーザーID
	 * @return ギフト券金額
	 */
	public Integer selectGiftAmount(Integer userId) {
		Integer giftAmount = userMapper.selectAmount(userId);

		return giftAmount;
	}


}
