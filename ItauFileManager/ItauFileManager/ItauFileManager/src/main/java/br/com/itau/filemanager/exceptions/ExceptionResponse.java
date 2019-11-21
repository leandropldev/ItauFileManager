package br.com.itau.filemanager.exceptions;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private Date timestamnp;
	private String message;
	private String details;
}
