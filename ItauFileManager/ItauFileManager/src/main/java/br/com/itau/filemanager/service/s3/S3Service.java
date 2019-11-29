package br.com.itau.filemanager.service.s3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import br.com.itau.filemanager.entitys.UploadedFileModel;
import br.com.itau.filemanager.exceptions.FileStorageException;

@Service
public class S3Service {
	
	private AmazonS3 s3;
	private String bucketName;
	private String region;
	
	@Autowired
	public S3Service(AmazonS3 amazonS3, String awsRegion, String awsS3Bucket) {
		this.s3 = amazonS3;
		this.region = awsRegion;
		this.bucketName = awsS3Bucket;
	}
	
	public List<UploadedFileModel> upload(MultipartFile[] files){
		
		List<UploadedFileModel> uploadedFiles = new ArrayList<>();
		
		for (MultipartFile file : files) {
			String originalName = file.getOriginalFilename();
			String s3FileName = getUniqueFileName(originalName);
			
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(file.getSize());
			try {
				PutObjectRequest request = new PutObjectRequest(bucketName, s3FileName, file.getInputStream(), metadata)
						.withCannedAcl(CannedAccessControlList.PublicRead);
				s3.putObject(request);
				
				String location = getFileLocation(s3FileName);
				
				UploadedFileModel uploadedFileModel = new UploadedFileModel(originalName, location, file.getContentType(), file.getSize());
				uploadedFiles.add(uploadedFileModel);
			} catch (Exception e) {
				throw new FileStorageException("Erro ao enviar ficheiro para o AWS S3");
			}
		}
		return uploadedFiles;
	}
	
	private String getFileLocation(String fileName) {
		return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
	}
	
	private String getUniqueFileName(String fileName) {
		return UUID.randomUUID().toString() + "_" + fileName;
	}
}