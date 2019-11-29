package br.com.itau.filemanager.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import br.com.itau.filemanager.config.FileStorageConfig;
import br.com.itau.filemanager.entitys.FileStoredEntity;
import br.com.itau.filemanager.entitys.UploadedFileModel;
import br.com.itau.filemanager.exceptions.FileNotFoundException;
import br.com.itau.filemanager.exceptions.FileStorageException;
import br.com.itau.filemanager.repository.FileStoredRepository;
import br.com.itau.filemanager.service.s3.S3Service;

@Service
public class FileStorageService {

	private final Path fileStorageLocation;
	
	@Autowired
	private FileStoredRepository fileStoredRepository;
	
	@Autowired
	private S3Service s3Service;
	
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
	
	public List<FileStoredEntity> uploadS3Files(MultipartFile[] files) {
		
		for (MultipartFile file : files) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			
			if (fileName.contains("..")) {
				throw new FileStorageException("Filename contains invalid path sequence: " + fileName);
			}
			if ("application/pdf".equals(file.getContentType())) {
				throw new FileStorageException("PDF is not a valid format to upload!");
			}
		}
		
		List<UploadedFileModel> uploadedS3Files = s3Service.upload(files);
		List<FileStoredEntity> fileStoredListEntity = new ArrayList<>();
		
		for (UploadedFileModel s3File : uploadedS3Files) {
			
			FileStoredEntity fileStoredEntity = FileStoredEntity.builder()
					.fileName(s3File.getNome())
					.fileType(s3File.getType())
					.fileDownloadUri(s3File.getLocation())
					.fileSize(s3File.getSize())
					.build();
			fileStoredListEntity.add(fileStoredEntity);
		}
		
		return fileStoredRepository.saveAll(fileStoredListEntity);
	}
	
	public List<FileStoredEntity> getAllAwsFiles() {
		return fileStoredRepository.findAll();
	}
}
