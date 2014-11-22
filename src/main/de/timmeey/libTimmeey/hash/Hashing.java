package de.timmeey.libTimmeey.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {

	public static String sha512(String toHash) {
		try {
			return hash("SHA-512", toHash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String hash(String hashMethod, String toHash)
			throws NoSuchAlgorithmException {
		MessageDigest md;
		md = MessageDigest.getInstance(hashMethod);

		md.update(toHash.getBytes());
		byte[] mb = md.digest();
		String out = "";
		for (int i = 0; i < mb.length; i++) {
			byte temp = mb[i];
			String s = Integer.toHexString(new Byte(temp));
			while (s.length() < 2) {
				s = "0" + s;
			}
			s = s.substring(s.length() - 2);
			out += s;
		}
		return out;

	}

}
