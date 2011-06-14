-- ----------------- Create constraints ---------------------------------
--	Copyright (c) Canadian Light Source, Inc. All rights reserved.
--	- see license.txt for details.
--	
--	Description:
--		CreateConstraints sql file.
--

ALTER TABLE login_group_role ADD CONSTRAINT fk_login_group_role_login_role_id FOREIGN KEY(login_role_id) REFERENCES login_role(login_role_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE login_group_role ADD CONSTRAINT fk_login_group_role_login_group_id FOREIGN KEY(login_group_id) REFERENCES login_group(login_group_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE login_group_member ADD CONSTRAINT fk_login_group_member_login_group_id FOREIGN KEY(login_group_id) REFERENCES login_group(login_group_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
