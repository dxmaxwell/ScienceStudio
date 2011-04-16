#------------------- Drop Survey-schema constraints ---------------------------------
#--	Copyright (c) Canadian Light Source, Inc. All rights reserved.
#--	- see license.txt for details.
#--	
#--	Description:
#--		DropConstraints sql file.
#--

USE cls_uso;

ALTER TABLE survey DROP FOREIGN KEY fk_survey_category_id;
ALTER TABLE survey_participant DROP FOREIGN KEY fk_survey_participant_survey_id;
ALTER TABLE survey_participant DROP FOREIGN KEY fk_survey_participant_person_id;
ALTER TABLE survey_participant_notify_event DROP FOREIGN KEY fk_survey_participant_notify_event_participant_id;
ALTER TABLE survey_section DROP FOREIGN KEY fk_survey_section_survey_id;
ALTER TABLE survey_question DROP FOREIGN KEY fk_survey_question_section_id;
ALTER TABLE survey_choice DROP FOREIGN KEY fk_survey_choice_question_id;
ALTER TABLE survey_feedback DROP FOREIGN KEY fk_survey_feedback_question_id;
ALTER TABLE survey_feedback DROP FOREIGN KEY fk_survey_feedback_participant_id;
