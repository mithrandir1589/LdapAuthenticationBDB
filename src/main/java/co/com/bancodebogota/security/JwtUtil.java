package co.com.bancodebogota.security;

import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {
	
	static void addAuthentication(HttpServletResponse res, String username) {
		String token = Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis()+ 6000000))
				.signWith(SignatureAlgorithm.HS512, "Mithr@ndir")
				.compact();
		res.addHeader("Authorization", "Bearer " + token);
	}
	
	static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(token != null) {
			String user = Jwts.parser()
					.setSigningKey("Mithr@ndir")
					.parseClaimsJws(token.replace("Bearer", ""))
					.getBody()
					.getSubject();
			return user != null ? 
					new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList()): null;
		}
		return null;
		
		
	}

}
