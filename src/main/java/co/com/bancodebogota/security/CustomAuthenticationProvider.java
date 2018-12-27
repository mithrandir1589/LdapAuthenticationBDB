package co.com.bancodebogota.security;

import java.io.IOException;
import java.util.ArrayList;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{

	private final String RESOURCE = "https://graph.microsoft.com";
    //Id de la aplicacion matriculada en Azure Active Directory
	private final String CLIENT_ID = "c7faecbf-ed5d-4a12-a1f6-0c9d52643d57";
	//URL a la cual se realiza el consumo post, 
	private final String URL = "https://login.microsoftonline.com/0f599d33-5912-42f8-9edc-294e3642a137/oauth2/token";
	
	//Client secret de la aplicacion
	private final String CLIENT_SECRET = "qnPlxJs27jiqjeocy62aDrh5S1TwHebi9Hwz4L2s/5U=";
	
	private final String GRANT_TYPE = "password";
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException, RestClientException {
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
	
	/**
	 * 
	 * @param username Usuario (ya descifado) que intenta autenticarse
	 * @param password Password (ya descrifrado) del usuario que intenta autenticarse
	 * @return Objeto en formato JSON con la respuesta al consumo rest para obtener autenticacion. Retorna null si la autenticacion
	 * no fue exitosa
	 */
	private JsonNode authenticateAzure(String username, String password) throws RestClientException{
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
		
		
		response = restTemplate.postForObject(URL, request, String.class);
		try {
			jsonResponse = mapper.readTree(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		return jsonResponse;
	}
}
