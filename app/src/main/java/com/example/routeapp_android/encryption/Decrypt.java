/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.routeapp_android.encryption;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;

import static com.example.routeapp_android.encryption.HexByteConverter.hexToByte;

/**
 *
 * @author Unai Pérez Sánchez
 */
public class Decrypt {
    public static String descifrarTexto(String codedMessage) {
        byte[] message = hexToByte(codedMessage);
        
        byte[] decodedMessage = null;
        try {
                // Public key
                byte fileKey[] = KeyReader.getKey();
                
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(fileKey);
                PrivateKey privateKey = keyFactory.generatePrivate(pKCS8EncodedKeySpec);

                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                decodedMessage = cipher.doFinal(message);
        } catch (Exception e) {
                Logger.getLogger(Decrypt.class.getName()).log(Level.SEVERE, null, e.getLocalizedMessage());
        }
        return new String(decodedMessage);
    }
}
