package br.com.itau.filemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.itau.filemanager.entitys.FileStoredEntity;

public interface FileStoredRepository extends JpaRepository<FileStoredEntity, Long>{

}
