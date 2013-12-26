package properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FileProperty extends PropertyHandler {	
	Properties props;
	File file;
	
	public FileProperty(String confDir, String propertyFileName) throws IOException {
		file = getFile(confDir, propertyFileName);
		try(FileInputStream inStream = new FileInputStream(file)){
			props = new Properties();
			props.load(inStream);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private static File getFile(String confDir, String propertyFileName) throws IOException{
		String completePath = confDir + propertyFileName;
		if(checkPath(completePath)){
			return new File(completePath);
		}else{
			//will never happen
			return null;
		}
	}
	
	private static boolean checkPath(String path) throws IOException{
		File file = new File(path);
		if(file.isDirectory()) throw new InvalidPathException(path, path + " is a Directory");
		if(!file.canWrite()) throw new InvalidPathException(path, "Cannot write to " + path);
		if(!file.canRead()) throw new InvalidPathException(path, "Cannot read from " + path);
		file.createNewFile();
		return true;
	}

	@Override
	public PropertiesAccessor addProperty(String key, String value) throws IOException {
			props.setProperty(key, value);
			store();
			return this;

	}



	@Override
	public String getProperty(String key) {
		return props.getProperty(key);
	}

	@Override
	public String getProperty(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}

	@Override
	public HashMap<String, String> getAllProperties() {
		// TODO Auto-generated method stub
		HashMap<String,String> result = new HashMap<>();
		for (Object key : props.keySet()) {
			result.put(key.toString(), props.getProperty(key.toString()));
		}
		return result;
	}

	@Override
	public boolean contains(String key) {
		// TODO Auto-generated method stub
		return props.containsKey(key);
	}

	@Override
	public PropertiesAccessor remove(String key) throws IOException {
			props.remove(key);
			store();
			return this;


	}

	@Override
	public PropertiesAccessor empty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertiesAccessor addProperties(Map<String, String> map) throws IOException {
		props.putAll(map);
		store();
		return this;
		
	}
	
	private boolean store() throws IOException{
		try(FileOutputStream out = new FileOutputStream(file)){
			props.store(out,"");
			return true;
		}
		
	}

}
