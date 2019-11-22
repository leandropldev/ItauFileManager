package br.com.itau.filemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import br.com.itau.filemanager.config.FileStorageConfig;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageConfig.class})
public class ItauFileManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItauFileManagerApplication.class, args);
	}

}
