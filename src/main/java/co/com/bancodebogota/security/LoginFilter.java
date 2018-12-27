package co.com.bancodebogota.security;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import co.com.bancodebogota.model.User;
import co.com.bancodebogota.utils.DecryptUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {


  private static LoginFilter loginFilter;

    
    public static LoginFilter getSingletonInstance() {
  	return LoginFilter.loginFilter;
  }

	
	
	
	
public LoginFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

@Override
public final void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
    final HttpServletResponse response = (HttpServletResponse) res;
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Authorization, Origin, Content-Type, Version");
    response.setHeader("Access-Control-Expose-Headers", "X-Requested-With, Authorization, Origin, Content-Type");
    

    final HttpServletRequest request = (HttpServletRequest) req;
    if (!request.getMethod().equals("OPTIONS")) {
    	chain.doFilter(req, res);
    } else {
    }
}
	
	
	private DecryptUtil decrypUtil = new DecryptUtil();
	
	public LoginFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
        LoginFilter.loginFilter = this;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
	    
		String input = request.getHeader("Authorization");
		System.out.println("=====================================================");
		System.out.println(input);
		System.out.println("=====================================================");
		String credentials[] = input.split(":");
		
		User user = new User();
		try {
			user.setUsername(decrypUtil.decryptMessage(credentials[0]));
			user.setPassword(decrypUtil.decryptMessage(credentials[1]));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
				
		Authentication authentication =  getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
						user.getUsername(),
						user.getPassword(),
						Collections.emptyList()						
				)
		);
		
		return authentication;
	}
}