package com.project.springsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.project.springsecurity.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private UserService userService;

	@Bean
	public AuthenticationSuccessHandler customAuthendicationHandler() {
		return new CustomAuthendicationSuccesHandler();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(registory -> {
			registory.requestMatchers("/", "/register/**","/signIn/**").permitAll();
			registory.requestMatchers("/admin/**").hasRole("ADMIN");
			registory.requestMatchers("/user/**").hasAnyRole("ADMIN", "USER");
			registory.anyRequest().authenticated();
		}).formLogin(httpSecurityFormLoginConfigurer -> {
			httpSecurityFormLoginConfigurer
			.loginPage("/login")
			.successHandler(customAuthendicationHandler())
			.permitAll();
		}).logout(httpSecurityFormLogoutConfigurer ->{
			httpSecurityFormLogoutConfigurer
			.logoutUrl("/logout")
			.logoutSuccessUrl("/");
		})
			.build();
	}

	/*** Load User to Login from Heap Memory */
	/*
	 * @Bean public UserDetailsService userDetailsService() { UserDetails user =
	 * User.builder().username("Sridhar")
	 * .password("$2a$12$1N7AIFjhe1lmdYj0ghEyYOVQRBFTIiFEKt4/iK6/8nBO6iW8uW6se").
	 * roles("USER").build(); UserDetails admin = User.builder().username("Ananya")
	 * .password("$2a$12$sR0qtyJexBsps85kDCn2e.kfvAKx0Uyl8AbupwX3kCgcPvF8aQSVW").
	 * roles("ADMIN", "USER") .build(); return new InMemoryUserDetailsManager(user,
	 * admin); }
	 */

	@Bean
	public UserDetailsService userDetailsService() {
		return userService;
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}