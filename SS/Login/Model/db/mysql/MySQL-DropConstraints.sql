-- ----------------- Create constraints ---------------------------------
--	Copyright (c) Canadian Light Source, Inc. All rights reserved.
--	- see license.txt for details.
--	
--	Description:
--		CreateConstraints sql file.
--

USE scstudio;

ALTER TABLE scstudio.login_group_role DROP FOREIGN KEY fk_login_group_role_login_role_id;
ALTER TABLE scstudio.login_group_role DROP FOREIGN KEY fk_login_group_role_login_group_id;
ALTER TABLE scstudio.login_group_member DROP FOREIGN KEY fk_login_group_member_login_group_id;
