#------------------- Create Survey-schema tables ---------------------------------
#--	Copyright (c) Canadian Light Source, Inc. All rights reserved.
#--	- see license.txt for details.
#--	
#--	Description:
#--		CreateTable sql file.
#--

USE cls_uso;

CREATE TABLE survey_category (
	category_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	category_name VARCHAR(50) NOT NULL,
	description VARCHAR(200)
) ENGINE=InnoDB;

CREATE TABLE survey (
	survey_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	survey_name VARCHAR(50) NOT NULL,
	category_id INT NOT NULL,
	description VARCHAR(2000),
	participant_description VARCHAR(100),
	status INT,                             #--0-Created, 1-Started, 2-Stopped
	deadline DATE,
	create_time TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE survey_participant (
	participant_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	survey_id INT NOT NULL,
	person_id INT NOT NULL,
	beamline VARCHAR(30),
	description VARCHAR(200),
	status INT,                             #--0-Created, 1-Started, 2-Submitted
	notification_date DATE,
	remind_date1 DATE,
	remind_date2 DATE
) ENGINE=InnoDB;

CREATE TABLE survey_participant_notify_event (
	event_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	participant_id INT NOT NULL,
	person_id INT NOT NULL,
	event VARCHAR(20),                      #--Notified, Reminded First Time, Reminded Second Time
	record_time TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE survey_section (
	section_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	survey_id INT NOT NULL,
	section_order INT,
	section_name VARCHAR(50) NOT NULL,
	description VARCHAR(200)
) ENGINE=InnoDB;

CREATE TABLE survey_question (
	question_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	section_id INT,
	question_order INT,
	question_text VARCHAR(2000) NOT NULL,
	question_type INT,                      #--0-multi-choices, 1-multi-choises+other, 2-PlainText
	answer_length INT,                      #--no use if question_type=0 
	image VARCHAR(50)                       #--Image Location
) ENGINE=InnoDB;

CREATE TABLE survey_choice (
	choice_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	question_id INT NOT NULL,
	choice_order INT,
	choice_text VARCHAR(500) NOT NULL,
	image VARCHAR(50)                       #--Image Location
) ENGINE=InnoDB;

CREATE TABLE survey_feedback (
	feedback_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	question_id INT NOT NULL,
	participant_id INT NOT NULL,
	choice INT,                             #--choice_id to any choice or 0 to other if question_type = 1 
	answer VARCHAR(2000)
) ENGINE=InnoDB;
