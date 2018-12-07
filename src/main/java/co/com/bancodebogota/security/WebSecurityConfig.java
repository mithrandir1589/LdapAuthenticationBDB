package co.com.bancodebogota.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.
				ldapAuthentication()
					.userDnPatterns("uid={0},ou=people")
					.groupSearchBase("ou=groups")
					.contextSource()
						.url("ldap://localhost:8389/dc=springframework,dc=org")
						.and()
					.passwordCompare()
						.passwordEncoder(new LdapShaPasswordEncoder())
						.passwordAttribute("userPassword");
		 
		 
		 
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
		.antMatchers("/login").permitAll()
		.antMatchers("/key").permitAll()
		.antMatchers("/message").permitAll()
		.anyRequest().authenticated()
		.and()
		.addFilterBefore(new LoginFilter("/login", authenticationManager()),
                UsernamePasswordAuthenticationFilter.class);
	}
	
}
