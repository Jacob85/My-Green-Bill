USE `mygreenbilldb` ;

drop procedure if exists `AddUser`;
-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
drop procedure if exists `CreateNewInvoice`;
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `CreateNewInvoice`(IN table_target VARCHAR(45), IN id INT(11), IN email VARCHAR(45),
										IN amount INT(11), IN issue_date DATE, IN due_date DATE,
										IN content TEXT, IN is_paid BOOLEAN)
BEGIN
	/* procedure varibles */ 
	DECLARE error_msg CONDITION FOR SQLSTATE '45000';
	DECLARE isCompanyExist INT;
	DECLARE isUserExist INT;
	DECLARE billingInfoId INT;

/* Check the table target -> user OR Company*/
IF NOT(STRCMP(table_target,"company")) THEN
	SELECT isCompanyIdExist(id) INTO isCompanyExist;
	IF isCompanyExist = 0 THEN
		SET @message_text = CONCAT('A comapny with the ID: ', id, ' and email ', email,' does not exist.');
		SIGNAL error_msg
		SET MESSAGE_TEXT = @message_text;
	ELSE
		SELECT company.billing_info_id
		FROM company
		WHERE company.id = id && company.email = email
		INTO billingInfoId;

		INSERT INTO invoice_info (amount, issue_date, due_date, content, billing_info_id, is_paid)
		VALUES(amount, issue_date, due_date, content, billingInfoId, is_paid);
	END IF;
ELSE
	SELECT isUserIdExist(id) INTO isUserExist;
	IF isUserExist=0 THEN
		SET @message_text = CONCAT('A user with the ID: ', id,' and email ', email,' does not exist.');
		SIGNAL error_msg
		SET MESSAGE_TEXT = @message_text;
	ELSE
		SELECT user.billing_info_id
		FROM user
		WHERE user.id = id && user.email = email
		INTO billingInfoId;

		INSERT INTO invoice_info (amount, issue_date, due_date, content, billing_info_id, is_paid)
		VALUES(amount, issue_date, due_date, content, billingInfoId, is_paid);
	END IF;
END IF;
END $$
DELIMITER ;

-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
drop procedure if exists `GetCompanyUnpaidInvoices`;
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetCompanyUnpaidInvoices`(in company_id int)
BEGIN

DECLARE error_msg CONDITION FOR SQLSTATE '45000';
declare isExist int;

select isCompanyIdExist(company_id) into isExist;
if isExist = 0
	then	
	SET @message_text = CONCAT('A Company with the ID: ', company_id, ' does not exist.');
		SIGNAL error_msg
		SET MESSAGE_TEXT = @message_text;
	else
		select 	company.name,
				billing_info.rank,
				invoice_info.issue_date,
				invoice_info.due_date,
				invoice_info.content,
				invoice_info.is_paid
		from company
		join billing_info on billing_info.id = company.billing_info_id
		join invoice_info on invoice_info.billing_info_id = billing_info.id && invoice_info.is_paid = 0
		where company.id = company_id;
end if;

END $$
DELIMITER ;

-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
drop procedure if exists `GetUserUnpaidInvoices`;
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetUserUnpaidInvoices`(in user_id int)
BEGIN

DECLARE error_msg CONDITION FOR SQLSTATE '45000';
declare isExist int;

select isUserIdExist(user_id) into isExist;
if isExist = 0
	then	
	SET @message_text = CONCAT('A User with the ID: ', user_id, ' does not exist.');
		SIGNAL error_msg
		SET MESSAGE_TEXT = @message_text;
	else
		select 	user.first_name,
				user.last_name,
				billing_info.rank,
				invoice_info.issue_date,
				invoice_info.due_date,
				invoice_info.content,
				invoice_info.is_paid
		from user
		join billing_info on billing_info.id = user.billing_info_id
		join invoice_info on invoice_info.billing_info_id = billing_info.id && invoice_info.is_paid = 0
		where user.id = user_id;
end if;

END $$
DELIMITER ;

-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
drop procedure if exists `GetUserInvoiceFromDate`;
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetUserInvoiceFromDate`(in user_id int, in start_date date, in end_date date)
BEGIN

DECLARE error_msg CONDITION FOR SQLSTATE '45000';
declare isExist int;

select isUserIdExist(user_id) into isExist;
if isExist = 0
	then	
	SET @message_text = CONCAT('A User with the ID: ', user_id, ' does not exist.');
		SIGNAL error_msg
		SET MESSAGE_TEXT = @message_text;
	else
		select 	user.first_name,
				user.last_name,
				billing_info.rank,
				invoice_info.issue_date,
				invoice_info.due_date,
				invoice_info.content,
				invoice_info.is_paid
		from user
		join billing_info on billing_info.id = user.billing_info_id
		join invoice_info on invoice_info.billing_info_id = billing_info.id && invoice_info.issue_date between start_date and end_date
		where user.id = user_id;
end if;

END $$
DELIMITER ;


-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
drop procedure if exists `NewMassage`;
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `NewMassage`(in from_email varchar(45),
								in from_id int,
								in to_email varchar(45),
								in to_id int,
								in cc varchar(45),
								in subject varchar(45),
								in content text,
								in status varchar(15),
								in file_name varchar(45),
								in path_to_file varchar(2048))
BEGIN

/* procedure varibles */ 
DECLARE error_msg CONDITION FOR SQLSTATE '45000';
declare isCompanyExist int;
declare isUserExist int;
declare masssageInfoId  int;

/* check if the user and the company exists in the system*/
select isCompanyIdExist(from_id) into isCompanyExist;
select isUserIdExist(to_id) into isUserExist;

if isCompanyExist = 0 || isUserExist=0 then
	SET @message_text = CONCAT('A comapny with the ID: ', from_id, ' or user with the ID: ',to_id,' does not exist.');
	SIGNAL error_msg
	SET MESSAGE_TEXT = @message_text;
else
	/* user and company exist */
	/* insert data into  massage_info */
	insert	into massage_info (send_to,send_from,subject,content,cc,date,status)
			values (to_email,from_email,subject,content,cc,CURDATE(),status);
	/* get the last insert id (massage_info id) */
	select LAST_INSERT_ID() into masssageInfoId;
	
	/* insert data into incoming_massages */
	insert 	into incoming_massages (user_id,user_email,massage_info_id)
			values (to_id,to_email,masssageInfoId);
	
	/*insert data into outgoing_massages*/
	insert	into outgoing_massages(massage_info_id,company_id,company_email) 
			values (masssageInfoId,from_id,from_email);

end if;

/* insert data into the file (if exist)*/
if file_name is not null && path_to_file is not null then
	/* file exists - > insert into file table */
	insert 	into file (path,name,massage_id)
			values (path_to_file,file_name,masssageInfoId);
end if;

END $$
DELIMITER ;

-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
drop procedure if exists `UpdateCompanyAddress`;
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateCompanyAddress`(IN id INT, IN street VARCHAR(45), IN house_number INT,
							IN city VARCHAR(45), IN postal_code VARCHAR(45),
							IN country VARCHAR(45))
BEGIN
	DECLARE error_msg CONDITION FOR SQLSTATE '45000';
	DECLARE isExist INT;

	SELECT isCompanyIdExist(id) INTO isExist;
	IF isExist = 0
	THEN
		SET @message_text = CONCAT('A Company with the ID: ', id, ' does not exist.');
			SIGNAL error_msg
			SET MESSAGE_TEXT = @message_text;
	ELSE
		UPDATE address
		JOIN company_has_address ON address.id = company_has_address.address_id
		SET
		address.street_name = street,
		address.house_number = house_number,
		address.city = city,
		address.postal_code = postal_code,
		address.country = country
		WHERE company_has_address.company_id = id;
	END IF;
END $$
DELIMITER ;

-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
drop procedure if exists `UpdateUserAddress`;
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateUserAddress`(IN id INT, IN street VARCHAR(45), IN house_number INT,
							IN city VARCHAR(45), IN postal_code VARCHAR(45),
							IN country VARCHAR(45))
BEGIN
	DECLARE error_msg CONDITION FOR SQLSTATE '45000';
	DECLARE isExist INT;

	SELECT isUserIdExist(id) INTO isExist;
	IF isExist = 0 THEN
		SET @message_text = CONCAT('A user with the ID: ', id, ' does not exist.');
		SIGNAL error_msg
		SET MESSAGE_TEXT = @message_text;
	ELSE
		UPDATE address
		JOIN user_has_address ON address.id = user_has_address.address_id
		SET
		address.street_name = street,
		address.house_number = house_number,
		address.city = city,
		address.postal_code = postal_code,
		address.country = country
		WHERE user_has_address.user_id = id;
	END IF;
END $$
DELIMITER ;


-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
drop procedure if exists `AddCompany`;
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `AddCompany`(IN id INT,IN email varchar(45), IN company_name varchar(45),
							IN pass varchar(45), IN subscription_plan INT, IN rank varchar(45),
							IN street VARCHAR(45), IN house_number INT,
							IN city VARCHAR(45), IN postal_code VARCHAR(45),
							IN country VARCHAR(45))
BEGIN
	DECLARE error_msg CONDITION FOR SQLSTATE '45000';
	DECLARE isExist INT;
	DECLARE addressId INT;
	
	SELECT isCompanyIdExist(id) INTO isExist;

	IF isExist=0 THEN
		SELECT CreateNewAddress(street, house_number, city, postal_code, country) INTO addressId;

		INSERT INTO billing_info (subscription_plans_id, rank)
		VALUES(subscription_plan, rank);

		INSERT INTO company
		VALUES(id, email, company_name, pass, CURDATE(), LAST_INSERT_ID());

		INSERT INTO company_has_address
		VALUES(id, email, addressId);
	ELSE
		SET @message_text = CONCAT('A comapny with the ID: ', id, ' already exist.');
		SIGNAL error_msg
		SET MESSAGE_TEXT = @message_text;
	END IF;
END $$
DELIMITER ;


-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
drop procedure if exists `AddUser`;
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `AddUser`(IN id INT,IN email varchar(45), IN first_name varchar(45),
							IN last_name varchar(45), IN pass varchar(45), IN hmail_account VARCHAR(45))
BEGIN
	DECLARE error_msg CONDITION FOR SQLSTATE '45000';
	DECLARE isExist INT;
	
	SELECT isUserIdExist(id) INTO isExist;

	IF isExist=0 THEN

		INSERT INTO user
		VALUES(id, email, first_name, last_name, pass, hmail_account, 0, CURDATE());

	ELSE
		SET @message_text = CONCAT('A user with the ID: ', id, ' already exist.');
		SIGNAL error_msg
		SET MESSAGE_TEXT = @message_text;
	END IF;
END $$
DELIMITER ;



-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
drop procedure if exists `AddMonthlyStatForUser`;
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `AddMonthlyStatForUser`(in user_id int, in start_date date, in end_date date)
BEGIN

/* Procedure Varibles*/
DECLARE error_msg CONDITION FOR SQLSTATE '45000';
declare isExist int;
declare monthName varchar(15);
declare logIns int;
declare recivedMassages int;
declare failedMassages int;
declare sucssessMassages int;
declare pendingMassages int;
declare userEmail varchar(45);

/* check if the company exists */ 
select isUserIdExist(user_id) into isExist;
if isExist = 0 then
	SET @message_text = CONCAT('A user with the ID: ', user_id, ' does not exist.');
	SIGNAL error_msg
	SET MESSAGE_TEXT = @message_text;
else
	select MONTHNAME(start_date) into monthName;   /*get the month name from the date*/
	
	select 
	count(user_has_log_in_event.user_id) 
	into logIns 
	from user_has_log_in_event 
	join log_in_event on log_in_event.id = user_has_log_in_event.log_in_event_id && log_in_event.date between start_date and end_date
	where user_has_log_in_event.user_id = user_id; /* get number of logins to the system*/
	/* get the number of sent massages*/
	select 
	count(incoming_massages.user_id) 
	into recivedMassages 
	from incoming_massages 
	join massage_info on incoming_massages.massage_info_id = massage_info.id && massage_info.date between start_date and end_date
	where incoming_massages.user_id = user_id; 
	/* get the user email*/
	select user.email into userEmail from user where user.id = user_id;
	/* get the sucssess Massages */ 
	select 
	count(incoming_massages.user_id) 
	into sucssessMassages
	from incoming_massages
	join massage_info on incoming_massages.massage_info_id = massage_info.id 
	where incoming_massages.user_id = user_id && massage_info.status = 'sent' && massage_info.date between start_date and end_date ;
	/*get the  pending massages */
	select 
	count(incoming_massages.user_id) 
	into pendingMassages
	from incoming_massages
	join massage_info on incoming_massages.massage_info_id = massage_info.id 
	where incoming_massages.user_id = user_id && massage_info.status = 'pending' && massage_info.date between start_date and end_date ;
	/*get the  failed massages */
	select 
	count(incoming_massages.user_id) 
	into failedMassages
	from incoming_massages
	join massage_info on incoming_massages.massage_info_id = massage_info.id 
	where incoming_massages.user_id = user_id && massage_info.status = 'failed' && massage_info.date between start_date and end_date ;
	
	/*insert the data into the table */
	insert into user_month_stats 	(month_name	,
									start_date,
									end_date,
									num_of_log_in,
									massage_recived,
									massage_failed,
									massage_success,
									massage_panding,
									user_id,
									user_email)
							values  (monthName,
									start_date,
									end_date,
									logIns,
									recivedMassages,
									failedMassages,
									sucssessMassages,
									pendingMassages,
									user_id,
									userEmail);
end if;

END $$
DELIMITER ;


-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
drop procedure if exists `AddMonthlyStatForCompany`;
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `AddMonthlyStatForCompany`(in company_id int, in start_date date, in end_date date)
BEGIN

/* Procedure Varibles*/
DECLARE error_msg CONDITION FOR SQLSTATE '45000';
declare isExist int;
declare monthName varchar(15);
declare logIns int;
declare sentMassages int;
declare failedMassages int;
declare sucssessMassages int;
declare pendingMassages int;
declare companyEmail varchar(45);

/* check if the company exists */ 
select isCompanyIdExist(company_id) into isExist;
if isExist = 0 then
	SET @message_text = CONCAT('A comapny with the ID: ', company_id, ' does not exist.');
	SIGNAL error_msg
	SET MESSAGE_TEXT = @message_text;
else
	select MONTHNAME(start_date) into monthName;   /*get the month name from the date*/
	/* get number of logins to the system this month*/
	select 
	count(company_has_log_in_event.company_id) 
	into logIns 
	from company_has_log_in_event 
	join log_in_event on log_in_event.id = company_has_log_in_event.log_in_event_id && log_in_event.date between start_date and end_date
	where company_has_log_in_event.company_id = company_id; 
	
	select 
	count(outgoing_massages.company_id)
	into sentMassages 
	from outgoing_massages
	join massage_info on outgoing_massages.massage_info_id = massage_info.id && massage_info.date between start_date and end_date
	where outgoing_massages.company_id = company_id; /* get the number of sent massages*/
	/* get the company email*/
	select company.email into companyEmail from company where company.id = company_id;
	/* get the sucssess Massages */ 
	select 
	count(outgoing_massages.company_id) 
	into sucssessMassages
	from outgoing_massages
	join massage_info on outgoing_massages.massage_info_id = massage_info.id 
	where outgoing_massages.company_id = company_id && massage_info.status = 'sent' && massage_info.date between start_date and end_date;
	/*get the  pending massages */
	select 
	count(outgoing_massages.company_id) 
	into pendingMassages
	from outgoing_massages
	join massage_info on outgoing_massages.massage_info_id = massage_info.id 
	where outgoing_massages.company_id = company_id && massage_info.status = 'pending' && massage_info.date between start_date and end_date;
	/*get the  failed massages */
	select 
	count(outgoing_massages.company_id) 
	into failedMassages
	from outgoing_massages
	join massage_info on outgoing_massages.massage_info_id = massage_info.id 
	where outgoing_massages.company_id = company_id && massage_info.status = 'failed' && massage_info.date between start_date and end_date;
	
	/*insert the data into the table */
	insert into company_month_stats (month_name,
									start_date,
									end_date,
									num_of_log_in,
									num_of_massages_sent,
									num_of_failed_massages,
									num_of_sucsses_massages_sent,
									num_of_pending_massages,
									company_id,
									company_email)
							values  (monthName,
									start_date,
									end_date,
									logIns,
									sentMassages,
									failedMassages,
									sucssessMassages,
									pendingMassages,
									company_id,
									companyEmail);
end if;
END $$
DELIMITER ;


-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
drop procedure if exists `GetAllMonthlyStatsForUser`;
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllMonthlyStatsForUser`(in user_id int)
begin

DECLARE error_msg CONDITION FOR SQLSTATE '45000';
declare isExist int;

select isUserIdExist(user_id) into isExist;

if isExist = 0 then
	SET @message_text = CONCAT('A user with the ID: ', user_id, ' does not exist.');
	SIGNAL error_msg
	SET MESSAGE_TEXT = @message_text;
else
	select * from user_month_stats where user_month_stats.user_id = user_id;
end if;
END $$
DELIMITER ;



-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
drop procedure if exists `GetAllMonthlyStatsForCompany`;
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllMonthlyStatsForCompany`(in company_id int)
begin

DECLARE error_msg CONDITION FOR SQLSTATE '45000';
declare isExist int;

select isCompanyIdExist(company_id) into isExist;

if isExist = 0 then
	SET @message_text = CONCAT('A comapny with the ID: ', company_id, ' does not exist.');
	SIGNAL error_msg
	SET MESSAGE_TEXT = @message_text;
else
	select * from company_month_stats where company_month_stats.company_id = company_id;
end if;
END $$
DELIMITER ;


-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
drop procedure if exists `GetMailTemplateManagedBy`;
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetMailTemplateManagedBy`(IN id INT)
BEGIN
	SELECT
		admin.id,
		admin.name AS admin_name,
		admin.email,
		mail_template.name AS template_name,
		mail_template.description,
		mail_template.context,
		mail_template.path_to_file,
		mail_template.create_date
	FROM admin
	JOIN admin_manage_mail_template ON admin.id = admin_manage_mail_template.admin_id
	JOIN mail_template ON admin_manage_mail_template.mail_template_id = mail_template.id
	WHERE admin.id = id;
END $$
DELIMITER ;


-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
drop procedure if exists `GetAllMailTemplatesFor`;
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllMailTemplatesFor`(IN target varchar(45))
BEGIN
	IF (target = 'All') THEN
		SELECT * FROM mail_template;
	ELSE
		SELECT * FROM mail_template WHERE mail_template.name LIKE target;
	END IF;
END $$
DELIMITER ;


-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
drop procedure if exists `GetAllUsersOfCompany`;
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllUsersOfCompany`(IN company_id INT, IN email varchar(45))
BEGIN
	DECLARE error_msg CONDITION FOR SQLSTATE '45000';
	DECLARE isExist INT;

	SELECT isCompanyIdExist(company_id) INTO isExist;
	IF isExist = 0 THEN
		SET @message_text = CONCAT('A comapny with the ID: ', company_id, ' does not exist.');
		SIGNAL error_msg
		SET MESSAGE_TEXT = @message_text;
	ELSE
		SELECT
			company.id AS company_id,
			company.name,
			company.email AS company_email,
			user.id	AS user_id,
			user.email AS user_email,
			user.first_name,
			user.last_name
		FROM company
		JOIN user_client_of_company ON user_client_of_company.company_id = company.id
				&& user_client_of_company.company_email = company.email
		JOIN user ON user_client_of_company.user_id = user.id
				&& user_client_of_company.user_email = user.email
		WHERE company.id = company_id && company.email = email;
	END IF;
END $$
DELIMITER ;



drop procedure if exists `GetUserAddress`;
-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetUserAddress`(IN user_id INT, IN email varchar(45))
BEGIN
	DECLARE error_msg CONDITION FOR SQLSTATE '45000';
	DECLARE isExist INT;

	SELECT isUserIdExist(user_id) INTO isExist;
	IF isExist = 0 THEN
		SET @message_text = CONCAT('A user with the ID: ', user_id, ' does not exist.');
		SIGNAL error_msg
		SET MESSAGE_TEXT = @message_text;
	ELSE
		SELECT
			user.id,
			user.first_name,
			user.last_name,
			user.email,
			address.country,
			address.city,
			address.street_name,
			address.house_number,
			address.postal_code
		FROM user
		JOIN user_has_address ON user_has_address.user_id = user.id
				&& user_has_address.user_email = user.email
		JOIN address ON user_has_address.address_id = address.id
		WHERE user.id = user_id && user.email = email;
	END IF;
END $$
DELIMITER ;



drop procedure if exists `GetCompanyAddress`;
-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetCompanyAddress`(IN company_id INT, IN email varchar(45))
BEGIN
	DECLARE error_msg CONDITION FOR SQLSTATE '45000';
	DECLARE isExist INT;

	SELECT isCompanyIdExist(company_id) INTO isExist;
	IF isExist = 0 THEN
		SET @message_text = CONCAT('A comapny with the ID: ', company_id, ' does not exist.');
		SIGNAL error_msg
		SET MESSAGE_TEXT = @message_text;
	ELSE
		SELECT
			company.id,
			company.name,
			company.email,
			address.country,
			address.city,
			address.street_name,
			address.house_number,
			address.postal_code
		FROM company
		JOIN company_has_address ON company_has_address.company_id = company.id
				&& company_has_address.company_email = company.email
		JOIN address ON company_has_address.address_id = address.id
		WHERE company.id = company_id && company.email = email;
	END IF;
END $$
DELIMITER ;



drop procedure if exists `GetAllCompanyOutgoingMassages`;
-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllCompanyOutgoingMassages`(in id int)
BEGIN

DECLARE error_msg CONDITION FOR SQLSTATE '45000';
DECLARE isExist INT;
	
SELECT isCompanyIdExist(id) INTO isExist;

IF isExist=0 THEN
	SET @message_text = CONCAT('A comapny with the ID: ', id, ' dosent exist.');
	SIGNAL error_msg
	SET MESSAGE_TEXT = @message_text;
else
	select 
		company.name as company_name,
		massage_info.send_to,
		massage_info.send_from,
		massage_info.subject,
		massage_info.content,
		file.name as file_name,
		file.path
	from company
	join outgoing_massages on company.id = outgoing_massages.company_id
	join massage_info on outgoing_massages.massage_info_id = massage_info.id
	left join file on massage_info.id = file.massage_id
	where company.id = id;
end if;
END $$
DELIMITER ;


drop procedure if exists `GetAllCompanyOutgoingMassagesToCostumer`;
-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllCompanyOutgoingMassagesToCostumer`(in company_id int, in user_id int)
BEGIN

declare user_email varchar(45);
DECLARE error_msg CONDITION FOR SQLSTATE '45000';
DECLARE isCompanyExist INT;
declare isUserExist int;
	
SELECT isCompanyIdExist(company_id) INTO isCompanyExist;
select isUserIdExist(user_id) into isUserExist;

IF isCompanyExist=0 || isUserExist=0  THEN
	SET @message_text = CONCAT('An error with the company id or the user id');
	SIGNAL error_msg
	SET MESSAGE_TEXT = @message_text;
else
	/*get the user email into my var*/
	select user.email into user_email from user where user.id = user_id;
	/*selecting the proper fildes from the tables */
	select 
		company.name as company_name,
		massage_info.send_to,
		massage_info.send_from,
		massage_info.subject,
		massage_info.content,
		file.name as file_name,
		file.path
	from company
	join outgoing_massages on company.id = outgoing_massages.company_id
	join massage_info on outgoing_massages.massage_info_id = massage_info.id
	left join file on massage_info.id = file.massage_id
	where company.id = company_id && massage_info.send_to = user_email;
end if;
END $$
DELIMITER ;


drop procedure if exists `GetAllCompanyContacts`;
-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllCompanyContacts`(in company_id int)
BEGIN


DECLARE error_msg CONDITION FOR SQLSTATE '45000';
declare isExist int;

select isCompanyIdExist(company_id) into isExist;

if isExist = 0 then
	SET @message_text = CONCAT('A comapny with the ID: ', company_id, ' does not exist.');
	SIGNAL error_msg
	SET MESSAGE_TEXT = @message_text;
else
	select 
		contact_person.name, 
		contact_person.position,  
		contact_info.email,
		contact_info.phone_number,
		contact_info.fax_number,
		contact_info.office_number
	from contact_person
	join contact_info on contact_info.contact_person_id = contact_person.id
	where contact_person.company_id = company_id;
end if;		

END $$
DELIMITER ;

drop procedure if exists `AddContactToCompany`;
-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `AddContactToCompany`(in company_id int,
																	in name varchar(45),
																	in position varchar(45),
																	in email varchar(45),
																	in phone_number varchar(12),
																	in office_number varchar(12),
																	in fax_number varchar(12))
BEGIN

DECLARE error_msg CONDITION FOR SQLSTATE '45000';
declare isExist int;
declare company_email varchar (45);

select isCompanyIdExist(company_id) into isExist;

if isExist = 0
	then	
	SET @message_text = CONCAT('A comapny with the ID: ', company_id, ' does not exist.');
		SIGNAL error_msg
		SET MESSAGE_TEXT = @message_text;
	else
	/* get the company email*/
	select company.email into company_email from company where company.id = company_id;
	/*insert data into contact info table*/
	insert into contact_person(name, position,company_id,company_email)values (name,position,company_id,company_email);
	/*insert data into the child table contact info */
	insert into contact_info(phone_number,email,fax_number,office_number,contact_person_id) values (phone_number,email,fax_number,office_number,LAST_INSERT_ID());
end if;

END $$
DELIMITER ;

drop procedure if exists `GetAllUserIncomingMassagesFromCompany`;
-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllUserIncomingMassagesFromCompany`(in user_id int, in company_id int)
BEGIN

declare company_email varchar(45);
select company.email into company_email from company where company.id = company_id;

select 
	user.first_name,
	user.last_name,
	massage_info.send_to,
	massage_info.send_from,
	massage_info.subject,
	massage_info.content,
	file.name,
	file.path
from user
join incoming_massages on user.id = incoming_massages.user_id
join massage_info on incoming_massages.massage_info_id = massage_info.id
left join file on massage_info.id = file.massage_id
where user.id = user_id && massage_info.send_from = company_email;

END $$
DELIMITER ;

drop procedure if exists `GetAllUserIncomingMassages`;
-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllUserIncomingMassages`(in user_id int)
BEGIN

DECLARE error_msg CONDITION FOR SQLSTATE '45000';
declare isExist int;

select isUserIdExist(user_id) into isExist;
if isExist = 0
	then	
	SET @message_text = CONCAT('A User with the ID: ', user_id, ' does not exist.');
		SIGNAL error_msg
		SET MESSAGE_TEXT = @message_text;
	else
	select 
		user.first_name,
		user.last_name,
		massage_info.send_to,
		massage_info.send_from,
		massage_info.subject,
		massage_info.content,
		file.name,
		file.path
	from user
	join incoming_massages on user.id = incoming_massages.user_id
	join massage_info on incoming_massages.massage_info_id = massage_info.id
	left join file on massage_info.id = file.massage_id
	where user.id = user_id;
END if;
END $$
DELIMITER ;

drop procedure if exists `GetAllNewCompanysBetween`;
-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllNewCompanysBetween`( in start_date date, in end_date date)
BEGIN

select company.name, company.id, company.email,company.join_date
from company
where company.join_date between start_date and end_date;

END $$
DELIMITER ;


drop procedure if exists `GetAllNewUsersBetween`;
-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllNewUsersBetween`( in start_date date, in end_date date)
BEGIN

select user.first_name, user.last_name, user.id,user.email,user.join_date
from user
where user.join_date between start_date and end_date ;

END $$
DELIMITER ;

drop procedure if exists `GetCompanyLoginsBetweenDates`;
-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE`GetCompanyLoginsBetweenDates` (in company_id int, in start_date date, in end_date date)
BEGIN

DECLARE error_msg CONDITION FOR SQLSTATE '45000';
declare isExist int;

select isCompanyIdExist(company_id) into isExist;
if isExist = 0 then	
	SET @message_text = CONCAT('A Company with the ID: ', company_id, ' does not exist.');
	SIGNAL error_msg
	SET MESSAGE_TEXT = @message_text;
else
	select company.name , company.id, log_in_event.date, log_in_event.time 
	from company
	join company_has_log_in_event on company.id = company_has_log_in_event.company_id 
	join log_in_event on log_in_event.id = company_has_log_in_event.log_in_event_id
	where company.id = company_id;

end if;
END $$
DELIMITER ;

drop procedure if exists `GetUserLoginsBetweenDates`;
-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetUserLoginsBetweenDates`(in user_id int, in start_date date, in end_date date)
BEGIN

DECLARE error_msg CONDITION FOR SQLSTATE '45000';
declare isExist int;

select isUserIdExist(user_id) into isExist;
if isExist = 0 then	
	SET @message_text = CONCAT('A User with the ID: ', user_id, ' does not exist.');
	SIGNAL error_msg
	SET MESSAGE_TEXT = @message_text;
else
	select user.first_name , user.last_name, user.id, log_in_event.date, log_in_event.time 
	from user
	inner join user_has_log_in_event on user.id = user_has_log_in_event.user_id 
	inner join log_in_event on log_in_event.id = user_has_log_in_event.log_in_event_id
	where user.id = user_id && log_in_event.date between start_date and end_date ;

END if;
END $$
DELIMITER ;

drop procedure if exists `GetCompanyLogins`;
-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetCompanyLogins`(in company_id int)
BEGIN

DECLARE error_msg CONDITION FOR SQLSTATE '45000';
declare isExist int;

select isCompanyIdExist(company_id) into isExist;
if isExist = 0 then	
	SET @message_text = CONCAT('A Company with the ID: ', company_id, ' does not exist.');
	SIGNAL error_msg
	SET MESSAGE_TEXT = @message_text;
else
	select company.name , company.id, log_in_event.date, log_in_event.time 
	from company
	join company_has_log_in_event on company.id = company_has_log_in_event.company_id 
	join log_in_event on log_in_event.id = company_has_log_in_event.log_in_event_id
	where company.id = company_id;

END if;
END $$
DELIMITER ;

drop procedure if exists `GetUserLogins`;
-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetUserLogins`(in user_id int)
BEGIN

DECLARE error_msg CONDITION FOR SQLSTATE '45000';
declare isExist int;

select isUserIdExist(user_id) into isExist;
if isExist = 0 then	
	SET @message_text = CONCAT('A User with the ID: ', user_id, ' does not exist.');
	SIGNAL error_msg
	SET MESSAGE_TEXT = @message_text;
else
	select user.first_name , user.last_name, user.id, log_in_event.date, log_in_event.time 
	from user
	inner join user_has_log_in_event on user.id = user_has_log_in_event.user_id 
	inner join log_in_event on log_in_event.id = user_has_log_in_event.log_in_event_id
	where user.id = user_id;

END if;
END $$
DELIMITER ;

drop procedure if exists `GetAllLogInEventAtDay`;
-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllLogInEventAtDay`(in input_date date)
BEGIN

select * from log_in_event where log_in_event.date=input_date;
END $$
DELIMITER ;

drop procedure if exists `GetSubsciptionPlanFor`;
-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetSubsciptionPlanFor`(IN target_audience varchar(45))
BEGIN
	if (target_audience = 'All') then
		select * from subscription_plans;
	else
		select * from subscription_plans where subscription_plans.target_audience=target_audience; 
	end if;
END $$
DELIMITER ;

drop procedure if exists `AddSubscriptionPlan`;
-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `AddSubscriptionPlan`(IN plan_name varchar(45)
																	,IN plan_description text
																	,IN input_path_to_file varchar(45)
																	,IN input_target_audience varchar(45))
BEGIN	

	insert into subscription_plans (name, description, path_to_fie, target_audience)
	values (plan_name, plan_description, input_path_to_file, input_target_audience);
END $$
DELIMITER ;


drop procedure if exists `GetAllUserInvoices`;
-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllUserInvoices`(IN user_id INT)
BEGIN

DECLARE error_msg CONDITION FOR SQLSTATE '45000';
declare isExist int;

select isUserIdExist(user_id) into isExist;
if isExist = 0 then	
	SET @message_text = CONCAT('A User with the ID: ', user_id, ' does not exist.');
	SIGNAL error_msg
	SET MESSAGE_TEXT = @message_text;
else
	SELECT user.id, user.billing_info_id, invoice_info.content,
		invoice_info.issue_date, invoice_info.due_date, invoice_info.amount
	FROM user, invoice_info
	WHERE user.id=user_id && user.billing_info_id = invoice_info.billing_info_id;
END if;
END $$
DELIMITER ;

drop procedure if exists `AddUserLoginEvent`;
-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `AddUserLoginEvent`(IN id INT,IN email varchar(45))
  BEGIN
    DECLARE error_msg CONDITION FOR SQLSTATE '45000';
    DECLARE isExist INT;

    SELECT isUserIdExist(id) INTO isExist;

    if isExist = 1 then

      INSERT INTO `mygreenbilldb`.`log_in_event` ( `date`, `time`) VALUES (CURDATE(), CURTIME());
      INSERT INTO `mygreenbilldb`.`user_has_log_in_event` (`user_id`, `user_email`, `log_in_event_id`) VALUES (id, email, LAST_INSERT_ID());

    else
      SET @message_text = CONCAT('A user with the ID: ', user_id, ' does not exist.');
      SIGNAL error_msg
      SET MESSAGE_TEXT = @message_text;
    end if;

  END$$
DELIMITER ;