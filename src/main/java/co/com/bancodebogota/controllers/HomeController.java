package co.com.bancodebogota.controllers;

import java.net.URI;

import javax.naming.AuthenticationException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import co.com.bancodebogota.security.LoginFilter;
import co.com.bancodebogota.services.KeyService;
import co.com.bancodebogota.services.LoginService;

@RestController
public class HomeController {
	@Autowired
	private KeyService keyService;
	
	@Autowired
	private LoginService loginService;
	
	@GetMapping("/key")
	public String getPublicKey() {
		return keyService.getPublicKey();
	}

	@GetMapping("/login")
	public ResponseEntity<String> getLogin(ServletRequest req,ServletResponse res) throws Exception {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
		String token = null;
		token = loginService.authenticate(request, response);		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Authorization", "Bearer " + token);		
        return new ResponseEntity<String>("Usuario autenticado con éxito", responseHeaders, HttpStatus.OK);
	}

}
