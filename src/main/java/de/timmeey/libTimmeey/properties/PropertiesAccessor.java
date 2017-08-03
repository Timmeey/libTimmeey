/**
 *
 */
package de.timmeey.libTimmeey.properties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author timmeey
 *
 */
public interface PropertiesAccessor {

	/**
	 * Adds a property to the property Handler
	 * @param key the key under which the value will be stored
	 * @param value the value to store
	 * @return the Properties Handler
     * @throws IOException
     */
    PropertiesAccessor addProperty(String key, String value) throws IOException;

    PropertiesAccessor addProperties(Map<String, String> map) throws
        IOException;

    String getProperty(String key);

    String getProperty(String key, String defaultVlaue);

    HashMap<String, String> getAllProperties();

    boolean contains(String key);

    PropertiesAccessor remove(String key) throws IOException;

    PropertiesAccessor empty();

}
