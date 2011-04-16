/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     UsernamePasswordUtils class.
 *     
 */
package ca.sciencestudio.security.util;

import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

/**
 * @author maxweld
 * 
 */
public abstract class UsernamePasswordUtils {

	/* Note: Letters and numbers which are easily confused have been removed. */
	public static final String CHARACTER_SET_LOWER_CASE_LETTERS = "abcdefghijkmnopqrstuvwxyz";
	public static final String CHARACTER_SET_UPPER_CASE_LETTERS = "ABCDEFGHJKLMNOPQRSTUVWXYZ";
	public static final String CHARACTER_SET_NUMBERS = "123456789";
	public static final String CHARACTER_SET_PUNCTUATION = "()[]<>?!:;@#$%^&*+-=";
	
	public static final String PATTERN_LOWER_CASE_LETTERS = ".*[a-z].*";
	public static final String PATTERN_UPPER_CASE_LETTERS = ".*[A-Z]+.*";
	public static final String PATTERN_NUMBERS = ".*[0-9]+.*";
	public static final String PATTERN_PUNCTUATION = ".*[()\\[\\]<>?!:;@#$%^&*+-=]+.*" ;
	
	
	public static String generateUsername(String firstName, String lastName) {
		
		String first = firstName.toLowerCase().replaceAll("[^a-z]+", "");
		String last = lastName.toLowerCase().replaceAll("[^a-z]+", "");
		
		int firstLength = Math.min(1, first.length()); 		
		int lastLength = Math.min(6, last.length());
		
		return last.substring(0,lastLength) + first.substring(0,firstLength);		
	}

	public static String generateUniqueUsername(String firstName, String lastName, String[] usernames) {
		
		String usernameBase = generateUsername(firstName, lastName);
				
		String usernameTest = usernameBase;
		
		for(int suffix = 0; true; suffix++) {
			
			boolean found = false;
			
			for(String username : usernames) {
				if(username.equals(usernameTest)) {
					found = true;
				}
			}
			
			if(!found) { 
				return usernameTest;
			}
			
			usernameTest = usernameBase + String.valueOf(suffix);
		}
	}
	
	public static String generatePassword(int length) {
		return generatePassword(length, false, false, false);
	}
	
	public static String generatePassword(int length, boolean upperCaseLetters) {
		return generatePassword(length, upperCaseLetters, false, false);
	}
	
	public static String generatePassword(int length, boolean upperCaseLetters, boolean numbers) {
		return generatePassword(length, upperCaseLetters, numbers, false);
	}
	
	public static String generatePassword(int length, boolean upperCaseLetters, boolean numbers, boolean punctuation) {
		
		length = Math.max(4, length);
		
		String characters = CHARACTER_SET_LOWER_CASE_LETTERS;
		
		if(upperCaseLetters) {
			characters += CHARACTER_SET_UPPER_CASE_LETTERS;
		}
		if(numbers) {
			characters += CHARACTER_SET_NUMBERS;
		}
		if(punctuation) {
			characters += CHARACTER_SET_PUNCTUATION;
		}
				
		Random random = new Random();
	
		while(true) {
	
			StringBuffer buffer = new StringBuffer();
			
			for(int i = 0; i<length; i++) {
				int randomIndex = random.nextInt(characters.length());
				char randomCharacter = characters.charAt(randomIndex);
				buffer.append(randomCharacter);
			}
			
			String password = buffer.toString();
			
			if(!password.matches(PATTERN_LOWER_CASE_LETTERS)) {
				continue;
			}
			if(upperCaseLetters) {
				if(!password.matches(PATTERN_UPPER_CASE_LETTERS)) {
					continue;
				}
			}
			if(numbers) {
				if(!password.matches(PATTERN_NUMBERS)) {
					continue;
				}
			}
			if(punctuation) {
				if(!password.matches(PATTERN_PUNCTUATION)) {
					continue;
				}
			}
			
			return password;
		}			
	}
	
	public static String encodeUnicodePassword(String password, String charset) {
		byte[] utf16leBytes = null;
		String utf16lePwd = null;
		try {
			utf16leBytes = password.getBytes(charset);
			utf16lePwd = new String(utf16leBytes);
		} catch (UnsupportedEncodingException e) {
		}
		
		return utf16lePwd;
	}
	
	public static String encryptPassword(String password, String algorithm) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(algorithm);
		byte[] hash = md.digest(password.getBytes());
		return new String(Base64.encodeBase64(hash));
	}
}
