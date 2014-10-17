/**
 * 
 */
package collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

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

}
