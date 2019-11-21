package br.com.itau.filemanager.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileResponseDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	private String fileName;
	private String fileDownloadUri;
	private String fileType;
	private long size;
}
