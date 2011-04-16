/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     CategoryUtils abstract class.
 *     
 */
package ca.sciencestudio.data.util;

import java.util.LinkedHashSet;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.sciencestudio.data.standard.StdCategories;
import ca.sciencestudio.data.standard.category.Category;
import ca.sciencestudio.data.standard.category.UniqueCategory;

/**
 * @author maxweld
 *
 */
public abstract class CategoryUtils implements StdCategories {

	private static final String CATEGORY_IDENTIFY_REGEX = "(?:([^:]*):)*(([^:]+):(\\d+)\\.(\\d+))";
	private static final Pattern CATEGORY_IDENTIFY_PATTERN = Pattern.compile(CATEGORY_IDENTIFY_REGEX);
	private static final int CATEGORY_IDENTITY_UID_GROUP = 1;
	private static final int CATEGORY_IDENTITY_BASE_GROUP = 2;
//	private static final int CATEGORY_IDENTITY_NAME_GROUP = 3;
//	private static final int CATEGORY_IDENTITY_MAJOR_GROUP = 4;
//	private static final int CATEGORY_IDENTITY_MINOR_GROUP = 5;
	
	public static Collection<UniqueCategory<?>> findCategories(Collection<String> identities) {
		Collection<UniqueCategory<?>> categories = new LinkedHashSet<UniqueCategory<?>>();
		for(String identity : identities) {
			Matcher matcher = CATEGORY_IDENTIFY_PATTERN.matcher(identity);
			if(matcher.matches()) {
				for(Category<?> category : CATEGORIES) {
					if(category.getIdentity().equals(matcher.group(CATEGORY_IDENTITY_BASE_GROUP))) {
						categories.add(category.getUniqueCategory(matcher.group(CATEGORY_IDENTITY_UID_GROUP)));
						break;
					}
				}
			}
		}
		return categories;
	}
	
	public static <T extends UniqueCategory<?>> boolean contains(Collection<T> uniqueCategories, String uid) {
		return !getCategories(uniqueCategories, uid).isEmpty();
	}

	public static <T extends UniqueCategory<?>> T getFirstCategory(Collection<T> uniqueCategories, String uid) {
		Collection<T> categories = getCategories(uniqueCategories, uid);
		if(categories.isEmpty()) {
			return null;
		}
		return categories.iterator().next();
	}
	
	public static <T extends UniqueCategory<?>> Collection<T> getCategories(Collection<T> uniqueCategories, String uid) {
		Collection<T> categories = new LinkedHashSet<T>();
		for(T uniqueCategory : uniqueCategories) {
			if(uniqueCategory.getUID().equals(uid)) {
				categories.add(uniqueCategory);	
			}	
		}
		return categories;
	}

	public static <T extends UniqueCategory<?>, C extends Category<U>, U extends UniqueCategory<?>> 
		boolean contains(Collection<T> uniqueCategories, C category) {
			return  !getCategories(uniqueCategories, category).isEmpty();
	}
	
	public static <T extends UniqueCategory<?>, C extends Category<U>, U extends UniqueCategory<?>> 
		U getFirstCategory(Collection<T> uniqueCategirues, C category) {
			Collection<U> categories = getCategories(uniqueCategirues, category);
			if(categories.isEmpty()) {
				return null;
			}
			return categories.iterator().next();
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends UniqueCategory<?>, C extends Category<U>, U extends UniqueCategory<?>> 
		Collection<U> getCategories(Collection<T> uniqueCategories, C category) {
			U u = category.getUniqueCategory(null);
			Collection<U> categories = new LinkedHashSet<U>();
			for(T uniqueCategory : uniqueCategories) {
				if(u.getClass().isInstance(uniqueCategory)) {
					categories.add((U)uniqueCategory);
				}
			}
			return categories;
	}
}
