package libJava;

import java.io.IOException;
import java.util.HashMap;

import properties.PropertiesAccessor;
import properties.PropertiesFactory;

public class PropertyTest {
	
	
	public static void main(String[] args) throws IOException {
		PropertiesAccessor prop = PropertiesFactory.getPropertiesAccessor("/tmp/", "muhtest.properties");
		prop.addProperty("muh", "kuh");
		
		HashMap<String, String> map = new HashMap<>();
		map.put("hallo","1");
		map.put("Hallo2", "2");
		prop.addProperties(map);
		System.out.println(prop.getProperty("mu1h"));
	}

}
