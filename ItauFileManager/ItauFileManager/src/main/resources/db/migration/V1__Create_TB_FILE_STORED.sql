CREATE TABLE IF NOT EXISTS `TB_FILE_STORED` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) NOT NULL,
  `file_type` varchar(255) NOT NULL,
  `file_uri` varchar(255) NOT NULL,
  `file_size` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;