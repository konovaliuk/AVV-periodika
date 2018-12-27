-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema periodika
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `periodika` ;

-- -----------------------------------------------------
-- Schema periodika
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `periodika` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;
USE `periodika` ;

-- -----------------------------------------------------
-- Table `periodika`.`periodical_category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `periodika`.`periodical_category` ;

CREATE TABLE IF NOT EXISTS `periodika`.`periodical_category` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `type` INT NOT NULL,
  `description` TEXT NULL,
  `deleted` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `periodika`.`user_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `periodika`.`user_type` ;

CREATE TABLE IF NOT EXISTS `periodika`.`user_type` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `periodika`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `periodika`.`user` ;

CREATE TABLE IF NOT EXISTS `periodika`.`user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_type_id` INT(11) NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `middle_name` VARCHAR(45) NULL DEFAULT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `address` VARCHAR(255) NOT NULL,
  `phone` VARCHAR(45) NULL DEFAULT NULL,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(32) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_user_type_idx` (`user_type_id` ASC) VISIBLE,
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE,
  CONSTRAINT `fk_user_user_type`
    FOREIGN KEY (`user_type_id`)
    REFERENCES `periodika`.`user_type` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 8
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `periodika`.`periodical`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `periodika`.`periodical` ;

CREATE TABLE IF NOT EXISTS `periodika`.`periodical` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL DEFAULT '',
  `description` TEXT NULL,
  `periodical_category_id` INT NOT NULL,
  `min_subscription_period` INT NOT NULL DEFAULT 1,
  `issues_per_period` INT NOT NULL DEFAULT 1,
  `price_per_period` DECIMAL(10,2) NOT NULL DEFAULT 0,
  `subscription_index` VARCHAR(10) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_periodical_category1_idx` (`periodical_category_id` ASC) VISIBLE,
  CONSTRAINT `fk_periodical_category1`
    FOREIGN KEY (`periodical_category_id`)
    REFERENCES `periodika`.`periodical_category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `periodika`.`payment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `periodika`.`payment` ;

CREATE TABLE IF NOT EXISTS `periodika`.`payment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` TIMESTAMP NOT NULL,
  `sum` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `periodika`.`subscription`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `periodika`.`subscription` ;

CREATE TABLE IF NOT EXISTS `periodika`.`subscription` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `periodical_id` INT NOT NULL,
  `payment_id` INT NULL,
  `period_start` DATE NOT NULL,
  `period_count` INT NOT NULL,
  `quantity` INT NOT NULL DEFAULT 1,
  `sum` DECIMAL(10,2) NOT NULL,
  `active` TINYINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_subscription_user1_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_user_subscription_periodical1_idx` (`periodical_id` ASC) VISIBLE,
  INDEX `fk_subscription_paiment1_idx` (`payment_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_subscription_periodical1`
    FOREIGN KEY (`periodical_id`)
    REFERENCES `periodika`.`periodical` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_subscription_paiment1`
    FOREIGN KEY (`payment_id`)
    REFERENCES `periodika`.`payment` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_subscription_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `periodika`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
