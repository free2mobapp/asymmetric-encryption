package co.uk.ksb.AsymmetricEncryption.crypto;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.crypto.Cipher;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service()
public class RSAService {
    private KeyFactory keyFactory;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    private String privateKeyClassPathResource;

    private String publicKeyClassPathResource;

    public RSAService(@Value("${publicKeyClassPathResource}") String pubKey,
                      @Value("${privateKeyClassPathResource}") String privKey )
            throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {

        privateKeyClassPathResource = privKey;
        publicKeyClassPathResource = pubKey;
        setKeyFactory();
        setPublicKey();
        setPrivateKey();

    }

    private void setPublicKey()
            throws InvalidKeySpecException, IOException {
        if (publicKey ==  null) {
            File file = ResourceUtils.getFile(publicKeyClassPathResource);

            String publicKeyPEM = new String(Files.readAllBytes(file.toPath()), "UTF-8");
            String stringAfter = publicKeyPEM
                    .replaceAll("(\\r|\\n)", "")
                    .replaceAll("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll("-----END PUBLIC KEY-----", "")
                    .trim();

            byte[] decoded = Base64
                    .getDecoder()
                    .decode(stringAfter);

            KeySpec keySpec
                    = new X509EncodedKeySpec(decoded);

            publicKey = keyFactory.generatePublic(keySpec);
        }
    }

    private void setPrivateKey()
            throws IOException, InvalidKeySpecException {
        if (privateKey ==  null) {
            File file = ResourceUtils.getFile(privateKeyClassPathResource);

            String privateKeyPEM = new String(Files.readAllBytes(file.toPath()), "UTF-8");
            String stringAfter = privateKeyPEM
                    .replaceAll("(\\r|\\n)", "")
                    .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll("-----END PRIVATE KEY-----", "")
                    .trim();

            byte[] decoded = Base64
                    .getDecoder()
                    .decode(stringAfter);

            KeySpec keySpec
                    = new PKCS8EncodedKeySpec(decoded);

            privateKey = keyFactory.generatePrivate(keySpec);
        }
    }

    private void setKeyFactory() throws NoSuchAlgorithmException {
        this.keyFactory = KeyFactory.getInstance("RSA");
    }

    public String encryptToBase64(String plainText) {
        String encoded = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            encoded = Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encoded;
    }

    public String decryptFromBase64(String base64EncodedEncryptedBytes) {
        String plainText = null;
        try {
            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decoded = Base64
                    .getDecoder()
                    .decode(base64EncodedEncryptedBytes);
            byte[] decrypted = cipher.doFinal(decoded);
            plainText = new String(decrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return plainText;
    }
}
