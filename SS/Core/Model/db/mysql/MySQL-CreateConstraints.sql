-- ----------------- Create constraints ---------------------------------
--	Copyright (c) Canadian Light Source, Inc. All rights reserved.
--	- see license.txt for details.
--	
--	Description:
--		CreateConstraints sql file.
--

-- ---------- person --------------------
ALTER TABLE account ADD CONSTRAINT fk_account_person_id FOREIGN KEY(person_id) REFERENCES person(person_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ---------- project --------------------
ALTER TABLE project ADD CONSTRAINT fk_project_facility_id FOREIGN KEY(facility_id) REFERENCES facility(facility_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE project_person ADD CONSTRAINT fk_project_person_project_id FOREIGN KEY(project_id) REFERENCES project(project_id) ON DELETE CASCADE ON UPDATE NO ACTION;

-- ---------- instrument -----------------
ALTER TABLE instrument ADD CONSTRAINT fk_instrument_laboratory_id FOREIGN KEY(laboratory_id) REFERENCES laboratory(laboratory_id) ON DELETE CASCADE ON UPDATE NO ACTION;

-- ---------- instrument_technique -------
ALTER TABLE instrument_technique ADD CONSTRAINT fk_instrument_technique_instrument_id FOREIGN KEY(instrument_id) REFERENCES instrument(instrument_id) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE instrument_technique ADD CONSTRAINT fk_instrument_technique_technique_id FOREIGN KEY(technique_id) REFERENCES technique(technique_id) ON DELETE CASCADE ON UPDATE NO ACTION;

-- ---------- laboratory -----------------
ALTER TABLE laboratory ADD CONSTRAINT fk_laboratory_facility_id FOREIGN KEY(facility_id) REFERENCES facility(facility_id) ON DELETE CASCADE ON UPDATE NO ACTION;

-- ---------- sample ---------------------
ALTER TABLE sample ADD CONSTRAINT fk_sample_project_id FOREIGN KEY(project_id) REFERENCES project(project_id) ON DELETE CASCADE ON UPDATE NO ACTION;

-- ----------- session ---------------------
ALTER TABLE session ADD CONSTRAINT fk_session_laboratory_id FOREIGN KEY (laboratory_id) REFERENCES laboratory(laboratory_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ---------- session_person --------------------
ALTER TABLE session_person ADD CONSTRAINT fk_session_person_session_id FOREIGN KEY(session_id) REFERENCES session(session_id) ON DELETE CASCADE ON UPDATE NO ACTION;

-- ----------- experiment ------------------
ALTER TABLE experiment ADD CONSTRAINT fk_experiment_session_id FOREIGN KEY (session_id) REFERENCES session(session_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE experiment ADD CONSTRAINT fk_experiment_instrument_technique_id FOREIGN KEY (instrument_technique_id) REFERENCES instrument_technique(instrument_technique_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------- scan --------------
ALTER TABLE scan ADD CONSTRAINT fk_scan_experiment_id FOREIGN KEY (experiment_id) REFERENCES experiment(experiment_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
