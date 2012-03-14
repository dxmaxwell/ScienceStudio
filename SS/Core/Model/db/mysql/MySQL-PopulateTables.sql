-- ----------------- Populate Core-schema tables ---------------------------------
--	Copyright (c) Canadian Light Source, Inc. All rights reserved.
--	- see license.txt for details.
--	
--	Description:
--		PopulateTable sql file.
--


-- =========================================================================================
-- Facility
-- =========================================================================================
insert into facility (name, long_name, description, phone_number, email_address, location, authc_url, home_url)
	values ('FLTY', 'Light Source Facility', '', '(555) 555-5555', '', 'City, Province, Country', 'https://cas.server.com/cas', 'http://localhost:8080/ss');


-- =========================================================================================
-- Laboratory
-- =========================================================================================
insert into laboratory (facility_id, name, long_name, description, phone_number, email_address, view_url)
	values (1, 'SIMVESPERS', 'SIMVESPERS', '(555) 555-5555', '', '', 'http://localhost:8080/ssvespers/simbeamline/main.html');

	
-- =========================================================================================
-- Instrument
-- =========================================================================================
insert into instrument (laboratory_id, name, long_name, description)
	values (1, 'Microprobe', 'Microprobe Endstation', '');


-- =========================================================================================
-- Technique
-- =========================================================================================
insert into technique (name, long_name, description) values ('XRF', 'X-Ray Fluorescence', '');
insert into technique (name, long_name, description) values ('XRD', 'X-Ray Diffraction', '');

-- =========================================================================================
-- Instrument Technique
-- =========================================================================================
insert into instrument_technique (instrument_id, technique_id) values (1,1);
insert into instrument_technique (instrument_id, technique_id) values (1,2);
