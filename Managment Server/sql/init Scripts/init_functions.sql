USE `mygreenbilldb` ;


DROP function IF EXISTS `isCompanyIdExist`;
DELIMITER $$
CREATE DEFINER=`root`@`localhost` FUNCTION `isCompanyIdExist`(idToCheck INT) RETURNS int(11)
BEGIN
	DECLARE res INT DEFAULT -1;
	
	SELECT IF( EXISTS(
				SELECT *
				FROM company
				WHERE company.id=idToCheck), 1, 0) INTO res;
	RETURN res;
END$$
DELIMITER ;

DROP function IF EXISTS `isUserIdExist`;
DELIMITER $$
CREATE DEFINER=`root`@`localhost` FUNCTION `isUserIdExist`(idToCheck INT) RETURNS int(11)
BEGIN
	DECLARE res INT DEFAULT -1;
	
	SELECT IF( EXISTS(
				SELECT *
				FROM user
				WHERE user.id=idToCheck), 1, 0) INTO res;
	RETURN res;
END$$
DELIMITER ;

DROP function IF EXISTS `CreateNewAddress`;

DELIMITER $$
USE `mygreenbilldb`$$
CREATE DEFINER=`root`@`localhost` FUNCTION `CreateNewAddress`(street VARCHAR(45), house_number INT, city VARCHAR(45), postal_code VARCHAR(45), country VARCHAR(45)) RETURNS int(11)
BEGIN
	INSERT INTO address (country, city, street_name, house_number, postal_code)
	VALUES(country, city, street, house_number, postal_code);
RETURN LAST_INSERT_ID();
END$$

DELIMITER ;


DROP function IF EXISTS `isUserExistsAndActive`;
DELIMITER $$

CREATE DEFINER=`root`@`localhost` FUNCTION `isUserExistsAndActive`(idToCheck INT) RETURNS int(11)
BEGIN
    DECLARE res INT DEFAULT -1;
	declare active int default 0;

 	SELECT IF( EXISTS(
				SELECT *
				FROM user
				WHERE user.id=idToCheck), 1, 0) INTO res;

select user.is_active  from user where user.id=idToCheck into active;

select if(res = 1 and active =1, 1,0)into res;
    RETURN res;
  END




