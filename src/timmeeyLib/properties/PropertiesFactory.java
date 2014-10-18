package timmeeyLib.properties;

import java.io.IOException;
import java.util.HashMap;


public class PropertiesFactory {
	private static String confDir;
	private static HashMap<String, PropertyHandler> handler = new HashMap<>();
	
	public static PropertiesAccessor getPropertiesAccessor(String propertyHandlerName) throws IOException{
		return getPropertiesAccessor(confDir, propertyHandlerName);
	}
	

	
	public static PropertiesAccessor getPropertiesAccessor(String confDir, String propertyHandlerName) throws IOException{
		PropertyHandler tmp_handler = handler.get(propertyHandlerName);
		if(tmp_handler == null){
			tmp_handler = new FileProperty(confDir,propertyHandlerName);
			handler.put(propertyHandlerName, tmp_handler);
		}
		return tmp_handler;		
	}
	
	public static void setConfDir(String confDir){
		PropertiesFactory.confDir = confDir; 
	}
	
	public static String getConfDir(){
		return confDir;
	}

	
	

}
