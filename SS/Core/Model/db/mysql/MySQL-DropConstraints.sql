-- ----------------- Drop constraints ---------------------------------
--	Copyright (c) Canadian Light Source, Inc. All rights reserved.
--	- see license.txt for details.
--	
--	Description:
--		DropConstraints sql file.
--

-- ---------- person --------------------
ALTER TABLE account DROP FOREIGN KEY fk_account_person_id;

-- ---------- project_person --------------------
ALTER TABLE project_person DROP FOREIGN KEY fk_project_person_project_id;

-- ---------- instrument -----------------
ALTER TABLE instrument DROP FOREIGN KEY fk_instrument_laboratory_id;

-- ---------- instrument_technique -------
ALTER TABLE instrument_technique DROP FOREIGN KEY fk_instrument_technique_instrument_id;
ALTER TABLE instrument_technique DROP FOREIGN KEY fk_instrument_technique_technique_id;

-- ---------- laboratory -----------------
ALTER TABLE laboratory DROP FOREIGN KEY fk_laboratory_facility_id;

-- ---------- sample ---------------------
ALTER TABLE sample DROP FOREIGN KEY fk_sample_project_id;

-- ----------- session ---------------------
ALTER TABLE session DROP FOREIGN KEY fk_session_laboratory_id;

-- ---------- session_person --------------------
ALTER TABLE session_person DROP FOREIGN fk_session_person_session_id;

-- ----------- experiment ------------------
ALTER TABLE experiment DROP FOREIGN KEY fk_experiment_session_id;
ALTER TABLE experiment DROP FOREIGN KEY fk_experiment_instrument_technique_id;

-- ----------- scan --------------
ALTER TABLE scan DROP FOREIGN KEY fk_scan_experiment_id;
