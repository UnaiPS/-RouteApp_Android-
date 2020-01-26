/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.routeapp_android.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Jon Calvo Gaminde
 */
public class HexByteConverter {

    public static byte[] hexToByte(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length()/2; i++){
            String s = hex.substring(i * 2, i * 2 + 2);
            int value = Integer.parseInt(s, 16);
            if (value > 127) {
                s = "-" + Integer.toHexString(256-value);
            }
            bytes[i] = Byte.parseByte(s, 16);
        }
        return bytes;
    }

    public static String byteToHex(byte[] bytes) {
        String hex = new String();
        for (int i = 0; i < bytes.length; i++){
            Byte b = bytes[i];
            String s = "0" + Integer.toHexString(b.intValue());
            hex += s.substring(s.length()-2, s.length());
        }
        return hex;
    }
}
