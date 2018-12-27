package co.com.bancodebogota.controllers;

import javax.naming.AuthenticationException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import co.com.bancodebogota.security.LoginFilter;
import co.com.bancodebogota.services.KeyService;

@RestController
public class HomeController {
	@Autowired
	private KeyService keyService;
	
	
	@GetMapping("/key")
	public String getPublicKey() {
		return keyService.getPublicKey();
	}

	@GetMapping("/login")
	public String getLogin(final ServletRequest req, final ServletResponse res) throws Exception {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
		try {
			Authentication authentication = LoginFilter.getSingletonInstance().attemptAuthentication(request, response);
			System.out.println(authentication.isAuthenticated());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		} catch (org.springframework.security.core.AuthenticationException e) {
			System.out.println("Fallo en la auth");
		}
        
        
        

		return null;
	}

}
