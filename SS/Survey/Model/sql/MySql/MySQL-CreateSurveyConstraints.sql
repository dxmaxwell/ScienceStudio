#------------------- Create Survey-schema constraints ---------------------------------
#--	Copyright (c) Canadian Light Source, Inc. All rights reserved.
#--	- see license.txt for details.
#--	
#--	Description:
#--		CreateConstraints sql file.
#--

USE cls_uso;

ALTER TABLE survey ADD CONSTRAINT fk_survey_category_id FOREIGN KEY(category_id) REFERENCES survey_category(category_id) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE survey_participant ADD CONSTRAINT fk_survey_participant_survey_id FOREIGN KEY(survey_id) REFERENCES survey(survey_id) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE survey_participant ADD CONSTRAINT fk_survey_participant_person_id FOREIGN KEY(person_id) REFERENCES cls_directory.person(id) ON DELETE NO ACTION ON UPDATE NO ACTION; 
ALTER TABLE survey_participant_notify_event ADD CONSTRAINT fk_survey_participant_notify_event_participant_id FOREIGN KEY(participant_id) REFERENCES survey_participant(participant_id) ON DELETE NO ACTION ON UPDATE NO ACTION; 
ALTER TABLE survey_section ADD CONSTRAINT fk_survey_section_survey_id FOREIGN KEY(survey_id) REFERENCES survey(survey_id) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE survey_question ADD CONSTRAINT fk_survey_question_section_id FOREIGN KEY(section_id) REFERENCES survey_section(section_id) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE survey_choice ADD CONSTRAINT fk_survey_choice_question_id FOREIGN KEY(question_id) REFERENCES survey_question(question_id) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE survey_feedback ADD CONSTRAINT fk_survey_feedback_question_id FOREIGN KEY(question_id) REFERENCES survey_question(question_id) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE survey_feedback ADD CONSTRAINT fk_survey_feedback_participant_id FOREIGN KEY(participant_id) REFERENCES survey_participant(participant_id) ON DELETE CASCADE ON UPDATE NO ACTION;
