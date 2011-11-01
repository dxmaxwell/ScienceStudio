/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    Permissions class.
 *     
 */
package ca.sciencestudio.model;

import java.io.Serializable;

/**
 * @author maxweld
 * 
 *
 */
public class Permissions implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean add;
	private boolean edit;
	private boolean admin;
	private boolean remove;
	
	public Permissions() {
		this(false, false, false, false);
	}
	
	public Permissions(boolean admin) {
		this(admin, admin, admin, admin);
	}
	
	public Permissions(Permissions p) {
		this(p.canAdmin(), p.canAdd(), p.canEdit(), p.canRemove());
	}
	
	public Permissions(boolean admin, boolean add, boolean edit, boolean remove) {
		this.add = add;
		this.edit = edit;
		this.admin = admin;
		this.remove = remove;
	}
	
	public void or(Permissions p) {
		add = add || p.canAdd();
		edit  = edit || p.canEdit();
		admin = admin || p.canAdmin();
		remove = remove || p.canRemove();
	}
	
	public void and(Permissions p) {
		add = add && p.canAdd();
		edit = edit && p.canEdit();
		admin = admin && p.canAdmin();
		remove = remove && p.canRemove();
	}
	
	public void invert() {
		add = !add;
		edit = !edit;
		admin = !admin;
		remove = !remove;
	}
	
	public boolean isAdd() {
		return add;
	}
	public boolean canAdd() {
		return add;
	}
	public void setAdd(boolean add) {
		this.add = add;
	}
	
	public boolean isEdit() {
		return edit;
	}
	public boolean canEdit() {
		return edit;
	}
	public void setEdit(boolean edit) {
		this.edit = edit;
	}
	
	public boolean isAdmin() {
		return admin;
	}
	public boolean canAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isRemove() {
		return remove;
	}
	public boolean canRemove() {
		return remove;
	}
	public void setRemove(boolean remove) {
		this.remove = remove;
	}
}
