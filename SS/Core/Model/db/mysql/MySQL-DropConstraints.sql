-- ----------------- Drop constraints ---------------------------------
--	Copyright (c) Canadian Light Source, Inc. All rights reserved.
--	- see license.txt for details.
--	
--	Description:
--		DropConstraints sql file.
--

USE scstudio;

ALTER TABLE scstudio.project_person DROP FOREIGN KEY fk_project_person_project_id;

ALTER TABLE scstudio.instrument DROP FOREIGN KEY fk_instrument_laboratory_id;

ALTER TABLE scstudio.instrument_technique DROP FOREIGN KEY fk_instrument_technique_instrument_id;
ALTER TABLE scstudio.instrument_technique DROP FOREIGN KEY fk_instrument_technique_technique_id;

ALTER TABLE scstudio.laboratory DROP FOREIGN KEY fk_laboratory_facility_id;

ALTER TABLE scstudio.sample DROP FOREIGN KEY fk_sample_project_id;

ALTER TABLE scstudio.session DROP FOREIGN KEY fk_session_project_id;
ALTER TABLE scstudio.session DROP FOREIGN KEY fk_session_laboratory_id;

ALTER TABLE scstudio.experiment DROP FOREIGN KEY fk_experiment_session_id;
ALTER TABLE scstudio.experiment DROP FOREIGN KEY fk_experiment_sample_id;
ALTER TABLE scstudio.experiment DROP FOREIGN KEY fk_experiment_instrument_technique_id;

ALTER TABLE scstudio.scan DROP FOREIGN KEY fk_scan_experiment_id;
