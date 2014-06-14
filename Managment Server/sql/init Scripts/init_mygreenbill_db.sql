SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `mygreenbilldb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `mygreenbilldb` ;

-- -----------------------------------------------------
-- Table `mygreenbilldb`.`subscription_plans`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`subscription_plans` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`subscription_plans` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NULL,
  `description` TEXT NULL,
  `path_to_fie` VARCHAR(128) NULL,
  `target_audience` ENUM('user','company') NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`billing_info`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`billing_info` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`billing_info` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `subscription_plans_id` INT NOT NULL,
  `rank` ENUM('gold','platinum') NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_billing_info_subscription_plans1_idx` (`subscription_plans_id` ASC),
  CONSTRAINT `fk_billing_info_subscription_plans1`
    FOREIGN KEY (`subscription_plans_id`)
    REFERENCES `mygreenbilldb`.`subscription_plans` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`user` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`user` (
  `id` INT NOT NULL,
  `email` VARCHAR(128) NOT NULL,
  `first_name` VARCHAR(128) NULL,
  `last_name` VARCHAR(128) NULL,
  `password` VARCHAR(128) NULL,
  `hmail_account_name` VARCHAR(128) NULL,
  `is_active` TINYINT(1) NULL,
  `join_date` DATE NULL,
  `forward_email` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`, `email`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`address`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`address` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`address` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `country` VARCHAR(128) NULL,
  `city` VARCHAR(128) NULL,
  `street_name` VARCHAR(128) NULL,
  `house_number` INT NULL,
  `postal_code` VARCHAR(128) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`company`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`company` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`company` (
  `id` INT NOT NULL,
  `email` VARCHAR(128) NOT NULL,
  `name` VARCHAR(128) NULL,
  `password` VARCHAR(128) NULL,
  `join_date` DATE NULL,
  `billing_info_id` INT NOT NULL,
  `logo_path` VARCHAR(128) NULL,
  `parser_name` VARCHAR(128) NULL,
  PRIMARY KEY (`id`, `email`),
  INDEX `fk_company_billing_info1_idx` (`billing_info_id` ASC),
  CONSTRAINT `fk_company_billing_info1`
    FOREIGN KEY (`billing_info_id`)
    REFERENCES `mygreenbilldb`.`billing_info` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`massage_info`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`massage_info` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`massage_info` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `send_to` VARCHAR(128) NULL,
  `send_from` VARCHAR(128) NULL,
  `subject` VARCHAR(128) NULL,
  `content` TEXT NULL,
  `cc` VARCHAR(128) NULL,
  `date` DATE NULL,
  `status` ENUM('sent','pending','failed') NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`file`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`file` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`file` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `path` VARCHAR(2048) NULL,
  `name` VARCHAR(128) NULL,
  `massage_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_file_massage1_idx` (`massage_id` ASC),
  CONSTRAINT `fk_file_massage1`
    FOREIGN KEY (`massage_id`)
    REFERENCES `mygreenbilldb`.`massage_info` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`user_client_of_company`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`user_client_of_company` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`user_client_of_company` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `user_email` VARCHAR(128) NOT NULL,
  `company_id` INT NOT NULL,
  `company_email` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_has_company_company1_idx` (`company_id` ASC, `company_email` ASC),
  INDEX `fk_user_has_company_user1_idx` (`user_id` ASC, `user_email` ASC),
  CONSTRAINT `fk_user_has_company_user1`
    FOREIGN KEY (`user_id` , `user_email`)
    REFERENCES `mygreenbilldb`.`user` (`id` , `email`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_has_company_company1`
    FOREIGN KEY (`company_id` , `company_email`)
    REFERENCES `mygreenbilldb`.`company` (`id` , `email`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`incoming_massages`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`incoming_massages` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`incoming_massages` (
  `user_id` INT NOT NULL,
  `user_email` VARCHAR(128) NOT NULL,
  `massage_info_id` INT NOT NULL,
  INDEX `fk_table1_user1_idx` (`user_id` ASC, `user_email` ASC),
  INDEX `fk_incoming_massages_massage_info1_idx` (`massage_info_id` ASC),
  PRIMARY KEY (`user_id`, `user_email`, `massage_info_id`),
  CONSTRAINT `fk_table1_user1`
    FOREIGN KEY (`user_id` , `user_email`)
    REFERENCES `mygreenbilldb`.`user` (`id` , `email`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_incoming_massages_massage_info1`
    FOREIGN KEY (`massage_info_id`)
    REFERENCES `mygreenbilldb`.`massage_info` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`outgoing_massages`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`outgoing_massages` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`outgoing_massages` (
  `massage_info_id` INT NOT NULL,
  `company_id` INT NOT NULL,
  `company_email` VARCHAR(128) NOT NULL,
  INDEX `fk_incoming_massages_massage_info1_idx` (`massage_info_id` ASC),
  INDEX `fk_outcoming_massages_company1_idx` (`company_id` ASC, `company_email` ASC),
  PRIMARY KEY (`massage_info_id`, `company_email`, `company_id`),
  CONSTRAINT `fk_incoming_massages_massage_info10`
    FOREIGN KEY (`massage_info_id`)
    REFERENCES `mygreenbilldb`.`massage_info` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_outcoming_massages_company1`
    FOREIGN KEY (`company_id` , `company_email`)
    REFERENCES `mygreenbilldb`.`company` (`id` , `email`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`invoice_info`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`invoice_info` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`invoice_info` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `amount` INT NULL,
  `issue_date` DATE NULL,
  `due_date` DATE NULL,
  `content` TEXT NULL,
  `billing_info_id` INT NOT NULL,
  `is_paid` TINYINT(1) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_invoice_info_billing_info1_idx` (`billing_info_id` ASC),
  CONSTRAINT `fk_invoice_info_billing_info1`
    FOREIGN KEY (`billing_info_id`)
    REFERENCES `mygreenbilldb`.`billing_info` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`contact_person`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`contact_person` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`contact_person` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NULL,
  `position` VARCHAR(128) NULL,
  `company_id` INT NOT NULL,
  `company_email` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_contact_person_company1_idx` (`company_id` ASC, `company_email` ASC),
  CONSTRAINT `fk_contact_person_company1`
    FOREIGN KEY (`company_id` , `company_email`)
    REFERENCES `mygreenbilldb`.`company` (`id` , `email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`contact_info`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`contact_info` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`contact_info` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `phone_number` VARCHAR(128) NULL,
  `email` VARCHAR(128) NULL,
  `fax_number` VARCHAR(128) NULL,
  `office_number` VARCHAR(128) NULL,
  `contact_person_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_contact_info_contact_person1_idx` (`contact_person_id` ASC),
  CONSTRAINT `fk_contact_info_contact_person1`
    FOREIGN KEY (`contact_person_id`)
    REFERENCES `mygreenbilldb`.`contact_person` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`admin`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`admin` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`admin` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(128) NULL,
  `password` VARCHAR(128) NULL,
  `name` VARCHAR(128) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`Inquiries_list`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`Inquiries_list` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`Inquiries_list` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `admin_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_inquiries_list_admin1_idx` (`admin_id` ASC),
  CONSTRAINT `fk_inquiries_list_admin1`
    FOREIGN KEY (`admin_id`)
    REFERENCES `mygreenbilldb`.`admin` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`regiter_Inquiry`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`regiter_Inquiry` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`regiter_Inquiry` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `recieved_date` DATE NULL,
  `subject` VARCHAR(128) NULL,
  `context` TEXT NULL,
  `status` ENUM('Opend','InProcess','Closed') NULL,
  `regiter_Inquirycol` VARCHAR(128) NULL,
  `Inquiries_list_id` INT NOT NULL,
  `user_type` ENUM('user','company') NULL COMMENT 'user_type = company or user {ENUM(\'user\',\'company\')}',
  PRIMARY KEY (`id`),
  INDEX `fk_regiter_Inquiry_Inquiries_list1_idx` (`Inquiries_list_id` ASC),
  CONSTRAINT `fk_regiter_Inquiry_Inquiries_list1`
    FOREIGN KEY (`Inquiries_list_id`)
    REFERENCES `mygreenbilldb`.`Inquiries_list` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`unregiter_Inquiry`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`unregiter_Inquiry` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`unregiter_Inquiry` (
  `id` INT NOT NULL,
  `name` VARCHAR(128) NULL,
  `recived_date` DATE NULL,
  `sunject` VARCHAR(128) NULL,
  `context` TEXT NULL,
  `unregiter_Inquirycol` VARCHAR(128) NULL,
  `email` VARCHAR(128) NULL,
  `phone_number` VARCHAR(128) NULL,
  `status` ENUM('user','company') NULL,
  `Inquiries_list_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_unregiter_Inquiry_Inquiries_list1_idx` (`Inquiries_list_id` ASC),
  CONSTRAINT `fk_unregiter_Inquiry_Inquiries_list1`
    FOREIGN KEY (`Inquiries_list_id`)
    REFERENCES `mygreenbilldb`.`Inquiries_list` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`mail_template`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`mail_template` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`mail_template` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NULL,
  `description` VARCHAR(1024) NULL,
  `context` TEXT NULL,
  `path_to_file` VARCHAR(128) NULL,
  `create_date` DATE NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`admin_manage_mail_template`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`admin_manage_mail_template` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`admin_manage_mail_template` (
  `admin_id` INT NOT NULL,
  `mail_template_id` INT NOT NULL,
  PRIMARY KEY (`admin_id`, `mail_template_id`),
  INDEX `fk_admin_has_mail_template_mail_template1_idx` (`mail_template_id` ASC),
  INDEX `fk_admin_has_mail_template_admin1_idx` (`admin_id` ASC),
  CONSTRAINT `fk_admin_has_mail_template_admin1`
    FOREIGN KEY (`admin_id`)
    REFERENCES `mygreenbilldb`.`admin` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_admin_has_mail_template_mail_template1`
    FOREIGN KEY (`mail_template_id`)
    REFERENCES `mygreenbilldb`.`mail_template` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`log_in_event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`log_in_event` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`log_in_event` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` DATE NULL,
  `time` TIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`user_has_log_in_event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`user_has_log_in_event` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`user_has_log_in_event` (
  `user_id` INT NOT NULL,
  `user_email` VARCHAR(128) NOT NULL,
  `log_in_event_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `user_email`, `log_in_event_id`),
  INDEX `fk_user_has_log_in_event_log_in_event1_idx` (`log_in_event_id` ASC),
  INDEX `fk_user_has_log_in_event_user1_idx` (`user_id` ASC, `user_email` ASC),
  CONSTRAINT `fk_user_has_log_in_event_user1`
    FOREIGN KEY (`user_id` , `user_email`)
    REFERENCES `mygreenbilldb`.`user` (`id` , `email`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_has_log_in_event_log_in_event1`
    FOREIGN KEY (`log_in_event_id`)
    REFERENCES `mygreenbilldb`.`log_in_event` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`company_has_log_in_event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`company_has_log_in_event` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`company_has_log_in_event` (
  `company_id` INT NOT NULL,
  `company_email` VARCHAR(128) NOT NULL,
  `log_in_event_id` INT NOT NULL,
  PRIMARY KEY (`company_id`, `company_email`, `log_in_event_id`),
  INDEX `fk_company_has_log_in_event_log_in_event1_idx` (`log_in_event_id` ASC),
  INDEX `fk_company_has_log_in_event_company1_idx` (`company_id` ASC, `company_email` ASC),
  CONSTRAINT `fk_company_has_log_in_event_company1`
    FOREIGN KEY (`company_id` , `company_email`)
    REFERENCES `mygreenbilldb`.`company` (`id` , `email`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_company_has_log_in_event_log_in_event1`
    FOREIGN KEY (`log_in_event_id`)
    REFERENCES `mygreenbilldb`.`log_in_event` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`user_has_address`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`user_has_address` ;
/* remove this table we do not need it any more */
/*CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`user_has_address` (
  `user_id` INT NOT NULL,
  `user_email` VARCHAR(128) NOT NULL,
  `address_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `user_email`, `address_id`),
  INDEX `fk_user_has_address_address1_idx` (`address_id` ASC),
  INDEX `fk_user_has_address_user1_idx` (`user_id` ASC, `user_email` ASC),
  CONSTRAINT `fk_user_has_address_user1`
    FOREIGN KEY (`user_id` , `user_email`)
    REFERENCES `mygreenbilldb`.`user` (`id` , `email`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_has_address_address1`
    FOREIGN KEY (`address_id`)
    REFERENCES `mygreenbilldb`.`address` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;*/


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`company_has_address`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`company_has_address` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`company_has_address` (
  `company_id` INT NOT NULL,
  `company_email` VARCHAR(128) NOT NULL,
  `address_id` INT NOT NULL,
  PRIMARY KEY (`company_id`, `company_email`, `address_id`),
  INDEX `fk_company_has_address_address1_idx` (`address_id` ASC),
  INDEX `fk_company_has_address_company1_idx` (`company_id` ASC, `company_email` ASC),
  CONSTRAINT `fk_company_has_address_company1`
    FOREIGN KEY (`company_id` , `company_email`)
    REFERENCES `mygreenbilldb`.`company` (`id` , `email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_company_has_address_address1`
    FOREIGN KEY (`address_id`)
    REFERENCES `mygreenbilldb`.`address` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`user_month_stats`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`user_month_stats` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`user_month_stats` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `user_email` VARCHAR(128) NOT NULL,
  `month_name` VARCHAR(128) NULL,
  `start_date` DATE NULL,
  `end_date` DATE NULL,
  `num_of_log_in` INT NULL,
  `massage_recived` INT NULL,
  `massage_failed` INT NULL,
  `massage_panding` INT NULL,
  `massage_success` INT NULL,
  PRIMARY KEY (`id`, `user_id`, `user_email`),
  INDEX `fk_month_stats_user1_idx` (`user_id` ASC, `user_email` ASC),
  CONSTRAINT `fk_month_stats_user1`
    FOREIGN KEY (`user_id` , `user_email`)
    REFERENCES `mygreenbilldb`.`user` (`id` , `email`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mygreenbilldb`.`company_month_stats`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`company_month_stats` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`company_month_stats` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `month_name` VARCHAR(128) NULL,
  `start_date` DATE NULL,
  `end_date` DATE NULL,
  `num_of_log_in` INT NULL,
  `num_of_massages_sent` INT NULL,
  `num_of_failed_massages` INT NULL,
  `num_of_sucsses_massages_sent` INT NULL,
  `num_of_pending_massages` INT NULL,
  `company_id` INT NOT NULL,
  `company_email` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`, `company_id`, `company_email`),
  INDEX `fk_company_month_stats_company1_idx` (`company_id` ASC, `company_email` ASC),
  CONSTRAINT `fk_company_month_stats_company1`
    FOREIGN KEY (`company_id` , `company_email`)
    REFERENCES `mygreenbilldb`.`company` (`id` , `email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mygreenbilldb`.`user_analytics`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mygreenbilldb`.`user_analytics` ;

CREATE TABLE IF NOT EXISTS `mygreenbilldb`.`user_analytics` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `user_email` VARCHAR(128) NOT NULL,
  `category` VARCHAR(128) NULL,
  `recieved_date` DATE NULL,
  `amount` INT(11) NULL,
  PRIMARY KEY (`id`, `user_id`, `user_email`),
  INDEX `fk_analytics_user1_idx` (`user_id` ASC, `user_email` ASC),
  CONSTRAINT `fk_analytics_user1`
  FOREIGN KEY (`user_id` , `user_email`)
  REFERENCES `mygreenbilldb`.`user` (`id` , `email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`subscription_plans`
-- -----------------------------------------------------
START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`subscription_plans` (`id`, `name`, `description`, `path_to_fie`, `target_audience`) VALUES (1, 'user gold plan', 'allow the user to add as many companys as he wishes for free, doesnt include bills analytic ', 'none', 'user');
INSERT INTO `mygreenbilldb`.`subscription_plans` (`id`, `name`, `description`, `path_to_fie`, `target_audience`) VALUES (2, 'user platinum plan', 'no ads, include bill analytic ', 'none', 'user');
INSERT INTO `mygreenbilldb`.`subscription_plans` (`id`, `name`, `description`, `path_to_fie`, `target_audience`) VALUES (3, 'company gold', 'pay as you go - fee for each mail successfuly sent ', 'none ', 'company');
INSERT INTO `mygreenbilldb`.`subscription_plans` (`id`, `name`, `description`, `path_to_fie`, `target_audience`) VALUES (4, 'comany platinum', 'monthly fee for unlimited amount of massages ', 'none ', 'company');

COMMIT;


-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`billing_info`
-- -----------------------------------------------------
START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`billing_info` (`id`, `subscription_plans_id`, `rank`) VALUES (1, 1, 'gold');
INSERT INTO `mygreenbilldb`.`billing_info` (`id`, `subscription_plans_id`, `rank`) VALUES (2, 2, 'platinum');
INSERT INTO `mygreenbilldb`.`billing_info` (`id`, `subscription_plans_id`, `rank`) VALUES (3, 3, 'gold');
INSERT INTO `mygreenbilldb`.`billing_info` (`id`, `subscription_plans_id`, `rank`) VALUES (4, 4, 'platinum');

COMMIT;


-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`user`
-- -----------------------------------------------------
START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`user` (`id`, `email`, `first_name`, `last_name`, `password`, `hmail_account_name`, `is_active`, `join_date`, `forward_email`) VALUES (038054664, 'yaki.ams@gmail.com', 'Jacob', 'Amsalem', 'afdd0b4ad2ec172c586e2150770fbf9e', 'e0df4b0c1f6105a4e2834251eddd8507', 1, '2013-09-21', 'yaki.ams@gmail.com');
INSERT INTO `mygreenbilldb`.`user` (`id`, `email`, `first_name`, `last_name`, `password`, `hmail_account_name`, `is_active`, `join_date`, `forward_email`) VALUES (038054665, 'ipeleg@mygreenbill.com', 'Idan', 'Peleg', '81dc9bdb52d04dc20036dbd8313ed055', 'ipeleg', 1, '2013-09-16', 'ipeleg@hotmail.com');

COMMIT;


-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`address`
-- -----------------------------------------------------
START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`address` (`id`, `country`, `city`, `street_name`, `house_number`, `postal_code`) VALUES (1, 'Israel', 'Raanana', 'Brandis', 13, '2013');
INSERT INTO `mygreenbilldb`.`address` (`id`, `country`, `city`, `street_name`, `house_number`, `postal_code`) VALUES (2, 'Israel', 'Ramat Gan', 'Ana Frank', 22, '2014');
INSERT INTO `mygreenbilldb`.`address` (`id`, `country`, `city`, `street_name`, `house_number`, `postal_code`) VALUES (3, 'Israel', 'Tel Aviv', 'Dizingof', 24, '2018');

COMMIT;


-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`company`
-- -----------------------------------------------------
START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`company` (`id`, `email`, `name`, `password`, `join_date`, `billing_info_id`, `logo_path`) VALUES (12345678, 'company@mail.com', 'Bank Leumi', '1234', '2013-09-15', 3, 'assets/img/CompaniesLogo/leumi.png');
INSERT INTO `mygreenbilldb`.`company` (`id`, `email`, `name`, `password`, `join_date`, `billing_info_id`, `logo_path`) VALUES (123456789, 'ipeleg@hotmail.com', 'Bank Hapolaim', '1234', '2013-09-16', 4, 'assets/img/CompaniesLogo/hapohalim.png');
INSERT INTO `mygreenbilldb`.`company` (`id`, `email`, `name`, `password`, `join_date`, `billing_info_id`, `logo_path`) VALUES (1234567, 'yaki.ams@gmail.com', 'Electric Company', '1234', '2013-09-16', 4, 'assets/img/CompaniesLogo/eci.png');

/* Orange, Cellcom and Yad2 */
INSERT INTO `mygreenbilldb`.`company` (`id`, `email`, `name`, `password`, `join_date`, `billing_info_id`, `logo_path`, `parser_name`) VALUES (12121212, 'cellcom.mygreenbill@gmail.com', 'Cellcom LTD', '1234', '2014-05-16', 4, 'assets/img/CompaniesLogo/cellcom.png', 'CellcomBillParser');
INSERT INTO `mygreenbilldb`.`company` (`id`, `email`, `name`, `password`, `join_date`, `billing_info_id`, `logo_path`, `parser_name`) VALUES (12445272, 'orange.mygreenbill@gmail.com', 'Orange LTD', '1234', '2014-05-16', 4, 'assets/img/CompaniesLogo/orange.png', 'OrangeBillParser');
INSERT INTO `mygreenbilldb`.`company` (`id`, `email`, `name`, `password`, `join_date`, `billing_info_id`, `logo_path`, `parser_name`) VALUES (12445, 'yad2.mygreenbill@gmail.com', 'Yad2 LTD', '1234', '2014-05-16', 4, 'assets/img/CompaniesLogo/yad2.png', 'Yad2BillParser');

COMMIT;


-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`massage_info`
-- -----------------------------------------------------
START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`massage_info` (`id`, `send_to`, `send_from`, `subject`, `content`, `cc`, `date`, `status`) VALUES (1, 'yaki.ams@gmail.com', 'ipeleg@hotmail.com', 'Welcome', 'hi mate, thanks for joining bank hapoalim mailing service via \"My Green Bill\", Regards Bank Hapoalim', NULL, '2013-09-21', 'sent');
INSERT INTO `mygreenbilldb`.`massage_info` (`id`, `send_to`, `send_from`, `subject`, `content`, `cc`, `date`, `status`) VALUES (2, 'yaki.ams@gmail.com', 'company@mail.com', 'Welcome', 'hi mate, thanks for joining bank leumi mailing service via \"My Green Bill\", Regards Bank Hapoalim', NULL, '2013-09-21', 'sent');
INSERT INTO `mygreenbilldb`.`massage_info` (`id`, `send_to`, `send_from`, `subject`, `content`, `cc`, `date`, `status`) VALUES (3, 'ipeleg@mygreenbill.com', 'ipeleg@hotmail.com', 'Welcome', 'hi mate, thanks for joining bank hapoalim mailing service via \"My Green Bill\", Regards Bank Hapoalim', NULL, '2013-09-21', 'pending');
INSERT INTO `mygreenbilldb`.`massage_info` (`id`, `send_to`, `send_from`, `subject`, `content`, `cc`, `date`, `status`) VALUES (4, 'ipeleg@mygreenbill.com', 'company@mail.com', 'Welcome', 'hi mate, thanks for joining bank leumi mailing service via \"My Green Bill\", Regards Bank Hapoalim', NULL, '2013-09-21', 'failed');
INSERT INTO `mygreenbilldb`.`massage_info` (`id`, `send_to`, `send_from`, `subject`, `content`, `cc`, `date`, `status`) VALUES (5, 'yaki.ams@gmail.com', 'company@mail.com', 'hello', 'hello world', NULL, '2013-10-05', 'sent');

COMMIT;


-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`file`
-- -----------------------------------------------------
START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`file` (`id`, `path`, `name`, `massage_id`) VALUES (1, '/path/tp/file.txt', 'welcomefile', 1);
INSERT INTO `mygreenbilldb`.`file` (`id`, `path`, `name`, `massage_id`) VALUES (2, '/path/tp/file.txt', 'welcomefile', 3);

COMMIT;


-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`user_client_of_company`
-- -----------------------------------------------------
START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`user_client_of_company` (`user_id`, `user_email`, `company_id`, `company_email`) VALUES (038054664, 'yaki.ams@gmail.com', 123456789, 'ipeleg@hotmail.com');
INSERT INTO `mygreenbilldb`.`user_client_of_company` (`user_id`, `user_email`, `company_id`, `company_email`) VALUES (038054665, 'ipeleg@mygreenbill.com', 123456789, 'ipeleg@hotmail.com');
INSERT INTO `mygreenbilldb`.`user_client_of_company` (`user_id`, `user_email`, `company_id`, `company_email`) VALUES (038054665, 'ipeleg@mygreenbill.com', 12345678, 'company@mail.com');

COMMIT;


-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`incoming_massages`
-- -----------------------------------------------------
START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`incoming_massages` (`user_id`, `user_email`, `massage_info_id`) VALUES (038054664, 'yaki.ams@gmail.com', 1);
INSERT INTO `mygreenbilldb`.`incoming_massages` (`user_id`, `user_email`, `massage_info_id`) VALUES (038054664, 'yaki.ams@gmail.com', 2);
INSERT INTO `mygreenbilldb`.`incoming_massages` (`user_id`, `user_email`, `massage_info_id`) VALUES (038054665, 'ipeleg@mygreenbill.com', 3);
INSERT INTO `mygreenbilldb`.`incoming_massages` (`user_id`, `user_email`, `massage_info_id`) VALUES (038054665, 'ipeleg@mygreenbill.com', 4);
INSERT INTO `mygreenbilldb`.`incoming_massages` (`user_id`, `user_email`, `massage_info_id`) VALUES (038054664, 'yaki.ams@gmail.com', 5);

COMMIT;


-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`outgoing_massages`
-- -----------------------------------------------------
START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`outgoing_massages` (`massage_info_id`, `company_id`, `company_email`) VALUES (1, 123456789, 'ipeleg@hotmail.com');
INSERT INTO `mygreenbilldb`.`outgoing_massages` (`massage_info_id`, `company_id`, `company_email`) VALUES (2, 12345678, 'company@mail.com');
INSERT INTO `mygreenbilldb`.`outgoing_massages` (`massage_info_id`, `company_id`, `company_email`) VALUES (3, 123456789, 'ipeleg@hotmail.com');
INSERT INTO `mygreenbilldb`.`outgoing_massages` (`massage_info_id`, `company_id`, `company_email`) VALUES (4, 12345678, 'company@mail.com');
INSERT INTO `mygreenbilldb`.`outgoing_massages` (`massage_info_id`, `company_id`, `company_email`) VALUES (5, 12345678, 'company@mail.com');

COMMIT;


-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`invoice_info`
-- -----------------------------------------------------
START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`invoice_info` (`id`, `amount`, `issue_date`, `due_date`, `content`, `billing_info_id`, `is_paid`) VALUES (1, 200, '2013-08-01', '2013-08-30', 'For 200 Msgs', 1, 0);
INSERT INTO `mygreenbilldb`.`invoice_info` (`id`, `amount`, `issue_date`, `due_date`, `content`, `billing_info_id`, `is_paid`) VALUES (2, 300, '2013-08-01', '2013-08-30', 'For 300 Msgs', 2, 0);
INSERT INTO `mygreenbilldb`.`invoice_info` (`id`, `amount`, `issue_date`, `due_date`, `content`, `billing_info_id`, `is_paid`) VALUES (3, 200, '2013-09-01', '2013-09-30', 'For 200 Msgs', 1, 1);
INSERT INTO `mygreenbilldb`.`invoice_info` (`id`, `amount`, `issue_date`, `due_date`, `content`, `billing_info_id`, `is_paid`) VALUES (4, 500, '2013-09-01', '2013-09-30', 'For 500 Msgs', 2, 1);
INSERT INTO `mygreenbilldb`.`invoice_info` (`id`, `amount`, `issue_date`, `due_date`, `content`, `billing_info_id`, `is_paid`) VALUES (5, 500, '2013-09-05', '2013-09-30', 'no limit', 3, 0);

COMMIT;


-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`contact_person`
-- -----------------------------------------------------
START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`contact_person` (`id`, `name`, `position`, `company_id`, `company_email`) VALUES (1, 'John Roberts', 'Mailing Manager', 123456789, 'ipeleg@hotmail.com');
INSERT INTO `mygreenbilldb`.`contact_person` (`id`, `name`, `position`, `company_id`, `company_email`) VALUES (2, 'Daina Cohen', 'Marketing Manager', 12345678, 'company@mail.com');
INSERT INTO `mygreenbilldb`.`contact_person` (`id`, `name`, `position`, `company_id`, `company_email`) VALUES (3, 'Rob Williams', 'Marketing Manager', 1234567, 'yaki.ams@gmail.com');

COMMIT;


-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`contact_info`
-- -----------------------------------------------------
START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`contact_info` (`id`, `phone_number`, `email`, `fax_number`, `office_number`, `contact_person_id`) VALUES (1, '050-9049749', 'mail@mail.com', '03-9508395', '039508395', 1);
INSERT INTO `mygreenbilldb`.`contact_info` (`id`, `phone_number`, `email`, `fax_number`, `office_number`, `contact_person_id`) VALUES (2, '050-9049789', 'email@mail.com', '039508396', '039508396', 2);

COMMIT;


-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`admin`
-- -----------------------------------------------------
START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`admin` (`id`, `email`, `password`, `name`) VALUES (1, 'yaki@gmail.com', 'Aa123456', 'Yaki');
INSERT INTO `mygreenbilldb`.`admin` (`id`, `email`, `password`, `name`) VALUES (2, 'Idan@mail.com', 'Aa12345', 'Idan');

COMMIT;


-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`mail_template`
-- -----------------------------------------------------
START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`mail_template` (`id`, `name`, `description`, `context`, `path_to_file`, `create_date`) VALUES (1, 'Welcome', 'Welcome to any new user', 'Hello $first_name $last_name \r\nThank you for joining My green Bill! to access the Dashboard please activate your account by clicking the following link: $link', 'path/to/file.png', '2013-08-01');
INSERT INTO `mygreenbilldb`.`mail_template` (`id`, `name`, `description`, `context`, `path_to_file`, `create_date`) VALUES (2, 'Password Reset', 'Reset the user password', 'Hello $first_name $last_name \r\nTo reset your password please press the following link: $link', 'path/to/file.png', '2013-08-02');
INSERT INTO `mygreenbilldb`.`mail_template` (`id`, `name`, `description`, `context`, `path_to_file`, `create_date`) VALUES (3, 'Monthly update', 'Display the monthly update to the user', 'your monthly update is... bla bla bla ', 'path/to/update.txt', '2013-08-03');
INSERT INTO `mygreenbilldb`.`mail_template` (`id`, `name`, `description`, `context`, `path_to_file`, `create_date`) VALUES (4, 'Unregister Customer', 'Message company to unregister a user', 'Hello $company_name\r\nPlease remove the following user:\r\nName: $first_name $last_name\r\nID: $user_id\r\nE-Mail: $user_email\r\nfrom your mailing list.', 'path/to/update.txt', '2013-08-03');
INSERT INTO `mygreenbilldb`.`mail_template` (`id`, `name`, `description`, `context`, `path_to_file`, `create_date`) VALUES (5, 'Register Customer', 'Message company to register a user', 'Hello $company_name\r\nPlease add the following user:\r\nName: $first_name $last_name\r\nID: $user_id\r\nE-Mail: $user_email\r\nto your mailing list.', 'path/to/update.txt', '2013-08-03');
INSERT INTO `mygreenbilldb`.`mail_template` (`id`, `name`, `description`, `context`, `path_to_file`, `create_date`) VALUES (6, 'Password Successfully Reset', 'Reset user password success', 'Hello $first_name $last_name \r\nYour Password was successfully restored\r\nNew Password: $pass', 'path/to/file.png', '2013-08-02');
COMMIT;


-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`admin_manage_mail_template`
-- -----------------------------------------------------
START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`admin_manage_mail_template` (`admin_id`, `mail_template_id`) VALUES (1, 1);
INSERT INTO `mygreenbilldb`.`admin_manage_mail_template` (`admin_id`, `mail_template_id`) VALUES (1, 2);
INSERT INTO `mygreenbilldb`.`admin_manage_mail_template` (`admin_id`, `mail_template_id`) VALUES (2, 3);

COMMIT;


-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`log_in_event`
-- -----------------------------------------------------
START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`log_in_event` (`id`, `date`, `time`) VALUES (1, '2013-09-21', '12:10:00');
INSERT INTO `mygreenbilldb`.`log_in_event` (`id`, `date`, `time`) VALUES (2, '2013-09-21', '12:20:57');
INSERT INTO `mygreenbilldb`.`log_in_event` (`id`, `date`, `time`) VALUES (3, '2013-09-21', '13:11:15');
INSERT INTO `mygreenbilldb`.`log_in_event` (`id`, `date`, `time`) VALUES (4, '2013-09-21', '13:15:06');
INSERT INTO `mygreenbilldb`.`log_in_event` (`id`, `date`, `time`) VALUES (5, '2013-09-22', '15:22:12');
INSERT INTO `mygreenbilldb`.`log_in_event` (`id`, `date`, `time`) VALUES (6, '2013-09-22', '19:22:01');
INSERT INTO `mygreenbilldb`.`log_in_event` (`id`, `date`, `time`) VALUES (7, '2013-09-27', '10:51:00');

COMMIT;


-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`user_has_log_in_event`
-- -----------------------------------------------------
START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`user_has_log_in_event` (`user_id`, `user_email`, `log_in_event_id`) VALUES (038054664, 'yaki.ams@gmail.com', 1);
INSERT INTO `mygreenbilldb`.`user_has_log_in_event` (`user_id`, `user_email`, `log_in_event_id`) VALUES (038054664, 'yaki.ams@gmail.com ', 2);
INSERT INTO `mygreenbilldb`.`user_has_log_in_event` (`user_id`, `user_email`, `log_in_event_id`) VALUES (038054665, 'ipeleg@mygreenbill.com', 3);
INSERT INTO `mygreenbilldb`.`user_has_log_in_event` (`user_id`, `user_email`, `log_in_event_id`) VALUES (038054665, 'ipeleg@mygreenbill.com', 4);

COMMIT;


-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`company_has_log_in_event`
-- -----------------------------------------------------
START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`company_has_log_in_event` (`company_id`, `company_email`, `log_in_event_id`) VALUES (12345678, 'company@mail.com', 5);
INSERT INTO `mygreenbilldb`.`company_has_log_in_event` (`company_id`, `company_email`, `log_in_event_id`) VALUES (123456789, 'ipeleg@hotmail.com', 6);
INSERT INTO `mygreenbilldb`.`company_has_log_in_event` (`company_id`, `company_email`, `log_in_event_id`) VALUES (123456789, 'ipeleg@hotmail.com', 7);

COMMIT;


-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`user_has_address`
-- -----------------------------------------------------
/*Remove the inserts to the table as well*/
/*START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`user_has_address` (`user_id`, `user_email`, `address_id`) VALUES (038054664, 'yaki.ams@gmail.com', 1);
INSERT INTO `mygreenbilldb`.`user_has_address` (`user_id`, `user_email`, `address_id`) VALUES (038054665, 'ipeleg@mygreenbill.com', 2);

COMMIT;*/


-- -----------------------------------------------------
-- Data for table `mygreenbilldb`.`company_has_address`
-- -----------------------------------------------------
START TRANSACTION;
USE `mygreenbilldb`;
INSERT INTO `mygreenbilldb`.`company_has_address` (`company_id`, `company_email`, `address_id`) VALUES (12345678, 'company@mail.com', 3);
INSERT INTO `mygreenbilldb`.`company_has_address` (`company_id`, `company_email`, `address_id`) VALUES (123456789, 'ipeleg@hotmail.com', 3);

COMMIT;

start transaction;
use `mygreenbilldb`;
/*Dummy Data for Electric (8 month)*/
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'ELECTRIC', '2014-01-01', 120);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'ELECTRIC', '2014-02-01', 330);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'ELECTRIC', '2014-03-01', 420);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'ELECTRIC', '2014-04-01', 170);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'ELECTRIC', '2014-05-01', 245);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'ELECTRIC', '2014-06-01', 168);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'ELECTRIC', '2014-07-01', 133);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'ELECTRIC', '2014-08-01', 350);

/*Dummy Data for Communication (8 month)*/
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'COMMUNICATION', '2014-01-01', 90);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'COMMUNICATION', '2014-02-01', 120);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'COMMUNICATION', '2014-03-01', 67);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'COMMUNICATION', '2014-04-01', 84);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'COMMUNICATION', '2014-05-01', 78);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'COMMUNICATION', '2014-06-01', 55);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'COMMUNICATION', '2014-07-01', 45);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'COMMUNICATION', '2014-08-01', 95);

/*Dummy Data for Gas (8 month)*/
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'GAS', '2014-01-01', 30);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'GAS', '2014-02-01', 19);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'GAS', '2014-03-01', 26);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'GAS', '2014-04-01', 27);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'GAS', '2014-05-01', 25);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'GAS', '2014-06-01', 30);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'GAS', '2014-07-01', 29);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'GAS', '2014-08-01', 33);

/*Dummy Data for Water (8 month)*/
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'WATER', '2014-01-01', 55);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'WATER', '2014-02-01', 69);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'WATER', '2014-03-01', 42);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'WATER', '2014-04-01', 36);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'WATER', '2014-05-01', 129);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'WATER', '2014-06-01', 40);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'WATER', '2014-07-01', 47);
insert into `mygreenbilldb`.`user_analytics`(`user_id`,`user_email`, `category`, `recieved_date`, `amount`) values (38054664, 'yaki.ams@gmail.com', 'WATER', '2014-08-01', 49);

commit ;

