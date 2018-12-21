package co.com.bancodebogota.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{

	private final String RESOURCE = "https://graph.microsoft.com";
    //Id de la aplicacion matriculada en Azure Active Directory
	private final String CLIENT_ID = "37321a58-0f0b-4609-823e-ab52f2c6709c";
	//URL a la cual se realiza el consumo post, 
	private final String URL = "https://login.microsoftonline.com/a07fdf2f-2830-4856-9f78-cd1314c423df/oauth2/token";
	//Client secret de la aplicacion
	private final String CLIENT_SECRET = "67vmEirVbnpLga+YdI9pmhpHi/Rx2Mw0keQFFly5kYY=";
	
	private final String GRANT_TYPE = "password";
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();
		
		JsonNode response = authenticateAzure(username, password);
		
		if(response == null) {
			return null;
		}
		return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	private JsonNode authenticateAzure(String username, String password){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		RestTemplate restTemplate = new RestTemplate();		
		String response = null;
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonResponse = null;
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String,String>();
		map.add("grant_type", GRANT_TYPE);
		map.add("resource", RESOURCE);
		map.add("client_id", CLIENT_ID);
		map.add("client_secret", CLIENT_SECRET);
		map.add("username", username);
		map.add("password", password);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String,String>>(map,headers);
		
		try {
			response = restTemplate.postForObject(URL, request, String.class);
			jsonResponse = mapper.readTree(response);
		} catch (RestClientException rce) {
			return null;
		} catch (IOException e) {
			return null;
		}

		return jsonResponse;
	}
}
