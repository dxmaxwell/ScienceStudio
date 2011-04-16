------------------- Create tables ---------------------------------
-- Copyright (c) Canadian Light Source, Inc. All rights reserved.
--	- see license.txt for details.
--	
--	Description:
--		CreateTable sql file.
-- NOTE: this script needs to be reviewed, column details added, etc.

USE scstudio;

-- --------------------- Person tables ------------------------------------------
CREATE TABLE scstudio.person (
	person_id INT AUTO_INCREMENT PRIMARY KEY,
	person_uid VARCHAR(100) NOT NULL UNIQUE,
	title VARCHAR(10),
	first_name VARCHAR(60) NOT NULL,
	middle_name VARCHAR(60), 
	last_name VARCHAR(60) NOT NULL,
	phone_number VARCHAR(50) NOT NULL,
	mobile_number VARCHAR(50),
	email_address VARCHAR(255) NOT NULL,
	modification_date DATETIME NOT NULL
) ENGINE=InnoDB;


-- ------------------ Project table --------------------------------------
CREATE TABLE scstudio.project (
	project_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(80) NOT NULL,
	description VARCHAR(255),
	start_date DATE,
	end_date DATE,
	status VARCHAR(20) NOT NULL
) ENGINE=InnoDB;


CREATE TABLE scstudio.project_person (
	project_person_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	project_id INT NOT NULL,
	person_uid VARCHAR(100) NOT NULL,
	project_role VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

-- ------------------ Sample tables --------------------------------------
CREATE TABLE scstudio.sample (
	sample_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	project_id INT NOT NULL,
	name VARCHAR(40),
	description VARCHAR(255),
	cas_number VARCHAR(45),
	state VARCHAR(40),
	quantity VARCHAR(100),
	hazards VARCHAR(255),
	other_hazards VARCHAR(255)
) ENGINE=InnoDB;


-- ------------------ Session tables -----------------------------------
CREATE TABLE scstudio.session (
	session_id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(60) NOT NULL,
	description VARCHAR(255),
	proposal VARCHAR(100),
	start_date DATETIME,
	end_date DATETIME,
	project_id INT NOT NULL,
	laboratory_id INT NOT NULL
) ENGINE=InnoDB;

CREATE TABLE scstudio.experiment (
	experiment_id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(60) NOT NULL,
	description VARCHAR(255),
	session_id INT NOT NULL,
	sample_id INT NOT NULL,
	instrument_technique_id INT NOT NULL
) ENGINE=InnoDB;

CREATE TABLE scstudio.scan (
	scan_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	data_url VARCHAR(1000),
	parameters VARCHAR(2000),
	start_date DATETIME,
	end_date DATETIME,
	experiment_id INT
) ENGINE=InnoDB;

-- ------------------ Facility tables -------------------------------------
CREATE TABLE scstudio.facility (
	facility_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(20) NOT NULL,
	long_name VARCHAR(80),
	description VARCHAR(255),
	phone_number VARCHAR(30),
	email_address VARCHAR(60),
	location VARCHAR(255)
) ENGINE=InnoDB;

CREATE TABLE scstudio.technique (
	technique_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(20) NOT NULL UNIQUE,
	long_name VARCHAR(80),
	description VARCHAR(255)
) ENGINE=InnoDB;

CREATE TABLE scstudio.instrument (
	instrument_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	laboratory_id INT NOT NULL,
	name VARCHAR(20) NOT NULL,
	long_name VARCHAR(80),
	description VARCHAR(255)
) ENGINE=InnoDB;

CREATE TABLE scstudio.instrument_technique (
	instrument_technique_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	instrument_id INT,
	technique_id INT
) ENGINE=InnoDB;

CREATE TABLE scstudio.laboratory(
	laboratory_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	facility_id INT NOT NULL,
	name VARCHAR(20) NOT NULL,
	long_name VARCHAR(60),
	description VARCHAR(255),
	phone_number VARCHAR(40),
	email_address VARCHAR(60),
	location VARCHAR(45),
	view_url VARCHAR(200)
) ENGINE=InnoDB;

