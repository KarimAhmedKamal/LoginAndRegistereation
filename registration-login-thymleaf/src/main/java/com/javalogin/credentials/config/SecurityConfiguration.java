package com.javalogin.credentials.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.javalogin.credentials.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Lazy
	@Autowired
	private UserService userService;
	
	@Bean
	 BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}

	@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
	
	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    	http.authorizeHttpRequests(
    				(requests) -> requests
    				.requestMatchers(
    		    			"/registration**",
    		    			"/js/**",
    		    			"/css/**",
    		    			"/img/**"
    		    	).permitAll()
    		    	.anyRequest()
    		    	.authenticated()
    			)
    	.formLogin(
    				(form) -> form
    				.loginPage("/login")
    		    	.permitAll()
    			)
    	.logout(
    				(logout) -> logout
    		    	.invalidateHttpSession(true)
    		    	.clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout")
    		        .permitAll()
    			);
        return http.build();
    }

}
