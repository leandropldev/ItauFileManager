package br.com.itau.filemanager.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import br.com.itau.filemanager.config.FileStorageConfig;
import br.com.itau.filemanager.entitys.FileStoredEntity;
import br.com.itau.filemanager.exceptions.FileNotFoundException;
import br.com.itau.filemanager.exceptions.FileStorageException;
import br.com.itau.filemanager.repository.FileStoredRepository;

@Service
public class FileStorageService {

	private final Path fileStorageLocation;
	
	@Autowired
	private FileStoredRepository fileStoredRepository;
	
	@Autowired
	public FileStorageService(FileStorageConfig fileStorageConfig) {
		this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception e) {
			throw new FileStorageException("Could not create the directory where the files will be stored", e);
		}
	}
	
	public String storeFile(MultipartFile file) {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if (fileName.contains("..")) {
				throw new FileStorageException("Filename contains invalid path sequence: " + fileName);
			}
			
			if ("application/pdf".equals(file.getContentType())) {
				throw new FileStorageException("PDF is not a valid format to upload!");
			}
			
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			
			//save to base
			FileStoredEntity fileStoredEntity = FileStoredEntity.builder()
				.fileName(fileName)
				.fileType(file.getContentType())
				.fileDownloadUri(targetLocation.toString())
				.fileSize(file.getSize())
				.build();
				
			fileStoredRepository.save(fileStoredEntity);
			
			return fileName;
		} catch (IOException e) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again", e);
		}
	}
	
	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new FileNotFoundException("File not found " + fileName);
			}
		} catch (Exception e) {
			throw new FileNotFoundException("File not found " + fileName);
		}
	}
}
