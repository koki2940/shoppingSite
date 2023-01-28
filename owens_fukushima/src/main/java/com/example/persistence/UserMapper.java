package com.example.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.domain.User;
import com.example.web.form.UserLoginForm;
import com.example.web.form.UserRegisterForm;

public interface UserMapper {

	//ユーザー名の重複チェックを行うメソッド
	public int selectUserName(UserRegisterForm form);

	//ユーザー情報を登録をするメソッド
	public void insert(User user);

	//ユーザー情報を取得し、アカウント有無を判定するメソッド
	public List<User> selectAccount(UserLoginForm form);

	//チャージした金額をDBのgift_amountに加算するメソッド
	public void updateAddAmount(@Param("chargeAmount") Integer chargeAmount, @Param("userId") Integer userId);

	//ギフト券の残高から商品の合計金額を減算するメソッド
	public void updateSubstractAmount(@Param("totalAmount") Integer totalAmont, @Param("userId") Integer userId);

	//ギフト券金額を取得するメソッド
	public Integer selectAmount(Integer userId);

}
