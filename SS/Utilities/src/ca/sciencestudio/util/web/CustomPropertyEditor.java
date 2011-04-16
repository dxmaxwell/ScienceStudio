/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *   CustomPropertyEditor class.
 *     
 */
package ca.sciencestudio.util.web;

import java.beans.PropertyEditor;

/**
 * @author maxweld
 *
 */
public class CustomPropertyEditor {
	
	private Class<?> requiredClass;
	private PropertyEditor propertyEditor;
	
	public CustomPropertyEditor(Class<?> requiredClass, PropertyEditor propertyEditor) {
		this.requiredClass = requiredClass;
		this.propertyEditor = propertyEditor;
	}

	public Class<?> getRequiredClass() {
		return requiredClass;
	}

	public PropertyEditor getPropertyEditor() {
		return propertyEditor;
	}

	@Override
	public String toString() {
		return "Custom editor for " + requiredClass.toString() + " and editor " + propertyEditor.toString();
	}	
}
