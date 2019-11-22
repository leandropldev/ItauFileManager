package br.com.itau.filemanager.exceptionHandler;

import java.util.Date;

import org.apache.tomcat.util.http.fileupload.FileUploadBase.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.itau.filemanager.exceptions.ExceptionResponse;
import br.com.itau.filemanager.exceptions.FileNotFoundException;
import br.com.itau.filemanager.exceptions.FileStorageException;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(FileNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleFileNotFoundException(Exception ex, WebRequest request){
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(FileStorageException.class)
	public final ResponseEntity<ExceptionResponse> handleFileStorageException(Exception ex, WebRequest request){
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(SizeLimitExceededException.class)
	public final ResponseEntity<ExceptionResponse> handleSizeLimitExceededException(Exception ex, WebRequest request){
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public final ResponseEntity<ExceptionResponse> handleMaxUploadSizeExceededException(Exception ex, WebRequest request){
		String[] messageParts = ex.getMessage().split(":");
		String msg = messageParts[messageParts.length - 1];
				
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), msg, request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}