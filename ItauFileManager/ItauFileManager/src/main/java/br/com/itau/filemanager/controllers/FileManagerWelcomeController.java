package br.com.itau.filemanager.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/welcome")
public class FileManagerWelcomeController {

	@GetMapping
	public String welcome() {
		return "Bem vindo à Aplicação de transferência de arquivos!!!!";
	}
}
