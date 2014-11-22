/**
 * 
 */
package de.timmeey.libTimmeey.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @author timmeey
 * 
 */
public class ListHelper {

	/**
	 * Returns a uniquified ArrayList for any given Collection.
	 * 
	 * @param coll
	 * @return a ArrayList with only unique elements
	 */
	@SuppressWarnings("rawtypes")
	public static <T> ArrayList<T> uniquifyList(Collection<T> coll) {
		return new ArrayList(new HashSet<T>(coll));

	}

	/**
	 * Meant for the quick creation of an ArrayList with just one value. More or
	 * less to perform a conversion from a single value to a List object
	 * 
	 * @param values
	 *            The value(s) that the list should contain
	 * @return a ArrayList with all the Values
	 */
	public static <T> List<T> initListWithValue(T... values) {
		return Arrays.asList(values);
	}
}
