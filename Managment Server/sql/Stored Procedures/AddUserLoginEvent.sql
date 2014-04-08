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
