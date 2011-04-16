/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *   	BindAndValidateUtil class.
 *     
 */
package ca.sciencestudio.util.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;

import ca.sciencestudio.util.web.CustomPropertyEditor;

/**
 * @author maxweld
 *
 */
public abstract class BindAndValidateUtils extends ValidationUtils {

	public static final DateFormat DATE_FORMAT_ISO8601_FULL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s z");
	public static final DateFormat DATE_FORMAT_ISO8601_LONG = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final DateFormat DATE_FORMAT_ISO8601_SHRT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static final DateFormat DATE_FORMAT_ISO8601_DATE = new SimpleDateFormat("yyyy-MM-dd");

	public static final DateFormat DATE_FORMAT_ISO8601_TIME_FULL = new SimpleDateFormat("HH:mm:ss.s z");
	public static final DateFormat DATE_FORMAT_ISO8601_TIME_LONG = new SimpleDateFormat("HH:mm:ss");
	public static final DateFormat DATE_FORMAT_ISO8601_TIME_SHRT = new SimpleDateFormat("HH:mm");
	
	public static final DateFormat DATE_FORMAT_SIMPLE = new SimpleDateFormat("MM/dd/yyyy");
	public static final DateFormat DATE_FORMAT_TIMESTAMP = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
	
	public static final CustomPropertyEditor CUSTOM_DATE_EDITOR_ISO8601_FULL = 
		new CustomPropertyEditor(Date.class, new CustomDateEditor(DATE_FORMAT_ISO8601_FULL, true));
	
	public static final CustomPropertyEditor CUSTOM_DATE_EDITOR_ISO8601_LONG =
		new CustomPropertyEditor(Date.class, new CustomDateEditor(DATE_FORMAT_ISO8601_LONG, true));
	
	public static final CustomPropertyEditor CUSTOM_DATE_EDITOR_ISO8601_DATE = 
		new CustomPropertyEditor(Date.class, new CustomDateEditor(DATE_FORMAT_ISO8601_DATE, true));
	
	public static final CustomPropertyEditor CUSTOM_DATE_EDITOR_SIMPLE = 
		new CustomPropertyEditor(Date.class, new CustomDateEditor(DATE_FORMAT_SIMPLE, true));
	
	public static final CustomPropertyEditor CUSTOM_DATE_EDITOR_TIMESTAMP = 
		new CustomPropertyEditor(Date.class, new CustomDateEditor(DATE_FORMAT_TIMESTAMP, true));
		
	public static BindException buildBindException() {
		return buildBindException(new Object());
	}
	
	public static BindException buildBindException(Object obj) {
		return buildBindException(obj, getNonCapitalizedSimpleName(obj));
	}
	
	public static BindException buildBindException(Object obj, String objName) {
		return new BindException(obj, objName);
	}
	
	public static BindException bindAndValidate(Object obj, HttpServletRequest request, Validator validator) {
		return bindAndValidate(obj, request, validator, null);
	}
	
	public static BindException bindAndValidate(Object obj, HttpServletRequest request, Validator validator, Map<String,CustomPropertyEditor> customEditorMap) {	
		ServletRequestDataBinder binder = new ServletRequestDataBinder(obj, getNonCapitalizedSimpleName(obj));
		return bindAndValidate(binder, request, validator, customEditorMap);
	}
	
	public static BindException bindAndValidate(Object obj, String objName, HttpServletRequest request, Validator validator) {
		return bindAndValidate(obj, objName, request, validator, null);
	}
		
	public static BindException bindAndValidate(Object obj, String objName, HttpServletRequest request, Validator validator, Map<String,CustomPropertyEditor> customEditorMap) {
		ServletRequestDataBinder binder = new ServletRequestDataBinder(obj, objName);
		return bindAndValidate(binder, request, validator, customEditorMap);
	}
	
	protected static BindException bindAndValidate(ServletRequestDataBinder binder, HttpServletRequest request, Validator validator, Map<String,CustomPropertyEditor> customEditorMap) {
		registerCustomEditors(binder, customEditorMap);
		binder.bind(request);
		return doValidate(binder, validator);
	}
	
	public static BindException bindAndValidate(Object obj,  Map<?,?> properties, Validator validator) {
		return bindAndValidate(obj, properties, validator, null);
	}
	
	public static BindException bindAndValidate(Object obj,  Map<?,?> properties, Validator validator, Map<String,CustomPropertyEditor> customEditorMap) {
		WebDataBinder binder = new WebDataBinder(obj, getNonCapitalizedSimpleName(obj));
		return bindAndValidate(binder, properties, validator, customEditorMap);
	}
	
	public static BindException bindAndValidate(Object obj, String objName,  Map<?,?> properties, Validator validator) {
		return bindAndValidate(obj, objName, properties, validator, null);
	}

	public static BindException bindAndValidate(Object obj, String objName,  Map<?,?> properties, Validator validator, Map<String,CustomPropertyEditor> customEditorMap) {
		WebDataBinder binder = new WebDataBinder(obj, objName);
		return bindAndValidate(binder, properties, validator, customEditorMap);
	}
	
	protected static BindException bindAndValidate(WebDataBinder binder, Map<?,?> properties, Validator validator, Map<String,CustomPropertyEditor> customEditorMap) {
		registerCustomEditors(binder, customEditorMap);
		binder.bind(new MutablePropertyValues(properties));
		return doValidate(binder, validator);
	}
	
	protected static void registerCustomEditors(DataBinder binder, Map<String,CustomPropertyEditor> customEditorMap) {		
		if(customEditorMap != null) {
			for(Map.Entry<String,CustomPropertyEditor> entry : customEditorMap.entrySet()) {
				binder.registerCustomEditor(entry.getValue().getRequiredClass(), entry.getValue().getPropertyEditor());
				break;
			}
		}
	}
	
	protected static BindException doValidate(DataBinder binder, Validator validator) {
		invokeValidator(validator, binder);
		return new BindException(binder.getBindingResult());
	}
	
	public static void invokeValidator(Validator validator, DataBinder binder) {
		invokeValidator(validator, binder.getTarget(), binder.getBindingResult());
	}

	protected static String getNonCapitalizedSimpleName(Object obj) {
		if(obj == null) {
			return "null";
		}
		
		String simpleName = obj.getClass().getSimpleName();
		if(simpleName.length() < 2) {
			return simpleName.toLowerCase();
		}
		
		return simpleName.substring(0,1).toLowerCase() + simpleName.substring(1);
	}
}
