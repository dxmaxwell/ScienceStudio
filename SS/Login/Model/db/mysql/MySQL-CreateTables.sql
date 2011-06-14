-- ----------------- Create tables ---------------------------------
-- Copyright (c) Canadian Light Source, Inc. All rights reserved.
--	- see license.txt for details.
--	
--	Description:
--		CreateTable sql file.
--

CREATE TABLE account (
	account_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	username VARCHAR(50) NOT NULL UNIQUE,
	password VARCHAR(100),
	person_gid VARCHAR(50) NOT NULL,
	creation_date DATETIME,
	status VARCHAR(40)
) ENGINE=InnoDB;

CREATE TABLE login_role (
	login_role_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE login_group_role (
	login_group_role_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	login_role_id INT NOT NULL,
	login_group_id INT NOT NULL
) ENGINE=InnoDB;

CREATE TABLE login_group (
	login_group_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE login_group_member (
	login_group_member_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	person_gid VARCHAR(50) NOT NULL,
	login_group_id INT NOT NULL
) ENGINE=InnoDB;

CREATE TABLE login_session (
	login_session_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	login_session_uuid VARCHAR(255) NOT NULL UNIQUE,
	login_session_data BLOB,
	login_session_timestamp TIMESTAMP
) ENGINE=InnoDB;

-- Not currently used, but possibly useful
-- CREATE TABLE login_history (
--  	login_history_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
--  	login_history_username VARCHAR(20),
--  	login_history_address VARCHAR(40),
--  	login_history_port INT,
--  	login_history_agent VARCHAR(200),
--  	login_history_timestamp TIMESTAMP,
--  	login_history_status VARCHAR(20)
-- ) ENGINE=InnoDB;
