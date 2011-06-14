-- ----------------- Create constraints ---------------------------------
--	Copyright (c) Canadian Light Source, Inc. All rights reserved.
--	- see license.txt for details.
--	
--	Description:
--		AlterTables sql file. Utility file to help with migration between SS and ANISE schemas.
--

ALTER TABLE account CHANGE person_uid person_gid VARCHAR(50) NOT NULL;
ALTER TABLE login_group_member CHANGE person_uid person_gid VARCHAR(50) NOT NULL;

DROP TABLE login_session;