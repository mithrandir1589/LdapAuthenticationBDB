package co.com.bancodebogota.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.bancodebogota.services.KeyService;

@RestController
public class HomeController {
	@Autowired
	private KeyService keyService;
	
	
	@GetMapping("/login")
	public String index() {
		return "Usuario autenticado con éxito";
	}
	
	@GetMapping("/key")
	public String getPublicKey() {
		return keyService.getPublicKey();
	}

}
