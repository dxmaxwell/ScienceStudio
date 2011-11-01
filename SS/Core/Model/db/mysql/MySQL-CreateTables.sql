------------------- Create tables ---------------------------------
-- Copyright (c) Canadian Light Source, Inc. All rights reserved.
--	- see license.txt for details.
--	
--	Description:
--		CreateTable sql file.
--

-- --------------------- Person tables ------------------------------------------
CREATE TABLE person (
	person_id INT AUTO_INCREMENT PRIMARY KEY,
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
CREATE TABLE project (
	project_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(80) NOT NULL,
	description VARCHAR(2000),
	start_date DATETIME,
	end_date DATETIME,
	status VARCHAR(20) NOT NULL
) ENGINE=InnoDB;


CREATE TABLE project_person (
	project_person_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	project_id INT NOT NULL,
	person_gid VARCHAR(20) NOT NULL,
	role VARCHAR(20) NOT NULL
) ENGINE=InnoDB;

-- ------------------ Sample tables --------------------------------------
CREATE TABLE sample (
	sample_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	project_id INT NOT NULL,
	name VARCHAR(80) NOT NULL,
	description VARCHAR(2000),
	cas_number VARCHAR(45),
	state VARCHAR(20),
	quantity VARCHAR(100),
	hazards VARCHAR(1000),
	other_hazards VARCHAR(1000)
) ENGINE=InnoDB;


-- ------------------ Session tables -----------------------------------
CREATE TABLE session (
	session_id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(80) NOT NULL,
	description VARCHAR(2000),
	proposal VARCHAR(100),
	start_date DATETIME,
	end_date DATETIME,
	project_gid VARCHAR(20) NOT NULL,
	laboratory_id INT NOT NULL
) ENGINE=InnoDB;

CREATE TABLE session_person (
	session_person_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	session_id INT NOT NULL,
	person_gid VARCHAR(20) NOT NULL,
	role VARCHAR(20) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE experiment (
	experiment_id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(80) NOT NULL,
	description VARCHAR(2000),
	session_id INT NOT NULL,
	source_gid VARCHAR(20) NOT NULL,
	instrument_technique_id INT NOT NULL
) ENGINE=InnoDB;

CREATE TABLE scan (
	scan_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(80) NOT NULL,
	data_url VARCHAR(1000),
	parameters VARCHAR(10000),
	parameters_type varchar(60),
	start_date DATETIME,
	end_date DATETIME,
	experiment_id INT NOT NULL
) ENGINE=InnoDB;

-- ------------------ Facility tables -------------------------------------
CREATE TABLE facility (
	facility_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(20) NOT NULL UNIQUE,
	long_name VARCHAR(80),
	description VARCHAR(2000),
	phone_number VARCHAR(50),
	email_address VARCHAR(255),
	location VARCHAR(255),
	authc_url VARCHAR(1000),
	home_url VARCHAR(1000)
) ENGINE=InnoDB;

CREATE TABLE technique (
	technique_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(20) NOT NULL UNIQUE,
	long_name VARCHAR(80),
	description VARCHAR(2000)
) ENGINE=InnoDB;

CREATE TABLE instrument (
	instrument_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	laboratory_id INT NOT NULL,
	name VARCHAR(20) NOT NULL,
	long_name VARCHAR(80),
	description VARCHAR(2000)
) ENGINE=InnoDB;

CREATE TABLE instrument_technique (
	instrument_technique_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	instrument_id INT NOT NULL,
	technique_id INT NOT NULL
) ENGINE=InnoDB;

CREATE TABLE laboratory(
	laboratory_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	facility_id INT NOT NULL,
	name VARCHAR(20) NOT NULL UNIQUE,
	long_name VARCHAR(80),
	description VARCHAR(2000),
	phone_number VARCHAR(50),
	email_address VARCHAR(255),
	location VARCHAR(255),
	view_url VARCHAR(1000)
) ENGINE=InnoDB;
