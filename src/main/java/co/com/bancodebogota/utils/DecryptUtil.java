package co.com.bancodebogota.utils;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class DecryptUtil {
	
	private final String PRIVATEKEYFILE = "privateKey";
	private final String PUBLICKEYFILE  = "publicKey";
	
	private String privateKey;	
	private String publicKey;	
	
	
	public String decryptMessage(String message) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
		byte[] byteString = cipher.doFinal(Base64.getDecoder().decode(message));
		return new String(byteString);
	}
	

	private PrivateKey getPrivateKey() throws GeneralSecurityException {
		try {
			this.publicKey = getKeyFromFile(PUBLICKEYFILE);
			this.privateKey = getKeyFromFile(PRIVATEKEYFILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BigInteger publicKeyInt = new BigInteger(this.publicKey, 16);
    	BigInteger privKeyInt = new BigInteger(this.privateKey, 16);
    	BigInteger exponentInt = new BigInteger("10001", 16);
        
    	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    	
    	RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(publicKeyInt, exponentInt);
    	PublicKey puk = keyFactory.generatePublic(publicKeySpec);
    	
    	RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(publicKeyInt, privKeyInt);
    	PrivateKey prk = keyFactory.generatePrivate(privateKeySpec);
    	
    	return prk;
	}
	
	private String getKeyFromFile(String fileName) throws IOException {

        ClassLoader classLoader = new DecryptUtil().getClass().getClassLoader();
 
        File file = new File(classLoader.getResource(fileName).getFile());
         
        String key = new String(Files.readAllBytes(file.toPath()));
        return key;
	}

}
