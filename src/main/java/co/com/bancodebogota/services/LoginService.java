package co.com.bancodebogota.services;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import co.com.bancodebogota.security.LoginFilter;

@Service
public class LoginService {
	
	public String authenticate(HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = null;
		String token= null;
		try {
			authentication = LoginFilter.getSingletonInstance().attemptAuthentication(request, response);
			System.out.println(authentication.isAuthenticated());
			System.out.println(response.getHeader("Authorization")); 
			System.out.println(authentication.getName());
			token = getToken(authentication.getName());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			return token;
			
		} catch (org.springframework.security.core.AuthenticationException | IOException | ServletException e) {
			return "Error en la autenticación";
		}        
	}
	
	private String getToken(String username) {
		RestTemplate restTemplate = new RestTemplate();
		String token = restTemplate.getForObject("http://tpkengeneratorbdb:8080/getToken"+"/"+username, String.class);
		return token;
	}

}
