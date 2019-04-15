package com.iig.gcp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.userdetails.InetOrgPersonContextMapper;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${parent.front.micro.services}")
	private String parent_micro_services;

	@Autowired

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();
		http

				.authorizeRequests().antMatchers("/*", "/login", "/hip/**", "/hip", "/hipmaster", "/hipmaster/**")
				.permitAll()

				.and().exceptionHandling().accessDeniedPage("/accessDenied").and().formLogin().loginPage("/login")
				.defaultSuccessUrl("/login/submit").permitAll();
	}

	@Bean
	public UserDetailsContextMapper userDetailsContextMapper() {
		return new InetOrgPersonContextMapper();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/assets/**");
	}
}