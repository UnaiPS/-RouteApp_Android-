/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyPair;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Logger;

/**
 *
 * @author Unai Pérez Sánchez
 */
public class KeyGen {

    /**
     * Genera el fichero con la clave privada
     */
    private Logger LOGGER = Logger.getLogger(KeyGen.class.getName());
    public void generatePrivateKey() {
        
            KeyPairGenerator generator;
            try {
                    generator = KeyPairGenerator.getInstance("RSA");
                    generator.initialize(1024); // Inicializamos el tamaño a 1024 bits
                    KeyPair keypair = generator.generateKeyPair();
                    PublicKey publicKey = keypair.getPublic(); // Clave Pública
                    PrivateKey privateKey = keypair.getPrivate(); // Clave Privada

                    // Publica
                    X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(".\\tmp\\Public.key"));
                    fileOutputStream.write(x509EncodedKeySpec.getEncoded());
                    fileOutputStream.close();

                    // Privada
                    PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
                    fileOutputStream = new FileOutputStream(new File(".\\tmp\\Private.key"));
                    fileOutputStream.write(pKCS8EncodedKeySpec.getEncoded());
                    fileOutputStream.close();
            } catch (Exception e) {
                    LOGGER.severe(e.getLocalizedMessage());
            }
    }

    public static void main(String[] args) {
            KeyGen keyGenerator = new KeyGen();
            keyGenerator.generatePrivateKey();
    }
}

