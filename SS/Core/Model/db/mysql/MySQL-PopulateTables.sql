-- ----------------- Populate Core-schema tables ---------------------------------
--	Copyright (c) Canadian Light Source, Inc. All rights reserved.
--	- see license.txt for details.
--	
--	Description:
--		PopulateTable sql file.
--

USE scstudio;


-- =========================================================================================
-- Facility									
-- =========================================================================================
insert into scstudio.facility (name, long_name, description, phone_number, email_address, location)
	values ('CLS', 'Canadian Light Source', '', '(306) 657-3500', '', 'Saskatoon, Saskatchewan, Canada'); 

-- =========================================================================================
-- Laboratory
-- =========================================================================================
insert into scstudio.laboratory (facility_id, name, long_name, description, phone_number, email_address, view_url) 
	values (1, 'VESPERS', 'VESPERS', '(306) 657-3620', '', '', '/ssvespers/beamline/main.html');

-- =========================================================================================
-- Instrument
-- =========================================================================================
insert into scstudio.instrument (laboratory_id, name, long_name, description) 
	values (1, 'Microprobe', 'Microprobe Endstation', '');

-- =========================================================================================
-- Technique
-- =========================================================================================
insert into scstudio.technique (name, long_name, description) values ('XRF', 'X-Ray Fluorescence', '');

-- =========================================================================================
-- Instrument Technique
-- =========================================================================================
insert into scstudio.instrument_technique (instrument_id, technique_id) values (1,1);
