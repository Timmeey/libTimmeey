package de.timmeey.libTimmeey.crypto;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.UUID;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import de.timmeey.libTimmeey.util.Base64Helper;

public class EncryptionTest {

    private PrivateKey privatKey;
    private PublicKey  publicKey;

    @Before
    public void setUp() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize((int) Math.pow(2, 9));
        KeyPair kp = kpg.genKeyPair();
        publicKey = kp.getPublic();
        privatKey = kp.getPrivate();

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testEncryption() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException {
        String testString = UUID.randomUUID().toString();
        byte[] encryptedString = Encryption.encrypt(testString.getBytes(), publicKey);
        byte[] decryptedString = Encryption.decrypt(encryptedString, privatKey);
        assertEquals("Encrypted and decrypted string should be the same", testString, new String(decryptedString));

    }

    @Test
    public void testSigning()
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, SignatureException, UnsupportedEncodingException, NoSuchProviderException {
        String testString = UUID.randomUUID().toString();
        byte[] signedByte = Encryption.sign(testString.getBytes("UTF-8"), privatKey);
        String signedString = Base64Helper.byteToBase64String(signedByte);
        byte[] signedByte1 = Base64Helper.base64StringToByte(signedString);
        assertEquals(true, Encryption.checkSignature(testString.getBytes("UTF-8"), signedByte1, publicKey));
        String wrongTestString = testString + "j";

        assertEquals("Should have failed due to a wrong signature", false,
                Encryption.checkSignature(wrongTestString.getBytes("UTF-8"), signedByte1, publicKey));

    }
}
