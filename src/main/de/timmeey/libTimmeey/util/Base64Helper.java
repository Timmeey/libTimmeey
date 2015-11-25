package de.timmeey.libTimmeey.util;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class Base64Helper {

	private final static Encoder encoder = Base64.getEncoder();
	private static final Decoder decoder = Base64.getDecoder();

	public static String byteToBase64String(byte[] input) {
		return encoder.encodeToString(input);
	}

	public static byte[] base64StringToByte(String input) {
		return decoder.decode(input);
	}

}
