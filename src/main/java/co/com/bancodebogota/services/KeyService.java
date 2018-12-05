package co.com.bancodebogota.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KeyService implements IKeyService {
	
	@Value("${key.public}")
	private String publicKey;
	
	@Value("${key.private}")
	private String privateKey;
		
	
	@Override
	public String getPublicKey() {
		return this.publicKey;
	}

	@Override
	public String getPrivateKey() {
		return this.privateKey;
	}	
}
