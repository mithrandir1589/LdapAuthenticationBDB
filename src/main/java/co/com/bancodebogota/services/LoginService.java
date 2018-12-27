package co.com.bancodebogota.services;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import co.com.bancodebogota.security.LoginFilter;

@Service
public class LoginService {
	
	public String authenticate(HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = null;
		String token= null;
		
		try {
			authentication = LoginFilter.getSingletonInstance().attemptAuthentication(request, response);
			token = getToken(authentication.getName());
			SecurityContextHolder.getContext().setAuthentication(authentication);
					
		} catch (RestClientException rce) {
			if (rce instanceof HttpClientErrorException) {
				HttpClientErrorException clientEx = ((HttpClientErrorException)rce);
				if(clientEx.getStatusCode() == HttpStatus.BAD_REQUEST){
					return HttpStatus.BAD_REQUEST.toString();
				}else {
					rce.printStackTrace();
					
					return "Ha ocurrido un error autenticando al usuario: " + rce.getMessage();
				}
			}
		}  catch (ServletException se) {
			se.printStackTrace();
			return "Ha ocurrido un error autenticando al usuario: " + se.getMessage();
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return "Ha ocurrido un error autenticando al usuario: " + ioe.getMessage();
		}
		return "Bearer " + token;	
	}
	
	private String getToken(String username) {
		RestTemplate restTemplate = new RestTemplate();
		String token = restTemplate.getForObject("http://tpkengeneratorbdb:8080/getToken"+"/"+username, String.class);
		return token;
	}

}