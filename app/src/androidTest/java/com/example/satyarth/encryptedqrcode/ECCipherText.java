package com.example.satyarth.encryptedqrcode;

/**
 * Created by satyarth on 10/15/17.
 */
import android.support.test.runner.AndroidJUnit4;
import android.widget.Toast;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;

import static org.junit.Assert.*;

public class ECCipherText {
    @Test
    public void test() throws Exception{
            KeyPair eccipherKey = ECCipher.generateKeyPair();
            String message ="hello world";
            ECCipher ecCipher = new ECCipher();
            byte[] encrypted  = ecCipher.encrypt(eccipherKey.getPublic(),message);
            byte[] decrypted = ecCipher.decrypt(eccipherKey.getPrivate(),encrypted);
            assertEquals(message, new String(decrypted));
    }
}