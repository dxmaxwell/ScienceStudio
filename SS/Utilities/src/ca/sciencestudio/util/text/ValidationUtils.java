/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ValidationUtils class.
 *     
 */
package ca.sciencestudio.util.text;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author maxweld
 *
 */
public abstract class ValidationUtils extends org.springframework.validation.ValidationUtils {

	public static Errors emptyErrors() {
		return emptyErrors(new Object());
	}
	
	public static Errors emptyErrors(Object obj) {
		return emptyErrors(obj, obj.getClass().getSimpleName());
	}
	
	public static Errors emptyErrors(Object obj, String objName) {
		return new BindException(obj, objName);
	}
	
	public static Errors invokeValidator(Validator validator, Object obj) {
		Errors errors = emptyErrors(obj);
		invokeValidator(validator, obj, errors);
		return errors;
	}
	
	public static Errors invokeValidator(Validator validator, Object obj, String objName) {
		Errors errors = emptyErrors(obj, objName);
		invokeValidator(validator, obj, errors);
		return errors;
	}
	
	public static void rejectIfExceedsLength(int length, Errors errors, String field, String errorCode) {
		if(rejectIfExceedsLength(errors.getFieldValue(field), length)) {
			errors.rejectValue(field, errorCode);
		}
	}
	
	public static void rejectIfExceedsLength(int length, Errors errors, String field, String errorCode, String defaultMessage) {
		if(rejectIfExceedsLength(errors.getFieldValue(field), length)) {
			errors.rejectValue(field, errorCode, defaultMessage);
		}
	}
	
	public static void rejectIfExceedsLength(int length, Errors errors, String field, String errorCode, Object[] errorArgs, String defaultMessage) {
		if(rejectIfExceedsLength(errors.getFieldValue(field), length)) {
			errors.rejectValue(field, errorCode, errorArgs, defaultMessage);
		}
	}
	
	protected static boolean rejectIfExceedsLength(Object obj, int length) {
		return (obj instanceof String) && (((String)obj).length() > length);
	}
	
	public static void rejectIfInvalidEmail(Errors errors, String field, String errorCode) {
		if(rejectIfInvalidEmail(errors.getFieldValue(field))) {
			errors.rejectValue(field, errorCode);
		}
	}
	
	public static void rejectIfInvalidEmail(Errors errors, String field, String errorCode, String defaultMessage) {
		if(rejectIfInvalidEmail(errors.getFieldValue(field))) {
			errors.rejectValue(field, errorCode, defaultMessage);
		}
	}
	
	public static void rejectIfInvalidEmail(Errors errors, String field, String errorCode, Object[] errorArgs, String defaultMessage) {
		if(rejectIfInvalidEmail(errors.getFieldValue(field))) {
			errors.rejectValue(field, errorCode, errorArgs, defaultMessage);
		}
	}
	
	protected static boolean rejectIfInvalidEmail(Object email) { 
		 return !((email instanceof String) && isValidEmail((String)email));
	}
	
	public static boolean isValidEmail(String email) {
		if(email == null) {
			return false;
		}
	
		if(email.indexOf("@") == -1) {
			return false;
		}
		
		String[] emailHalves = email.split("@");
		if(emailHalves.length > 2) {
			return false;
		}
		else if(emailHalves[0].length() < 1 || emailHalves[0].startsWith(".") || emailHalves[0].endsWith(".")) {
			return false;
		}
		else if(emailHalves[1].length() < 1 || emailHalves[1].startsWith(".") || emailHalves[1].endsWith(".") || emailHalves[1].length() < 3 || emailHalves[1].indexOf(".") == -1) {
			return false;
		}
		
		for(int i = 0; i<emailHalves.length; i++){
			String emailPortion = emailHalves[i];
			for(int j = 0; j<emailPortion.length(); j++){
				char letter = emailPortion.charAt(j);
				if(letter == '`' || letter == '~' || letter == '!' || letter == '#' || letter == '$' || letter == '%' ||
						letter == '^' || letter == '&' || letter == '*' || letter == '(' || letter == ')' || letter == '+' ||
						letter == '=' || letter == '{' || letter == '}' || letter == '[' || letter == ']' || letter == '|' ||
						letter == '\\' || letter == ':' || letter == ';' || letter == '\'' || letter == '"' || letter == '<' ||
						letter == ',' || letter == '>' || letter == '?' || letter == '/' )
					return false;
			}
		}
		return true;
	}
	
	public static void rejectIfInvalidTelephone(Errors errors, String field, String errorCode) {
		if(rejectIfInvalidTelephone(errors.getFieldValue(field))) {
			errors.rejectValue(field, errorCode);
		}
	}
	
	public static void rejectIfInvalidTelephone(Errors errors, String field, String errorCode, String defaultMessage) {
		if(rejectIfInvalidTelephone(errors.getFieldValue(field))) {
			errors.rejectValue(field, errorCode, defaultMessage);
		}
	}
	
	public static void rejectIfInvalidTelephone(Errors errors, String field, String errorCode, Object[] errorArgs, String defaultMessage) {
		if(rejectIfInvalidTelephone(errors.getFieldValue(field))) {
			errors.rejectValue(field, errorCode, errorArgs, defaultMessage);
		}
	}
	
	protected static boolean rejectIfInvalidTelephone(Object telephone) { 
		 return !((telephone instanceof String) && isValidTelephone((String)telephone));
	}
	
	public static boolean isValidTelephone(String telephoneNumber){
		if(telephoneNumber == null)
			return false;
		else{
			if(telephoneNumber.indexOf("-") == -1||telephoneNumber.length() < 8)
				return false;
			String[] telephonePieces = telephoneNumber.split("-");
			if((telephonePieces.length <= 4 && !telephoneNumber.startsWith("+")) && 
					(telephonePieces[telephonePieces.length-1].length() != 4 ||
					telephonePieces[telephonePieces.length-2].length() != 3)){
				return false;
			}
			if((telephonePieces.length == 3 || telephonePieces.length == 4) && !telephoneNumber.startsWith("+")){
				String temp;
				if(telephonePieces.length == 4){
					temp = telephonePieces[0].replaceFirst("\\(", "");
					temp = temp.replaceFirst("\\)", "");
					if(temp.length() != 1)
						return false;
				}
				temp = telephonePieces[telephonePieces.length-3].replaceFirst("\\(", "");
				temp = temp.replaceFirst("\\)", "");
				if(temp.length() != 3)
					return false;
			}
				
			for(int i = 0; i < telephonePieces.length; i++){
				String number = telephonePieces[i];
				if(telephonePieces.length > 2 && i < telephonePieces.length-2){
					if(telephonePieces.length == 4){
						if(number.startsWith("+"))
							number = number.replaceFirst("\\+", "");
					}
					if(number.startsWith("(")&&number.endsWith(")")){
						number = number.replaceFirst("\\(", "");
						number = number.replaceFirst("\\)", "");
					}
				}
				if(number.length() == 0)
					return false;
				for(int j=0; j < number.length(); j++){
					if(number.charAt(j) != '0' && number.charAt(j) != '1' && number.charAt(j) != '2' 
						&& number.charAt(j) != '3' && number.charAt(j) != '4' && number.charAt(j) != '5' && number.charAt(j) != '6' 
						&& number.charAt(j) != '7' && number.charAt(j) != '8' && number.charAt(j) != '9')
						return false;
				}
			}			
		}
		return true;
	}
}
