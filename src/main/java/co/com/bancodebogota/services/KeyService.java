/**
 * @author mquint2
 * Clase encargada de obtener la llave publica y retornarla para que las aplicaciones front que hagan uso de ella 
 * cifren las credenciales de los usuarios
 */
package co.com.bancodebogota.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KeyService implements IKeyService {
	//TODO Definir una ubicacion y forma de generacion de las llaves que actualmente estan en el archivo application.porperties
	@Value("${key.public}")
	private String publicKey;
	
	/**
	 * @return La llave publica 
	 */
	@Override
	public String getPublicKey() {
		return this.publicKey;
	}	
}
