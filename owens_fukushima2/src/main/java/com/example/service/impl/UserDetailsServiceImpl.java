package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.domain.UserInfo;
import com.example.service.UserDBService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserDBService service;

	@Override
	public UserDetails loadUserByUsername(String userName)throws UsernameNotFoundException{

		//ユーザー情報取得
		UserInfo loginUser = service.getLoginUser(userName);

		//ユーザーが存在しない場合
		if(loginUser == null) {
			throw new UsernameNotFoundException("class not found");
		}

		//権限リスト作成
		GrantedAuthority authority = new SimpleGrantedAuthority(String.valueOf(loginUser.getAuthority()));

		List<GrantedAuthority> authorities = new ArrayList<>();

		authorities.add(authority);

		//UserDetails生成
		UserDetails userDetails = (UserDetails)new User(loginUser.getUserName(),loginUser.getPassword()
				,authorities);


		return userDetails;

	}


}
