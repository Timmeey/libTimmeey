package de.timmeey.libTimmeey.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Encryption {
	private static final Logger logger = LoggerFactory
			.getLogger(Encryption.class);

	public static boolean checkSignature(byte[] valueToCheck,
			byte[] digitalSignature, PublicKey publicKey)
			throws NoSuchAlgorithmException, NoSuchProviderException,
			InvalidKeyException, SignatureException {
		logger.trace("checkSignature({},{},{})", valueToCheck.length,
				digitalSignature.length, publicKey.getAlgorithm());
		if (!publicKey.getAlgorithm().equals("RSA")) {
			throw new InvalidKeyException("Only RSA keys are supported atm");
		}
		Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initVerify(publicKey);

		signature.update(valueToCheck);

		boolean verified = signature.verify(digitalSignature);
		logger.trace("Finished checking the Signature. Result was {}", verified);
		return verified;
	}

	public static byte[] sign(byte[] message, PrivateKey privKey)
			throws SignatureException, NoSuchAlgorithmException,
			InvalidKeyException {
		if (!privKey.getAlgorithm().equals("RSA")) {
			throw new InvalidKeyException("Only RSA keys are supported atm");
		}
		byte[] decryptedMessage = null;
		final Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initSign(privKey);
		signature.update(message);
		return signature.sign();

	}

	public static byte[] decrypt(byte[] message, PrivateKey privKey)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {
		if (!privKey.getAlgorithm().equals("RSA")) {
			throw new InvalidKeyException("Only RSA keys are supported atm");
		}
		byte[] decryptedMessage = null;
		final Cipher cipher = Cipher.getInstance(privKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privKey);
		decryptedMessage = cipher.doFinal(message);

		return decryptedMessage;
	}

	public static byte[] encrypt(byte[] message, PublicKey publicKey)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		if (!publicKey.getAlgorithm().equals("RSA")) {
			throw new InvalidKeyException("Only RSA keys are supported atm");
		}
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] cipherData = cipher.doFinal(message);
		return cipherData;
	}
}
