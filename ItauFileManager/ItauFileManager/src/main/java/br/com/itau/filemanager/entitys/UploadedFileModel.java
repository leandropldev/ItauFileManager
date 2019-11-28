package br.com.itau.filemanager.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UploadedFileModel {
	private String nome;
	private String location;
	private String type;
	private long size;

}
