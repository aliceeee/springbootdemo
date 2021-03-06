package org.alice;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled=true)
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	}
	
//	@Bean
//	public ReflectionSaltSource reflectionSaltSource(){
//		ReflectionSaltSource reflectionSaltSource = new ReflectionSaltSource();
//		reflectionSaltSource.setUserPropertyToUse("username");
//		return reflectionSaltSource;
//	}
//	
//	@Bean
//	public ShaPasswordEncoder shaPasswordEncoder(){
//		ShaPasswordEncoder encoder = new ShaPasswordEncoder();
//		return encoder;
//	}
//	
//	@Bean
//	public DaoAuthenticationProvider daoAuthenticationProvider(){
//		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//		daoAuthenticationProvider.setPasswordEncoder(shaPasswordEncoder());
//		daoAuthenticationProvider.setSaltSource(reflectionSaltSource());
//		return daoAuthenticationProvider;
//	}

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		@Autowired
		private DataSource dataSource;
//		@Autowired
//		private DaoAuthenticationProvider daoAuthenticationProvider;

		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable().antMatcher("/api/**").authorizeRequests().anyRequest().hasAnyRole("ADMIN","USER").and().httpBasic();
		}

		@Override
		public void configure(AuthenticationManagerBuilder auth) throws Exception {
			ShaPasswordEncoder encoder = new ShaPasswordEncoder();
			auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(encoder);
		}
	}

	@Configuration
	@Order(2)
	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN").and().withUser("user").password("user").roles("USER");
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable().authorizeRequests().antMatchers( "/login", "/logout", "/error").permitAll().anyRequest().authenticated().and()
					.formLogin().loginPage("/login.html").permitAll().and().logout().permitAll().logoutSuccessUrl("/login.html")
					.invalidateHttpSession(true);
		}
	}

}