package co.com.bancodebogota.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


import co.com.bancodebogota.services.KeyService;

@RestController
public class HomeController {
	@Autowired
	private KeyService keyService;
	
	
	@GetMapping("/key")
	public String getPublicKey() {
		return keyService.getPublicKey();
	}
	
	@GetMapping("/message")
	public String getMessage(@RequestHeader("Authorization") String message) throws Exception {
		return message;
	}

}
