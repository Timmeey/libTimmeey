package de.timmeey.libTimmeey.networking;

public interface NetSerializer {

	public String toJson(Object object);

	public <T> T fromJson(String string, Class<T> clazz);
}
