package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) throws Exception{
		//セキュリティを適用しない
		web.ignoring()
		 	.antMatchers("/webjars/**")
		 	.antMatchers("/css/**")
		 	.antMatchers("/js/**")
		 	.antMatchers("/h2-console/**");

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception{
		//ログイン不要ページの設定
		http
			.authorizeRequests()
			.antMatchers("/home").permitAll() //直リンクok
			.antMatchers("/login").permitAll() //直リンクok
			.antMatchers("/register").permitAll() //直リンクok
			.antMatchers("/user-register").permitAll() //直リンクok
			.antMatchers("/user-register-check").permitAll() //直リンクok
			.antMatchers("/insertUser-end").permitAll() //直リンクok
			.antMatchers("/user-register-finish").permitAll() //直リンクok
			.anyRequest().authenticated();//それ以外は直リンクng

		//ログイン処理
		http.formLogin()
			.loginProcessingUrl("/login") //ログイン処理のパス
			.loginPage("/home")  //ログインページの指定
			.usernameParameter("userName")//ログインページのユーザー名
			.passwordParameter("password") //ログインページのパスワード
			.failureUrl("/home") //ログイン失敗時の遷移先
			.defaultSuccessUrl("/login", true); //成功後の遷移先

		//ログアウト処理
		http
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutUrl("/logout")
				.invalidateHttpSession(true);
		//※ログアウト成功時は上記の.loginPageで指定したURLに遷移するようデフォルトで設定されている


		//CSRF対策を一時的に無効
		http.csrf().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		PasswordEncoder encoder = passwordEncoder();

		//ユーザーデータで認証(DBのデータを使用)
		auth
		.userDetailsService(userDetailsService)

		.passwordEncoder(encoder);

//		//インメモリで認証テスト
//		auth
//		.inMemoryAuthentication()
//			.withUser("user")
//				.password(encoder.encode("user"))
//				.roles("GENERAL")
//				.and()
//				.withUser("admin")
//					.password(encoder.encode("admin"))
//					.roles("ADMIN");

	}
}
