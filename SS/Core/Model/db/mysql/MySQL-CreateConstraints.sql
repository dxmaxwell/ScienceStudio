-- ----------------- Create constraints ---------------------------------
--	Copyright (c) Canadian Light Source, Inc. All rights reserved.
--	- see license.txt for details.
--	
--	Description:
--		CreateConstraints sql file.
--

USE scstudio;

-- ---------- scstudio.project --------------------
ALTER TABLE scstudio.project_person ADD CONSTRAINT fk_project_person_project_id FOREIGN KEY(project_id) REFERENCES scstudio.project(project_id) ON DELETE CASCADE ON UPDATE NO ACTION;

-- ---------- scstudio.instrument -----------------
ALTER TABLE scstudio.instrument ADD CONSTRAINT fk_instrument_laboratory_id FOREIGN KEY(laboratory_id) REFERENCES scstudio.laboratory(laboratory_id) ON DELETE CASCADE ON UPDATE NO ACTION;

-- ---------- scstudio.instrument_technique -------
ALTER TABLE scstudio.instrument_technique ADD CONSTRAINT fk_instrument_technique_instrument_id FOREIGN KEY(instrument_id) REFERENCES scstudio.instrument(instrument_id) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE scstudio.instrument_technique ADD CONSTRAINT fk_instrument_technique_technique_id FOREIGN KEY(technique_id) REFERENCES scstudio.technique(technique_id) ON DELETE CASCADE ON UPDATE NO ACTION;

-- ---------- scstudio.laboratory -----------------
ALTER TABLE scstudio.laboratory ADD CONSTRAINT fk_laboratory_facility_id FOREIGN KEY(facility_id) REFERENCES scstudio.facility(facility_id) ON DELETE CASCADE ON UPDATE NO ACTION;

-- ---------- scstudio.sample ---------------------
ALTER TABLE scstudio.sample ADD CONSTRAINT fk_sample_project_id FOREIGN KEY(project_id) REFERENCES scstudio.project(project_id) ON DELETE CASCADE ON UPDATE NO ACTION;

-- ----------- scstudio.session ---------------------
ALTER TABLE scstudio.session ADD CONSTRAINT fk_session_project_id FOREIGN KEY (project_id) REFERENCES scstudio.project(project_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE scstudio.session ADD CONSTRAINT fk_session_laboratory_id FOREIGN KEY (laboratory_id) REFERENCES scstudio.laboratory(laboratory_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------- scstudio.experiment ------------------
ALTER TABLE scstudio.experiment ADD CONSTRAINT fk_experiment_session_id FOREIGN KEY (session_id) REFERENCES scstudio.session(session_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE scstudio.experiment ADD CONSTRAINT fk_experiment_sample_id FOREIGN KEY (sample_id) REFERENCES scstudio.sample(sample_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE scstudio.experiment ADD CONSTRAINT fk_experiment_instrument_technique_id FOREIGN KEY (instrument_technique_id) REFERENCES scstudio.instrument_technique(instrument_technique_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- --------- scstudio.scan --------------
ALTER TABLE scstudio.scan ADD CONSTRAINT fk_scan_experiment_id FOREIGN KEY (experiment_id) REFERENCES scstudio.experiment(experiment_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
