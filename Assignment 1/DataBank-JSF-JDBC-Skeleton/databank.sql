-- -----------------------------------------------------
-- Schema
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `databank` ;
CREATE SCHEMA IF NOT EXISTS `databank` DEFAULT CHARACTER SET utf8mb4 ;
USE `databank` ;

-- -----------------------------------------------------
-- Table `databank`.`person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `databank`.`person` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(50) NOT NULL,
  `last_name` VARCHAR(50) NOT NULL,
  `email` VARCHAR(100) NULL,
  `phone` VARCHAR(10) NULL,
  `created` BIGINT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

SET SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
CREATE USER IF NOT EXISTS 'cst8277'@'localhost' IDENTIFIED BY '8277';
GRANT ALL ON `databank`.* TO 'cst8277'@'localhost';