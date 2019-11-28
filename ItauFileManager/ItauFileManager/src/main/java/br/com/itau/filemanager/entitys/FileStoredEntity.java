package br.com.itau.filemanager.entitys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_FILE_STORED")
public class FileStoredEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "file_name", nullable = false, length = 80)
	private String fileName;
	
	@Column(name = "file_type", nullable = false, length = 80)
	private String fileType;
	
	@Column(name = "file_uri", nullable = false, length = 80)
	@Lob
	private String fileDownloadUri;
	
	@Column(name = "file_size", nullable = false)
	private Long fileSize;
}
