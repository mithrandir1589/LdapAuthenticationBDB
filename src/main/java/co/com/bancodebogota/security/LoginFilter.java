package co.com.bancodebogota.security;

import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;
import co.com.bancodebogota.model.User;
import co.com.bancodebogota.utils.DecryptUtil;



public class LoginFilter extends AbstractAuthenticationProcessingFilter {

	
	private DecryptUtil decrypUtil = new DecryptUtil();
	
	public LoginFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
	}

	
	static void addAuthentication(HttpServletResponse res, String username) {
		RestTemplate restTemplate = new RestTemplate();
		String token = restTemplate.getForObject("http://tpkengeneratorbdb:8080/getToken"+"/"+username, String.class);
		res.addHeader("Authorization", "Bearer " + token);
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		String input = request.getHeader("Authorization");
		String credentials[] = input.split(":");
		
		User user = new User();
		try {
			user.setUsername(decrypUtil.decryptMessage(credentials[0]));
			user.setPassword(decrypUtil.decryptMessage(credentials[1]));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
						user.getUsername(),
						user.getPassword(),
						Collections.emptyList()						
				)
		);
	}
	
	@Override
    protected void successfulAuthentication(
            HttpServletRequest req,
            HttpServletResponse res, FilterChain chain,
            Authentication auth) throws IOException, ServletException {
		addAuthentication(res, auth.getName());        
    }
}
