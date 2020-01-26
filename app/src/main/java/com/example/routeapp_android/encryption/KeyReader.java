/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.routeapp_android.encryption;

import android.content.res.AssetManager;
import android.content.res.Resources;

import com.example.routeapp_android.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Unai Pérez Sánchez
 */
public class KeyReader {

    private static byte[] key;
    public static void setKeyResource(InputStream keyResource) throws IOException {
        ByteArrayOutputStream ous = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            int read = 0;
            while ((read = keyResource.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        }finally {
            try {
                if (ous != null)
                    ous.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (keyResource != null)
                    keyResource.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        key = ous.toByteArray();
    }

    public static byte[] getKey() {
        return key;
    }
}
