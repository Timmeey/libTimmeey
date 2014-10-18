/**
 * 
 */
package timmeeyLib.properties;

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
	public PropertiesAccessor addProperty(String key, String value) throws IOException;
	
	public PropertiesAccessor addProperties(Map<String, String> map) throws IOException;
	
	
	public String getProperty(String key);
	
	public String getProperty(String key, String defaultVlaue);
	
	public HashMap<String, String> getAllProperties();
	
	public boolean contains(String key);
	
	public PropertiesAccessor remove(String key) throws IOException;
	
	public PropertiesAccessor empty();
	
}
