/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     CompareUtils abstract class.
 *     
 */
package ca.sciencestudio.util;

import java.util.Map;
import java.util.Collection;

/**
 * @author maxweld
 *
 */
public abstract class CompareUtils {
	
	// All Equal //

	public boolean allEqual(int left, int... right) {
		for(int r : right) {
			if(left != r) {
				return false;
			}
		}
		return true;
	}
	
	public boolean allEqual(int[] left, int right) {
		for(int l : left) {
			if(l != right) {
				return false;
			}
		}
		return true;
	}
	
	public boolean allEqual(double left, double... right) {
		for(double r : right) {
			if(left != r) {
				return false;
			}
		}
		return true;
	}
	
	public boolean allEqual(double[] left, double right) {
		for(double l : left) {
			if(l != right) {
				return false;
			}
		}
		return true;
	}
	
	public static <T> boolean allEqual(T left, T... right) {
		for(T r : right) {
			if(!left.equals(r)) {
				return false;
			}
		}
		return true;
	}

	public static <T> boolean allEqual(T[] left, T right) {
		for(T l : left) {
			if(!l.equals(right)) {
				return false;
			}
		}
		return true;
	}
	
	public static <T> boolean allEqual(T left, Collection<T> right) {
		for(T r : right) {
			if(!left.equals(r)) {
				return false;
			}
		}
		return true;
	}

	public static <T> boolean allEqual(Collection<T> left, T right) {
		for(T l : left) {
			if(!l.equals(right)) {
				return false;
			}
		}
		return true;
	}
	
	// One Equal //
	
	public static boolean oneEqual(int left, int... right) {
		for(int r : right) {
			if(left == r) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean oneEqual(int[] left, int right) {
		for(int l : left) {
			if(l == right) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean oneEqual(double left, double... right) {
		for(double r : right) {
			if(left == r) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean oneEqual(double[] left, double right) {
		for(double l : left) {
			if(l == right) {
				return true;
			}
		}
		return false;
	}
	
	public static <T> boolean oneEqual(T left, T... right) {		
		for(T r : right) {
			if(left.equals(r)) {
				return true;
			}
		}
		return false;
	}
	
	public static <T> boolean oneEqual(T[] left, T right) {		
		for(T l : left) {
			if(l.equals(right)) {
				return true;
			}
		}
		return false;
	}
	
	public static <T> boolean oneEqual(T left, Collection<T> right) {
		for(T r : right) {
			if(left.equals(r)) {
				return true;
			}
		}
		return false;
	}
	
	public static <T> boolean oneEqual(Collection<T> left, T right) {
		for(T l : left) {
			if(l.equals(right)) {
				return true;
			}
		}
		return false;
	}
	
	// All Less Than Or Equal //
	
	public static boolean allLessThanOrEqual(int left, int... right) {
		for(int r : right) {
			if(left > r) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean allLessThanOrEqual(int[] left, int right) {
		for(int l : left) {
			if(l > right) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean allLessThanOrEqual(double left, double... right) {
		for(double r : right) {
			if(left > r) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean allLessThanOrEqual(double[] left, double right) {
		for(double l : left) {
			if(l > right) {
				return false;
			}
		}
		return true;
	}
	
	public static <T extends Comparable<T>> boolean allLessThanOrEqual(T left, T... right) {
		for(T r : right) {
			if(left.compareTo(r) > 0) {
				return false;
			}
		}
		return true;
	}
	
	public static <T extends Comparable<T>> boolean allLessThanOrEqual(T[] left, T right) {
		for(T l : left) {
			if(l.compareTo(right) > 0) {
				return false;
			}
		}
		return true;
	}
	
	public static <T extends Comparable<T>> boolean allLessThanOrEqual(T left, Collection<T> right) {
		for(T r : right) {
			if(left.compareTo(r) > 0) {
				return false;
			}
		}
		return true;
	}
	
	public static <T extends Comparable<T>> boolean allLessThanOrEqual(Collection<T> left, T right) {
		for(T l : left) {
			if(l.compareTo(right) > 0) {
				return false;
			}
		}
		return true;
	}
	
	// One Less Than Or Equal //
	
	public static boolean oneLessThanOrEqual(int left, int... right) {
		for(int r : right) {
			if(left <= r) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean oneLessThanOrEqual(int[] left, int right) {
		for(int l : left) {
			if(l <= right) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean oneLessThanOrEqual(double left, double... right) {
		for(double r : right) {
			if(left <= r) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean oneLessThanOrEqual(double[] left, double right) {
		for(double l : left) {
			if(l <= right) {
				return true;
			}
		}
		return false;
	}
	
	public static <T extends Comparable<T>> boolean oneLessThanOrEqual(T left, T... right) {
		for(T r : right) {
			if(left.compareTo(r) <= 0) {
				return true;
			}
		}
		return false;
	}
	
	public static <T extends Comparable<T>> boolean oneLessThanOrEqual(T[] left, T right) {
		for(T l : left) {
			if(l.compareTo(right) <= 0) {
				return true;
			}
		}
		return false;
	}
	
	public static <T extends Comparable<T>> boolean oneLessThanOrEqual(T left, Collection<T> right) {
		for(T r : right) {
			if(left.compareTo(r) <= 0) {
				return true;
			}
		}
		return false;
	}
	
	public static <T extends Comparable<T>> boolean oneLessThanOrEqual(Collection<T> left, T right) {
		for(T l : left) {
			if(l.compareTo(right) <= 0) {
				return true;
			}
		}
		return false;
	}
	
	// All Less Than //
	
	public static boolean allLessThan(int left, int... right) {
		for(int r : right) {
			if(left >= r) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean allLessThan(int[] left, int right) {
		for(int l : left) {
			if(l >= right) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean allLessThan(double left, double... right) {
		for(double r : right) {
			if(left >= r) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean allLessThan(double[] left, double right) {
		for(double l : left) {
			if(l >= right) {
				return false;
			}
		}
		return true;
	}
	
	public static <T extends Comparable<T>> boolean allLessThan(T left, T... right) {
		for(T r : right) {
			if(left.compareTo(r) >= 0) {
				return false;
			}
		}
		return true;
	}
	
	public static <T extends Comparable<T>> boolean allLessThan(T[] left, T right) {
		for(T l : left) {
			if(l.compareTo(right) >= 0) {
				return false;
			}
		}
		return true;
	}
	
	public static <T extends Comparable<T>> boolean allLessThan(T left, Collection<T> right) {
		for(T r : right) {
			if(left.compareTo(r) >= 0) {
				return false;
			}
		}
		return true;
	}
	
	public static <T extends Comparable<T>> boolean allLessThan(Collection<T> left, T right) {
		for(T l : left) {
			if(l.compareTo(right) >= 0) {
				return false;
			}
		}
		return true;
	}
	
	// One Less Than //
	
	public static boolean oneLessThan(int left, int... right) {
		for(int r : right) {
			if(left < r) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean oneLessThan(int[] left, int right) {
		for(int l : left) {
			if(l < right) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean oneLessThan(double left, double... right) {
		for(double r : right) {
			if(left < r) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean oneLessThan(double[] left, double right) {
		for(double l : left) {
			if(l < right) {
				return true;
			}
		}
		return false;
	}
	
	public static <T extends Comparable<T>> boolean oneLessThan(T left, T... right) {
		for(T r : right) {
			if(left.compareTo(r) < 0) {
				return true;
			}
		}
		return false;
	}
	
	public static <T extends Comparable<T>> boolean oneLessThan(T[] left, T right) {
		for(T l : left) {
			if(l.compareTo(right) < 0) {
				return true;
			}
		}
		return false;
	}
	
	public static <T extends Comparable<T>> boolean oneLessThan(T left, Collection<T> right) {
		for(T r : right) {
			if(left.compareTo(r) < 0) {
				return true;
			}
		}
		return false;
	}
	
	public static <T extends Comparable<T>> boolean oneLessThan(Collection<T> left, T right) {
		for(T l : left) {
			if(l.compareTo(right) < 0) {
				return true;
			}
		}
		return false;
	}
	
	// All Greater Than Or Equal //
	
	public static boolean allGreaterThanOrEqual(int left, int... right) {
		for(int r : right) {
			if(left < r) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean allGreaterThanOrEqual(int[] left, int right) {
		for(int l : left) {
			if(l < right) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean allGreaterThanOrEqual(double left, double... right) {
		for(double r : right) {
			if(left < r) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean allGreaterThanOrEqual(double[] left, double right) {
		for(double l : left) {
			if(l < right) {
				return false;
			}
		}
		return true;
	}
	
	public static <T extends Comparable<T>> boolean allGreaterThanOrEqual(T left, T... right) {
		for(T r : right) {
			if(left.compareTo(r) < 0) {
				return false;
			}
		}
		return true;
	}
	
	public static <T extends Comparable<T>> boolean allGreaterThanOrEqual(T[] left, T right) {
		for(T l : left) {
			if(l.compareTo(right) < 0) {
				return false;
			}
		}
		return true;
	}
	
	public static <T extends Comparable<T>> boolean allGreaterThanOrEqual(T left, Collection<T> right) {
		for(T r : right) {
			if(left.compareTo(r) < 0) {
				return false;
			}
		}
		return true;
	}
	
	public static <T extends Comparable<T>> boolean allGreaterThanOrEqual(Collection<T> left, T right) {
		for(T l : left) {
			if(l.compareTo(right) < 0) {
				return false;
			}
		}
		return true;
	}
	
	// One Greater Than Or Equal //
	
	public static boolean oneGreaterThanOrEqual(int left, int... right) {
		for(int r : right) {
			if(left >= r) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean oneGreaterThanOrEqual(int[] left, int right) {
		for(int l : left) {
			if(l >= right) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean oneGreaterThanOrEqual(double left, double... right) {
		for(double r : right) {
			if(left >= r) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean oneGreaterThanOrEqual(double[] left, double right) {
		for(double l : left) {
			if(l >= right) {
				return true;
			}
		}
		return false;
	}
	
	public static <T extends Comparable<T>> boolean oneGreaterThanOrEqual(T left, T... right) {
		for(T r : right) {
			if(left.compareTo(r) >= 0) {
				return true;
			}
		}
		return false;
	}
	
	public static <T extends Comparable<T>> boolean oneGreaterThanOrEqual(T[] left, T right) {
		for(T l : left) {
			if(l.compareTo(right) >= 0) {
				return true;
			}
		}
		return false;
	}
	
	public static <T extends Comparable<T>> boolean oneGreaterThanOrEqual(T left, Collection<T> right) {
		for(T r : right) {
			if(left.compareTo(r) >= 0) {
				return true;
			}
		}
		return false;
	}
	
	public static <T extends Comparable<T>> boolean oneGreaterThanOrEqual(Collection<T> left, T right) {
		for(T l : left) {
			if(l.compareTo(right) >= 0) {
				return true;
			}
		}
		return false;
	}
	
	
	// All Greater Than //
	
	public static boolean allGreaterThan(int left, int... right) {
		for(int r : right) {
			if(left <= r) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean allGreaterThan(int[] left, int right) {
		for(int l : left) {
			if(l <= right) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean allGreaterThan(double left, double... right) {
		for(double r : right) {
			if(left <= r) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean allGreaterThan(double[] left, double right) {
		for(double l : left) {
			if(l <= right) {
				return false;
			}
		}
		return true;
	}
	
	public static <T extends Comparable<T>> boolean allGreaterThan(T left, T... right) {
		for(T r : right) {
			if(left.compareTo(r) <= 0) {
				return false;
			}
		}
		return true;
	}
	
	public static <T extends Comparable<T>> boolean allGreaterThan(T[] left, T right) {
		for(T l : left) {
			if(l.compareTo(right) <= 0) {
				return false;
			}
		}
		return true;
	}
	
	public static <T extends Comparable<T>> boolean allGreaterThan(T left, Collection<T> right) {
		for(T r : right) {
			if(left.compareTo(r) <= 0) {
				return false;
			}
		}
		return true;
	}
	
	public static <T extends Comparable<T>> boolean allGreaterThan(Collection<T> left, T right) {
		for(T l : left) {
			if(l.compareTo(right) <= 0) {
				return false;
			}
		}
		return true;
	}
	
	// One Greater Than //
	
	public static boolean oneGreaterThan(int left, int... right) {
		for(int r : right) {
			if(left > r) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean oneGreaterThan(int[] left, int right) {
		for(int l : left) {
			if(l > right) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean oneGreaterThan(double left, double... right) {
		for(double r : right) {
			if(left > r) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean oneGreaterThan(double[] left, double right) {
		for(double l : left) {
			if(l > right) {
				return true;
			}
		}
		return false;
	}
	
	public static <T extends Comparable<T>> boolean oneGreaterThan(T left, T... right) {
		for(T r : right) {
			if(left.compareTo(r) > 0) {
				return true;
			}
		}
		return false;
	}
	
	public static <T extends Comparable<T>> boolean oneGreaterThan(T[] left, T right) {
		for(T l : left) {
			if(l.compareTo(right) > 0) {
				return true;
			}
		}
		return false;
	}
	
	public static <T extends Comparable<T>> boolean oneGreaterThan(T left, Collection<T> right) {
		for(T r : right) {
			if(left.compareTo(r) > 0) {
				return true;
			}
		}
		return false;
	}
	
	public static <T extends Comparable<T>> boolean oneGreaterThan(Collection<T> left, T right) {
		for(T l : left) {
			if(l.compareTo(right) > 0) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean equal(float left, float right, float epsilon) {
		return ((left==right) || ((Math.abs(left-right)/(Math.abs(left)+Math.abs(right)))<=Math.abs(epsilon)));
	}
	
	public static boolean equal(double left, double right, double epsilon) {
		return ((left==right) || ((Math.abs(left-right)/(Math.abs(left)+Math.abs(right)))<=Math.abs(epsilon)));
	}
	
	public static boolean equalSignum(float left, float right) {
		return Math.signum(left) == Math.signum(right);
	}
	
	public static boolean equalSignum(double left, double right) {
		return Math.signum(left) == Math.signum(right);
	}
	
	public static boolean agree(float left, float right, float uncertainty) {
		return (Math.abs(left - right) <= Math.abs(2.0 * uncertainty));
	}
	
	public static boolean agree(double left, double right, double uncertainty) {
		return (Math.abs(left - right) <= Math.abs(2.0 * uncertainty));
	}
	
	public static boolean agree(float left, float leftUncertainty, float right, float rightUncertainty) {
		return (Math.abs(left - right) <= (Math.abs(leftUncertainty) + Math.abs(rightUncertainty)));
	}
	
	public static boolean agree(double left, double leftUncertainty, double right, double rightUncertainty) {
		return (Math.abs(left - right) <= (Math.abs(leftUncertainty) + Math.abs(rightUncertainty)));
	}
	
	public static boolean isNotEmptyMap(Object obj) {
		return (obj instanceof Map<?,?>) && !(((Map<?,?>)obj).isEmpty());
	}
	
	public static boolean isNotEmptyString(Object obj) {
		return (obj instanceof String) && (((String)obj).length() > 0);
	}
	
	public static boolean isNotEmptyCollection(Object obj) {	
		return (obj instanceof Collection<?>) && !(((Collection<?>)obj).isEmpty()); 
	}
}
