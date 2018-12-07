package co.com.bancodebogota.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service

public class KeyService implements IKeyService {
	
	@Value("${key.public}")
	private String publicKey;
	
	@Override
	public String getPublicKey() {
		return this.publicKey;
	}	
}
