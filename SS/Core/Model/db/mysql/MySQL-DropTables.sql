-- ----------------- Drop tables ---------------------------------
--	Copyright (c) Canadian Light Source, Inc. All rights reserved.
--	- see license.txt for details.
--	
--	Description:
--		DropTable sql file.


USE scstudio;

-- -------------------- Person tables ------------------------------------------
DROP TABLE scstudio.person;

-- ----------------- Project table --------------------------------------
DROP TABLE scstudio.project;
DROP TABLE scstudio.project_person;

-- ----------------- Sample tables --------------------------------------
DROP TABLE scstudio.sample;

-- ----------------- Session tables -----------------------------------
DROP TABLE scstudio.session;
DROP TABLE scstudio.experiment;
DROP TABLE scstudio.scan;

-- ----------------- Facility tables -------------------------------------
DROP TABLE scstudio.facility;
DROP TABLE scstudio.technique;
DROP TABLE scstudio.instrument;
DROP TABLE scstudio.instrument_technique;
DROP TABLE scstudio.laboratory;
