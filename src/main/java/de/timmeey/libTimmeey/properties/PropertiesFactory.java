package de.timmeey.libTimmeey.properties;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class PropertiesFactory {
	private static final File homeDir = org.apache.commons.io.FileUtils
			.getUserDirectory();
	private static File confDir;
	private static HashMap<String, PropertiesAccessor> handler = new
		HashMap<>();

	/**
	 * Returns the actual propertiesFileHandler either by creating the specified
	 * PropertiesFile or by opening an existing
	 *
	 * @param propertiesFileName
	 *            The propertiesFileName
	 * @return the propertiesHandler
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public static PropertiesAccessor getPropertiesAccessor(
			String propertiesFileName) throws IOException,
		IllegalStateException {
		if (confDir == null || !confDir.isDirectory()) {
			throw new IllegalStateException("Conf dir not yet set");
		}
		return getPropertiesAccessor(confDir.getAbsolutePath(),
				propertiesFileName);
	}

	/**
	 * An extended Method for creating a PropertiesFile in a self specified
	 * Directory. CAUTION: Don't use propertiesFiles with the same name in
	 * different Directories
	 *
	 * @param confDir
	 *            the Directoy in where the propertiesfile will be created
	 * @param propertyHandlerName
	 *            the PropertiesFileName
	 * @return the actual propertiesHandler
	 * @throws IOException
	 */
	public static PropertiesAccessor getPropertiesAccessor(String confDir,
			String propertyHandlerName) throws IOException {
		PropertiesAccessor tmp_handler = handler.get(propertyHandlerName);
		if (tmp_handler == null) {
			tmp_handler = new FileProperty(confDir, propertyHandlerName);
			handler.put(propertyHandlerName, tmp_handler);
		}
		return tmp_handler;
	}

	public static String getConfDir() {
		return confDir.getAbsolutePath();
	}

	/**
	 * Creates the standard configDirectory in the Users Home Dir
	 *
	 * @param appName
	 *            Name of the app
	 * @throws IOException
	 */
	public static void setConfDir(String appName) throws IOException {
		File confDir = new File(homeDir.getAbsolutePath() + File.separator
				+ "." + appName);
		org.apache.commons.io.FileUtils.forceMkdir(confDir);
		PropertiesFactory.confDir = confDir;
	}

}
