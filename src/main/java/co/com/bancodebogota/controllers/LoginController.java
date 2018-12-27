package co.com.bancodebogota.controllers;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import co.com.bancodebogota.services.KeyService;
import co.com.bancodebogota.services.LoginService;

@RestController
public class LoginController {
	
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
		HttpHeaders responseHeaders = new HttpHeaders();
		String authResponse = loginService.authenticate(request, response);
		if(authResponse.equals(HttpStatus.BAD_REQUEST.toString())) {
			return new ResponseEntity<String>("Usuario o contraseña errado", responseHeaders, HttpStatus.UNAUTHORIZED);
		}else if(authResponse.startsWith("Bearer")) {
			responseHeaders.set("Authorization",authResponse);		
	        return new ResponseEntity<String>("Usuario autenticado con éxito", responseHeaders, HttpStatus.OK);
		}else {
			return new ResponseEntity<String>(authResponse, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
}
