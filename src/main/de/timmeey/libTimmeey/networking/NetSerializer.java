package de.timmeey.libTimmeey.networking;

import de.timmeey.libTimmeey.exceptions.checked.SerializerException;

public interface NetSerializer {

	public String toJson(Object object) throws SerializerException;

	public <T> T fromJson(String string, Class<T> clazz);
}
